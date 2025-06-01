package com.happypill.application.service.user;

import com.happypill.application.auth.UserContext;
import com.happypill.application.entity.HappypillUser;
import com.happypill.application.exception.global.BusinessException;
import com.happypill.application.repository.happypilluser.HappypillUserRepository;
import com.happypill.application.service.user.request.UserUpdateRequest;
import com.happypill.application.service.user.response.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.happypill.application.exception.custom.ExceptionCode.USER_NOT_FOUND;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final HappypillUserRepository userRepository;

    public UserInfoResponse getCurrentUserInfo(UserContext userContext) {
        HappypillUser user = userRepository.findById(userContext.getId())
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));
        return UserInfoResponse.from(user);
    }

    @Transactional
    public UserInfoResponse updateCurrentUser(UserContext userContext, UserUpdateRequest userUpdateRequest) {
        HappypillUser user = userRepository.findById(userContext.getId())
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));
        user.changeUser(userUpdateRequest.nickName());
        return UserInfoResponse.from(user);
    }
}
