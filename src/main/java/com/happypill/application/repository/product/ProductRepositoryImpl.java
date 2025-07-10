package com.happypill.application.repository.product;

import com.happypill.application.entity.enums.Language;
import com.happypill.application.service.product.response.ProductListResponse;
import com.happypill.application.service.product.response.QProductListResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.happypill.application.entity.QBestProduct.bestProduct;
import static com.happypill.application.entity.QCategory.category;
import static com.happypill.application.entity.QCategoryInfo.categoryInfo;
import static com.happypill.application.entity.QProduct.product;
import static com.happypill.application.entity.QProductInfo.productInfo;
@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ProductListResponse> findAllBestProductsByLanguage(Language language) {
        return jpaQueryFactory
                .select(new QProductListResponse(
                        product.id.stringValue(),
                        category.id.stringValue(),
                        productInfo.name,
                        categoryInfo.name,
                        productInfo.company,
                        product.price,
                        productInfo.briefDescription,
                        product.thumbnailUrl,
                        bestProduct.id.isNotNull()
                ))
                .from(product)
                .join(productInfo).on(productInfo.product.eq(product).and(productInfo.language.eq(language)))
                .join(product.category, category)
                .join(categoryInfo).on(categoryInfo.category.eq(category).and(categoryInfo.language.eq(language)))
                .leftJoin(bestProduct).on(bestProduct.product.eq(product))
                .where(bestProduct.isNotNull())
                .fetch();
    }
}