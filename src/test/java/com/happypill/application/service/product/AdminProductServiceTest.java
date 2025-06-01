package com.happypill.application.service.product;

import com.happypill.application.entity.Category;
import com.happypill.application.entity.Product;
import com.happypill.application.entity.ProductInfo;
import com.happypill.application.entity.ProductPrice;
import com.happypill.application.exception.global.BusinessException;
import com.happypill.application.repository.category.CategoryRepository;
import com.happypill.application.repository.product.ProductRepository;
import com.happypill.application.repository.productinfo.ProductInfoRepository;
import com.happypill.application.repository.productprice.ProductPriceRepository;
import com.happypill.application.service.admin.AdminProductService;
import com.happypill.application.service.admin.response.AdminProductInfoResponse;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
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
    @DisplayName("ProductPrice 가 존재하지 않으면 에러 발생")
    void getProductDetails_1() {
        //given
        Category category = Category.of(1L, "thumbnailURL", "bannerURL");
        categoryRepository.save(category);

        Product product = Product.of(1L, 3, true, "thumbnailURL", false, category);
        productRepository.save(product);

        List<ProductInfo> productInfo = Arrays.asList(
                ProductInfo.of(1L, "KO", "제품명_KO", "수량 상세_KO", "경고 메시지_KO", "사용법_KO", "contentImageURL_KO", "설명_KO", "회사명_KO", "간략 설명_KO", product),
                ProductInfo.of(2L, "EN", "제품명_EN", "수량 상세_EN", "경고 메시지_EN", "사용법_EN", "contentImageURL_EN", "설명_EN", "회사명_EN", "간략 설명_EN", product)
        );
        productInfoRepository.saveAll(productInfo);

        //when //then
        assertThatThrownBy(() -> adminProductService.getProductDetails(product.getProductId()))
                .isInstanceOf(BusinessException.class)
                .hasMessage("해당 상품 가격을 찾을 수 없습니다.");

    }

    @Test
    @DisplayName("ProductInfo 가 존재하지 않으면 에러 발생")
    void getProductDetails_2() {
        //given
        Category category = Category.of(1L, "thumbnailURL", "bannerURL");
        categoryRepository.save(category);

        Product product = Product.of(1L, 3, true, "thumbnailURL", false, category);
        productRepository.save(product);

        ProductPrice productPrice = ProductPrice.of(1L, 3500, true, product);
        productPriceRepository.save(productPrice);

        //when //then
        assertThatThrownBy(() -> adminProductService.getProductDetails(product.getProductId()))
                .isInstanceOf(BusinessException.class)
                .hasMessage("해당 상품 정보를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("Product, ProductInfo, ProductPrice 가 존재하면 200 응답")
    void getProductDetails_3() {
        //given
        Category category = Category.of(1L, "thumbnailURL", "bannerURL");
        categoryRepository.save(category);

        Product product = Product.of(1L, 3, true, "thumbnailURL", false, category);
        productRepository.save(product);

        List<ProductInfo> productInfo = Arrays.asList(
                ProductInfo.of(1L, "KO", "제품명_KO", "수량 상세_KO", "경고 메시지_KO", "사용법_KO", "contentImageURL_KO", "설명_KO", "회사명_KO", "간략 설명_KO", product),
                ProductInfo.of(2L, "EN", "제품명_EN", "수량 상세_EN", "경고 메시지_EN", "사용법_EN", "contentImageURL_EN", "설명_EN", "회사명_EN", "간략 설명_EN", product)
        );
        productInfoRepository.saveAll(productInfo);

        ProductPrice productPrice = ProductPrice.of(1L, 3500, true, product);
        productPriceRepository.save(productPrice);

        //when
        AdminProductInfoResponse response = adminProductService.getProductDetails(product.getProductId());

        //then
        assertThat(response).isNotNull();
        assertThat(response.productId()).isEqualTo(1L);
        assertThat(response.productInfo())
                .anySatisfy(info -> {
                    assertThat(info.name()).isEqualTo("제품명_KO");
                    assertThat(info.company()).isEqualTo("회사명_KO");
                });
    }
}