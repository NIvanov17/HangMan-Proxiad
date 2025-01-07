package com.example.controller.api;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.DTOs.AdminRolesDTO;
import com.example.model.DTOs.RoleDTO;
import com.example.service.PlayerService;
import com.example.util.JwtUtils;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@Tag(name = "Admin API Controller")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminAPIController {

	private final JwtUtils jwt;
	private final PlayerService playerService;

	@Autowired
	public AdminAPIController(JwtUtils jwt, PlayerService playerService) {
		this.jwt = jwt;
		this.playerService = playerService;
	}
	
	@GetMapping("api/v1/admin")
	public ResponseEntity<String> adminOverview() {
	    return ResponseEntity.ok("Admin endpoint is working!");
	}

	@GetMapping("api/v1/admin/users")
	public ResponseEntity<Page<AdminRolesDTO>> adminAccess(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "6") int size, HttpServletRequest req) {

		String token = jwt.getTokenFromRequest(req);
		String username = jwt.extractUsername(token);
		Pageable pageable = PageRequest.of(page, size);

		return ResponseEntity.ok(playerService.getAllUserRolesDTO(username,pageable));
	}
	
	@PutMapping("api/v1/admin/users/roles")
	public ResponseEntity<String> addingRole(@RequestBody RoleDTO dto) {
		
		playerService.addRole(dto);

		return ResponseEntity.ok("Succesfully added role!");
	}
	
	@DeleteMapping("api/v1/admin/users/roles")
	public ResponseEntity<String> removingRole(@RequestBody RoleDTO dto){
		
		playerService.removeRole(dto);
		
		return ResponseEntity.ok("Succesfully removing role!");
	}
	
	
	
}
