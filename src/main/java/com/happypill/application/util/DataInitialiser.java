package com.happypill.application.util;

import com.happypill.application.entity.*;
import com.happypill.application.entity.enums.Language;
import com.happypill.application.repository.category.CategoryRepository;
import com.happypill.application.repository.categoryinfo.CategoryInfoRepository;
import com.happypill.application.repository.product.ProductRepository;
import com.happypill.application.repository.productinfo.ProductInfoRepository;
import com.happypill.application.repository.productprice.ProductPriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitialiser implements ApplicationRunner {

    private final CategoryRepository categoryRepository;
    private final CategoryInfoRepository categoryInfoRepository;
    private final ProductRepository productRepository;
    private final ProductInfoRepository productInfoRepository;
    private final ProductPriceRepository productPriceRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Category categoryOne = Category.of(SnowflakeUtil.nextId(), "www.category_firstThumbnail.com", "www.category_firstBanner.com");
        Category categoryTwo = Category.of(SnowflakeUtil.nextId(), "www.category_secondThumbnail.com", "www.category_secondBanner.com");
        List<Category> categoryList = Arrays.asList(categoryOne, categoryTwo);

        CategoryInfo categoryInfoOne = CategoryInfo.of(SnowflakeUtil.nextId(), Language.KO, "비타민", "비타민은 몸에 좋아요",  categoryOne);
        CategoryInfo categoryInfoTwo = CategoryInfo.of(SnowflakeUtil.nextId(), Language.EN, "vitamin", "vitamin is necessary for health",  categoryOne);
        CategoryInfo categoryInfoThree = CategoryInfo.of(SnowflakeUtil.nextId(), Language.KO, "철분", "몸이 튼튼해져요",  categoryTwo);
        List<CategoryInfo> categoryInfoList = Arrays.asList(categoryInfoOne, categoryInfoTwo, categoryInfoThree);

        Product productOne = Product.of(SnowflakeUtil.nextId(), 300, "www.product_firstThumbnail.com", categoryOne);
        Product productTwo = Product.of(SnowflakeUtil.nextId(), 12, "www.product_secondThumbnail.com", categoryOne);
        List<Product> productList = Arrays.asList(productOne, productTwo);

        ProductInfo productInfoOne = ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "비타민 C", "30 캡슐", "밥과 함께 드시오", "물과 함께 섭취하시오. 하루에 3개.", "Content image", "매우 강력한 마법의 알약", "삼성제약", "오줌이 노래져요", productOne);
        ProductInfo productInfoTwo = ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "Vitamin C", "30 capsules", "Take with meal", "Please take 3 capsules daily", "Content image", "Magic pill", "Samsung chemist", "it can make your pee yellow", productOne);
        ProductInfo productInfoThree = ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "비타민 K", "10 캡슐", "밥과 함께 3알 드세요", "과대 섭취 금지", "Content image", "매우 화려한 알약", "삼성제약", "이건 어디 좋은지 몰라요 저희도", productTwo);
        List<ProductInfo> productInfoList = Arrays.asList(productInfoOne, productInfoTwo, productInfoThree);

        ProductPrice priceOne = ProductPrice.of(SnowflakeUtil.nextId(), 15000, false, productOne);
        ProductPrice priceTwo = ProductPrice.of(SnowflakeUtil.nextId(), 25000, true, productOne);
        ProductPrice priceThree = ProductPrice.of(SnowflakeUtil.nextId(), 8000, true, productTwo);
        List<ProductPrice> productPriceList = Arrays.asList(priceOne, priceTwo, priceThree);

        categoryRepository.saveAll(categoryList);
        categoryInfoRepository.saveAll(categoryInfoList);
        productRepository.saveAll(productList);
        productInfoRepository.saveAll(productInfoList);
        productPriceRepository.saveAll(productPriceList);
    }
}