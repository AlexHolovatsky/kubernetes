package com.shop.ua.controllers;

import com.shop.ua.component.RepositoryManager;
import com.shop.ua.models.Goods;
import com.shop.ua.models.Image;
import com.shop.ua.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import org.apache.commons.io.FilenameUtils;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.io.File;

@Controller
@RequiredArgsConstructor
public class WebGoodsController {
    @Autowired
    private RepositoryManager repositoryManager;

    @GetMapping("/shop")
    public String approvedGoods(Model model, @RequestParam(name = "search", required = false) String search) {
        List<Goods> searchResults;

        if (search != null && !search.isEmpty()) {
            searchResults = repositoryManager.getGoodsService().searchGoodsByKeyword(search);
        } else {
            searchResults = repositoryManager.getGoodsService().listApprovedGoods();
        }

        model.addAttribute("searchResults", searchResults);
        model.addAttribute("search", search);

        return "TestNewDesign";
    }

    @GetMapping("/shop/goods/{id}")
    public String goodsInfo(@PathVariable Long id, Model model){
        Goods goods = repositoryManager.getGoodsService().getGoodsById(id);
        model.addAttribute("goods", goods);
        model.addAttribute("images", goods.getImages());
        return "productdetails";
    }


    @PostMapping("/shop/goods/create")
    public String createGoods(@RequestParam("files") MultipartFile[] files, Goods goods) throws IOException {
        repositoryManager.getGoodsService().saveGoods(goods, files);
//        return "redirect:/shop";
        return "redirect:/TestNewDesign";
    }

    @PostMapping("/shop/goods/delete/{id}")
    public String deleteGoods(@PathVariable Long id){
        repositoryManager.getGoodsService().deleteGoods(id);
//        return "redirect:/shop";
        return "redirect:/TestNewDesign";
    }

    @GetMapping("/shop/addgood")
    public String addGood(){
        return "addgood";
    }

    @GetMapping("/shop/product/{id}")
    public String getProductDetails(@PathVariable Long id, Model model, Authentication authentication) {
        Goods product = repositoryManager.getGoodsRepository().findById(id).orElse(null);
        User user = null;

        model.addAttribute("title", "Details");

        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("isAuthenticated", true);
            String userEmail = authentication.getName();
            user = repositoryManager.getUserRepository().findByEmail(userEmail);
        }

        if (product != null) {
            model.addAttribute("product", product);
            model.addAttribute("user", user);
            return "productdetails";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/search")
    public String searchGoods(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
        List<Goods> searchResults = repositoryManager.getGoodsService().searchGoodsByKeyword(keyword);
        model.addAttribute("searchResults", searchResults);
        return "GoodsResult"; // Назва шаблону Thymeleaf для відображення результатів пошуку
    }


}
