package com.story.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainController {

	@RequestMapping(value = "/")
	public String toIndex() {
		return "index";
	}
	
}
