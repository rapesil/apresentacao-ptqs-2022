package com.peixoto.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(classes = TestingWebApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Tag("integrationTest")
@ActiveProfiles("test")
@ContextConfiguration(loader = SpringBootContextLoader.class)
@DisplayName("Testes de integração - Livros - Antes de correções")
public class BookIntegrationTest {

    @LocalServerPort
    private int localPort;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = localPort;
    }

    @Test
    void t1() {
        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .auth()
            .basic("admin1", "test")
        .when()
            .get("/books/1")
        .then().log().all()
            .statusCode(200);
    }

    @Test
    void t2() {
        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .auth()
            .basic("admin1", "test")
        .when()
            .get("/books/")
        .then().log().all()
            .statusCode(200);
    }

    @Test
    void t3() {
        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .auth()
            .basic("admin1", "test")
            .body("{\n" +
                "    \"id\": 3,\n" +
                "    \"title\": \"Selenium WebDriver - Descomplicando testes com java\",\n" +
                "    \"author\": \"Rafael Peixoto\",\n" +
                "    \"bookCategory\": \"Qualidade de Software\",\n" +
                "    \"imagePath\": null\n" +
                "}")
        .when()
            .post("/books/")
        .then().log().all()
            .statusCode(200);
    }
}



