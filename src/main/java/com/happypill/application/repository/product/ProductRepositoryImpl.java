package com.happypill.application.repository.product;

import com.happypill.application.entity.enums.Language;
import com.happypill.application.service.product.response.ProductListResponse;
import com.happypill.application.service.product.response.QProductListResponse;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
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
    public List<ProductListResponse> scrollProductsByLanguageAndCategoryWithBestProduct(Long categoryId, Long lastProductId, int size, Language language) {
        BooleanBuilder baseCondition = new BooleanBuilder();
        baseCondition.and(product.isAvailable.isTrue());

        if (categoryId != null) {
            baseCondition.and(category.id.eq(categoryId));
        }

        if (lastProductId != null) {
            boolean lastIsBest = jpaQueryFactory
                    .selectOne()
                    .from(bestProduct)
                    .where(bestProduct.product.id.eq(lastProductId))
                    .fetchFirst() != null;

            BooleanExpression isBestExpr = bestProduct.id.isNotNull();
            BooleanExpression cursorCondition = lastIsBest
                    ? isBestExpr.and(product.id.lt(lastProductId)).or(isBestExpr.not())
                    : isBestExpr.not().and(product.id.lt(lastProductId));

            baseCondition.and(cursorCondition);
        }

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
                .join(productInfo)
                    .on(
                            productInfo.product.eq(product)
                                .and(productInfo.language.eq(language))
                    )
                .join(product.category, category)
                .join(categoryInfo)
                    .on(
                            categoryInfo.category.eq(category)
                                .and(categoryInfo.language.eq(language))
                    )
                .leftJoin(bestProduct)
                    .on(bestProduct.product.eq(product))
                .where(baseCondition)
                .orderBy(
                        new CaseBuilder()
                                .when(bestProduct.id.isNotNull()).then(1)
                                .otherwise(0)
                                .desc(),
                        product.id.desc()
                )
                .limit(size + 1)
                .fetch();
    }
}
