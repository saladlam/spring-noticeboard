package info.saladlam.example.spring.noticeboard.controller;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import jakarta.validation.Valid;

import info.saladlam.example.spring.noticeboard.service.ApplicationDateTimeService;
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

@Controller
@RequestMapping("/manage")
public class PrivateController {

	private static final String REDIRECT_MANAGE = "redirect:/manage";
	private static final Map<Integer, String> statusMap = new HashMap<>();

	static {
		statusMap.put(null, "status.unknown");
		statusMap.put(MessageDto.WAITING_APPROVE, "status.waitingApprove");
		statusMap.put(MessageDto.APPROVED, "status.approved");
		statusMap.put(MessageDto.PUBLISHED, "status.published");
		statusMap.put(MessageDto.EXPIRED, "status.expired");
	}

	@Autowired
	private MessageService messageService;
	@Autowired
	private ApplicationDateTimeService timeService;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setDisallowedFields("id", "owner", "approvedBy", "approvedDate", "status");
		binder.setValidator(new MessageDtoValidator());
	}

	@GetMapping
	public String manage(Model model) {
		List<MessageDto> userMessages = this.messageService.findByOwner(this.getLoginName(), this.getCurrentLocalDateTime());
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
		return REDIRECT_MANAGE;
	}

	@GetMapping("/{messageId}")
	public String editMessage(@PathVariable("messageId") long id, Model model) {
		MessageDto message = this.messageService.findOne(id, this.getCurrentLocalDateTime());
		if (Objects.nonNull(message) && message.getStatus().equals(MessageDto.WAITING_APPROVE)
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
			return "redirect:/manage/" + id;
		}

		MessageDto originalMessage = this.messageService.findOne(id, this.getCurrentLocalDateTime());
		if (Objects.nonNull(originalMessage) && (originalMessage.getStatus().equals(MessageDto.WAITING_APPROVE))
				&& originalMessage.getOwner().equals(this.getLoginName())) {
			originalMessage.setPublishDate(message.getPublishDate());
			originalMessage.setRemoveDate(message.getRemoveDate());
			originalMessage.setDescription(message.getDescription());
			this.messageService.save(originalMessage);
		} else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}
		return REDIRECT_MANAGE;
	}

	@GetMapping("/{messageId}/approve")
	public String approve(@PathVariable("messageId") long id) {
		if (this.getLoginAuthorities().contains("ADMIN")) {
			this.messageService.approve(id, this.getLoginName(), this.getCurrentLocalDateTime());
		} else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}
		return REDIRECT_MANAGE;
	}

	private String getLoginName() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth.getName();
	}

	private List<String> getLoginAuthorities() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
	}

	private LocalDateTime getCurrentLocalDateTime() {
		return timeService.getCurrentLocalDateTime();
	}

}
