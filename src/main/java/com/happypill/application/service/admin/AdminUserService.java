package com.happypill.application.service.admin;

import com.happypill.application.entity.HappypillUser;
import com.happypill.application.pagination.CustomPage;
import com.happypill.application.repository.happypilluser.HappypillUserRepository;
import com.happypill.application.service.admin.response.AdminUserListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminUserService {

    private final HappypillUserRepository userRepository;

    //모든 회원 조회
    public CustomPage<AdminUserListResponse> getAllUsers(Pageable pageable) {
        Page<HappypillUser> users = userRepository.getAllUsers(pageable);

        Page<AdminUserListResponse> responses = users
                .map(AdminUserListResponse::from);

        return new CustomPage<>(responses);
    }
}