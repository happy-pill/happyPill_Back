package com.happypill.application.service.product;

import com.happypill.application.entity.Product;
import com.happypill.application.entity.ProductInfo;
import com.happypill.application.entity.ProductPrice;
import com.happypill.application.entity.enums.Language;
import com.happypill.application.exception.custom.ExceptionCode;
import com.happypill.application.exception.global.BusinessException;
import com.happypill.application.repository.product.ProductRepository;
import com.happypill.application.repository.productprice.ProductPriceRepository;
import com.happypill.application.service.product.dto.response.CustomPageResponse;
import com.happypill.application.service.product.dto.response.ProductInfoResponse;
import com.happypill.application.service.product.dto.response.ProductResponse;
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
                productInfoList.getLast().getProduct().getProductId()
        );

        return response;
    }

    public ProductInfoResponse getProduct(Long productId, Locale locale) {
        Language language = Language.parseLanguage(locale.getLanguage());
        Product product = productRepository.findByProductId(productId).orElseThrow(() -> new BusinessException(ExceptionCode.PRODUCT_NOT_FOUND));
        ProductInfo productInfo = productRepository.getProductInfoByProductId(product.getProductId(), language).orElseThrow(() -> new BusinessException(ExceptionCode.PRODUCT_INFO_NOT_FOUND));
        ProductPrice price = productPriceRepository.getCurrentPriceByProductId(product.getProductId()).orElseThrow(() -> new BusinessException(ExceptionCode.PRODUCT_PRICE_NOT_FOUND));

        return ProductInfoResponse.from(product, productInfo, price.getPrice());
    }

    private int getCurrentPrice(ProductInfo productInfo) {
        ProductPrice price = productPriceRepository.getCurrentPriceByProductId(productInfo.getProduct().getProductId()).orElseThrow(() -> new BusinessException(ExceptionCode.PRODUCT_PRICE_NOT_FOUND));

        return price.getPrice();
    }
}
