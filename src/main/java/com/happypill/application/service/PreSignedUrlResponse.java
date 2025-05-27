package com.happypill.application.service;

public record PreSignedUrlResponse(
        String preSignedUrl, // 업로드할 경로
        String publicUrl //실제 CDN 경로
) {
}
