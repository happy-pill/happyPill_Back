package com.happypill.infra.presignedurl;


import com.happypill.application.client.PreSignedUrlProvider;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;

@Profile("prod")
@Component
@RequiredArgsConstructor
public class S3PreSignedUrlProvider implements PreSignedUrlProvider {

    private final S3Presigner s3Presigner = S3Presigner
            .builder()
//            .region(Region.AP_NORTHEAST_2)
//            .credentialsProvider(DefaultCredentialsProvider.create())
            .build();

    private final S3Properties s3Properties;

    @Override
    public PreSignedUrlResult generatePutPreSignedURL(String key, Duration ttl) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(s3Properties.bucketName())
                .key(key)
                .build();
        PutObjectPresignRequest putObjectPresignRequest = PutObjectPresignRequest
                .builder().putObjectRequest(putObjectRequest)
                .signatureDuration(ttl)
                .build();
        PresignedPutObjectRequest request = s3Presigner.presignPutObject(putObjectPresignRequest);

        return new PreSignedUrlResult(request.url().toExternalForm(), s3Properties.cdnBaseUrl() + "/" + key);
    }

    @PreDestroy
    public void closePreSigner() {
        s3Presigner.close();
    }

}
