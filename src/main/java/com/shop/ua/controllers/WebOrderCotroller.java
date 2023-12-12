package com.shop.ua.controllers;

import com.shop.ua.component.RepositoryManager;
import com.shop.ua.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;

@Controller
public class WebOrderCotroller {

    @Autowired
    private RepositoryManager repositoryManager;

    @GetMapping("/order")
    public String cart(Model model, Authentication authentication) {
        String currentUsername = (authentication != null) ? authentication.getName() : null;
        User user = repositoryManager.getUserRepository().findByEmail(currentUsername);
        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("isAuthenticated", true);
        }

        List<Cart> cartItems = repositoryManager.getCartRepository().findByUserAndOrdered(user, true);
        model.addAttribute("user", user);
        model.addAttribute("cartItems", cartItems);

        return "order";
    }
    @PostMapping("/order/buy/{cartId}")
    public String buyFromCart(@PathVariable Long cartId) {
        Cart cartItem = repositoryManager.getCartRepository().findById(cartId).orElse(null);

        if (cartItem != null) {
            cartItem.setOrdered(false);

            repositoryManager.getCartRepository().save(cartItem);
        }
        return "redirect:/order";
    }
}
