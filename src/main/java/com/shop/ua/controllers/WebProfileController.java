package com.shop.ua.controllers;

import com.shop.ua.component.RepositoryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import com.shop.ua.models.User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class WebProfileController {
    @Autowired
    private RepositoryManager repositoryManager;

    @GetMapping("/userprofile")
    public String cart(Model model, Authentication authentication) {
        String currentUsername = (authentication != null) ? authentication.getName() : null;

        User user = repositoryManager.getUserRepository().findByEmail(currentUsername);
        model.addAttribute("user", user);

        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("isAuthenticated", true);
        }

        return "userprofile";
    }
}
