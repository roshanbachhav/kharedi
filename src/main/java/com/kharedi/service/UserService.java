package com.kharedi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.kharedi.model.User;
import com.kharedi.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void userRemoveById(Long id){
        userRepository.deleteById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    
    public void saveUser(User user) {
    	userRepository.save(user);
    }
    
    public void updateUserDetails(Long userId, String address, String postcode, String city) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setAddress(address);
            user.setPostcode(postcode);
            user.setCity(city);
            userRepository.save(user);
        }
    }


}
