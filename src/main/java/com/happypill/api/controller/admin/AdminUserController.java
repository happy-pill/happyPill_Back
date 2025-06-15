package com.happypill.api.controller.admin;

import com.happypill.application.pagination.CustomPage;
import com.happypill.application.service.admin.AdminUserService;
import com.happypill.application.service.admin.request.AdminUserUpdateRequest;
import com.happypill.application.service.admin.response.AdminUserDetailResponse;
import com.happypill.application.service.admin.response.AdminUserListResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final AdminUserService adminUserService;

    //모든 회원 조회
    @GetMapping
    //TODO : 추가 예정 @PreAuthorize("hasRole('ADMIN')")
    public CustomPage<AdminUserListResponse> getUsers(@RequestParam(value = "page", defaultValue = "1") int page,
                                                      @RequestParam(value = "size", defaultValue = "7") int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return adminUserService.getAllUsers(pageable);
    }

    //특정 회원 조회
    @GetMapping("/{userId}")
    //TODO : 추가 예정 @PreAuthorize("hasRole('ADMIN')")
    public AdminUserDetailResponse getUserDetail(@PathVariable Long userId) {
        return adminUserService.getUserDetails(userId);
    }

    //회원 정보 수정
    @PatchMapping("/{userId}")
    public AdminUserDetailResponse updateUser(@PathVariable Long userId,
                                              @Valid @RequestBody AdminUserUpdateRequest request){
        return adminUserService.updateUserProfile(userId, request);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        adminUserService.deleteUser(userId);
    }
}