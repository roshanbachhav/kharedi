package com.kharedi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kharedi.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	User findByUsername(String username);
	
	Optional<User> findByEmail(String email);

}
