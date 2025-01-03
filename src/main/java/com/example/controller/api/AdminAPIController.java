package com.example.controller.api;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Admin API Controller")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminAPIController {

	@GetMapping("api/v1/admin")
	public ResponseEntity<String> adminAccess() {
	    Subject subject = SecurityUtils.getSubject();

	    if (subject.hasRole("ADMIN")) {
	        return ResponseEntity.ok("Welcome, Admin!");
	    } else {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
	    }
	}
}
