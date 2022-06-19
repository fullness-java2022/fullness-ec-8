package com.fullness.ec;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class TopController {
	@GetMapping
	public String top() {
		return "index";
	}
	@PostMapping("/login")
	public String login(@RequestParam String userName, @RequestParam String password, Model model) {
		model.addAttribute("userName", userName);
		return "menu";
	}
}
