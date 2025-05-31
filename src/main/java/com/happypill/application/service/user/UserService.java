package com.happypill.application.service.user;

import com.happypill.application.auth.UserContext;
import com.happypill.application.entity.HappypillUser;
import com.happypill.application.repository.happypilluser.HappypillUserRepository;
import com.happypill.application.service.user.request.UserUpdateRequest;
import com.happypill.application.service.user.response.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final HappypillUserRepository userRepository;

    public UserInfoResponse getCurrentUserInfo(UserContext userContext) {
        HappypillUser user = userRepository.findById(userContext.getId())
                .orElseThrow(() -> new RuntimeException("추후 도메인 에러로 변경"));
        return UserInfoResponse.from(user);
    }

    @Transactional
    public UserInfoResponse updateCurrentUser(UserContext userContext, UserUpdateRequest userUpdateRequest) {
        HappypillUser user = userRepository.findById(userContext.getId())
                .orElseThrow(() -> new RuntimeException("추후 도메인 에러로 변경"));
        user.changeUser(userUpdateRequest.nickName());
        return UserInfoResponse.from(user);
    }
}
