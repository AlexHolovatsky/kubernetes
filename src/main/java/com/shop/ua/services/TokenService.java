package com.shop.ua.services;

import com.shop.ua.models.User;
import com.shop.ua.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TokenService {

    @Autowired
    private UserRepository userRepository;

    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    public void saveTokenToUser(User user, String token) {
        user.setEmailVerificationToken(token);
        userRepository.save(user);
    }
}

