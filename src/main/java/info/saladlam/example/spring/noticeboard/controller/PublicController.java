package info.saladlam.example.spring.noticeboard.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import info.saladlam.example.spring.noticeboard.repository.MessageRepository;
import info.saladlam.example.spring.noticeboard.service.ApplicationDateTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import info.saladlam.example.spring.noticeboard.dto.MessageDto;
import info.saladlam.example.spring.noticeboard.service.MessageService;

@Controller
@RequestMapping("/")
public class PublicController {

	@Autowired
	private MessageService messageService;
	@Autowired
	private ApplicationDateTimeService timeService;

	@GetMapping
	public String index(Model model) {
		List<MessageDto> messages = messageService.findPublished(this.getCurrentLocalDateTime());
		model.addAttribute("messages", messages);
		return "public/index";
	}

	@GetMapping("/login")
	public String login(Boolean error, Model model) {
		if (Objects.nonNull(error) && error.equals(Boolean.TRUE)) {
			model.addAttribute("error", true);
		} else {
			model.addAttribute("error", false);
		}
		return "public/login";
	}

	private LocalDateTime getCurrentLocalDateTime() {
		return timeService.getCurrentLocalDateTime();
	}

}
