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

    public AdminProductInfoResponse getProductDetails(Long productId){
        Product product = getProductOrThrow(productId);
        List<ProductInfo> productInfo = getProductInfoOrThrow(productId);
        ProductPrice productPrice = getProductPriceOrThrow(productId);

        return AdminProductInfoResponse.from(product, productInfo, productPrice);
    }

    private Product getProductOrThrow(Long productId){
        return productRepository.findByProductId(productId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.PRODUCT_NOT_FOUND));
    }

    private List<ProductInfo> getProductInfoOrThrow(Long productId){
        List<ProductInfo> infos = productInfoRepository.findAllByProductId(productId);
        if(infos.isEmpty())
            throw new BusinessException(ExceptionCode.PRODUCT_INFO_NOT_FOUND);
        return infos;
    }

    private ProductPrice getProductPriceOrThrow(Long productId){
        return productPriceRepository.findCurrentPriceByProduct(productId)
                .orElseThrow(()->new BusinessException(ExceptionCode.PRODUCT_PRICE_NOT_FOUND));
    }
}

