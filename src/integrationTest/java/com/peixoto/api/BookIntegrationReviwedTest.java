package com.peixoto.api;

import com.peixoto.api.dto.NewBookDTO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(classes = TestingWebApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Tag("integrationTest")
@ActiveProfiles("test")
@ContextConfiguration(loader = SpringBootContextLoader.class)
@DisplayName("Testes de integração - Livros - Após as correções")
public class BookIntegrationReviwedTest {

    @LocalServerPort
    private int localPort;

    @Autowired
    private BookHelpers helper;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = localPort;

        helper.deleteByIdIfExists(3L);
    }

    @Test
    @DisplayName("Deve buscar livro pelo id com sucesso")
    void t1() {
        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .auth()
            .basic("admin1", "test")
            .when()
            .get("/books/1")
            .then().log().all()
            .statusCode(200)
            .body("title", Matchers.is("Testes de software"))
            .body("author", Matchers.is("Rafael Peixoto" ));
    }

    @Test
    @DisplayName("Deve buscar todos os livros cadastrados com sucesso")
    void t2() {
        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .auth()
            .basic("admin1", "test")
            .when()
            .get("/books/")
            .then().log().all()
            .statusCode(200)
            .body("size()", Matchers.equalTo(2));
    }

    @Test
    @DisplayName("Deve inserir um novo livro com sucesso")
    void t3() {
        final var novoLivro = NewBookDTO.builder()
            .title("Selenium WebDriver - Descomplicando testes com java")
            .author("Rafael Peixoto")
            .bookCategory("Qualidade de Software")
            .build();

        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .auth()
            .basic("admin1", "test")
            .body(novoLivro)
        .when()
            .post("/books/")
        .then().log().all()
            .statusCode(201);
    }

}
