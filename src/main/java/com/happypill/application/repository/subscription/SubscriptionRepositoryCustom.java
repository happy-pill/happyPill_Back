package com.happypill.application.repository.subscription;

import com.happypill.application.entity.enums.Language;
import com.happypill.application.entity.enums.OrderStatus;
import com.happypill.application.service.admin.response.AdminSubscriptionListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SubscriptionRepositoryCustom {
    Page<AdminSubscriptionListResponse> findSubscriptionsByLanguageAndCompletionAndStatus(
            Language language, boolean isCompleted, OrderStatus orderStatus, Pageable pageable
    );

    Page<AdminSubscriptionListResponse> searchSubscriptionsByLanguage(Pageable pageable, String keyword, Language language);
}
