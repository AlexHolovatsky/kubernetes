package com.shop.ua.controllers;

import com.shop.ua.component.RepositoryManager;
import com.shop.ua.models.Cart;
import com.shop.ua.models.Goods;
import com.shop.ua.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class WebCartController {
    @Autowired
    private RepositoryManager repositoryManager;

    @GetMapping("/cart")
    public String cart(Model model, Authentication authentication) {
        String currentUsername = (authentication != null) ? authentication.getName() : null;

        User user = repositoryManager.getUserRepository().findByEmail(currentUsername);
        List<Cart> cartItems = repositoryManager.getCartRepository().findByUserAndOrdered(user, false);
        model.addAttribute("user", user);
        model.addAttribute("cartItems", cartItems);

        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("isAuthenticated", true);
        }

        return "cart";
    }

    @PostMapping("/cart/add/{userId}/{goodsId}")
    public String addToCart(@PathVariable Long userId, @PathVariable Long goodsId) {
        User user = repositoryManager.getUserRepository().findById(userId).orElse(null);
        Goods product = repositoryManager.getGoodsRepository().findById(goodsId).orElse(null);

        if (user != null && product != null) {
            Cart cartItem = new Cart();
            cartItem.setUser(user);
            cartItem.setGoods(product);
            cartItem.setOrdered(false);

            repositoryManager.getCartRepository().save(cartItem);
        }

        return "redirect:/cart";
    }

    @PostMapping("/cart/buy/{cartId}")
    public String buyFromCart(@PathVariable Long cartId) {
        Cart cartItem = repositoryManager.getCartRepository().findById(cartId).orElse(null);

        if (cartItem != null) {
            cartItem.setOrdered(true);

            repositoryManager.getCartRepository().save(cartItem);
        }
        return "redirect:/cart";
    }

    @PostMapping("/cart/buyAll")
    public String updateAllOrderedStatus(@RequestParam boolean newOrderedStatus) {
        List<Cart> cartItems = repositoryManager.getCartRepository().findAll();

        cartItems.forEach(cartItem -> cartItem.setOrdered(newOrderedStatus));

        repositoryManager.getCartRepository().saveAll(cartItems);

        return "redirect:/cart";
    }

    @PostMapping("/cart/remove/{cartId}")
    public String removeFromCart(@PathVariable Long cartId) {
        repositoryManager.getCartRepository().findById(cartId).ifPresent(cartItem -> repositoryManager.getCartRepository().delete(cartItem));

        return "redirect:/cart";
    }

}
