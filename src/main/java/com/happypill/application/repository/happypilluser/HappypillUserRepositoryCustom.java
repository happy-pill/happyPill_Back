package com.happypill.application.repository.happypilluser;

import com.happypill.application.entity.enums.Language;
import com.happypill.application.service.admin.response.AdminUserListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HappypillUserRepositoryCustom {
    Page<AdminUserListResponse> searchUsersByLanguage(Pageable pageable, String keyword);
}
