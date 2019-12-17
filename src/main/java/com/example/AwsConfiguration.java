package com.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Configuration
public class AwsConfiguration {

    @Value("${example.region}")
    private String regionName;

    @Value("${example.bucketName}")
    private String bucketName;

    @Value("${example.accessKeyId}")
    private String accessKeyId;

    @Value("${example.secretAccessKey}")
    private String secretAccessKey;

    @Value("${localstack.s3Endpoint.internal}")
    private String localStackS3EndpointInternal;

    @Value("${localstack.s3Endpoint.external}")
    private String localStackS3EndpointExternal;

    @Bean
    @Profile("!DEV")
    S3Client s3Client(AwsCredentials awsCredentials) {
        return S3Client.builder().region(Region.of(regionName))
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();
    }

    @Bean
    @Profile("DEV")
    S3Client localStackS3Client(AwsCredentials awsCredentials) {
        return S3Client.builder().region(Region.of(regionName))
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .endpointOverride(URI.create(localStackS3EndpointInternal))
                .build();
    }

    @Bean
    AwsCredentials awsCredentials(){
        return AwsBasicCredentials.create(accessKeyId, secretAccessKey);
    }

    @Profile("DEV")
    @Bean
    S3Bucket bucketUrl(){
        return new S3Bucket(bucketName, new StringBuffer(localStackS3EndpointExternal).append("/").append(bucketName).append("/").toString());
    }

    @Profile("!DEV")
    @Bean
    S3Bucket localStackBucketUrl(){
        return new S3Bucket(bucketName, new StringBuilder("https://").append(bucketName).append(".s3.amazonaws.com/").toString());
    }



}
