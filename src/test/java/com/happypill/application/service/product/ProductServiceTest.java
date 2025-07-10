package com.happypill.application.service.product;

import com.happypill.application.entity.*;
import com.happypill.application.entity.enums.Language;
import com.happypill.application.exception.custom.ExceptionCode;
import com.happypill.application.exception.global.BusinessException;
import com.happypill.application.repository.bestproduct.BestProductRepository;
import com.happypill.application.repository.category.CategoryRepository;
import com.happypill.application.repository.categoryinfo.CategoryInfoRepository;
import com.happypill.application.repository.product.ProductRepository;
import com.happypill.application.repository.productinfo.ProductInfoRepository;
import com.happypill.application.repository.productprice.ProductPriceRepository;
import com.happypill.application.service.product.response.*;
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
    private BestProductRepository bestProductRepository;

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

        Product productOne = Product.of(SnowflakeUtil.nextId(), 25000, 300, "www.product_firstThumbnail.com", categoryOne);
        Product productTwo = Product.of(SnowflakeUtil.nextId(), 8000, 12, "www.product_secondThumbnail.com", categoryOne);
        Product productThree = Product.of(SnowflakeUtil.nextId(), 8000, 12, "www.product_secondThumbnail.com", categoryOne);
        Product productFour = Product.of(SnowflakeUtil.nextId(), 8000, 12, "www.product_secondThumbnail.com", categoryOne);
        Product productFive = Product.of(SnowflakeUtil.nextId(), 8000, 12, "www.product_secondThumbnail.com", categoryOne);
        Product productSix = Product.of(SnowflakeUtil.nextId(), 8000, 12, "www.product_secondThumbnail.com", categoryOne);
        Product productSeven = Product.of(SnowflakeUtil.nextId(), 8000, 12, "www.product_secondThumbnail.com", categoryOne);
        Product productEight = Product.of(SnowflakeUtil.nextId(), 8000, 12, "www.product_secondThumbnail.com", categoryOne);
        List<Product> productList = Arrays.asList(productOne, productTwo, productThree, productFour, productFive, productSix, productSeven, productEight);

        ProductInfo productInfoOne = ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "비타민 C", "30 캡슐", "밥과 함께 드시오", "물과 함께 섭취하시오. 하루에 3개.", "Content image", "매우 강력한 마법의 알약", "삼성제약", "오줌이 노래져요", productOne);
        ProductInfo productInfoTwo = ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "Vitamin C", "30 capsules", "Take with meal", "Please take 3 capsules daily", "Content image", "Magic pill", "Samsung chemist", "it can make your pee yellow", productOne);
        ProductInfo productInfoThree = ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "비타민 K", "10 캡슐", "밥과 함께 3알 드세요", "과대 섭취 금지", "Content image", "매우 화려한 알약", "삼성제약", "이건 어디 좋은지 몰라요 저희도", productTwo);
        List<ProductInfo> productInfoList = Arrays.asList(productInfoOne, productInfoTwo, productInfoThree);

//        ProductPrice priceOne = ProductPrice.of(SnowflakeUtil.nextId(), 15000, false, productOne);
//        ProductPrice priceTwo = ProductPrice.of(SnowflakeUtil.nextId(), 25000, true, productOne);
//        ProductPrice priceThree = ProductPrice.of(SnowflakeUtil.nextId(), 8000, true, productTwo);
//        List<ProductPrice> productPriceList = Arrays.asList(priceOne, priceTwo, priceThree);

        categoryRepository.saveAll(categoryList);
        categoryInfoRepository.saveAll(categoryInfoList);
        productRepository.saveAll(productList);
        productInfoRepository.saveAll(productInfoList);
//        productPriceRepository.saveAll(productPriceList);
    }

    @AfterEach
    void clearDB() {
        bestProductRepository.deleteAll();
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
        Long categoryId = category.getId();
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
        Long categoryId = category.getId();
        Long lastProductId = product.getId();
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
        Long productId = randomProduct.getId();
        Product product = productRepository.findById(productId).orElseThrow(() -> new BusinessException(ExceptionCode.PRODUCT_NOT_FOUND));
        ProductInfo productInfo = productRepository.getProductInfoByProductId(productId, language).orElseThrow(() -> new BusinessException(ExceptionCode.PRODUCT_INFO_NOT_FOUND));
//        ProductPrice price = productPriceRepository.findCurrentPriceByProduct(productId).orElseThrow(() -> new BusinessException(ExceptionCode.PRODUCT_PRICE_NOT_FOUND));

        ProductInfoResponse result = productService.getProduct(productId, locale);

        assertThat(result.productId()).isEqualTo(product.getId().toString());
        assertThat(result.name()).isEqualTo(productInfo.getName());
        assertThat(result.company()).isEqualTo(productInfo.getCompany());
        assertThat(result.price()).isEqualTo(product.getPrice());
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
        Long productId = randomProduct.getId();
        Product product = productRepository.findById(productId).orElseThrow(() -> new BusinessException(ExceptionCode.PRODUCT_NOT_FOUND));
        ProductInfo productInfo = productRepository.getProductInfoByProductId(productId, language).orElseThrow(() -> new BusinessException(ExceptionCode.PRODUCT_INFO_NOT_FOUND));
//        ProductPrice price = productPriceRepository.findCurrentPriceByProduct(productId).orElseThrow(() -> new BusinessException(ExceptionCode.PRODUCT_PRICE_NOT_FOUND));

        ProductInfoResponse result = productService.getProduct(productId, locale);

        assertThat(result.productId()).isEqualTo(product.getId().toString());
        assertThat(result.name()).isEqualTo(productInfo.getName());
        assertThat(result.company()).isEqualTo(productInfo.getCompany());
//        assertThat(result.price()).isEqualTo(price.getPrice());
        assertThat(result.briefDescription()).isEqualTo(productInfo.getBriefDescription());
        assertThat(result.thumbnailUrl()).isEqualTo(product.getThumbnailUrl());
        assertThat(result.contentImageUrl()).isEqualTo(productInfo.getContentImageUrl());
        assertThat(result.quantityDetails()).isEqualTo(productInfo.getQuantityDetails());
        assertThat(result.usage()).isEqualTo(productInfo.getUsage());
        assertThat(result.warningMessage()).isEqualTo(productInfo.getWarningMessage());
    }

    @Test
    @DisplayName("다른 고객이 함께 본 상품 확인")
    public void getRecommendation() {
        List<Product> products = productRepository.findAllProducts();
        Product firstProduct = products.get(0);
        Product lastProduct = products.get(7);

        List<ProductRelatedResponse> recommendations = productService.getRecommendation();

        assertThat(recommendations.size()).isEqualTo(8);
        assertThat(recommendations.getFirst().productId()).isEqualTo(firstProduct.getId().toString());
        assertThat(recommendations.getFirst().thumbnailUrl()).isEqualTo(firstProduct.getThumbnailUrl());
        assertThat(recommendations.getFirst().price()).isEqualTo(firstProduct.getPrice());
        assertThat(recommendations.getLast().productId()).isEqualTo(lastProduct.getId().toString());
        assertThat(recommendations.getLast().thumbnailUrl()).isEqualTo(lastProduct.getThumbnailUrl());
        assertThat(recommendations.getLast().price()).isEqualTo(lastProduct.getPrice());
    }

    @Test
    @DisplayName("[모든 Best 상품 조회] Locale 이 EN 일 경우 상품 관련 정보는 영어로 출력한다.")
    void getBestProducts_1() {
        //given
        clearDB();

        Category categoryOne = Category.of(SnowflakeUtil.nextId(), "www.category_firstThumbnail.com", "www.category_firstBanner.com");
        Category categoryTwo = Category.of(SnowflakeUtil.nextId(), "www.category_secondThumbnail.com", "www.category_secondBanner.com");
        List<Category> categoryList = Arrays.asList(categoryOne, categoryTwo);

        CategoryInfo categoryInfoOne = CategoryInfo.of(SnowflakeUtil.nextId(), Language.KO, "비타민", "비타민은 몸에 좋아요",  categoryOne);
        CategoryInfo categoryInfoTwo = CategoryInfo.of(SnowflakeUtil.nextId(), Language.EN, "vitamin", "vitamin is necessary for health",  categoryOne);
        CategoryInfo categoryInfoThree = CategoryInfo.of(SnowflakeUtil.nextId(), Language.KO, "철분", "몸이 튼튼해져요", categoryTwo);
        CategoryInfo categoryInfoFour = CategoryInfo.of(SnowflakeUtil.nextId(), Language.EN, "Iron", "Helps strengthen the body", categoryTwo);
        List<CategoryInfo> categoryInfoList = Arrays.asList(categoryInfoOne, categoryInfoTwo, categoryInfoThree, categoryInfoFour);

        Product productOne = Product.of(SnowflakeUtil.nextId(), 25000, 300, "www.product_firstThumbnail.com", categoryOne);
        Product productTwo = Product.of(SnowflakeUtil.nextId(), 8000, 12, "www.product_secondThumbnail.com", categoryOne);
        Product productThree = Product.of(SnowflakeUtil.nextId(), 8000, 12, "www.product_secondThumbnail.com", categoryOne);
        Product productFour = Product.of(SnowflakeUtil.nextId(), 8000, 12, "www.product_secondThumbnail.com", categoryOne);
        Product productFive = Product.of(SnowflakeUtil.nextId(), 8000, 12, "www.product_secondThumbnail.com", categoryTwo);
        Product productSix = Product.of(SnowflakeUtil.nextId(), 8000, 12, "www.product_secondThumbnail.com", categoryTwo);
        Product productSeven = Product.of(SnowflakeUtil.nextId(), 8000, 12, "www.product_secondThumbnail.com", categoryTwo);
        Product productEight = Product.of(SnowflakeUtil.nextId(), 8000, 12, "www.product_secondThumbnail.com", categoryTwo);

        List<Product> productList = Arrays.asList(productOne, productTwo, productThree, productFour, productFive, productSix, productSeven, productEight);
        List<Product> bestProductList = Arrays.asList(productOne, productTwo, productFive);

        ProductInfo productInfoOne = ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "비타민 C", "30 캡슐", "밥과 함께 드시오", "물과 함께 섭취하시오. 하루에 3개.", "Content image", "매우 강력한 마법의 알약", "삼성제약", "오줌이 노래져요", productOne);
        ProductInfo productInfoTwo = ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "Vitamin C", "30 capsules", "Take with meal", "Please take 3 capsules daily", "Content image", "Magic pill", "Samsung chemist", "it can make your pee yellow", productOne);
        ProductInfo productInfoThree = ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "비타민 K", "10 캡슐", "밥과 함께 3알 드세요", "과대 섭취 금지", "Content image", "매우 화려한 알약", "삼성제약", "이건 어디 좋은지 몰라요 저희도", productTwo);
        ProductInfo productInfoFour = ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "Vitamin K", "10 capsules", "Take with meal", "Please take 3 capsules daily", "Content image", "Magic pill", "Samsung chemist", "it can make your pee yellow", productTwo);
        ProductInfo productInfoFive = ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "비타민 K", "10 캡슐", "밥과 함께 3알 드세요", "과대 섭취 금지", "Content image", "매우 화려한 알약", "삼성제약", "이건 어디 좋은지 몰라요 저희도", productThree);
        ProductInfo productInfoSix = ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "Vitamin K", "10 capsules", "Take with meal", "Please take 3 capsules daily", "Content image", "Magic pill", "Samsung chemist", "it can make your pee yellow", productThree);
        ProductInfo productInfoSeven = ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "비타민 D", "15 캡슐", "하루에 한 알", "햇빛 대신 섭취하세요", "Content image", "뼈에 좋아요", "삼성제약", "과다 복용 금지", productFour);
        ProductInfo productInfoEight = ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "Vitamin D", "15 capsules", "Once a day", "Take as sunlight replacement", "Content image", "Good for bones", "Samsung chemist", "Do not overdose", productFour);
        ProductInfo productInfoNine = ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "철분제", "60 정", "식후 복용", "변비 유발 주의", "Content image", "빈혈에 도움", "삼성제약", "과다 복용 금지", productFive);
        ProductInfo productInfoTen = ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "Iron Pill", "60 tablets", "After meal", "May cause constipation", "Content image", "Helps with anemia", "Samsung chemist", "Do not overdose", productFive);
        ProductInfo productInfoEleven = ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "오메가3", "90 정", "식후 복용", "기름기 많음", "Content image", "혈관 건강에 도움", "삼성제약", "임산부는 복용 전 의사 상담", productSix);
        ProductInfo productInfoTwelve = ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "Omega-3", "90 tablets", "After meal", "High in fat", "Content image", "Supports heart health", "Samsung chemist", "Consult doctor if pregnant", productSix);
        ProductInfo productInfoThirteen = ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "루테인", "30 정", "하루 한 알", "눈에 도움", "Content image", "눈 건강에 도움", "삼성제약", "야간운전 전 복용 금지", productSeven);
        ProductInfo productInfoFourteen = ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "Lutein", "30 tablets", "Once a day", "Supports vision", "Content image", "Good for eye health", "Samsung chemist", "Avoid before night driving", productSeven);
        ProductInfo productInfoFifteen = ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "칼슘제", "40 정", "식후 복용", "우유와 함께 복용 권장", "Content image", "뼈 건강에 도움", "삼성제약", "비타민D와 함께 섭취 권장", productEight);
        ProductInfo productInfoSixteen = ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "Calcium", "40 tablets", "After meal", "Recommended with milk", "Content image", "Supports bone health", "Samsung chemist", "Take with vitamin D", productEight);

        List<ProductInfo> productInfoList = Arrays.asList(productInfoOne, productInfoTwo, productInfoThree, productInfoFour, productInfoFive, productInfoSix, productInfoSeven, productInfoEight, productInfoNine, productInfoTen, productInfoEleven, productInfoTwelve, productInfoThirteen, productInfoFourteen, productInfoFifteen, productInfoSixteen);

        List<BestProduct> bestProducts = bestProductList.stream()
                .map(product -> BestProduct.of(SnowflakeUtil.nextId(), product))
                .toList();

        categoryRepository.saveAll(categoryList);
        categoryInfoRepository.saveAll(categoryInfoList);
        productRepository.saveAll(productList);
        productRepository.flush();
        productInfoRepository.saveAll(productInfoList);
        bestProductRepository.saveAll(bestProducts);

        Locale locale = Locale.ENGLISH;

        //when
        List<ProductListResponse> responseList = productService.getBestProducts(locale);

        //then
        assertThat(responseList)
                .extracting(ProductListResponse::getProductName)
                .allMatch(productName -> isEnglish(productName));
    }

    @Test
    @DisplayName("[모든 Best 상품 조회] 모든 상품 중 BestProduct 에 포함된 상품들만 조회한다.")
    void getBestProducts_2() {
        //given
        clearDB();

        Category categoryOne = Category.of(SnowflakeUtil.nextId(), "www.category_firstThumbnail.com", "www.category_firstBanner.com");
        Category categoryTwo = Category.of(SnowflakeUtil.nextId(), "www.category_secondThumbnail.com", "www.category_secondBanner.com");
        List<Category> categoryList = Arrays.asList(categoryOne, categoryTwo);

        CategoryInfo categoryInfoOne = CategoryInfo.of(SnowflakeUtil.nextId(), Language.KO, "비타민", "비타민은 몸에 좋아요",  categoryOne);
        CategoryInfo categoryInfoTwo = CategoryInfo.of(SnowflakeUtil.nextId(), Language.EN, "vitamin", "vitamin is necessary for health",  categoryOne);
        CategoryInfo categoryInfoThree = CategoryInfo.of(SnowflakeUtil.nextId(), Language.KO, "철분", "몸이 튼튼해져요", categoryTwo);
        CategoryInfo categoryInfoFour = CategoryInfo.of(SnowflakeUtil.nextId(), Language.EN, "Iron", "Helps strengthen the body", categoryTwo);
        List<CategoryInfo> categoryInfoList = Arrays.asList(categoryInfoOne, categoryInfoTwo, categoryInfoThree, categoryInfoFour);

        Product productOne = Product.of(SnowflakeUtil.nextId(), 25000, 300, "www.product_firstThumbnail.com", categoryOne);
        Product productTwo = Product.of(SnowflakeUtil.nextId(), 8000, 12, "www.product_secondThumbnail.com", categoryOne);
        Product productThree = Product.of(SnowflakeUtil.nextId(), 8000, 12, "www.product_secondThumbnail.com", categoryOne);
        Product productFour = Product.of(SnowflakeUtil.nextId(), 8000, 12, "www.product_secondThumbnail.com", categoryOne);
        Product productFive = Product.of(SnowflakeUtil.nextId(), 8000, 12, "www.product_secondThumbnail.com", categoryTwo);
        Product productSix = Product.of(SnowflakeUtil.nextId(), 8000, 12, "www.product_secondThumbnail.com", categoryTwo);
        Product productSeven = Product.of(SnowflakeUtil.nextId(), 8000, 12, "www.product_secondThumbnail.com", categoryTwo);
        Product productEight = Product.of(SnowflakeUtil.nextId(), 8000, 12, "www.product_secondThumbnail.com", categoryTwo);

        List<Product> productList = Arrays.asList(productOne, productTwo, productThree, productFour, productFive, productSix, productSeven, productEight);
        List<Product> bestProductList = Arrays.asList(productOne, productTwo, productFive);

        ProductInfo productInfoOne = ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "비타민 C", "30 캡슐", "밥과 함께 드시오", "물과 함께 섭취하시오. 하루에 3개.", "Content image", "매우 강력한 마법의 알약", "삼성제약", "오줌이 노래져요", productOne);
        ProductInfo productInfoTwo = ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "Vitamin C", "30 capsules", "Take with meal", "Please take 3 capsules daily", "Content image", "Magic pill", "Samsung chemist", "it can make your pee yellow", productOne);
        ProductInfo productInfoThree = ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "비타민 K", "10 캡슐", "밥과 함께 3알 드세요", "과대 섭취 금지", "Content image", "매우 화려한 알약", "삼성제약", "이건 어디 좋은지 몰라요 저희도", productTwo);
        ProductInfo productInfoFour = ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "Vitamin K", "10 capsules", "Take with meal", "Please take 3 capsules daily", "Content image", "Magic pill", "Samsung chemist", "it can make your pee yellow", productTwo);
        ProductInfo productInfoFive = ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "비타민 K", "10 캡슐", "밥과 함께 3알 드세요", "과대 섭취 금지", "Content image", "매우 화려한 알약", "삼성제약", "이건 어디 좋은지 몰라요 저희도", productThree);
        ProductInfo productInfoSix = ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "Vitamin K", "10 capsules", "Take with meal", "Please take 3 capsules daily", "Content image", "Magic pill", "Samsung chemist", "it can make your pee yellow", productThree);
        ProductInfo productInfoSeven = ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "비타민 D", "15 캡슐", "하루에 한 알", "햇빛 대신 섭취하세요", "Content image", "뼈에 좋아요", "삼성제약", "과다 복용 금지", productFour);
        ProductInfo productInfoEight = ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "Vitamin D", "15 capsules", "Once a day", "Take as sunlight replacement", "Content image", "Good for bones", "Samsung chemist", "Do not overdose", productFour);
        ProductInfo productInfoNine = ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "철분제", "60 정", "식후 복용", "변비 유발 주의", "Content image", "빈혈에 도움", "삼성제약", "과다 복용 금지", productFive);
        ProductInfo productInfoTen = ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "Iron Pill", "60 tablets", "After meal", "May cause constipation", "Content image", "Helps with anemia", "Samsung chemist", "Do not overdose", productFive);
        ProductInfo productInfoEleven = ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "오메가3", "90 정", "식후 복용", "기름기 많음", "Content image", "혈관 건강에 도움", "삼성제약", "임산부는 복용 전 의사 상담", productSix);
        ProductInfo productInfoTwelve = ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "Omega-3", "90 tablets", "After meal", "High in fat", "Content image", "Supports heart health", "Samsung chemist", "Consult doctor if pregnant", productSix);
        ProductInfo productInfoThirteen = ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "루테인", "30 정", "하루 한 알", "눈에 도움", "Content image", "눈 건강에 도움", "삼성제약", "야간운전 전 복용 금지", productSeven);
        ProductInfo productInfoFourteen = ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "Lutein", "30 tablets", "Once a day", "Supports vision", "Content image", "Good for eye health", "Samsung chemist", "Avoid before night driving", productSeven);
        ProductInfo productInfoFifteen = ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "칼슘제", "40 정", "식후 복용", "우유와 함께 복용 권장", "Content image", "뼈 건강에 도움", "삼성제약", "비타민D와 함께 섭취 권장", productEight);
        ProductInfo productInfoSixteen = ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "Calcium", "40 tablets", "After meal", "Recommended with milk", "Content image", "Supports bone health", "Samsung chemist", "Take with vitamin D", productEight);

        List<ProductInfo> productInfoList = Arrays.asList(productInfoOne, productInfoTwo, productInfoThree, productInfoFour, productInfoFive, productInfoSix, productInfoSeven, productInfoEight, productInfoNine, productInfoTen, productInfoEleven, productInfoTwelve, productInfoThirteen, productInfoFourteen, productInfoFifteen, productInfoSixteen);

        List<BestProduct> bestProducts = bestProductList.stream()
                .map(product -> BestProduct.of(SnowflakeUtil.nextId(), product))
                .toList();

        categoryRepository.saveAll(categoryList);
        categoryInfoRepository.saveAll(categoryInfoList);
        productRepository.saveAll(productList);
        productRepository.flush();
        productInfoRepository.saveAll(productInfoList);
        bestProductRepository.saveAll(bestProducts);

        Locale locale = Locale.ENGLISH;

        //when
        List<ProductListResponse> responseList = productService.getBestProducts(locale);

        //then
        assertThat(responseList).hasSize(3);
    }

    private boolean isEnglish(String text) {
        return text.chars().allMatch(ch -> (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || ch == ' ');
    }
}