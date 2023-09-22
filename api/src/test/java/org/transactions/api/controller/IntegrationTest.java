package org.transactions.api.controller;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import io.restassured.http.Header;
import io.restassured.specification.RequestSpecification;
import org.apache.http.client.utils.URIBuilder;
import org.junit.jupiter.api.Test;
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
public class IntegrationTest {

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

        // Get Token from Keycloak server
        String token = getToken();
        System.out.println(token);
        //token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJuT3JMWF80a2s3Ri1nMmQySWJTaHhUYThVU3N4UF9NeElQSFZ4azlCWW5jIn0.eyJleHAiOjE2OTUzMTMyOTIsImlhdCI6MTY5NTMxMjk5MiwiYXV0aF90aW1lIjoxNjk1MzEyNjQ3LCJqdGkiOiIyMTJmMTIzMC0wZTZhLTQwMTgtOWIxMi1iNzdhODgyOWI4ZGIiLCJpc3MiOiJodHRwOi8va2V5Y2xvYWsuMTI3LTAtMC0xLm5pcC5pby9yZWFsbXMvdHJhbnNhY3Rpb25zIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6IjE0ZTkyMjdmLWFjNmMtNGU1Yi1iYWYxLTE1ZWZiYjU0Yzc1YiIsInR5cCI6IkJlYXJlciIsImF6cCI6InRyYW5zYWN0aW9ucy1hcGkiLCJzZXNzaW9uX3N0YXRlIjoiNDZmYzU3YTctY2EzNS00ZDdiLWFiNzUtOGY3MzMyZDdjODRlIiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHA6Ly9sb2NhbGhvc3QiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInRyYW5zYWN0aW9uLXJlYWRlciIsIm9mZmxpbmVfYWNjZXNzIiwiZGVmYXVsdC1yb2xlcy10cmFuc2FjdGlvbnMiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoicmVhZGVyIHByb2ZpbGUgd3JpdGVyIGVtYWlsIiwic2lkIjoiNDZmYzU3YTctY2EzNS00ZDdiLWFiNzUtOGY3MzMyZDdjODRlIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJuYW1lIjoiSnVsaWVuIEdpcmFyZCIsInByZWZlcnJlZF91c2VybmFtZSI6Imp1Z2lyYXJkIiwiZ2l2ZW5fbmFtZSI6Ikp1bGllbiIsImZhbWlseV9uYW1lIjoiR2lyYXJkIiwiZW1haWwiOiJnaXJhcmQuanVsaWVuQGdtYWlsLmNvbSJ9.hxx5d5B8FaEvdl-uvJeWQzn-YaIVV1KBJXQKgsHSPbGA8697gdbndJRM9X43ijVtCB12ly8BlPYC0ULQEU2USywWpbIxXve26AgrRE91H-DyK4SOP1qZJYyWK4ob9cwiaRklceS32ifAqPDulF6x4fvo0Q5VGdneohe9wYWF_rzic_JBY3KPJVj43OTqMFBo2oGc_knImRWXAoaFWU_EeC7rfUzSr8pomPhN-PkfoPeTZqoxvMzuJiAWHTLrRtwmFkXFEEL2hkeKkemk1azL64cU9nxsE7xTLPsVkeBPpHGrsZ4GFFWhysGxTOLMWNhNP5IJMgA1NUMgRAmZSL_cqA";

        // Call API
        RequestSpecification request = given()
            .body("{\n" +
                    "\t\"date\": \"2023-09-01T22:18:37.683+01:00\",\n" +
                    "\t\"transactions\": [\n" +
                    "\t\t{\n" +
                    "\t\t\t\"income\" : 0,\n" +
                    "\t\t\t\"outcome\": 69.57,\n" +
                    "            \"category\" : {\n" +
                    "                \"id\" : 26\n" +
                    "            },\n" +
                    "\t\t\t\"bankAccount\": {\n" +
                    "\t\t\t\t\"id\" : 30\n" +
                    "\t\t\t}\n" +
                    "\t\t}\n" +
                    "\t]\n" +
                    "}")
            .header(new Header("Authorization", "Bearer " + token))
            .header(new Header("Content-Type", "application/json"));
        request.when()
            .post("http://localhost:" + randomServerPort + "/transactions")

        .then()
            .statusCode(201);

        // Check entry in database

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
