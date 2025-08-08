package com.happypill.application.service.upload;

import com.happypill.application.client.PreSignedUrlProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadService {

    private final Duration presignedUrlTTL = Duration.ofMinutes(3);

    private final PreSignedUrlProvider preSignedUrlProvider;

    public PreSignedUrlProvider.PreSignedUrlResult generatePresignedUrl(String fileName) {
        String extension = extractExtension(fileName);
        String key = UUID.randomUUID() + "." + extension;
        return preSignedUrlProvider.generatePutPreSignedURL(key, presignedUrlTTL);
    }

    private String extractExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == fileName.length() - 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "유효하지 않은 파일명: 확장자가 없습니다. -> " + fileName);
        }
        return fileName.substring(lastDotIndex + 1).toLowerCase();
    }
}
