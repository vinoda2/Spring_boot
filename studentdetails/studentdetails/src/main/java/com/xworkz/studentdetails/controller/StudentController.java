package com.xworkz.studentdetails.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class StudentController {
	
	@GetMapping
	public String onSave() {
		return "this is Student controller";
	}

}
