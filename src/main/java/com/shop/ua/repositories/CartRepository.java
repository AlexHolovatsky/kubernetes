package com.shop.ua.repositories;

import com.shop.ua.models.Cart;
import com.shop.ua.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByUserAndOrdered(User user, boolean ordered);
}
