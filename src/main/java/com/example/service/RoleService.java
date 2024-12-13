package com.example.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.enums.PlayerRole;
import com.example.model.Role;
import com.example.repository.RoleRepository;

@Service
public class RoleService {

	private RoleRepository roleRepository;

	@Autowired
	public RoleService(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}
	
	public void initRoles() {
		Arrays.stream(PlayerRole.values())
		.forEach(r->{
			Role role = new Role();
			role.setName(r);
			roleRepository.save(role);
		});
	}
	
	public Role getRoleByName(PlayerRole user) {
		return roleRepository.findByName(user);
	}
}
