package com.peixoto.api;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.peixoto.api.dto.NewBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.net.URISyntaxException;
import java.nio.file.Paths;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.port;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.awaitility.Awaitility.await;

public class S3IntegrationTest extends IntegrationTest {

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${spring.aws.s3.bucket}")
    private String bucketName;

    private final String key = "cover/file.png";


    @BeforeEach
    public void setup() {
        // Just create the bucket on localstack
        amazonS3.createBucket(bucketName);

        // The next assert is not necessary
        // I just put it to demonstrate the key does not exist.
        assertThatThrownBy(() -> amazonS3.getObject(bucketName, key)).isInstanceOf(AmazonS3Exception.class);

        baseURI = "http://localhost";
        port = getLocalPort();
    }

    @Test
    @DisplayName("Deve salvar um novo livro e também inserir a capa no S3")
    void shouldSaveNewBookAndPutCoverImageOns3() throws URISyntaxException {
        var resource = S3IntegrationTest.class.getResource("/file.png");
        var file = Paths.get(resource.toURI()).toFile();

        var book = NewBook.builder()
            .author("Rafael Peixoto")
            .title("Selenium WebDriver: Descomplicando testes automatizados em Java")
            .bookCategory("Test Automation")
            .build();

        given()
            .multiPart("file", file, "multipart/form-data")
            .multiPart("book", book, "application/json")
        .when()
            .post("/s3/books")
        .then()
            .statusCode(201);

        await().untilAsserted(() ->
                assertThat(amazonS3.getObject(bucketName, key)).isNotNull());
    }

    @Test
    void shouldReturnBadRequestWhenNoFileHasSent() {
        var book = NewBook.builder()
            .author("Rafael Peixoto")
            .title("Selenium WebDriver: Descomplicando testes automatizados em Java")
            .bookCategory("Test Automation")
            .build();

        given()
            .log().all()
            .multiPart("book", book, "application/json")
        .when()
            .post("/s3/books")
        .then()
            .log().all()
            .statusCode(400);
    }
}
