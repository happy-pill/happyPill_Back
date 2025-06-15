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

@Tag(name="[ê´€ë¦¬ì] Œì›", description = "ê´€ë¦¬ìê°€ ¬ìš©•ë³´ë¥ì¡°íšŒ/ê´€ë¦¬í•˜ê¸„í•œ API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final AdminUserService adminUserService;

    @Operation(summary = "ëª¨ë“  Œì› ì¡°íšŒ", description = "ëª¨ë“  Œì› •ë³´ë¥ì¶œë ¥˜ê¸° „í•œ API")
    @AuthFailureResponses
    @OKAndServerErrorResponses
    @GetMapping
    //TODO : ì¶”ê ˆì • @PreAuthorize("hasRole('ADMIN')")
    public CustomPage<AdminUserListResponse> getUsers(@RequestParam(value = "page", defaultValue = "1") int page,
                                                      @RequestParam(value = "size", defaultValue = "7") int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return adminUserService.getAllUsers(pageable);
    }

    @Operation(summary = "¹ì • Œì› ì¡°íšŒ", description = "Œì› •ë³´ ˜ì • Œì› •ë³´ë¥ì¶œë ¥˜ê¸° „í•œ API")
    @AuthFailureResponses
    @OKAndServerErrorResponses
    @ApiResponse(responseCode = "404", description = "Œì› •ë³´ê°€ ì¡´ì¬˜ì ŠëŠ” ê²½ìš°", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{userId}")
    //TODO : ì¶”ê ˆì • @PreAuthorize("hasRole('ADMIN')")
    public AdminUserDetailResponse getUserDetail(@PathVariable Long userId) {
        return adminUserService.getUserDetails(userId);
    }

    //Œì› •ë³´ ˜ì •
    @PatchMapping("/{userId}")
    public AdminUserDetailResponse updateUser(@PathVariable Long userId,
                                              @Valid @RequestBody AdminUserUpdateRequest request){
        return adminUserService.updateUserProfile(userId, request);
    }
}