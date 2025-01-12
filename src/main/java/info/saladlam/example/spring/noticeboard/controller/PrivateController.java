package info.saladlam.example.spring.noticeboard.controller;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
import info.saladlam.example.spring.noticeboard.dto.MessageMvcDto;
import info.saladlam.example.spring.noticeboard.dto.MessageMvcDtoValidator;
import info.saladlam.example.spring.noticeboard.service.MessageService;

@Controller
@RequestMapping("/manage")
public class PrivateController {

	private static final String REDIRECT_MANAGE = "redirect:/manage";
	private static final String MESSAGE_TEMPLATE = "private/message";
	private static final Map<Integer, String> statusMap = new HashMap<>();

	static {
		statusMap.put(null, "status.unknown");
		statusMap.put(MessageDto.WAITING_APPROVE, "status.waitingApprove");
		statusMap.put(MessageDto.APPROVED, "status.approved");
		statusMap.put(MessageDto.PUBLISHED, "status.published");
		statusMap.put(MessageDto.EXPIRED, "status.expired");
	}

	private static void configureModel(Model model, boolean isEdit, String postHandler, MessageMvcDto message) {
		model.addAttribute("isEdit", isEdit);
		model.addAttribute("postHandler", postHandler);
		model.addAttribute("message", message);
	}

	@Autowired
	private MessageService messageService;
	@Autowired
	private ApplicationDateTimeService timeService;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setValidator(new MessageMvcDtoValidator());
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
		configureModel(model, false, "new", new MessageMvcDto());
		return MESSAGE_TEMPLATE;
	}

	@PostMapping("/new")
	public String saveCreateMessage(@Valid @ModelAttribute("message") MessageMvcDto message, BindingResult errors, Model model) {
		if (errors.hasErrors()) {
			configureModel(model, false, "new", message);
			return MESSAGE_TEMPLATE;
		}

		MessageDto dto = new MessageDto();
		dto.setPublishDate(message.getPublishDate());
		dto.setRemoveDate(message.getRemoveDate());
		dto.setDescription(message.getDescription());
		dto.setOwner(this.getLoginName());
		this.messageService.save(dto);
		return REDIRECT_MANAGE;
	}

	@GetMapping("/{messageId}")
	public String editMessage(@PathVariable("messageId") long id, Model model) {
		MessageDto message = this.messageService.findOne(id, this.getCurrentLocalDateTime());
		if (Objects.nonNull(message) && message.getStatus().equals(MessageDto.WAITING_APPROVE)
				&& message.getOwner().equals(this.getLoginName())) {
			configureModel(model, true, Long.toString(id), new MessageMvcDto(message));
		} else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}
		return MESSAGE_TEMPLATE;
	}

	@PostMapping("/{messageId}")
	public String saveEditMessage(@Valid @ModelAttribute("message") MessageMvcDto message,
								  BindingResult errors, Model model, @PathVariable("messageId") long id) {
		if (errors.hasErrors()) {
			configureModel(model, true, Long.toString(id), message);
			return MESSAGE_TEMPLATE;
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
		return authorities.stream().map(GrantedAuthority::getAuthority).toList();
	}

	private LocalDateTime getCurrentLocalDateTime() {
		return timeService.getCurrentLocalDateTime();
	}

}
