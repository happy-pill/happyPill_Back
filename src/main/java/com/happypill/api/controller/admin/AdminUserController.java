package com.happypill.api.controller.admin;

import com.happypill.application.pagination.CustomPage;
import com.happypill.application.service.admin.AdminUserService;
import com.happypill.application.service.admin.request.AdminUserUpdateRequest;
import com.happypill.application.service.admin.response.AdminSubscriptionListResponse;
import com.happypill.application.service.admin.response.AdminUserDetailResponse;
import com.happypill.application.service.admin.response.AdminUserListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@Tag(name = "[관리자] 회원", description = "관리자가 사용자 정보를 조회/관리하기 위한 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private static final String LANGUAGE_HEADER = "Language";

    private final AdminUserService adminUserService;

    @Operation(summary = "모든 회원 조회", description = "모든 회원 정보를 출력하기 위한 API")
    @GetMapping
    //TODO : 추가 예정 @PreAuthorize("hasRole('ADMIN')")
    public CustomPage<AdminUserListResponse> getUsers(@RequestParam(value = "page", defaultValue = "1") int page,
                                                      @RequestParam(value = "size", defaultValue = "7") int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return adminUserService.getAllUsers(pageable);
    }

    @Operation(summary = "특정 회원 조회", description = "회원 정보 수정 시 회원 정보를 출력하기 위한 API")
    @GetMapping("/{userId}")
    //TODO : 추가 예정 @PreAuthorize("hasRole('ADMIN')")
    public AdminUserDetailResponse getUserDetail(@PathVariable Long userId) {
        return adminUserService.getUserDetails(userId);
    }

    //회원 정보 수정
    @PatchMapping("/{userId}")
    public AdminUserDetailResponse updateUser(@PathVariable Long userId,
                                              @Valid @RequestBody AdminUserUpdateRequest request) {
        return adminUserService.updateUserProfile(userId, request);
    }

    @GetMapping("/subscriptions")
    //TODO : 추가 예정 @PreAuthorize("hasRole('ADMIN')")
    public CustomPage<AdminSubscriptionListResponse> getSubscriptions(@RequestParam(value = "page", defaultValue = "1") int page,
                                                                      @RequestParam(value = "size", defaultValue = "8") int size,
                                                                      @RequestHeader(LANGUAGE_HEADER) String headerLanguage){
        Locale locale = Locale.forLanguageTag(headerLanguage);
        Pageable pageable = PageRequest.of(page - 1, size);
        return adminUserService.getAllSubscriptions(pageable, locale);
    }
}