package com.happypill.infra.presignedurl;

import com.happypill.application.client.PreSignedUrlProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Profile("!prod")
@Component
@RequiredArgsConstructor
public class NoOpsPreSignedUrlProvider implements PreSignedUrlProvider {

    private final S3Properties s3Properties;


    @Override
    public PreSignedUrlResult generatePutPreSignedURL(String key, Duration ttl) {
        return new PreSignedUrlResult("https://www.uploadurl.com", "https://www.cdnurl.com");
    }
}
