package com.kharedi.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kharedi.model.User;
import com.kharedi.model.UserDetail;
import com.kharedi.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		Optional<User> user = userRepository.findByEmail(email);
		user.orElseThrow(() -> new UsernameNotFoundException("Opps User not found...?"));
		
		return user.map(UserDetail::new).get();
	}
	

}
