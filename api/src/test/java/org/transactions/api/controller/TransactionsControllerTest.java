package org.transactions.api.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.transactions.ITransactionService;
import org.transactions.model.Transaction;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TransactionsControllerTest {

    @InjectMocks
    TransactionsController controller;

    @Mock
    ITransactionService service;

    @Test
    void getAll() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        Mockito.when(service.getAllTransactions()).thenReturn(new ArrayList<Transaction>());

        ResponseEntity<List<Transaction>> result = controller.getAll();

        assertEquals(0, result.getBody().size());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void getTransaction() {
    }
}