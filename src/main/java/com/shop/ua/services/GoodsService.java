package com.shop.ua.services;

import com.shop.ua.component.RepositoryManager;
import com.shop.ua.models.Goods;
import com.shop.ua.models.Image;
import com.shop.ua.repositories.GoodsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

import java.io.File;

import org.apache.commons.io.FilenameUtils;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoodsService{
    @Autowired
    private RepositoryManager repositoryManager;
    private static final Logger logger = LoggerFactory.getLogger(GoodsService.class);
    private final GoodsRepository goodsRepository;

    public List<Goods> listGoods(String title){
        if (title != null) return goodsRepository.findByTitle(title);
        return goodsRepository.findAll();
    }

    public List<Goods> searchGoodsByKeyword(String keyword) {
        // Додайте новий метод для пошуку за назвою та описом
        List<Goods> byTitle = goodsRepository.findByTitleContaining(keyword);
        List<Goods> byDescription = goodsRepository.findByDescriptionContaining(keyword);

        // Об'єднайте результати двох запитів і поверніть унікальні товари
        Set<Goods> uniqueResults = new HashSet<>(byTitle);
        uniqueResults.addAll(byDescription);

        return new ArrayList<>(uniqueResults);
    }

    public List<Goods> listUnapprovedGoods() {
        return goodsRepository.findByApproved(false);
    }

    public Image getImageById(Long id) {
        return repositoryManager.getImageRepository().findById(id).orElse(null);
    }

    public List<Goods> listApprovedGoods() {
        return goodsRepository.findByApproved(true);
    }

    public String saveImageToFileSystem(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(file.getOriginalFilename());
        String filePath = "C:\\javastudy\\project\\ShopProject\\ua\\src\\main\\resources\\images\\" + fileName;
        file.transferTo(new File(filePath));
        logger.debug("Saved image file to path: {}", filePath);
        return filePath;
    }

    public void saveGoods(Goods goods, MultipartFile[] files) throws IOException {
        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                if (file.getSize() != 0) {
                    Image image = toImageEntity(file);
                    goods.addImageToGoods(image);
                }
            }
        }

        logger.info("Saved new Good. Title: {}; Price: {}", goods.getTitle(), goods.getPrice());
        goods.setApproved(false);
        Goods goodsFromDb = goodsRepository.save(goods);

        if (!goodsFromDb.getImages().isEmpty()) {
            goodsFromDb.setPreviewImageId(goodsFromDb.getImages().get(0).getId());
            goodsRepository.save(goodsFromDb);
        }
    }

    private Image toImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());

        // Змініть цей код, щоб зберігати шлях у поле imagePath
        String fileName = UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(file.getOriginalFilename());
        String filePath = "C:\\javastudy\\project\\ShopProject\\ua\\src\\main\\resources\\images\\" + fileName;
        file.transferTo(new File(filePath));

        // Збереження шляху до файлу в поле imagePath
        image.setImagePath(filePath);

        return image;
    }


    public void deleteGoods(Long id){
        goodsRepository.deleteById(id);
    }

    public Goods getGoodsById(Long id){
        return goodsRepository.findById(id).orElse(null);
    }

    public void approveGoods(Long id) {
        Goods goods = goodsRepository.findById(id).orElse(null);
        if (goods != null) {
            goods.setApproved(true);
            goodsRepository.save(goods);
        }
    }


    public void rejectProduct(Long id) {
        goodsRepository.deleteById(id);
    }
}
