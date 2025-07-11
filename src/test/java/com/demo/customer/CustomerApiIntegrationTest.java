
package com.demo.customer;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.server.*;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerApiIntegrationTest {

    @LocalServerPort
    private int port;

    private static WireMockServer wireMockServer;

    @BeforeAll
    static void setupWireMock() {
        wireMockServer = new WireMockServer(8089); // Use a free port
        wireMockServer.start();
        // Configure stubs here if you have external dependencies
    }

    @AfterAll
    static void stopWireMock() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    @BeforeEach
    void setupRestAssured() {
        RestAssured.port = port;
    }

    @Test
    void testCreateReadUpdateDeleteCustomer() {

        String uniqueEmail = "testuser_" + UUID.randomUUID() + "@example.com";
        // Create
        String customerId =
                given()
                        .contentType(ContentType.JSON)
                        .body("{\n" +
                                "  \"firstName\": \"Raghut\",\n" +
                                "  \"middleName\": \"testt\",\n" +
                                "  \"lastName\": \"Tamminat\",\n" +
                                "  \"emailAddress\": \"" + uniqueEmail + "\",\n" +
                                "  \"phoneNumber\": \"+1 (595) 123-4567\"\n" +
                                "}")
                        .when()
                        .post("/api/customers")
                        .then()
                        .statusCode(200)
                        .body("firstName", equalTo("Raghut"))
                        .extract().path("id");

        // Read
        given()
                .when()
                .get("/api/customers/{id}", customerId)
                .then()
                .statusCode(200)
                .body("firstName", equalTo("Raghut"));

        // Update
        given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"firstName\": \"Raghut\",\n" +
                        "  \"middleName\": \"testt\",\n" +
                        "  \"lastName\": \"Tamminat\",\n" +
                        "  \"emailAddress\": \"" + uniqueEmail + "\",\n" +
                        "  \"phoneNumber\": \"+1 (595) 123-4567\"\n" +
                        "}")
                .when()
                .put("/api/customers/{id}", customerId)
                .then()
                .statusCode(200)
                .body("firstName", equalTo("Raghut"));

        // Delete
        when()
                .delete("/api/customers/{id}", customerId)
                .then()
                .statusCode(204);

        // Verify Deletion
        when()
                .get("/api/customers/{id}", customerId)
                .then()
                .statusCode(404);
    }
}
