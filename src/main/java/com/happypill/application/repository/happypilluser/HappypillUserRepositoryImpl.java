package com.happypill.application.repository.happypilluser;

import com.happypill.application.service.admin.response.AdminUserListResponse;
import com.happypill.application.service.admin.response.QAdminUserListResponse;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.happypill.application.entity.QHappypillUser.happypillUser;

@Repository
@RequiredArgsConstructor
public class HappypillUserRepositoryImpl implements HappypillUserRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<AdminUserListResponse> searchUsersByKeyword(Pageable pageable, String keyword) {
        BooleanBuilder keywordBuilder = new BooleanBuilder();

        if(keyword != null && !keyword.isBlank()) {
            keywordBuilder.or(happypillUser.nickname.containsIgnoreCase(keyword));
            keywordBuilder.or(happypillUser.loginEmail.containsIgnoreCase(keyword));
        }

        List<AdminUserListResponse> content = jpaQueryFactory
                .select(new QAdminUserListResponse(
                        happypillUser.id.stringValue(),
                        happypillUser.nickname,
                        happypillUser.loginEmail,
                        happypillUser.provider,
                        happypillUser.createdAt,
                        happypillUser.updatedAt,
                        happypillUser.isDeleted
                ))
                .from(happypillUser)
                .where(keywordBuilder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory
                .select(happypillUser.count())
                .from(happypillUser)
                .where(keywordBuilder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0L);
    }
}
