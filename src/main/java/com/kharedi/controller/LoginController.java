package com.kharedi.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.kharedi.Global.Global;
import com.kharedi.model.Role;
import com.kharedi.model.User;
import com.kharedi.repository.RoleRepository;
import com.kharedi.repository.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@GetMapping("/login")
	public String Login(Model m) {
		Global.cart.clear();
		return "login";
	}
	
//	@PostMapping("/login")
//    public String loginProcess(HttpServletRequest request) {
//        String username = request.getParameter("username");
//        String password = request.getParameter("password");
//
//        User user = userRepository.findByUsername(username);
//        if (user != null && bCryptPasswordEncoder.matches(password, user.getPassword())) {
//        	return "redirect:/product";
//        } else {
//            return "redirect:/login?error";
//        }
//    }

	@GetMapping("/register")
	public String registerView() {
		return "register";
	}

	@PostMapping("/register")
	public String registration(Model m, User user, HttpServletRequest request) throws ServletException {
		String userPassword = user.getPassword();
		String encryptedPassword = bCryptPasswordEncoder.encode(userPassword);
		user.setPassword(encryptedPassword);
		List<Role> role = new ArrayList<>();
		role.add(roleRepository.findById(2).get());
		user.setRole(role);
		userRepository.save(user);
		request.login(user.getEmail(), userPassword);
		return "redirect:/product";
	}
	

}
