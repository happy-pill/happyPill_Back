package com.happypill.infra.presignedurl;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aws.s3")
public record S3Properties(
        String bucketName,
        String cdnBaseUrl
) {
}
