package com.happypill.application.service.product;

import com.happypill.application.entity.Product;
import com.happypill.application.entity.ProductInfo;
import com.happypill.application.entity.ProductPrice;
import com.happypill.application.entity.enums.Language;
import com.happypill.application.exception.custom.ExceptionCode;
import com.happypill.application.exception.global.BusinessException;
import com.happypill.application.repository.order.OrderRepository;
import com.happypill.application.repository.product.ProductRepository;
import com.happypill.application.repository.productprice.ProductPriceRepository;
import com.happypill.application.service.product.dto.response.CustomPageResponse;
import com.happypill.application.service.product.dto.response.ProductInfoResponse;
import com.happypill.application.service.product.dto.response.ProductResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductPriceRepository productPriceRepository;
    private final OrderRepository orderRepository;
    private final int SIZE_RECOMMENDATION = 8;

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

    public List<ProductInfoResponse> getRecommendation() {
        List<Long> topSellingProductIds = orderRepository.findTopSellingProductIds();
        List<Product> topProducts = new ArrayList<>();

        if (!topSellingProductIds.isEmpty()) {
            topProducts.addAll(productRepository.findByProductIdIn(topSellingProductIds));
        }

        if (topProducts.size() < SIZE_RECOMMENDATION) {
            List<Product> fallbackProducts = productRepository.findAllProducts();
            for (Product p : fallbackProducts) {
                if (topProducts.size() >= 8) {
                    break;
                }
                if (!topProducts.contains(p)) {
                    topProducts.add(p);
                }
            }
        }

        List<ProductInfoResponse> response = new ArrayList<>(
                topProducts.stream()
                        .map(p -> ProductInfoResponse.from(p, getCurrentPrice(p)))
                        .toList()
        );

        return response;
    }

    private int getCurrentPrice(ProductInfo productInfo) {
        // TO DO Change to ExceptionCode.Product_price_not_found
        ProductPrice price = productPriceRepository.getCurrentPriceByProductInfoId(productInfo.getProduct().getProductId()).orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));

        return price.getPrice();
    }

    private int getCurrentPrice(Product product) {
        // TO DO Change to ExceptionCode.Product_price_not_found
        ProductPrice price = productPriceRepository.getCurrentPriceByProductInfoId(product.getProductId()).orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));

        return price.getPrice();
    }
}
