package com.happypill.application.service;


public interface PreSignedUrlProvider {

    /**
     * 유니크한 키(경로)를 주면, 그 경로에 맞는 pre_signed_url과, public url(cdn 등 실제경로)를 반환
     *
     * @param key storage path
     * @return {@link PreSignedUrlResponse}
     */
    PreSignedUrlResponse generateUploadUrl(String key); //key should be unique (postId?)
}
