package com.happypill.application.service.admin;

import com.happypill.application.entity.HappypillUser;
import com.happypill.application.exception.custom.ExceptionCode;
import com.happypill.application.exception.global.BusinessException;
import com.happypill.application.pagination.CustomPage;
import com.happypill.application.repository.happypilluser.HappypillUserRepository;
import com.happypill.application.service.admin.request.AdminUserUpdateRequest;
import com.happypill.application.service.admin.response.AdminUserDetailResponse;
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

    //특정 회원 조회
    public AdminUserDetailResponse getUserDetails(Long userId) {
        HappypillUser user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));
        return AdminUserDetailResponse.from(user);
    }

    //회원 정보 수정
    @Transactional
    public AdminUserDetailResponse updateUserProfile(Long userId, AdminUserUpdateRequest request) {
        HappypillUser user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));

        if(request.nickName() != null)
            user.changeUser(request.nickName());
        if(request.notifyEmail() != null){
            String newEmail = request.notifyEmail();
            if(!newEmail.equals(user.getLoginEmail())){
                boolean isDuplicated = userRepository.existEmailConflict(newEmail, userId); //자신을 제외한 다른 사람들의 loginEmail, notifyEmail 과 겹치는 경우 예외 발생
                if(isDuplicated)
                    throw new BusinessException(ExceptionCode.EMAIL_DUPLICATED);
            }
            user.changeNotifyEmail(newEmail);
        }

        return AdminUserDetailResponse.from(user);
    }
}