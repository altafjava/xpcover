package com.gmc.main.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	@GetMapping("/api/v1/test")
	public String test() {
		return "This is for testing. Project working fine";
	}
	
	@GetMapping("/api/v1/test-secure")
	public String testSecure() {
		return "This is for secure testing. Project working fine";
	}
}
