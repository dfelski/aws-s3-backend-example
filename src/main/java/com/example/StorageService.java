package com.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
class StorageService {

    @Value("${example.bucketName}")
    private String bucketName;

    @Value("${example.region}")
    private String regionName;

    @Value("${example.accessKeyId}")
    private String accessKeyId;

    @Value("${example.secretAccessKey}")
    private String secretAccessKey;


    private S3Client s3Client;

    @PostConstruct
    void init() {
        Region region = Region.of(regionName);
        AwsCredentials credentials =
                AwsBasicCredentials.create(accessKeyId, secretAccessKey);
        s3Client = S3Client.builder().region(region).credentialsProvider(StaticCredentialsProvider.create(credentials)).build();
    }

    int store(MultipartFile file) throws IOException {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(file.getOriginalFilename())
                .build();
        PutObjectResponse putObjectResponse = s3Client.putObject(putObjectRequest, RequestBody.fromByteBuffer(ByteBuffer.wrap(file.getBytes())));
        return putObjectResponse.sdkHttpResponse().statusCode();
    }

    List<URI> loadAll() {
        ListObjectsRequest listObjects = ListObjectsRequest.builder()
                .bucket(bucketName)
                .build();
        ListObjectsResponse listObjectsResponse = s3Client.listObjects(listObjects);
        return listObjectsResponse.contents().stream()
                .map(s3o -> URI.create(
                        new StringBuilder("https://").append(bucketName).append(".s3.amazonaws.com/").append(s3o.key()).toString()))
                .collect(Collectors.toList());
    }
}
