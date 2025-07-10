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
    public List<ProductListResponse> findAllProductsByLanguage(Long categoryId, Long lastProductId, int size, Language language) {
        Boolean lastIsBest = null; //이전 요청에서 받은 lastProductId 가 bestProduct 에 포함된 상품인지 여부
        if (lastProductId != null) {
            lastIsBest = jpaQueryFactory
                    .selectOne()
                    .from(bestProduct)
                    .where(bestProduct.product.id.eq(lastProductId)) //lastProductId 가 bestProduct 에 포함되어있는지 필터 적용
                    .fetchFirst() != null;
        }

        BooleanBuilder baseCondition = new BooleanBuilder();
        baseCondition.and(product.isAvailable.isTrue());

        if (categoryId != null) {
            baseCondition.and(category.id.eq(categoryId));
        }

        if (lastProductId != null && lastIsBest != null) { //첫 번째 요청 이후 적용
            BooleanExpression isBestExpr = bestProduct.id.isNotNull();
            BooleanExpression cursorCondition;

            if (Boolean.TRUE.equals(lastIsBest)) { //isBest = true 이면서 product.id < lastProductId 인 데이터 출력 또는 isBest = false 인 데이터 출력
                cursorCondition = isBestExpr.and(product.id.lt(lastProductId))
                        .or(isBestExpr.not());
            } else { //이전 상품이 bestProduct 가 아니었다면 isBest = false 이면서 product.id < lastProductId 만 출력
                cursorCondition = isBestExpr.not().and(product.id.lt(lastProductId));
            }

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
                .join(productInfo).on(productInfo.product.eq(product).and(productInfo.language.eq(language)))
                .join(product.category, category)
                .join(categoryInfo).on(categoryInfo.category.eq(category).and(categoryInfo.language.eq(language)))
                .leftJoin(bestProduct).on(bestProduct.product.eq(product))
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