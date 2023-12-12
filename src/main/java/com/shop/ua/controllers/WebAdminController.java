package com.shop.ua.controllers;

import com.shop.ua.component.RepositoryManager;
import com.shop.ua.models.Goods;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class WebAdminController {
    @Autowired
    private RepositoryManager repositoryManager;

    @GetMapping("/admin")
    public String admin(Model model){
        List<Goods> unapprovedGoods = repositoryManager.getGoodsService().listUnapprovedGoods();
        model.addAttribute("unapprovedGoods", unapprovedGoods);
        model.addAttribute("users", repositoryManager.getUserService().list());
        return "admin";
    }


    @PostMapping("/admin/user/ban/{id}")
    public String bannedUser(@PathVariable("id") Long id){
        repositoryManager.getUserService().banUser(id);
        return "redirect:/admin";
    }

    @PostMapping("/admin/user/unban/{id}")
    public String unbannedUser(@PathVariable("id") Long id){
        repositoryManager.getUserService().unbanUser(id);
        return "redirect:/admin";
    }

    @PostMapping("/admin/user/op/{id}")
    public String oppUser(@PathVariable("id") Long id){
        repositoryManager.getUserService().assignAdminRole(id);
        return "redirect:/admin";
    }

    @PostMapping("/admin/user/unop/{id}")
    public String unoppUser(@PathVariable("id") Long id){
        repositoryManager.getUserService().removeAdminRole(id);
        return "redirect:/admin";
    }

    @PostMapping("/admin/goods/approve/{id}")
    public String approveGoods(@PathVariable("id") Long id) {
        repositoryManager.getGoodsService().approveGoods(id);
        return "redirect:/admin";
    }

    @PostMapping("/admin/goods/reject/{id}")
    public String rejectGoods(@PathVariable("id") Long id) {
        repositoryManager.getGoodsService().rejectProduct(id);
        return "redirect:/admin";
    }

}
