package com.happypill.api.controller.admin;

import com.happypill.application.exception.global.AuthResponse;
import com.happypill.application.exception.global.ErrorResponse;
import com.happypill.application.pagination.CustomPage;
import com.happypill.application.service.admin.AdminUserService;
import com.happypill.application.service.admin.response.AdminUserDetailResponse;
import com.happypill.application.service.admin.response.AdminUserListResponse;
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

@Tag(name="AdminUserController", description = "관리자가 사용자 정보를 조회/관리하기 위한 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final AdminUserService adminUserService;

    @Operation(summary = "모든 회원 조회", description = "모든 회원 정보를 출력하기 위한 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청이 정상적으로 처리되었을 때"),
            @ApiResponse(responseCode = "401", description = "인증 실패 했을 때", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "403", description = "인가 실패 했을 때", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부에서 오류 발생 했을 때", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    //TODO : 추가 예정 @PreAuthorize("hasRole('ADMIN')")
    public CustomPage<AdminUserListResponse> getUsers(@RequestParam(value = "page", defaultValue = "1") int page,
                                                      @RequestParam(value = "size", defaultValue = "7") int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return adminUserService.getAllUsers(pageable);
    }

    @Operation(summary = "특정 회원 조회", description = "회원 정보 수정 시 회원 정보를 출력하기 위한 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청이 정상적으로 처리되었을 때"),
            @ApiResponse(responseCode = "401", description = "인증 실패 했을 때", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "403", description = "인가 실패 했을 때", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "404", description = "회원 정보가 존재하지 않는 경우", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부에서 오류 발생 했을 때", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{userId}")
    //TODO : 추가 예정 @PreAuthorize("hasRole('ADMIN')")
    public AdminUserDetailResponse getUserDetail(@PathVariable Long userId) {
        return adminUserService.getUserDetails(userId);
    }
}