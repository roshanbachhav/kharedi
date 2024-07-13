package com.kharedi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kharedi.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
	
	

}
