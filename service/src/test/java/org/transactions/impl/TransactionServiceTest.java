package org.transactions.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.transactions.connector.ITransactionDataSource;
import org.transactions.exception.TransactionNotFoundException;
import org.model.transactions.Transaction;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Test
    @DisplayName("GET Transactions - Nominal case")
    void getAllTransactions() {
    }

    @Test
    @DisplayName("GET Transaction - Nominal Case")
    void getTransaction(@Mock ITransactionDataSource dataSource) {
        // Prepare test
        Transaction transaction = new Transaction().id("IdForTest");
        Optional<Transaction> opt = Optional.of(transaction);

        when(dataSource.getTransaction(anyString())).thenReturn(opt);

        // Run Tests
        Transaction result = new TransactionService(dataSource).getTransaction("IdForTest");

        // Assertions
        assertEquals(transaction, result, "Wrong transaction retrieved");
    }

    @Test
    @DisplayName("GET Transaction - Throw Exception")
    void getTransactionException(@Mock ITransactionDataSource dataSource) {
        // Prepare tests
        when(dataSource.getTransaction(anyString())).thenReturn(Optional.empty());

        // Assertions & Run tests
        assertThrows(TransactionNotFoundException.class, () -> new TransactionService(dataSource).getTransaction("idOfaTransaction"));
    }
}