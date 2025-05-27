package com.happypill.application.service;


public class NoOpsPreSignedUrlProvider implements PreSignedUrlProvider {
    @Override
    public PreSignedUrlResponse generateUploadUrl(String key) {
        return null;
    }
}
