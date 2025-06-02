package com.happypill.application.service.admin;

import com.happypill.application.entity.Product;
import com.happypill.application.entity.ProductInfo;
import com.happypill.application.entity.ProductPrice;
import com.happypill.application.exception.custom.ExceptionCode;
import com.happypill.application.exception.global.BusinessException;
import com.happypill.application.repository.product.ProductRepository;
import com.happypill.application.repository.productinfo.ProductInfoRepository;
import com.happypill.application.repository.productprice.ProductPriceRepository;
import com.happypill.application.service.admin.response.AdminProductInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminProductService {

    private final ProductRepository productRepository;
    private final ProductInfoRepository productInfoRepository;
    private final ProductPriceRepository productPriceRepository;

    public AdminProductInfoResponse getProductDetails(Long productId) {
        Product product = productRepository.findByProductId(productId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.PRODUCT_NOT_FOUND));

        List<ProductInfo> productInfo = productInfoRepository.findAllByProductId(productId);
        if (productInfo.isEmpty()) {
            throw new BusinessException(ExceptionCode.PRODUCT_INFO_NOT_FOUND);
        }

        ProductPrice productPrice = productPriceRepository.findCurrentPriceByProduct(productId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.PRODUCT_PRICE_NOT_FOUND));

        return AdminProductInfoResponse.from(product, productInfo, productPrice);
    }
}