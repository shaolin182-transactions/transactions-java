package org.transactions.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.model.error.Error;
import org.model.transactions.Transaction;
import org.model.transactions.builder.TransactionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityDataConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.transactions.ITransactionService;
import org.transactions.api.security.SecurityConfig;
import org.transactions.connector.ITransactionDataSource;
import org.transactions.exception.TransactionNotFoundException;
import org.transactions.persistence.repositories.TransactionsRepository;

import java.time.OffsetDateTime;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.model.transactions.TransactionCategoryType.EXTRA;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Some integration tests for testing api layer
 */

@WebMvcTest(controllers = TransactionsController.class)
class TransactionsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ITransactionService service;

    @MockBean
    ITransactionDataSource datasource;

    @MockBean
    TransactionsRepository repository;

    @MockBean
    JwtDecoder jwtDecoder;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("Get All Transactions - Nominal Case")
    @Test
    void getAll() throws Exception {

        when(service.getAllTransactions()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/transactions")
                .contentType("application/json")
                .with(jwt(builder -> builder.claim("scope", new String("reader")))))
                .andExpect(status().isOk());

    }

    @DisplayName("Get Transaction - Check Error Handling")
    @Test
    void getTransactionException() throws Exception {

        when(service.getTransaction(Mockito.anyString())).thenThrow(TransactionNotFoundException.class);

        MvcResult result = mockMvc.perform(get("/transactions/anyId")
                .with(jwt(builder -> builder.claim("scope", new String("reader")))))
                .andExpect(status().isNotFound())
                .andReturn();

        Error error = new Error(100, "No transaction Found");
        String response = result.getResponse().getContentAsString();

        Assertions.assertThat(objectMapper.writeValueAsString(error)).isEqualToIgnoringCase(response);
    }

    @DisplayName("Get Transaction - Nominal Case")
    @Test
    void getTransaction() throws Exception {

        Transaction expectedTransaction = new TransactionBuilder()
                .addTransactions()
                    .addTransaction()
                        .withCategory().withId(1).withCategory("desc").withLabel("label").done()
                        .withBankAccount().withCategory("cat").withId(2).withLabel("label").done()
                        .withIncome(12f).withOutcome(0f).done()
                .done()
                .withCost(12L)
                .build();

        when(service.getTransaction(Mockito.anyString())).thenReturn(expectedTransaction);

        MvcResult result = mockMvc.perform(get("/transactions/anyId")
                .with(jwt(builder -> builder.claim("scope", new String("reader")))))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        Assertions.assertThat(objectMapper.writeValueAsString(expectedTransaction)).isEqualToIgnoringCase(response);
    }

    @Test
    @DisplayName("Create Transaction - Nominal Case")
    void createTransaction() throws Exception {

        Transaction expectedTransaction = new TransactionBuilder()
                .addTransactions()
                    .addTransaction()
                        .withCategory().withId(1).withCategory("aCategory").withLabel("aLabel").withType(EXTRA).done()
                        .withBankAccount().withCategory("aCategory").withId(1).withLabel("aLabel").done()
                        .withIncome(0f).withOutcome(123.5f).done()
                .done()
                .withDate(OffsetDateTime.now())
                .build();

        when(service.createTransaction(Mockito.any())).thenReturn(expectedTransaction);

        MvcResult result = mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(expectedTransaction))
                        .with(jwt(builder -> builder.claim("scope", new String("writer")))))
                .andExpect(status().isCreated())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        Assertions.assertThat(objectMapper.writeValueAsString(expectedTransaction)).isEqualToIgnoringCase(response);
    }

    @DisplayName("Create Transaction - Nominal case with JSON")
    @Test
    void createTransactionWithJSON() throws Exception {
        String transaction = "{" +
                "\"date\": \"2020-05-01T22:16:37.683+01:00\"," +
                "\"transactions\": [" +
                "{" +
                "\"bankAccount\": {\"id\": 1, \"category\": \"aCategory\", \"label\": \"aLabel\"}," +
                "\"income\": 0, \"outcome\": 123.5, \"description\": \"Some description\"," +
                "\"category\":  {\"id\": 1, \"category\": \"aCategory\", \"label\": \"aLabel\", \"type\" : \"EXTRA\"}" +
                "}" +
                "]" +
                "}";

        MvcResult result = mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(transaction)
                .with(jwt(builder -> builder.claim("scope", new String("writer")))))
                .andExpect(status().isCreated())
                .andReturn();


    }

    @DisplayName("Create Transaction - JSON with incorrect data")
    @Test
    void createTransactionWrongJSON() throws Exception {
        String transaction = "{" +

                "\"transactions\": [" +
                "{" +
                "\"bankAccountt\": {\"id\": 1, \"category\": \"aCategory\", \"label\": \"aLabel\"}," +
                "\"income\": 0, \"outcome\": 123.5, \"description\": \"Some description\"," +
                "\"category\":  {\"id\": 1, \"category\": \"aCategory\", \"label\": \"aLabel\"}" +
                "}" +
                "]" +
                "}";

        MvcResult result = mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(transaction)
                .with(jwt(builder -> builder.claim("scope", new String("writer")))))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @DisplayName("Create Transaction - Correct JSON with invalid data")
    @Test
    void createTransactionWWithInvalidData() throws Exception {
        String transaction = "{" +

                "\"transactions\": [" +
                "{" +
                "\"income\": 10, \"outcome\": 123.5, \"description\": \"Some description\"," +
                "\"category\":  {\"id\": 1, \"category\": \"aCategory\", \"label\": \"aLabel\"}" +
                "\"from\": {\"id\": 1, \"category\": \"aCategory\", \"label\": \"aLabel\"}," +
                "}" +
                "]" +
                "}";

        MvcResult result = mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(transaction)
                .with(jwt(builder -> builder.claim("scope", new String("writer")))))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @DisplayName("Delete Transaction - Nominal Case")
    @Test
    void deleteTransaction() throws Exception {

        mockMvc.perform(delete("/transactions/someId")
                .contentType(MediaType.APPLICATION_JSON)
                .with(jwt(builder -> builder.claim("scope", new String("writer")))))
                .andExpect(status().isNoContent());
    }

    @DisplayName("Patch Transaction - Nominal Case")
    @Test
    void patchTransaction() throws Exception {

        MvcResult result = mockMvc.perform(patch("/transactions/someId")
                .contentType("application/json-patch+json")
                .content("[\n" +
                        "  { \"op\": \"replace\", \"path\": \"/baz\", \"value\": \"boo\" },\n" +
                        "  { \"op\": \"add\", \"path\": \"/hello\", \"value\": [\"world\"] },\n" +
                        "  { \"op\": \"remove\", \"path\": \"/foo\" }\n" +
                        "]")
                .with(jwt(builder -> builder.claim("scope", new String("writer")))))
                .andExpect(status().isOk()).andReturn();

        Mockito.verify(service, Mockito.times(1)).patchTransaction(any(), any());
    }

    @DisplayName("Update Transaction - Nominal Case")
    @Test
    void updateTransaction() throws Exception {

        Transaction expectedTransaction = new TransactionBuilder()
                .addTransactions()
                    .addTransaction()
                        .withCategory().withId(1).withCategory("aCategory").withLabel("aLabel").withType(EXTRA).done()
                        .withBankAccount().withCategory("aCategory").withId(1).withLabel("aLabel").done()
                        .withIncome(0f).withOutcome(123.5f).done()
                        .done()
                .withId("someId")
                .withDate(OffsetDateTime.now())
                .build();

        when(service.saveTransaction(Mockito.anyString(), Mockito.any())).thenReturn(expectedTransaction);

        MvcResult result = mockMvc.perform(put("/transactions/someId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(expectedTransaction))
                .with(jwt(builder -> builder.claim("scope", new String("writer")))))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        Assertions.assertThat(objectMapper.writeValueAsString(expectedTransaction)).isEqualToIgnoringCase(response);
    }
}