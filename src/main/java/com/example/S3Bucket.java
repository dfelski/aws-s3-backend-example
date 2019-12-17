package com.example;

import java.net.URI;

/**
 * Representation of the AWS S3 bucket.
 */
class S3Bucket {

    private String name;
    private String baseUrl;

    S3Bucket(String name, String baseUrl) {
        this.name = name;
        this.baseUrl = baseUrl;
    }

    public String getName() {
        return name;
    }

    URI getResourceUrl(String resource){
        return URI.create(new StringBuilder(baseUrl).append(resource).toString());
    }

}
