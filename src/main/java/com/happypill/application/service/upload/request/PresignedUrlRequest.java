package com.happypill.application.service.upload.request;

import jakarta.validation.constraints.NotEmpty;

public record PresignedUrlRequest(

        @NotEmpty
        String fileName
) {
}
