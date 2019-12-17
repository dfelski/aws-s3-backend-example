package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.stream.Collectors;

@Service
class StorageService {

    private S3Client s3Client;
    private S3Bucket s3Bucket;

    @Autowired
    StorageService(S3Client s3Client, S3Bucket s3Bucket){
        this.s3Client = s3Client;
        this.s3Bucket = s3Bucket;
    }

    int store(MultipartFile file) throws IOException {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(s3Bucket.getName())
                .key(file.getOriginalFilename())
                .contentType(file.getContentType())
                .build();
        PutObjectResponse putObjectResponse = s3Client.putObject(putObjectRequest, RequestBody.fromByteBuffer(ByteBuffer.wrap(file.getBytes())));
        return putObjectResponse.sdkHttpResponse().statusCode();
    }

    List<URI> loadAll() {
        ListObjectsRequest listObjects = ListObjectsRequest.builder()
                .bucket(s3Bucket.getName())
                .build();
        ListObjectsResponse listObjectsResponse = s3Client.listObjects(listObjects);
        return listObjectsResponse.contents().stream()
                .map(s3o -> s3Bucket.getResourceUrl(s3o.key()))
                .collect(Collectors.toList());
    }
}
