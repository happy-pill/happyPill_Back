package com.happypill.application.service.product;

import com.happypill.application.entity.ProductInfo;
import com.happypill.application.entity.ProductPrice;
import com.happypill.application.entity.enums.Language;
import com.happypill.application.exception.custom.ExceptionCode;
import com.happypill.application.exception.global.BusinessException;
import com.happypill.application.repository.product.ProductRepository;
import com.happypill.application.repository.productprice.ProductPriceRepository;
import com.happypill.application.service.product.dto.response.ProductResponse;
import com.happypill.application.service.product.dto.response.CustomPageResponse;
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
            // TO DO Change to ExceptionCode.Product_Info_not_found
            ZonedDateTime createdAt = productRepository.getProductInfoByProductId(lastProductId, language).orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND)).getProduct().getCreatedAt();
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

    private int getCurrentPrice(ProductInfo productInfo) {
        // TO DO Change to ExceptionCode.Product_price_not_found
        ProductPrice price = productPriceRepository.getCurrentPriceByProductInfoId(productInfo.getProduct().getProductId()).orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));

        return price.getPrice();
    }
}
