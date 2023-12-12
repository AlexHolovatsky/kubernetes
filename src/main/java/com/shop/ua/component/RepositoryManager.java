package com.shop.ua.component;

import com.shop.ua.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.shop.ua.repositories.*;
import org.springframework.stereotype.Component;

@Component
public class RepositoryManager {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GoodsRepository goodsRepository;
    // Without implements repository
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    public UserRepository getUserRepository() {
        return userRepository;
    }


    public GoodsRepository getGoodsRepository() {
        return goodsRepository;
    }

    //impl repository

    public CartRepository getCartRepository() {
        return cartRepository;
    }

    public ImageRepository getImageRepository() {
        return imageRepository;
    }

    public GoodsService getGoodsService() {
        return goodsService;
    }

    public UserService getUserService() {
        return userService;
    }

    public TokenService getTokenService() {
        return tokenService;
    }

    public EmailService getEmailService() {
        return emailService;
    }

    public CustomUserDetailsService getCustomUserDetailsService() {
        return customUserDetailsService;
    }

}
