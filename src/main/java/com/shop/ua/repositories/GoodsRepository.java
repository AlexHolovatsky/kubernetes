package com.shop.ua.repositories;

import com.shop.ua.models.Goods;
import com.shop.ua.services.GoodsService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface GoodsRepository extends JpaRepository<Goods, Long> {
    List<Goods> findByTitle(String title);

    List<Goods> findByApproved(boolean approved);
    List<Goods> findByTitleContaining(String keyword);
    List<Goods> findByDescriptionContaining(String keyword);
}
