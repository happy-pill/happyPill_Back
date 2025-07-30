package com.happypill.application.repository.category;

import com.happypill.application.entity.enums.Language;
import com.happypill.application.service.admin.response.AdminCategoryListResponse;
import com.happypill.application.service.admin.response.QAdminCategoryListResponse;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.happypill.application.entity.QCategory.category;
import static com.happypill.application.entity.QCategoryInfo.categoryInfo;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<AdminCategoryListResponse> searchCategoriesByKeyword(Pageable pageable, Language language, String keyword) {
        BooleanBuilder keyBuilder = new BooleanBuilder();

        if(keyword != null && !keyword.isBlank()) {
            keyBuilder.or(categoryInfo.name.containsIgnoreCase(keyword));
        }

        List<AdminCategoryListResponse> content = jpaQueryFactory
                .select(new QAdminCategoryListResponse(
                        category.id,
                        categoryInfo.name,
                        categoryInfo.description,
                        category.thumbnailUrl,
                        category.bannerUrl
                ))
                .from(category)
                .join(categoryInfo)
                .on(
                        categoryInfo.language.eq(language)
                                .and(categoryInfo.category.id.eq(category.id))
                )
                .where(
                        keyBuilder
                )
                .orderBy(category.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, content.size());
    }
}
