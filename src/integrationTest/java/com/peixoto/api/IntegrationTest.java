package com.peixoto.api;

import com.peixoto.api.config.AwsConfigTest;
import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

@Tag("integrationTest")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestingWebApplication.class)
@ActiveProfiles("test")
@ContextConfiguration(loader = SpringBootContextLoader.class)
@TestPropertySource(locations = "classpath:application-test.yml")
@Import(AwsConfigTest.class)
@Testcontainers
@AutoConfigureWireMock(port = 0)
public class IntegrationTest {

    private static DockerImageName localstackImage = DockerImageName.parse("localstack/localstack:0.11.3");

    @Container
    private static final LocalStackContainer LOCAL_STACK = new LocalStackContainer(localstackImage)
            .withServices(S3);

    @LocalServerPort
    private int localPort;

    public int getLocalPort() {
        return localPort;
    }

    public static LocalStackContainer getLocalStackContainer() {
        return LOCAL_STACK;
    }
}
