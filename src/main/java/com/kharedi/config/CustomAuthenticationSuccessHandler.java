package com.kharedi.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.kharedi.model.Role;
import com.kharedi.model.User;
import com.kharedi.repository.RoleRepository;
import com.kharedi.repository.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Autowired
    public CustomAuthenticationSuccessHandler(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        String email = token.getPrincipal().getAttributes().get("email").toString();
        if(userRepository.findByEmail(email).isPresent()) {
            
        } else {
            User user = new User();
            user.setFirstName(token.getPrincipal().getAttributes().get("given_name").toString());
            user.setLastName(token.getPrincipal().getAttributes().get("family_name").toString());
            user.setEmail(email);
            
            List<Role> role = new ArrayList<>();
            role.add(roleRepository.findById(2).get());
            user.setRole(role);
            
            userRepository.save(user);
        }
        
        redirectStrategy.sendRedirect(request, response, "/");
    }
}
