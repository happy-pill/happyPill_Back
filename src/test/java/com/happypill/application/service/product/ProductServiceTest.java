package com.happypill.application.service.product;

import com.happypill.application.entity.*;
import com.happypill.application.entity.enums.Language;
import com.happypill.application.exception.custom.ExceptionCode;
import com.happypill.application.exception.global.BusinessException;
import com.happypill.application.repository.category.CategoryRepository;
import com.happypill.application.repository.categoryinfo.CategoryInfoRepository;
import com.happypill.application.repository.product.ProductRepository;
import com.happypill.application.repository.productinfo.ProductInfoRepository;
import com.happypill.application.repository.productprice.ProductPriceRepository;
import com.happypill.application.service.product.dto.response.CustomPageResponse;
import com.happypill.application.service.product.dto.response.ProductInfoResponse;
import com.happypill.application.service.product.dto.response.ProductResponse;
import com.happypill.application.util.SnowflakeUtil;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
public class ProductServiceTest {
    @Autowired
    private ProductPriceRepository productPriceRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryInfoRepository categoryInfoRepository;

    @Autowired
    private ProductInfoRepository productInfoRepository;

    @Autowired
    private ProductService productService;

    @BeforeEach
    void setUpDB() {
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

    @AfterEach
    void clearDB() {
        productPriceRepository.deleteAll();
        productInfoRepository.deleteAll();
        productRepository.deleteAll();
        categoryInfoRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    @DisplayName("잘못된 카테고리 아이디 입력")
    public void getAllProductsWithWrongCategoryId() {
        Locale locale = Locale.of("KO");
        Long categoryId = 1L;
        int size = 4;

        CustomPageResponse<ProductResponse> result = productService.getAllProducts(categoryId, null, locale, size);

        assertThat(result.products()).isEmpty();
    }

    @Test
    @DisplayName("잘못된 카테고리와 마지막 상품 아이디 입력")
    public void getAllProductsWithWrongCategoryIdAndLastProductId() {
        Locale locale = Locale.of("KO");
        Long categoryId = 1L;
        Long lastProductId = 1L;
        int size = 4;

        assertThatThrownBy(() -> productService.getAllProducts(categoryId, lastProductId, locale, size))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ExceptionCode.PRODUCT_INFO_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("첫 알맞은 카테고리 아이디 입력")
    public void getAllProductsWithoutLastProductId() {
        Locale locale = Locale.of("KO");
        Category category = categoryRepository.findAll().getFirst();
        Long categoryId = category.getCategoryId();
        Long lastProductId = null;
        int size = 4;

        CustomPageResponse<ProductResponse> result = productService.getAllProducts(categoryId, lastProductId, locale, size);

        assertThat(result.products()).hasSize(2);
    }

    @Test
    @DisplayName("알맞은 카테고리 아이디 입력")
    public void getAllProducts() {
        Locale locale = Locale.of("KO");
        Category category = categoryRepository.findAll().getFirst();
        Product product = productRepository.findAll().getFirst();
        Long categoryId = category.getCategoryId();
        Long lastProductId = product.getProductId();
        int size = 4;

        CustomPageResponse<ProductResponse> result = productService.getAllProducts(categoryId, lastProductId, locale, size);

        assertThat(result.products()).hasSize(1);
    }

    @Test
    @DisplayName("제대로 상품 정보 출력")
    public void getProduct() {
        Locale locale = Locale.of("KO");
        Language language = Language.parseLanguage(locale.getLanguage());
        Product randomProduct = productRepository.findAll().getFirst();
        Long productId = randomProduct.getProductId();
        Product product = productRepository.findByProductId(productId).orElseThrow(() -> new BusinessException(ExceptionCode.PRODUCT_NOT_FOUND));
        ProductInfo productInfo = productRepository.getProductInfoByProductId(productId, language).orElseThrow(() -> new BusinessException(ExceptionCode.PRODUCT_INFO_NOT_FOUND));
        ProductPrice price = productPriceRepository.findCurrentPriceByProduct(productId).orElseThrow(() -> new BusinessException(ExceptionCode.PRODUCT_PRICE_NOT_FOUND));

        ProductInfoResponse result = productService.getProduct(productId, locale);

        assertThat(result.productId()).isEqualTo(product.getProductId().toString());
        assertThat(result.name()).isEqualTo(productInfo.getName());
        assertThat(result.company()).isEqualTo(productInfo.getCompany());
        assertThat(result.price()).isEqualTo(price.getPrice());
        assertThat(result.briefDescription()).isEqualTo(productInfo.getBriefDescription());
        assertThat(result.thumbnailUrl()).isEqualTo(product.getThumbnailUrl());
        assertThat(result.contentImageUrl()).isEqualTo(productInfo.getContentImageUrl());
        assertThat(result.quantityDetails()).isEqualTo(productInfo.getQuantityDetails());
        assertThat(result.usage()).isEqualTo(productInfo.getUsage());
        assertThat(result.warningMessage()).isEqualTo(productInfo.getWarningMessage());
    }

    @Test
    @DisplayName("상품을 찾을 수 없음")
    public void failToFindProduct() {
        Locale locale = Locale.of("KO");
        Long invalidProductId = 1L;

        assertThatThrownBy(() -> productService.getProduct(invalidProductId, locale))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ExceptionCode.PRODUCT_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("언어가 한국어나 영어가 아님")
    public void enterWrongLanguage() {
        Locale locale = Locale.of("Invalid");
        Language language = Language.parseLanguage(locale.getLanguage());
        Product randomProduct = productRepository.findAll().getFirst();
        Long productId = randomProduct.getProductId();
        Product product = productRepository.findByProductId(productId).orElseThrow(() -> new BusinessException(ExceptionCode.PRODUCT_NOT_FOUND));
        ProductInfo productInfo = productRepository.getProductInfoByProductId(productId, language).orElseThrow(() -> new BusinessException(ExceptionCode.PRODUCT_INFO_NOT_FOUND));
        ProductPrice price = productPriceRepository.findCurrentPriceByProduct(productId).orElseThrow(() -> new BusinessException(ExceptionCode.PRODUCT_PRICE_NOT_FOUND));

        ProductInfoResponse result = productService.getProduct(productId, locale);

        assertThat(result.productId()).isEqualTo(product.getProductId().toString());
        assertThat(result.name()).isEqualTo(productInfo.getName());
        assertThat(result.company()).isEqualTo(productInfo.getCompany());
        assertThat(result.price()).isEqualTo(price.getPrice());
        assertThat(result.briefDescription()).isEqualTo(productInfo.getBriefDescription());
        assertThat(result.thumbnailUrl()).isEqualTo(product.getThumbnailUrl());
        assertThat(result.contentImageUrl()).isEqualTo(productInfo.getContentImageUrl());
        assertThat(result.quantityDetails()).isEqualTo(productInfo.getQuantityDetails());
        assertThat(result.usage()).isEqualTo(productInfo.getUsage());
        assertThat(result.warningMessage()).isEqualTo(productInfo.getWarningMessage());
    }
}