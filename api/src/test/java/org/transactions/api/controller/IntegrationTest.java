package org.transactions.api.controller;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.restassured.http.Header;
import org.apache.http.client.utils.URIBuilder;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.model.transactions.Transaction;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.transactions.utils.TransactionsMongoDbContainer;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * Test that receive request from a random authenticated client
 * and then realize action to persist in database
 */
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Tag("IntegrationTest")
public class IntegrationTest {

    public static final String AUTHORIZATION = "Authorization";
    @Container
    static KeycloakContainer keycloak = new KeycloakContainer("quay.io/keycloak/keycloak:21.1.2")
            .withRealmImportFile("keycloak/realm-export.json");

    @Container
    static final TransactionsMongoDbContainer mongoDbContainer = new TransactionsMongoDbContainer();

    @LocalServerPort
    int randomServerPort;

    @DynamicPropertySource
    static void registerResourceServerIssuerProperty(DynamicPropertyRegistry registry) {
        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri", () -> keycloak.getAuthServerUrl() + "realms/transaction");
        registry.add("spring.security.oauth2.resourceserver.jwt.jwk-set-uri", () -> keycloak.getAuthServerUrl() + "realms/transaction/protocol/openid-connect/certs");

        registry.add("spring.data.mongodb.host", () -> mongoDbContainer.getHost());
        registry.add("spring.data.mongodb.port", () -> mongoDbContainer.getMappedPort(27017));
    }

    @Test
    void integrationTest() throws URISyntaxException {

        String urlApp = "http://localhost:" + randomServerPort;

        // Get Token from Keycloak server
        String token = getToken();
        String headerAuthValue = "Bearer " + token;

        // 1 - Get All Transactions - should be empty
        given()
            .header(new Header(AUTHORIZATION, headerAuthValue))
            .header(new Header("Content-Type", "application/json"))
        .when()
            .get(urlApp + "/transactions")
        .then()
            .statusCode(200)
            .body("$", Matchers.empty());

        // 2 - Create a transaction
        String id = given()
            .body("{\n" +
                    "\t\"date\": \"2020-05-04T22:16:37.683+01:00\",\n" +
                    "\t\"transactions\": [\n" +
                    "\t\t{\n" +
                    "\t\t\t\"income\" : 0,\n" +
                    "\t\t\t\"outcome\": 1276.87,\n" +
                    "\t\t\t\"category\": {\n" +
                    "\t\t\t\t\"id\": 1,\n" +
                    "\t\t\t\t\"category\": \"Maison\",\n" +
                    "\t\t\t\t\"label\": \"Loyer/Prêt\",\n" +
                    "\t\t\t\t\"type\": \"FIXE\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"bankAccount\": {\n" +
                    "\t\t\t\t\"id\" : 12,\n" +
                    "\t\t\t\t\"category\": \"Commun\",\n" +
                    "\t\t\t\t\"label\": \"CMB\"\n" +
                    "\t\t\t},\n" +
                    "\t\t\t\"description\": \"Loyer\"\n" +
                    "\t\t}\n" +
                    "\t]\n" +
                    "}")
            .header(new Header("Authorization", headerAuthValue))
            .header(new Header("Content-Type", "application/json"))
        .when()
            .post(urlApp + "/transactions")
        .then()
            .statusCode(201)
            .extract()
                .path("id");

        // 3 - Patch transaction
        given()
            .header(new Header(AUTHORIZATION, headerAuthValue))
            .header(new Header("Content-Type", "application/json-patch+json"))
            .body("[\n" +
                    "    {\n" +
                    "        \"op\" : \"replace\",\n" +
                    "        \"path\" : \"/transactions/0/outcome\",\n" +
                    "        \"value\": 50\n" +
                    "    }\n" +
                    "]")
        .when()
            .patch(urlApp + "/transactions/" + id)
        .then()
            .statusCode(200);

        // 4 - Consult transaction
        Transaction result = given()
            .header(new Header(AUTHORIZATION, headerAuthValue))
            .header(new Header("Content-Type", "application/json"))
        .when()
            .get(urlApp + "/transactions/" + id)
        .then()
            .statusCode(200)
            .extract().body().as(Transaction.class);

        // Assertions
        Assertions.assertEquals(id, result.getId());
        Assertions.assertEquals(1, result.getTransactions().size());
        Assertions.assertEquals(50, result.getTransactions().get(0).getOutcome());
        Assertions.assertEquals(0, result.getTransactions().get(0).getIncome());
        Assertions.assertEquals(12, result.getTransactions().get(0).getBankAccount().getId());
        Assertions.assertEquals(1, result.getTransactions().get(0).getCategory().getId());

        // 5 - Delete Transaction
        given()
            .header(new Header(AUTHORIZATION, headerAuthValue))
            .header(new Header("Content-Type", "application/json"))
        .when()
            .delete(urlApp + "/transactions/" + id)
        .then()
            .statusCode(204);

        // 6 - Check that no transactions exists
        given()
                .header(new Header(AUTHORIZATION, headerAuthValue))
                .header(new Header("Content-Type", "application/json"))
                .when()
                .get(urlApp + "/transactions")
                .then()
                .statusCode(200)
                .body("$", Matchers.empty());

    }


    private String getToken() throws URISyntaxException {
        URI authorizationURI = new URIBuilder(keycloak.getAuthServerUrl() + "realms/transaction/protocol/openid-connect/token").build();

        String result = given()
                .formParams(Map.of(
                        "grant_type", Collections.singletonList("password"),
                        "client_id", Collections.singletonList("transactions-client"),
                        "username", Collections.singletonList("jane.doe@baeldung.com"),
                        "password", Collections.singletonList("s3cr3t")
                ))
                .contentType("application/x-www-form-urlencoded")
                .post(authorizationURI)
                .then()
                    .statusCode(200)
                    .extract()
                        .path("access_token");

        return result;
    }
}