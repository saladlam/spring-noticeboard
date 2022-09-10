package info.saladlam.example.spring.noticeboard.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import info.saladlam.example.spring.noticeboard.dto.MessageDto;
import info.saladlam.example.spring.noticeboard.dto.MessageDtoValidator;
import info.saladlam.example.spring.noticeboard.service.MessageService;
import info.saladlam.example.spring.noticeboard.support.CustomJava8TimeEditor;

@Controller
@RequestMapping("/manage")
public class PrivateController {

	private final static Map<Integer, String> statusMap = new HashMap<>();

	static {
		statusMap.put(null, "status.unknown");
		statusMap.put(MessageDto.WAITING_APPROVE, "status.waitingApprove");
		statusMap.put(MessageDto.APPROVED, "status.approved");
		statusMap.put(MessageDto.PUBLISHED, "status.published");
		statusMap.put(MessageDto.EXPIRED, "status.expired");
	}

	@Autowired
	private MessageService messageService;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setDisallowedFields("id", "owner", "approvedBy", "approvedDate", "status");
		binder.setValidator(new MessageDtoValidator());
		binder.registerCustomEditor(LocalDateTime.class, new CustomJava8TimeEditor<LocalDateTime>(
				DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"), LocalDateTime::from, true));
	}

	@GetMapping
	public String manage(Model model) {
		List<MessageDto> userMessages = this.messageService.findByOwner(this.getLoginName(), this.getCurrentTime());
		model.addAttribute("statusMap", statusMap);
		model.addAttribute("userMessages", userMessages);

		if (this.getLoginAuthorities().contains("ADMIN")) {
			List<MessageDto> waitingApproveMessages = this.messageService.findWaitingApprove();
			model.addAttribute("waitingApproveMessages", waitingApproveMessages);
		}
		return "private/manage";
	}

	@GetMapping("/new")
	public String createMessage(Model model) {
		model.addAttribute("isEdit", false);
		model.addAttribute("postHandler", "new/save");
		model.addAttribute("message", new MessageDto());
		return "private/message";
	}

	@PostMapping("/new/save")
	public String saveCreateMessage(@Valid @ModelAttribute MessageDto message, BindingResult errors) {
		if (errors.hasErrors()) {
			return "redirect:/manage/new";
		}

		message.setOwner(this.getLoginName());
		this.messageService.save(message);
		return "redirect:/manage";
	}

	@GetMapping("/{messageId}")
	public String editMessage(@PathVariable("messageId") long id, Model model) {
		MessageDto message = this.messageService.findOne(id, this.getCurrentTime());
		if (Objects.nonNull(message) && message.getStatus() == MessageDto.WAITING_APPROVE
				&& message.getOwner().equals(this.getLoginName())) {
			model.addAttribute("isEdit", true);
			model.addAttribute("postHandler", id + "/save");
			model.addAttribute("message", message);
		} else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}
		return "private/message";
	}

	@PostMapping("/{messageId}/save")
	public String saveEditMessage(@PathVariable("messageId") long id, @Valid @ModelAttribute MessageDto message,
			BindingResult errors) {
		if (errors.hasErrors()) {
			return "redirect:/manage/" + String.valueOf(id);
		}

		MessageDto originalMessage = this.messageService.findOne(id, this.getCurrentTime());
		if (Objects.nonNull(originalMessage) && (originalMessage.getStatus() == MessageDto.WAITING_APPROVE)
				&& originalMessage.getOwner().equals(this.getLoginName())) {
			originalMessage.setPublishDate(message.getPublishDate());
			originalMessage.setRemoveDate(message.getRemoveDate());
			originalMessage.setDescription(message.getDescription());
			this.messageService.save(originalMessage);
		} else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}
		return "redirect:/manage";
	}

	@GetMapping("/{messageId}/approve")
	public String approve(@PathVariable("messageId") long id) {
		if (this.getLoginAuthorities().contains("ADMIN")) {
			this.messageService.approve(id, this.getLoginName(), this.getCurrentTime());
		} else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}
		return "redirect:/manage";
	}

	private String getLoginName() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth.getName();
	}

	private List<String> getLoginAuthorities() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		return authorities.stream().map(obj -> obj.getAuthority()).collect(Collectors.toList());
	}

	private LocalDateTime getCurrentTime() {
		return LocalDateTime.now();
	}

}
