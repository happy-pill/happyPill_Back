package com.happypill.application.repository.category;

import com.happypill.application.entity.enums.Language;
import com.happypill.application.service.admin.response.AdminCategoryListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryRepositoryCustom {
    Page<AdminCategoryListResponse> searchCategoriesByKeyword(Pageable pageable, Language language, String keyword);
}
