package com.happypill.application.repository.subscription;

import com.happypill.application.entity.*;
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

@Repository
@RequiredArgsConstructor
public class SubscriptionRepositoryImpl implements SubscriptionRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public Page<AdminSubscriptionListResponse> findSubscriptionsByLanguageAndCompletionAndStatus(Language language, boolean isCompleted, OrderStatus orderStatus, Pageable pageable) {
        QSubscription s = QSubscription.subscription;
        QOrderLine ol = QOrderLine.orderLine;
        QHappypillUser u = QHappypillUser.happypillUser;
        QOrder o = QOrder.order;
        QProduct p = QProduct.product;
        QProductInfo pi = QProductInfo.productInfo;

        //모든 구독 상품 조회 쿼리
        List<AdminSubscriptionListResponse> content = jpaQueryFactory
                .select(new QAdminSubscriptionListResponse(
                        pi.name,
                        u.notifyEmail,
                        s.id.stringValue(),
                        s.nextDeliveryDate
                ))
                .from(s)
                .join(s.orderLine, ol)
                .join(s.user, u)
                .join(ol.order, o)
                .join(ol.product, p)
                .join(pi).on(pi.product.eq(p).and(pi.language.eq(language)))
                .where(
                        s.isCompleted.eq(isCompleted),
                        o.status.eq(orderStatus)
                )
                .orderBy(s.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 카운트 쿼리 (페이지네이션을 위한 쿼리)
        Long total = jpaQueryFactory
                .select(s.count())
                .from(s)
                .join(s.orderLine, ol)
                .join(ol.order, o)
                .where(
                        s.isCompleted.eq(isCompleted),
                        o.status.eq(orderStatus)
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0L);
    }
}
