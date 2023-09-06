package com.kzh.imageuploadapp.bucket;

public enum BucketName {

    PROFILE_IMAGE("YOUR S3 BUCKET NAME HERE");

    private final String bucketName;

    BucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketName() {
        return bucketName;
    }
}
