package com.peixoto.api.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
public class AwsConfig {

    @Value("${spring.aws.accessKey}")
    private String accessKey;

    @Value("${spring.aws.secretKey}")
    private String secretKey;

    @Value("${spring.aws.region}")          private String region;
    @Value("${spring.aws.s3.url}")          private String s3EndpointUrl;
    @Value("${spring.aws.s3.bucket}")       private String bucketName;

    private BasicAWSCredentials awsCredentials() {
        return new BasicAWSCredentials(accessKey, secretKey);
    }

    @Bean
    public AmazonS3 getAmazonS3() {
        return AmazonS3ClientBuilder
            .standard()
            .withCredentials(new AWSStaticCredentialsProvider(awsCredentials()))
            .withEndpointConfiguration(getEndpointConfiguration(s3EndpointUrl))
            .build();
    }

    private EndpointConfiguration getEndpointConfiguration(String url) {
        return new EndpointConfiguration(url, region);
    }
}
