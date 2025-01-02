package com.example.controller.api;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Admin API Controller")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminAPIController {

}
