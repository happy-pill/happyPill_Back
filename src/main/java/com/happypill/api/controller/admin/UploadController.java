package com.happypill.api.controller.admin;

import com.happypill.application.client.PreSignedUrlProvider;
import com.happypill.application.service.upload.UploadService;
import com.happypill.application.service.upload.request.PresignedUrlRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 기획상, 업로드는 Admin만 가능
 */
@RestController
@RequiredArgsConstructor
public class UploadController {

    private final UploadService uploadService;

    @PostMapping("/api/admin/upload/presigned-url")
    public PreSignedUrlProvider.PreSignedUrlResult generatePresignedUrl(@RequestBody @Valid PresignedUrlRequest request) {
        return uploadService.generatePresignedUrl(request.fileName());
    }
}
