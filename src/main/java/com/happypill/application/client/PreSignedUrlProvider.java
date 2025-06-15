package com.happypill.application.client;

import java.time.Duration;

public interface PreSignedUrlProvider {

    PreSignedUrlResult generatePutPreSignedURL(String key, Duration ttl);

    record PreSignedUrlResult(String uploadUrl, String cdnUrl) {
    }

}
