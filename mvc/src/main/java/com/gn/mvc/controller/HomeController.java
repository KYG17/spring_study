package com.gn.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	
	@GetMapping({"","/"})
	public String home() {
		// ->src/main/resources/templates/home.html로 가주세요!
		return "home";
	}
	
}
