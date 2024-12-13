package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.enums.PlayerRole;
import com.example.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	@Query("SELECT r.name FROM Role r JOIN r.players p WHERE p.username = :username")
	List<String> findRolesByUsername(@Param("username") String username);

	Role findByName(PlayerRole user);
}
