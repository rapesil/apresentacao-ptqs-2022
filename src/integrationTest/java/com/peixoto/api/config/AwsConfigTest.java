package com.peixoto.api.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.localstack.LocalStackContainer.Service;

import static com.peixoto.api.IntegrationTest.getLocalStackContainer;

@TestConfiguration
@Profile("test")
public class AwsConfigTest {

    @Bean
    public AmazonS3 getAmazonS3() {
        return AmazonS3ClientBuilder
            .standard()
            .withEndpointConfiguration(
                new AwsClientBuilder.EndpointConfiguration(
                    getLocalStackContainer().getEndpointOverride(Service.S3).toString(),
                    getLocalStackContainer().getRegion()))
            .withCredentials(
                    new AWSStaticCredentialsProvider(new BasicAWSCredentials(getLocalStackContainer().getAccessKey(),
                        getLocalStackContainer().getSecretKey())))
            .build();
    }
}
