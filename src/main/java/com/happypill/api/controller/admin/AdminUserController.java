package com.happypill.api.controller.admin;

import com.happypill.application.exception.global.ErrorResponse;
import com.happypill.application.pagination.CustomPage;
import com.happypill.application.service.admin.AdminUserService;
import com.happypill.application.service.admin.request.AdminUserUpdateRequest;
import com.happypill.application.service.admin.response.AdminUserDetailResponse;
import com.happypill.application.service.admin.response.AdminUserListResponse;
import com.happypill.application.swagger.AuthFailureResponses;
import jakarta.validation.Valid;
import com.happypill.application.swagger.OKAndServerErrorResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Tag(name="[관리자] 회원", description = "관리자가 사용자 정보를 조회/관리하기 위한 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final AdminUserService adminUserService;

    @Operation(summary = "모든 회원 조회", description = "모든 회원 정보를 출력하기 위한 API")
    @AuthFailureResponses
    @OKAndServerErrorResponses
    @GetMapping
    //TODO : 추가 예정 @PreAuthorize("hasRole('ADMIN')")
    public CustomPage<AdminUserListResponse> getUsers(@RequestParam(value = "page", defaultValue = "1") int page,
                                                      @RequestParam(value = "size", defaultValue = "7") int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return adminUserService.getAllUsers(pageable);
    }

    @Operation(summary = "특정 회원 조회", description = "회원 정보 수정 시 회원 정보를 출력하기 위한 API")
    @AuthFailureResponses
    @OKAndServerErrorResponses
    @ApiResponse(responseCode = "404", description = "회원 정보가 존재하지 않는 경우", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
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
}