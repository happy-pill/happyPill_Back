package com.happypill.api.controller.admin;

import com.happypill.application.exception.global.AuthResponse;
import com.happypill.application.exception.global.ErrorResponse;
import com.happypill.application.pagination.CustomPage;
import com.happypill.application.service.admin.AdminProductService;
import com.happypill.application.service.admin.request.AdminProductCreateRequest;
import com.happypill.application.service.admin.request.AdminProductUpdateRequest;
import com.happypill.application.service.admin.response.AdminProductInfoResponse;
import com.happypill.application.service.admin.response.AdminProductListResponse;
import com.happypill.application.service.admin.response.AdminProductPriceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Locale;

@Tag(name = "[관리자] 상품", description = "관리자가 상품 정보를 조회/관리하기 위한 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/products")
public class AdminProductController {

    private static final String LANGUAGE_HEADER = "Language";

    private final AdminProductService adminProductService;

    @Operation(summary = "모든 상품 조회", description = "모든 상품들을 출력하기 위한 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청이 정상적으로 처리되었을 때"),
            @ApiResponse(responseCode = "401", description = "인증 실패 했을 때", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "403", description = "인가 실패 했을 때", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "404", description = "카테고리 정보가 존재하지 않는 경우", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부에서 오류 발생 했을 때", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    //TODO : 추가 예정 @PreAuthorize("hasRole('ADMIN')")
    public CustomPage<AdminProductListResponse> getProducts(@RequestParam(value = "categories", required = false) Long categoryId,
                                                            @RequestParam(value = "page", defaultValue = "1") int page,
                                                            @RequestParam(value = "size", defaultValue = "8") int size,
                                                            @RequestHeader(LANGUAGE_HEADER) String headerLanguage) {
        Locale locale = Locale.forLanguageTag(headerLanguage);
        Pageable pageable = PageRequest.of(page - 1, size);
        return adminProductService.getAllProducts(categoryId, pageable, locale);
    }

    @Operation(summary = "특정 상품 조회", description = "상품 정보 수정 시 상품 정보를 출력하기 위한 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청이 정상적으로 처리되었을 때"),
            @ApiResponse(responseCode = "401", description = "인증 실패 했을 때", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "403", description = "인가 실패 했을 때", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "404", description = "상픔 정보가 존재하지 않는 경우", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부에서 오류 발생 했을 때", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{productId}")
    //TODO : 추가 예정 @PreAuthorize("hasRole('ADMIN')")
    public AdminProductInfoResponse getProductDetail(@PathVariable Long productId) {
        return adminProductService.getProductDetails(productId);
    }

    @Operation(summary = "금액 기록 조회", description = "관리자가 상품의 가격을 히스토리별로 조회할 수 있기 위한 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청이 정상적으로 처리되었을 때"),
            @ApiResponse(responseCode = "401", description = "인증 실패 했을 때", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "403", description = "인가 실패 했을 때", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "404", description = "상픔 정보가 존재하지 않는 경우", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부에서 오류 발생 했을 때", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{productId}/price-history")
    //TODO : 추가 예정 @PreAuthorize("hasRole('ADMIN')")
    public CustomPage<AdminProductPriceResponse> getProductPriceHistory(@PathVariable(value = "productId") Long productId,
                                                                        @RequestParam(value = "page", defaultValue = "1") int page,
                                                                        @RequestParam(value = "size", defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return adminProductService.getAllProductPrices(productId, pageable);
    }

    @Operation(summary = "상품 등록", description = "관리자가 새로운 상품을 등록하기 위한 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "요청이 정상적으로 처리되었을 때"),
            @ApiResponse(responseCode = "401", description = "인증 실패 했을 때", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "403", description = "인가 실패 했을 때", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "요청 DTO의 유효성 검증에 실패했을 때", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부에서 오류 발생 했을 때", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

    })
    @PostMapping
    //TODO : 추가 예정 @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createProduct(@Valid @RequestBody AdminProductCreateRequest request) {
        long productId = adminProductService.createProduct(request);
        return ResponseEntity.created(URI.create("/api/admin/products/" + productId)).build();
    }

    @Operation(summary = "상품 수정", description = "관리자가 상품 정보를 수정하기 위한 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청이 정상적으로 처리되었을 때"),
            @ApiResponse(responseCode = "401", description = "인증 실패 했을 때", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "403", description = "인가 실패 했을 때", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "요청 DTO의 유효성 검증에 실패했을 때", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "상픔 정보가 존재하지 않는 경우", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부에서 오류 발생 했을 때", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/{productId}")
    //TODO : 추가 예정 @PreAuthorize("hasRole('ADMIN')")
    public AdminProductInfoResponse updateProduct(@PathVariable("productId") Long productId,
                                                  @Valid @RequestBody AdminProductUpdateRequest request) {
        return adminProductService.updateProduct(productId, request);
    }

    @Operation(summary = "상품 삭제", description = "관리자가 상품을 삭제하기 위한 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "요청이 정상적으로 처리되었을 때"),
            @ApiResponse(responseCode = "401", description = "인증 실패 했을 때", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "403", description = "인가 실패 했을 때", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "404", description = "상픔 정보가 존재하지 않는 경우", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부에서 오류 발생 했을 때", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{productId}")
    //TODO : 추가 예정 @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteProduct(@PathVariable(value = "productId") Long productId) {
        adminProductService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }
}