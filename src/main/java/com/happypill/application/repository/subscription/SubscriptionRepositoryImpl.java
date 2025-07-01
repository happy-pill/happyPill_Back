package com.happypill.application.repository.subscription;

import com.happypill.application.entity.enums.Language;
import com.happypill.application.entity.enums.OrderStatus;
import com.happypill.application.service.admin.response.AdminSubscriptionListResponse;
import com.happypill.application.service.admin.response.QAdminSubscriptionListResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.happypill.application.entity.QHappypillUser.happypillUser;
import static com.happypill.application.entity.QOrder.order;
import static com.happypill.application.entity.QOrderLine.orderLine;
import static com.happypill.application.entity.QProduct.product;
import static com.happypill.application.entity.QProductInfo.productInfo;
import static com.happypill.application.entity.QSubscription.subscription;

@Repository
@RequiredArgsConstructor
public class SubscriptionRepositoryImpl implements SubscriptionRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<AdminSubscriptionListResponse> findSubscriptionsByLanguageAndCompletionAndStatus(Language language, boolean isCompleted, OrderStatus orderStatus, Pageable pageable) {

        //모든 구독 상품 조회 쿼리
        List<AdminSubscriptionListResponse> content = jpaQueryFactory
                .select(new QAdminSubscriptionListResponse(
                        productInfo.name,
                        happypillUser.notifyEmail,
                        subscription.id.stringValue(),
                        subscription.nextDeliveryDate
                ))
                .from(subscription)
                .join(subscription.orderLine, orderLine)
                .join(subscription.user, happypillUser)
                .join(orderLine.order, order)
                .join(orderLine.product, product)
                .join(productInfo).on(productInfo.product.eq(product).and(productInfo.language.eq(language)))
                .where(
                        subscription.isCompleted.eq(isCompleted),
                        order.status.eq(orderStatus)
                )
                .orderBy(subscription.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 카운트 쿼리 (페이지네이션을 위한 쿼리)
        Long total = jpaQueryFactory
                .select(subscription.count())
                .from(subscription)
                .join(subscription.orderLine, orderLine)
                .join(orderLine.order, order)
                .where(
                        subscription.isCompleted.eq(isCompleted),
                        order.status.eq(orderStatus)
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0L);
    }
}
