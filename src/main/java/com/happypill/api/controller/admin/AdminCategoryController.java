package com.happypill.api.controller.admin;

import com.happypill.application.pagination.CustomPage;
import com.happypill.application.service.admin.AdminCategoryService;
import com.happypill.application.service.admin.request.AdminCategoryRequest;
import com.happypill.application.service.admin.request.AdminCategoryUpdateRequest;
import com.happypill.application.service.admin.response.AdminCategoryInfoResponse;
import com.happypill.application.service.admin.response.AdminCategoryListResponse;
import com.happypill.application.service.category.dto.response.CategoryNamesResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Locale;

@Tag(name = "[관리자] 카테고리", description = "관리자가 카테고리 정보를 조회/관리하기 위한 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/categories")
public class AdminCategoryController {

    private final AdminCategoryService adminCategoryService;

    @Operation(summary = "모든 카테고리 조회", description = "카테고리 리스트를 조회합니다.")
    @GetMapping
    //TODO : 추가 예정 @PreAuthorize("hasRole('ADMIN')")
    public CustomPage<AdminCategoryListResponse> getCategories(@RequestParam(value = "page", defaultValue = "1") int page,
                                                               @RequestParam(value = "size", defaultValue = "5") int size,
                                                               Locale locale) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return adminCategoryService.getAllCategories(pageable, locale);
    }

    @PostMapping
//   ToDo @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> saveCategories(@RequestBody @Valid AdminCategoryRequest adminCategoryCreateRequestList) {
        adminCategoryService.saveCategories(adminCategoryCreateRequestList);

        return ResponseEntity.created(URI.create("/api/admin/categories")).build();
    }

    @GetMapping("{categoryId}")
    //TODO : 추가 예정 @PreAuthorize("hasRole('ADMIN')")
    public AdminCategoryInfoResponse getCategoryDetail(@PathVariable Long categoryId){
        return adminCategoryService.getCategoryDetails(categoryId);
    }

    @GetMapping("/names")
    //TODO : 추가 예정 @PreAuthorize("hasRole('ADMIN')")
    public List<CategoryNamesResponse> getCategoryNames(){
        return adminCategoryService.getCategoryNames();
    }

    @PatchMapping("/{categoryId}")
    //TODO : 추가 예정 @PreAuthorize("hasRole('ADMIN')")
    public AdminCategoryInfoResponse updateCategory(@PathVariable Long categoryId,
                                                    @Valid @RequestBody AdminCategoryUpdateRequest request) {
        return adminCategoryService.updateCategory(categoryId, request);
    }

    @DeleteMapping("/{categoryId}")
    //TODO : 추가 예정 @PreAuthorize("hasRole('ADMIN')")
    public void deleteCategory(@PathVariable Long categoryId){
        adminCategoryService.deleteCategory(categoryId);
    }
}