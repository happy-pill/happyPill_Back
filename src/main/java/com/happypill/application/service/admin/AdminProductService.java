package com.happypill.application.service.admin;

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
import com.happypill.application.service.admin.response.AdminProductInfoResponse;
import com.happypill.application.service.admin.response.AdminProductListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminProductService {

    private final ProductRepository productRepository;
    private final ProductInfoRepository productInfoRepository;
    private final ProductPriceRepository productPriceRepository;
    private final CategoryRepository categoryRepository;

    public CustomPage<AdminProductListResponse> getAllProducts(Long categoryId, Pageable pageable, Locale locale) {
        Language language = Language.parseLanguage(locale.getLanguage());

        if(categoryId != null){
            boolean isExist = categoryRepository.existsById(categoryId);
            if(!isExist)
                throw new BusinessException(ExceptionCode.CATEGORY_NOT_FOUND);
        }

        Page<AdminProductListResponse> productInfos = (categoryId == null) ?
                productRepository.getAllProductInfosByLanguage(language, pageable) :
                productRepository.getAllProductInfosByCategoryAndLanguage(categoryId, language, pageable);

        return new CustomPage<>(productInfos);
    }

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