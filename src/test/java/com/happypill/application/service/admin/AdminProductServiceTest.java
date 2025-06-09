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
import com.happypill.application.service.admin.response.AdminProductInfoResponse;
import com.happypill.application.service.admin.response.AdminProductListResponse;
import com.happypill.application.service.admin.response.AdminProductPriceResponse;
import com.happypill.application.service.product.request.ProductInfoRequest;
import com.happypill.application.util.SnowflakeUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

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

    private Category savedCategory;
    private Product savedProduct;

    @BeforeEach
    void setUp() {
        Category category = Category.of(SnowflakeUtil.nextId(), " https://xxx.com/xxx", " https://xxx.com/xxx");
        savedCategory = categoryRepository.save(category);

        Product product = Product.of(SnowflakeUtil.nextId(), 3, true, " https://xxx.com/xxx", false, category);
        savedProduct = productRepository.save(product);

        List<ProductInfo> productInfo = Arrays.asList(
                ProductInfo.of(SnowflakeUtil.nextId(), Language.KO, "제품명_KO", "수량 상세_KO", "경고 메시지_KO", "사용법_KO", "https://xxx.com/xxx_KO", "설명_KO", "회사명_KO", "간략 설명_KO", product),
                ProductInfo.of(SnowflakeUtil.nextId(), Language.EN, "제품명_EN", "수량 상세_EN", "경고 메시지_EN", "사용법_EN", "https://xxx.com/xxx_EN", "설명_EN", "회사명_EN", "간략 설명_EN", product)
        );
        productInfoRepository.saveAll(productInfo);

        ProductPrice productPrice = ProductPrice.of(SnowflakeUtil.nextId(), 3500, true, product);
        productPriceRepository.save(productPrice);
    }

    @Test
    @DisplayName("[특정 상품 조회] ProductPrice 가 존재하지 않으면 에러가 발생한다.")
    void getProductDetails_1() {
        //given
        productPriceRepository.deleteAllInBatch();

        //when
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            adminProductService.getProductDetails(savedProduct.getProductId());
        });

        //then
        assertThat(exception.getExceptionCode()).isEqualTo(ExceptionCode.PRODUCT_PRICE_NOT_FOUND);
    }

    @Test
    @DisplayName("[특정 상품 조회] ProductInfo 가 존재하지 않으면 에러가 발생한다.")
    void getProductDetails_2() {
        //given
        productInfoRepository.deleteAllInBatch();

        //when
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            adminProductService.getProductDetails(savedProduct.getProductId());
        });

        // then
        assertThat(exception.getExceptionCode()).isEqualTo(ExceptionCode.PRODUCT_INFO_NOT_FOUND);
    }

    @Test
    @DisplayName("[특정 상품 조회] Product, ProductInfo, ProductPrice 가 존재하면 200 상태코드로 응답한다.")
    void getProductDetails_3() {
        //when
        AdminProductInfoResponse response = adminProductService.getProductDetails(savedProduct.getProductId());

        //then
        assertThat(response).isNotNull();
        assertThat(response.productId()).isEqualTo(savedProduct.getProductId());
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
        Locale locale = Locale.forLanguageTag("ko");
        Pageable pageable = PageRequest.of(0, 10);

        //when
        CustomPage<AdminProductListResponse> result = adminProductService.getAllProducts(savedCategory.getCategoryId(), pageable, locale);

        //then
        assertThat(result.contents()).isNotNull();
        assertThat(result.contents()).hasSize(1);
    }

    @Test
    @DisplayName("[모든 상품 조회] categoryId 값이 null 값이 아닌 존재하지 않는 값인 경우 에러가 발생한다.")
    void getAllProducts_2() {
        //given
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
        Locale locale = Locale.forLanguageTag("en");
        Pageable pageable = PageRequest.of(0, 10);

        //when
        CustomPage<AdminProductListResponse> result = adminProductService.getAllProducts(savedCategory.getCategoryId(), pageable, locale);

        //then
        assertThat(result.contents())
                .extracting(AdminProductListResponse::briefDescription)
                .containsExactly("간략 설명_EN");
    }

    @Test
    @DisplayName("[금액 기록 조회] product 와 productPrice 가 존재하는 경우 productPrice 를 반환한다.")
    void getAllProductPrices_1() {
        //given
        Pageable pageable = PageRequest.of(0, 5);

        //when
        CustomPage<AdminProductPriceResponse> customPage = adminProductService.getAllProductPrices(savedProduct.getProductId(), pageable);

        //then
        assertThat(customPage.contents())
                .extracting(AdminProductPriceResponse::price)
                .contains(3500);
    }

    @Test
    @DisplayName("[금액 기록 조회]  productId 가 존재하지 않는 값인 경우 에러가 발생한다.")
    void getAllProductPrices_2() {
        //given
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
        List<ProductInfoRequest> productInfoList = List.of(
                new ProductInfoRequest(Language.EN, "상품명_EN", "간단설명_EN", "상세설명_EN", "https://xxx.com/xxx", "회사명_EN", "용량_EN", "섭취방법_EN", "주의사항_EN")
        );
        AdminProductCreateRequest request = new AdminProductCreateRequest(String.valueOf(savedCategory.getCategoryId()), "https://xxx.com/xxx", true, 33, 30000, productInfoList);

        //when //then
        assertThatThrownBy(() -> adminProductService.createProduct(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ExceptionCode.KO_LANGUAGE_REQUIRED.getMessage());
    }

    @Test
    @DisplayName("[상품 등록] AdminProductCreateRequest 필드 모두 유효하면 ProductId 를 반환한다.")
    void createProduct_2() {
        //given
        List<ProductInfoRequest> productInfoList = List.of(
                new ProductInfoRequest(Language.KO, "상품명_KO", "간단설명_KO", "상세설명_KO", "https://xxx.com/xxx", "회사명_KO", "용량_KO", "섭취방법_KO", "주의사항_KO"),
                new ProductInfoRequest(Language.EN, "상품명_EN", "간단설명_EN", "상세설명_EN", "https://xxx.com/xxx", "회사명_EN", "용량_EN", "섭취방법_EN", "주의사항_EN")
        );
        AdminProductCreateRequest request = new AdminProductCreateRequest(String.valueOf(savedCategory.getCategoryId()), "https://xxx.com/xxx", true, 33, 30000, productInfoList);

        //when
        long productId = adminProductService.createProduct(request);

        //then
        assertThat(productId).isGreaterThan(0);
    }

    @Test
    @DisplayName("[상품 등록] Category 가 존재하지 않으면 에러를 반환한다.")
    void createProduct_4() {
        //given
        List<ProductInfoRequest> productInfoList = List.of(
                new ProductInfoRequest(Language.KO, "상품명_KO", "간단설명_KO", "상세설명_KO", "https://xxx.com/xxx", "회사명_KO", "용량_KO", "섭취방법_KO", "주의사항_KO"),
                new ProductInfoRequest(Language.EN, "상품명_EN", "간단설명_EN", "상세설명_EN", "https://xxx.com/xxx", "회사명_EN", "용량_EN", "섭취방법_EN", "주의사항_EN")
        );
        AdminProductCreateRequest request = new AdminProductCreateRequest(String.valueOf(1000L), "https://xxx.com/xxx", true, 33, 30000, productInfoList);

        //when //then
        assertThatThrownBy(()-> adminProductService.createProduct(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ExceptionCode.CATEGORY_NOT_FOUND.getMessage());
    }
}