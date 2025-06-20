package com.happypill.application.service.admin;

import com.happypill.application.entity.Category;
import com.happypill.application.entity.Product;
import com.happypill.application.entity.ProductInfo;
import com.happypill.application.entity.ProductPrice;
import com.happypill.application.entity.enums.Language;
import com.happypill.application.exception.custom.ExceptionCode;
import com.happypill.application.exception.global.BusinessException;
import com.happypill.application.pagination.CustomPage;
import com.happypill.application.repository.category.CategoryRepository;
import com.happypill.application.repository.product.ProductRepository;
import com.happypill.application.repository.productinfo.ProductInfoRepository;
import com.happypill.application.repository.productprice.ProductPriceRepository;
import com.happypill.application.service.admin.request.AdminProductCreateRequest;
import com.happypill.application.service.admin.request.AdminProductUpdateRequest;
import com.happypill.application.service.admin.response.AdminProductInfoResponse;
import com.happypill.application.service.admin.response.AdminProductListResponse;
import com.happypill.application.service.admin.response.AdminProductPriceResponse;
import com.happypill.application.service.product.request.ProductInfoRequest;
import com.happypill.application.util.SnowflakeUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class AdminProductServiceTest {

    @Autowired
    private AdminProductService adminProductService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductInfoRepository productInfoRepository;

    @Autowired
    private ProductPriceRepository productPriceRepository;

    @Test
    @DisplayName("[특정 상품 조회] ProductPrice 가 존재하지 않으면 에러가 발생한다.")
    void getProductDetails_1() {
        //given
        Category category = Category.of(SnowflakeUtil.nextId(), " https://xxx.com/xxx", " https://xxx.com/xxx");
        categoryRepository.save(category);

        Product product = Product.of(SnowflakeUtil.nextId(), 1000, 3, true, " https://xxx.com/xxx", false, category);
        productRepository.save(product);

        List<ProductInfo> productInfo = Arrays.asList(
                ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "제품명_KO", "수량 상세_KO", "경고 메시지_KO", "사용법_KO", "https://xxx.com/xxx_KO", "설명_KO", "회사명_KO", "간략 설명_KO", product),
                ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "제품명_EN", "수량 상세_EN", "경고 메시지_EN", "사용법_EN", "https://xxx.com/xxx_EN", "설명_EN", "회사명_EN", "간략 설명_EN", product)
        );
        productInfoRepository.saveAll(productInfo);

        //when
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            adminProductService.getProductDetails(product.getId());
        });

        //then
        assertThat(exception.getExceptionCode()).isEqualTo(ExceptionCode.PRODUCT_PRICE_NOT_FOUND);
    }

    @Test
    @DisplayName("[특정 상품 조회] ProductInfo 가 존재하지 않으면 에러가 발생한다.")
    void getProductDetails_2() {
        //given
        Category category = Category.of(SnowflakeUtil.nextId(), " https://xxx.com/xxx", " https://xxx.com/xxx");
        categoryRepository.save(category);

        Product product = Product.of(SnowflakeUtil.nextId(), 1000, 3, true, " https://xxx.com/xxx", false, category);
        productRepository.save(product);

        ProductPrice productPrice = ProductPrice.of(SnowflakeUtil.nextId(), 3500, true, product);
        productPriceRepository.save(productPrice);

        //when
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            adminProductService.getProductDetails(product.getId());
        });

        // then
        assertThat(exception.getExceptionCode()).isEqualTo(ExceptionCode.PRODUCT_INFO_NOT_FOUND);
    }

    @Test
    @DisplayName("[특정 상품 조회] Product, ProductInfo, ProductPrice 가 존재하면 200 상태코드로 응답한다.")
    void getProductDetails_3() {
        //given
        Category category = Category.of(SnowflakeUtil.nextId(), " https://xxx.com/xxx", " https://xxx.com/xxx");
        categoryRepository.save(category);

        Product product = Product.of(SnowflakeUtil.nextId(), 1000, 3, true, " https://xxx.com/xxx", false, category);
        productRepository.save(product);

        List<ProductInfo> productInfo = Arrays.asList(
                ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "제품명_KO", "수량 상세_KO", "경고 메시지_KO", "사용법_KO", "https://xxx.com/xxx_KO", "설명_KO", "회사명_KO", "간략 설명_KO", product),
                ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "제품명_EN", "수량 상세_EN", "경고 메시지_EN", "사용법_EN", "https://xxx.com/xxx_EN", "설명_EN", "회사명_EN", "간략 설명_EN", product)
        );
        productInfoRepository.saveAll(productInfo);

        ProductPrice productPrice = ProductPrice.of(SnowflakeUtil.nextId(), 3500, true, product);
        productPriceRepository.save(productPrice);

        //when
        AdminProductInfoResponse response = adminProductService.getProductDetails(product.getId());

        //then
        assertThat(response).isNotNull();
        assertThat(response.productId()).isEqualTo(String.valueOf(product.getId()));
        assertThat(response.productInfo())
                .anySatisfy(info -> {
                    assertThat(info.name()).isEqualTo("제품명_KO");
                    assertThat(info.company()).isEqualTo("회사명_KO");
                });
    }

    @Test
    @DisplayName("[모든 상품 조회] categoryId, locale, pageable 값이 올바르게 주어졌을 때 커스텀 페이지의 contents 에는 1개의 값이 포함된다.")
    void getAllProducts_1() {
        //given
        Category category = Category.of(SnowflakeUtil.nextId(), " https://xxx.com/xxx", " https://xxx.com/xxx");
        categoryRepository.save(category);

        Product product = Product.of(SnowflakeUtil.nextId(), 1000, 3, true, " https://xxx.com/xxx", false, category);
        productRepository.save(product);

        List<ProductInfo> productInfo = Arrays.asList(
                ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "제품명_KO", "수량 상세_KO", "경고 메시지_KO", "사용법_KO", "https://xxx.com/xxx_KO", "설명_KO", "회사명_KO", "간략 설명_KO", product),
                ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "제품명_EN", "수량 상세_EN", "경고 메시지_EN", "사용법_EN", "https://xxx.com/xxx_EN", "설명_EN", "회사명_EN", "간략 설명_EN", product)
        );
        productInfoRepository.saveAll(productInfo);

        ProductPrice productPrice = ProductPrice.of(SnowflakeUtil.nextId(), 3500, true, product);
        productPriceRepository.save(productPrice);

        Locale locale = Locale.forLanguageTag("ko");
        Pageable pageable = PageRequest.of(0, 10);

        //when
        CustomPage<AdminProductListResponse> result = adminProductService.getAllProducts(category.getId(), pageable, locale);

        //then
        assertThat(result.contents()).isNotNull();
        assertThat(result.contents()).hasSize(1);
    }

    @Test
    @DisplayName("[모든 상품 조회] categoryId 값이 null 값이 아닌 존재하지 않는 값인 경우 에러가 발생한다.")
    void getAllProducts_2() {
        //given
        Category category = Category.of(SnowflakeUtil.nextId(), " https://xxx.com/xxx", " https://xxx.com/xxx");
        categoryRepository.save(category);

        Product product = Product.of(SnowflakeUtil.nextId(), 1000, 3, true, " https://xxx.com/xxx", false, category);
        productRepository.save(product);

        List<ProductInfo> productInfo = Arrays.asList(
                ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "제품명_KO", "수량 상세_KO", "경고 메시지_KO", "사용법_KO", "https://xxx.com/xxx_KO", "설명_KO", "회사명_KO", "간략 설명_KO", product),
                ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "제품명_EN", "수량 상세_EN", "경고 메시지_EN", "사용법_EN", "https://xxx.com/xxx_EN", "설명_EN", "회사명_EN", "간략 설명_EN", product)
        );
        productInfoRepository.saveAll(productInfo);

        ProductPrice productPrice = ProductPrice.of(SnowflakeUtil.nextId(), 3500, true, product);
        productPriceRepository.save(productPrice);

        Locale locale = Locale.forLanguageTag("ko");
        Pageable pageable = PageRequest.of(0, 10);

        //when
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            adminProductService.getAllProducts(1000L, pageable, locale);
        });

        // then
        assertThat(exception.getExceptionCode()).isEqualTo(ExceptionCode.CATEGORY_NOT_FOUND);
    }

    @Test
    @DisplayName("[모든 상품 조회] locale 값이 en인 경우 커스텀 페이지의 contents 에는 영어로 작성된 내용들이 포함된다.")
    void getAllProducts_3() {
        //given
        Category category = Category.of(SnowflakeUtil.nextId(), " https://xxx.com/xxx", " https://xxx.com/xxx");
        categoryRepository.save(category);

        Product product = Product.of(SnowflakeUtil.nextId(), 1000, 3, true, " https://xxx.com/xxx", false, category);
        productRepository.save(product);

        List<ProductInfo> productInfo = Arrays.asList(
                ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "제품명_KO", "수량 상세_KO", "경고 메시지_KO", "사용법_KO", "https://xxx.com/xxx_KO", "설명_KO", "회사명_KO", "간략 설명_KO", product),
                ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "제품명_EN", "수량 상세_EN", "경고 메시지_EN", "사용법_EN", "https://xxx.com/xxx_EN", "설명_EN", "회사명_EN", "간략 설명_EN", product)
        );
        productInfoRepository.saveAll(productInfo);

        ProductPrice productPrice = ProductPrice.of(SnowflakeUtil.nextId(), 3500, true, product);
        productPriceRepository.save(productPrice);

        Locale locale = Locale.forLanguageTag("en");
        Pageable pageable = PageRequest.of(0, 10);

        //when
        CustomPage<AdminProductListResponse> result = adminProductService.getAllProducts(category.getId(), pageable, locale);

        //then
        assertThat(result.contents())
                .extracting(AdminProductListResponse::briefDescription)
                .containsExactly("간략 설명_EN");
    }

    @Test
    @DisplayName("[금액 기록 조회] product 와 productPrice 가 존재하는 경우 productPrice 를 반환한다.")
    void getAllProductPrices_1() {
        //given
        Category category = Category.of(SnowflakeUtil.nextId(), " https://xxx.com/xxx", " https://xxx.com/xxx");
        categoryRepository.save(category);

        Product product = Product.of(SnowflakeUtil.nextId(), 1000, 3, true, " https://xxx.com/xxx", false, category);
        productRepository.save(product);

        List<ProductInfo> productInfo = Arrays.asList(
                ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "제품명_KO", "수량 상세_KO", "경고 메시지_KO", "사용법_KO", "https://xxx.com/xxx_KO", "설명_KO", "회사명_KO", "간략 설명_KO", product),
                ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "제품명_EN", "수량 상세_EN", "경고 메시지_EN", "사용법_EN", "https://xxx.com/xxx_EN", "설명_EN", "회사명_EN", "간략 설명_EN", product)
        );
        productInfoRepository.saveAll(productInfo);

        ProductPrice productPrice = ProductPrice.of(SnowflakeUtil.nextId(), 3500, true, product);
        productPriceRepository.save(productPrice);

        Pageable pageable = PageRequest.of(0, 5);

        //when
        CustomPage<AdminProductPriceResponse> customPage = adminProductService.getAllProductPrices(product.getId(), pageable);

        //then
        assertThat(customPage.contents())
                .extracting(AdminProductPriceResponse::price)
                .contains(3500);
    }

    @Test
    @DisplayName("[금액 기록 조회]  productId 가 존재하지 않는 값인 경우 에러가 발생한다.")
    void getAllProductPrices_2() {
        //given
        Category category = Category.of(SnowflakeUtil.nextId(), " https://xxx.com/xxx", " https://xxx.com/xxx");
        categoryRepository.save(category);

        Product product = Product.of(SnowflakeUtil.nextId(), 1000, 3, true, " https://xxx.com/xxx", false, category);
        productRepository.save(product);

        List<ProductInfo> productInfo = Arrays.asList(
                ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "제품명_KO", "수량 상세_KO", "경고 메시지_KO", "사용법_KO", "https://xxx.com/xxx_KO", "설명_KO", "회사명_KO", "간략 설명_KO", product),
                ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "제품명_EN", "수량 상세_EN", "경고 메시지_EN", "사용법_EN", "https://xxx.com/xxx_EN", "설명_EN", "회사명_EN", "간략 설명_EN", product)
        );
        productInfoRepository.saveAll(productInfo);

        ProductPrice productPrice = ProductPrice.of(SnowflakeUtil.nextId(), 3500, true, product);
        productPriceRepository.save(productPrice);

        Pageable pageable = PageRequest.of(0, 5);

        //when
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            adminProductService.getAllProductPrices(1000L, pageable);
        });

        //then
        assertThat(exception.getExceptionCode()).isEqualTo(ExceptionCode.PRODUCT_NOT_FOUND);
    }

    @Test
    @DisplayName("[상품 등록] 한국어로 된 ProductInfo 가 없으면 에러를 반환한다.")
    void createProduct_1() {
        //given
        Category category = Category.of(SnowflakeUtil.nextId(), " https://xxx.com/xxx", " https://xxx.com/xxx");
        categoryRepository.save(category);

        Product product = Product.of(SnowflakeUtil.nextId(), 1000, 3, true, " https://xxx.com/xxx", false, category);
        productRepository.save(product);

        List<ProductInfo> productInfo = Arrays.asList(
                ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "제품명_KO", "수량 상세_KO", "경고 메시지_KO", "사용법_KO", "https://xxx.com/xxx_KO", "설명_KO", "회사명_KO", "간략 설명_KO", product),
                ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "제품명_EN", "수량 상세_EN", "경고 메시지_EN", "사용법_EN", "https://xxx.com/xxx_EN", "설명_EN", "회사명_EN", "간략 설명_EN", product)
        );
        productInfoRepository.saveAll(productInfo);

        ProductPrice productPrice = ProductPrice.of(SnowflakeUtil.nextId(), 3500, true, product);
        productPriceRepository.save(productPrice);

        List<ProductInfoRequest> productInfoList = List.of(
                new ProductInfoRequest(Language.EN, "상품명_EN", "간단설명_EN", "상세설명_EN", "https://xxx.com/xxx", "회사명_EN", "용량_EN", "섭취방법_EN", "주의사항_EN")
        );
        AdminProductCreateRequest request = new AdminProductCreateRequest(category.getId(), "https://xxx.com/xxx", true, 33, 30000, productInfoList);

        //when //then
        assertThatThrownBy(() -> adminProductService.createProduct(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ExceptionCode.KO_LANGUAGE_REQUIRED.getMessage());
    }

    @Test
    @DisplayName("[상품 등록] AdminProductCreateRequest 필드 모두 유효하면 ProductId 를 반환한다.")
    void createProduct_2() {
        //given
        Category category = Category.of(SnowflakeUtil.nextId(), " https://xxx.com/xxx", " https://xxx.com/xxx");
        categoryRepository.save(category);

        Product product = Product.of(SnowflakeUtil.nextId(), 1000, 3, true, " https://xxx.com/xxx", false, category);
        productRepository.save(product);

        List<ProductInfo> productInfo = Arrays.asList(
                ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "제품명_KO", "수량 상세_KO", "경고 메시지_KO", "사용법_KO", "https://xxx.com/xxx_KO", "설명_KO", "회사명_KO", "간략 설명_KO", product),
                ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "제품명_EN", "수량 상세_EN", "경고 메시지_EN", "사용법_EN", "https://xxx.com/xxx_EN", "설명_EN", "회사명_EN", "간략 설명_EN", product)
        );
        productInfoRepository.saveAll(productInfo);

        ProductPrice productPrice = ProductPrice.of(SnowflakeUtil.nextId(), 3500, true, product);
        productPriceRepository.save(productPrice);

        List<ProductInfoRequest> productInfoList = List.of(
                new ProductInfoRequest(Language.KO, "상품명_KO", "간단설명_KO", "상세설명_KO", "https://xxx.com/xxx", "회사명_KO", "용량_KO", "섭취방법_KO", "주의사항_KO"),
                new ProductInfoRequest(Language.EN, "상품명_EN", "간단설명_EN", "상세설명_EN", "https://xxx.com/xxx", "회사명_EN", "용량_EN", "섭취방법_EN", "주의사항_EN")
        );
        AdminProductCreateRequest request = new AdminProductCreateRequest(category.getId(), "https://xxx.com/xxx", true, 33, 30000, productInfoList);

        //when
        long productId = adminProductService.createProduct(request);

        //then
        assertThat(productId).isGreaterThan(0);
    }

    @Test
    @DisplayName("[상품 등록] Category 가 존재하지 않으면 에러를 반환한다.")
    void createProduct_4() {
        //given
        Category category = Category.of(SnowflakeUtil.nextId(), "https://xxx.com/xxx", "https://xxx.com/xxx");
        categoryRepository.save(category);

        Product product = Product.of(SnowflakeUtil.nextId(), 1000, 3, true, "https://xxx.com/xxx", false, category);
        productRepository.save(product);

        List<ProductInfo> productInfo = Arrays.asList(
                ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "제품명_KO", "수량 상세_KO", "경고 메시지_KO", "사용법_KO", "https://xxx.com/xxx_KO", "설명_KO", "회사명_KO", "간략 설명_KO", product),
                ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "제품명_EN", "수량 상세_EN", "경고 메시지_EN", "사용법_EN", "https://xxx.com/xxx_EN", "설명_EN", "회사명_EN", "간략 설명_EN", product)
        );
        productInfoRepository.saveAll(productInfo);

        ProductPrice productPrice = ProductPrice.of(SnowflakeUtil.nextId(), 3500, true, product);
        productPriceRepository.save(productPrice);

        List<ProductInfoRequest> productInfoList = List.of(
                new ProductInfoRequest(Language.KO, "상품명_KO", "간단설명_KO", "상세설명_KO", "https://xxx.com/xxx", "회사명_KO", "용량_KO", "섭취방법_KO", "주의사항_KO"),
                new ProductInfoRequest(Language.EN, "상품명_EN", "간단설명_EN", "상세설명_EN", "https://xxx.com/xxx", "회사명_EN", "용량_EN", "섭취방법_EN", "주의사항_EN")
        );
        AdminProductCreateRequest request = new AdminProductCreateRequest(1000L, "https://xxx.com/xxx", true, 33, 30000, productInfoList);

        //when //then
        assertThatThrownBy(()-> adminProductService.createProduct(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ExceptionCode.CATEGORY_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("[상품 수정] 경로 변수의 productId가 존재하지 않는 Product 면 에러를 반환한다.")
    void updateProduct_1(){
        //given
        Category category = Category.of(SnowflakeUtil.nextId(), "https://xxx.com/xxx", "https://xxx.com/xxx");
        categoryRepository.save(category);

        Product product = Product.of(SnowflakeUtil.nextId(), 1000, 3, true, "https://xxx.com/xxx", false, category);
        productRepository.save(product);

        List<ProductInfo> productInfo = Arrays.asList(
                ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "제품명_KO", "수량 상세_KO", "경고 메시지_KO", "사용법_KO", "https://xxx.com/xxx_KO", "설명_KO", "회사명_KO", "간략 설명_KO", product),
                ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "제품명_EN", "수량 상세_EN", "경고 메시지_EN", "사용법_EN", "https://xxx.com/xxx_EN", "설명_EN", "회사명_EN", "간략 설명_EN", product)
        );
        productInfoRepository.saveAll(productInfo);

        ProductPrice productPrice = ProductPrice.of(SnowflakeUtil.nextId(), 3500, true, product);
        productPriceRepository.save(productPrice);

        List<ProductInfoRequest> productInfos = List.of(
                new ProductInfoRequest(Language.KO, "비타민C 1000", "건강을 위한 비타민", "하루 한 알로 충분한 비타민C 섭취", "https://update.com/xxx", "헬스케어코리아", "100정", "하루 1회 1정 섭취", "과다 섭취 시 부작용이 있을 수 있습니다."),
                new ProductInfoRequest(Language.EN, "Vitamin C 1000", "Vitamin for your health", "One pill a day provides sufficient vitamin C", "https://update.com/xxx", "Healthcare Korea", "100 tablets", "Take 1 tablet daily", "Overconsumption may cause side effects.")
        );
        AdminProductUpdateRequest request = new AdminProductUpdateRequest(category.getId(), "https://update.com/xxx", true, 30, 2990, productInfos);

        //when //then
        assertThatThrownBy(()->adminProductService.updateProduct(1000L, request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ExceptionCode.PRODUCT_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("[상품 수정] price 수정 시 기존의 ProductPrice 엔티티의 isUsed 필드는 false 로 설정하고 새로운 ProductPrice 를 생성한다.")
    void updateProduct_2(){
        //given
        Category category = Category.of(SnowflakeUtil.nextId(), "https://xxx.com/xxx", "https://xxx.com/xxx");
        categoryRepository.save(category);

        Product product = Product.of(SnowflakeUtil.nextId(), 1000, 3, true, "https://xxx.com/xxx", false, category);
        productRepository.save(product);

        List<ProductInfo> productInfo = Arrays.asList(
                ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "제품명_KO", "수량 상세_KO", "경고 메시지_KO", "사용법_KO", "https://xxx.com/xxx_KO", "설명_KO", "회사명_KO", "간략 설명_KO", product),
                ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "제품명_EN", "수량 상세_EN", "경고 메시지_EN", "사용법_EN", "https://xxx.com/xxx_EN", "설명_EN", "회사명_EN", "간략 설명_EN", product)
        );
        productInfoRepository.saveAll(productInfo);

        ProductPrice productPrice = ProductPrice.of(SnowflakeUtil.nextId(), 3500, true, product);
        productPriceRepository.save(productPrice);

        List<ProductInfoRequest> productInfos = List.of(
                new ProductInfoRequest(Language.KO, "비타민C 1000", "건강을 위한 비타민", "하루 한 알로 충분한 비타민C 섭취", "https://update.com/xxx", "헬스케어코리아", "100정", "하루 1회 1정 섭취", "과다 섭취 시 부작용이 있을 수 있습니다."),
                new ProductInfoRequest(Language.EN, "Vitamin C 1000", "Vitamin for your health", "One pill a day provides sufficient vitamin C", "https://update.com/xxx", "Healthcare Korea", "100 tablets", "Take 1 tablet daily", "Overconsumption may cause side effects.")
        );
        AdminProductUpdateRequest request = new AdminProductUpdateRequest(category.getId(), "https://update.com/xxx", true, 30, 2990, productInfos);

        //when
        adminProductService.updateProduct(product.getId(), request);

        // then
        ProductPrice originalPrice = productPriceRepository.findById(productPrice.getId()).orElseThrow();
        assertThat(originalPrice.isUsed()).isFalse();

        ProductPrice updatedPrice = productPriceRepository.findCurrentPriceByProduct(product.getId()).orElseThrow();
        assertThat(updatedPrice.isUsed()).isTrue();
        assertThat(updatedPrice.getPrice()).isEqualTo(2990);
    }

    @Test
    @DisplayName("[상품 수정] 기존에 한국어와 영어로 된 ProductInfo 가 있을 때 한국어로 된 ProductInfo 만 수정 시 영어로 된 ProductInfo 는 수정되지 않고 한국어로 된 ProductInfo 만 수정된다.")
    void updateProduct_3(){
        //given
        Category category = Category.of(SnowflakeUtil.nextId(), "https://xxx.com/xxx", "https://xxx.com/xxx");
        categoryRepository.save(category);

        Product product = Product.of(SnowflakeUtil.nextId(), 1000, 3, true, "https://xxx.com/xxx", false, category);
        productRepository.save(product);

        List<ProductInfo> productInfo = Arrays.asList(
                ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "제품명_KO", "수량 상세_KO", "경고 메시지_KO", "사용법_KO", "https://xxx.com/xxx_KO", "설명_KO", "회사명_KO", "간략 설명_KO", product),
                ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "제품명_EN", "수량 상세_EN", "경고 메시지_EN", "사용법_EN", "https://xxx.com/xxx_EN", "설명_EN", "회사명_EN", "간략 설명_EN", product)
        );
        productInfoRepository.saveAll(productInfo);

        ProductPrice productPrice = ProductPrice.of(SnowflakeUtil.nextId(), 3500, true, product);
        productPriceRepository.save(productPrice);

        List<ProductInfoRequest> productInfos = List.of(
                new ProductInfoRequest(Language.KO, "비타민C 1000", "건강을 위한 비타민", "하루 한 알로 충분한 비타민C 섭취", "https://update.com/xxx", "헬스케어코리아", "100정", "하루 1회 1정 섭취", "과다 섭취 시 부작용이 있을 수 있습니다.")
        );
        AdminProductUpdateRequest request = new AdminProductUpdateRequest(category.getId(), "https://update.com/xxx", true, 30, 2990, productInfos);

        //when
        adminProductService.updateProduct(product.getId(), request);

        //then
        List<ProductInfo> productInfoList = productInfoRepository.findAllByProductId(product.getId());
        assertThat(productInfoList)
                .extracting(ProductInfo::getName)
                .anyMatch(name -> name.contains("EN"));
    }

    @Test
    @DisplayName("[상품 삭제] 경로 변수의 productId가 존재하지 않는 Product면 에러를 반환한다.")
    void deleteProduct_1(){
        //given
        Category category = Category.of(SnowflakeUtil.nextId(), "https://xxx.com/xxx", "https://xxx.com/xxx");
        categoryRepository.save(category);

        Product product = Product.of(SnowflakeUtil.nextId(), 1000, 3, true, "https://xxx.com/xxx", false, category);
        productRepository.save(product);

        //when //then
        assertThatThrownBy(()->adminProductService.deleteProduct(1000L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ExceptionCode.PRODUCT_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("[상품 삭제] 상품이 삭제되면 isAvailable 은 false, isDeleted 는 true 가 된다.")
    void deleteProduct_2(){
        //given
        Category category = Category.of(SnowflakeUtil.nextId(), "https://xxx.com/xxx", "https://xxx.com/xxx");
        categoryRepository.save(category);

        Product product = Product.of(SnowflakeUtil.nextId(), 1000, 3, true, "https://xxx.com/xxx", false, category);
        productRepository.save(product);

        //when
        adminProductService.deleteProduct(product.getId());

        //then
        Product updatedProduct = productRepository.findById(product.getId()).orElseThrow();
        assertThat(updatedProduct.isAvailable()).isFalse();
        assertThat(updatedProduct.isDeleted()).isTrue();
    }
}