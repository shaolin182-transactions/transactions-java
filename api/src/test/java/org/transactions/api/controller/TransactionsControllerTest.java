package org.transactions.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.model.error.Error;
import org.model.transactions.builder.TransactionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.transactions.ITransactionService;
import org.transactions.connector.ITransactionDataSource;
import org.transactions.exception.TransactionNotFoundException;
import org.model.transactions.Transaction;
import org.transactions.persistence.repositories.TransactionsRepository;

import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("Get All Transactions - Nominal Case")
    @Test
    void getAll() throws Exception {

        when(service.getAllTransactions()).thenReturn(new ArrayList<Transaction>());

        mockMvc.perform(get("/transactions")
                .contentType("application/json"))
                .andExpect(status().isOk());

    }

    @DisplayName("Get Transaction - Check Error Handling")
    @Test
    void getTransactionException() throws Exception {

        when(service.getTransaction(Mockito.anyString())).thenThrow(TransactionNotFoundException.class);

        MvcResult result = mockMvc.perform(get("/transactions/anyId"))
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
                        .withIncome(12f).withOutcome(0f).done()
                .done()
                .withCost(12l)
                .withBankAccountFrom().withCategory("cat").withId(2).withLabel("label").done()
                .build();

        when(service.getTransaction(Mockito.anyString())).thenReturn(expectedTransaction);

        MvcResult result = mockMvc.perform(get("/transactions/anyId"))
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
                        .withCategory().withId(1).withCategory("aCategory").withLabel("aLabel").done()
                        .withIncome(0f).withOutcome(123.5f).done()
                .done()
                .withBankAccountFrom().withCategory("aCategory").withId(1).withLabel("aLabel").done()
                .build();

        when(service.createTransaction(Mockito.any())).thenReturn(expectedTransaction);

        MvcResult result = mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(expectedTransaction)))
                .andExpect(status().isCreated())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        Assertions.assertThat(objectMapper.writeValueAsString(expectedTransaction)).isEqualToIgnoringCase(response);
    }

    @DisplayName("Create Transaction - JSON with incorrect data")
    @Test
    public void createTransactionWrongJSON() throws Exception {
        String transaction = "{" +
                "\"fromm\": {\"id\": 1, \"category\": \"aCategory\", \"label\": \"aLabel\"}," +
                "\"transactions\": [" +
                "{" +
                "\"income\": 0, \"outcome\": 123.5, \"description\": \"Some description\"," +
                "\"category\":  {\"id\": 1, \"category\": \"aCategory\", \"label\": \"aLabel\"}" +
                "}" +
                "]" +
                "}";

        MvcResult result = mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(transaction))
                .andExpect(status().isBadRequest())
                .andReturn();

    }
}