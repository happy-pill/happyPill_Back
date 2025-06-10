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
import com.happypill.application.service.product.response.ProductInfoResponse;
import com.happypill.application.util.SnowflakeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminProductService {

    private final ProductRepository productRepository;
    private final ProductInfoRepository productInfoRepository;
    private final ProductPriceRepository productPriceRepository;
    private final CategoryRepository categoryRepository;

    //모든 상품 조회
    public CustomPage<AdminProductListResponse> getAllProducts(Long categoryId, Pageable pageable, Locale locale) {
        Language language = Language.parseLanguage(locale.getLanguage());

        if (categoryId != null) {
            boolean isExist = categoryRepository.existsById(categoryId);
            if (!isExist)
                throw new BusinessException(ExceptionCode.CATEGORY_NOT_FOUND);
        }

        Page<ProductInfo> productInfos = (categoryId == null) ?
                productInfoRepository.getAllProductInfosByLanguage(language, pageable) :
                productInfoRepository.getAllProductInfosByCategoryAndLanguage(categoryId, language, pageable);

        Page<AdminProductListResponse> responsePage = productInfos.map(productInfo -> {
                    Product product = productInfo.getProduct();
                    int price = getCurrentPrice(productInfo);
                    return AdminProductListResponse.from(product, productInfo, price);
                }
        );
        return new CustomPage<>(responsePage);
    }

    //특정 상품 조회
    public AdminProductInfoResponse getProductDetails(Long productId) {
        Product product = productRepository.findByProductId(productId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.PRODUCT_NOT_FOUND));

        List<ProductInfo> productInfo = productInfoRepository.findAllByProductId(productId);
        if (productInfo.isEmpty()) {
            throw new BusinessException(ExceptionCode.PRODUCT_INFO_NOT_FOUND);
        }

        ProductPrice productPrice = productPriceRepository.getCurrentPriceByProductId(productId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.PRODUCT_PRICE_NOT_FOUND));

        return AdminProductInfoResponse.fromEntity(product, productInfo, productPrice);
    }

    //금액 기록 조회
    public CustomPage<AdminProductPriceResponse> getAllProductPrices(Long productId, Pageable pageable){
        boolean isExist = productRepository.existsById(productId);
        if(!isExist) {
            throw new BusinessException(ExceptionCode.PRODUCT_NOT_FOUND);
        }

        Page<ProductPrice> productPrices = productPriceRepository.getCurrentPriceByProductId(productId, pageable);
        Page<AdminProductPriceResponse> responses = productPrices
                .map(AdminProductPriceResponse::from);

        return new CustomPage<>(responses);
    }

    //상품 등록
    @Transactional
    public long createProduct(AdminProductCreateRequest request) {
        boolean isKoreanExist = request.productInfos().stream()
                .map(ProductInfoRequest::language)
                .anyMatch(language -> language == Language.KO);
        if(!isKoreanExist)
            throw new BusinessException(ExceptionCode.KO_LANGUAGE_REQUIRED);

        Category category = categoryRepository.findByCategoryId(Long.valueOf(request.categoryId()))
                .orElseThrow(() -> new BusinessException(ExceptionCode.CATEGORY_NOT_FOUND));

        Product product = Product.of(
                SnowflakeUtil.nextId(),
                request.stock(),
                request.isAvailable(),
                request.thumbnailUrl(),
                false,
                category);
        productRepository.save(product);

        ProductPrice productPrice = ProductPrice.of(SnowflakeUtil.nextId(), request.price(), true, product);
        productPriceRepository.save(productPrice);

        List<ProductInfo> productInfoList = request.productInfos().stream()
                .map(dto -> ProductInfo.of(
                        SnowflakeUtil.nextId(),
                        dto.language(),
                        dto.name(),
                        dto.quantityDetails(),
                        dto.warningMessage(),
                        dto.usage(),
                        dto.contentImageUrl(),
                        dto.description(),
                        dto.company(),
                        dto.briefDescription(),
                        product
                ))
                .collect(Collectors.toList());
        productInfoRepository.saveAll(productInfoList);
        return product.getProductId();
    }

    // 상품 수정
    @Transactional
    public AdminProductInfoResponse updateProduct(Long productId, AdminProductUpdateRequest request) {
        Product product = this.productRepository.findByProductId(productId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.PRODUCT_NOT_FOUND));

        ProductPrice productPrice = this.productPriceRepository.findCurrentPriceByProduct(productId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.PRODUCT_PRICE_NOT_FOUND));

        Category category = this.categoryRepository.findByCategoryId(Long.valueOf(request.categoryId()))
                .orElseThrow(() -> new BusinessException(ExceptionCode.CATEGORY_NOT_FOUND));

        product.update(request.stock(), request.isAvailable(), request.thumbnailUrl(), category);

        ProductPrice createdPrice = productPrice.createNextPrice(request.price(), product);
        this.productPriceRepository.save(createdPrice);

        Map<Language, ProductInfo> infoMap = productInfoRepository
                .findAllByProductId(product.getProductId())
                .stream()
                .collect(Collectors.toMap(ProductInfo::getLanguage, Function.identity()));

        request.productInfos().forEach(dto -> {
            ProductInfo info = Optional.ofNullable(infoMap.get(dto.language()))
                    .orElseThrow(() -> new BusinessException(ExceptionCode.PRODUCT_INFO_NOT_FOUND));

            info.update(
                    dto.name(),
                    dto.briefDescription(),
                    dto.description(),
                    dto.contentImageUrl(),
                    dto.company(),
                    dto.quantityDetails(),
                    dto.usage(),
                    dto.warningMessage()
            );
        });

        List<ProductInfoResponse> responses = infoMap.values().stream()
                .map(ProductInfoResponse::from)
                .toList();

        return AdminProductInfoResponse.fromDto(product, responses, createdPrice);
    }

    private int getCurrentPrice(ProductInfo productInfo) {
        ProductPrice price = productPriceRepository.getCurrentPriceByProductId(productInfo.getProduct().getProductId())
                .orElseThrow(() -> new BusinessException(ExceptionCode.PRODUCT_PRICE_NOT_FOUND));

        return price.getPrice();
    }
}