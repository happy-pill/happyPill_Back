package com.happypill.application.service.product;

import com.happypill.application.entity.Product;
import com.happypill.application.entity.ProductInfo;
import com.happypill.application.entity.enums.Language;
import com.happypill.application.exception.custom.ExceptionCode;
import com.happypill.application.exception.global.BusinessException;
import com.happypill.application.repository.product.ProductRepository;
import com.happypill.application.repository.productprice.ProductPriceRepository;
import com.happypill.application.service.product.response.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductPriceRepository productPriceRepository;

    /**
     * loadAllProducts 메소드로 대체됨
     */
    @Deprecated
    public CustomPageResponse<ProductResponse> getAllProducts(Long categoryId, Long lastProductId, Locale locale, int size) {
        Language language = Language.parseLanguage(locale.getLanguage());
        List<ProductInfo> productInfos;
        if (lastProductId != null) {
            ZonedDateTime createdAt = productRepository.getProductInfoByProductId(lastProductId, language).orElseThrow(() -> new BusinessException(ExceptionCode.PRODUCT_INFO_NOT_FOUND)).getProduct().getCreatedAt();
            productInfos = productRepository.getAllProductInfoByCategory(categoryId, createdAt, language);
        } else {
            productInfos = productRepository.getAllProductInfoByCategory(categoryId, language);
        }
        boolean hasNext = productInfos.size() > size;
        List<ProductInfo> productInfoList = hasNext ? productInfos.subList(0, size) : productInfos;

        if (productInfos.isEmpty()) {
            return new CustomPageResponse<>(
                    productInfoList.stream()
                            .map(pi -> ProductResponse.from(pi, getCurrentPrice(pi)))
                            .toList(),
                    false,
                    null
            );
        }

        CustomPageResponse<ProductResponse> response = new CustomPageResponse<>(
                productInfoList.stream()
                        .map(pi -> ProductResponse.from(pi, getCurrentPrice(pi)))
                        .toList(),
                hasNext,
                productInfoList.getLast().getProduct().getId()
        );

        return response;
    }

    public CustomPageResponse<ProductListResponse> loadAllProducts(Long categoryId, Long lastProductId, int size, Locale locale) {
        Language language = Language.parseLanguage(locale.getLanguage());

        List<ProductListResponse> content = productRepository.scrollProductsByLanguageAndCategoryWithBestProduct(categoryId, lastProductId, size, language);

        boolean hasNext = content.size() > size;

        List<ProductListResponse> result = hasNext ? content.subList(0, size) : content;

        Long nextLastProductId = result.isEmpty() ? null : Long.valueOf(result.getLast().getProductId());

        return new CustomPageResponse<>(result, hasNext, nextLastProductId);
    }

    public ProductInfoResponse getProduct(Long productId, Locale locale) {
        Language language = Language.parseLanguage(locale.getLanguage());
        Product product = productRepository.findById(productId).orElseThrow(() -> new BusinessException(ExceptionCode.PRODUCT_NOT_FOUND));
        ProductInfo productInfo = productRepository.getProductInfoByProductId(product.getId(), language).orElseThrow(() -> new BusinessException(ExceptionCode.PRODUCT_INFO_NOT_FOUND));

        return ProductInfoResponse.from(product, productInfo);
    }

    public List<ProductRelatedResponse> getRecommendation() {
        return productRepository.findTop8ByOrderByCreatedAt()
                .stream()
                .map(ProductRelatedResponse::from)
                .toList();
    }

    public List<ProductListResponse> getBestProducts(Locale locale) {
        Language language = Language.parseLanguage(locale.getLanguage());

        return productRepository.findAllBestProductsByLanguage(language);
    }

    private int getCurrentPrice(ProductInfo productInfo) {
//        ProductPrice price = productPriceRepository.getCurrentPriceByProductId(productInfo.getProduct().getProductId()).orElseThrow(() -> new BusinessException(ExceptionCode.PRODUCT_PRICE_NOT_FOUND));
//        return price.getPrice();
        return productRepository.findById(productInfo.getProduct().getId()).orElseThrow(() -> new BusinessException(ExceptionCode.PRODUCT_INFO_NOT_FOUND))
                .getPrice();
    }
}
