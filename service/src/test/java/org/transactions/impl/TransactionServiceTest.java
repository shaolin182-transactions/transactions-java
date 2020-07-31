package org.transactions.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.model.transactions.TransactionType;
import org.model.transactions.builder.TransactionBuilder;
import org.transactions.connector.ITransactionDataSource;
import org.transactions.exception.TransactionNotFoundException;
import org.model.transactions.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Test
    @DisplayName("GET All Transactions - Nominal case")
    void getAllTransactions(@Mock ITransactionDataSource dataSource) {
        // Prepare test
        when(dataSource.getAllTransactions()).thenReturn(new ArrayList<Transaction>());

        // Run method to test
        List<Transaction> result = new TransactionService(dataSource).getAllTransactions();

        // Assertions
        verify(dataSource, times(1)).getAllTransactions();
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
        verify(dataSource, times(1)).getTransaction(anyString());
    }

    @Test
    @DisplayName("GET Transaction - Throw Exception")
    void getTransactionException(@Mock ITransactionDataSource dataSource) {
        // Prepare tests
        when(dataSource.getTransaction(anyString())).thenReturn(Optional.empty());

        // Assertions & Run tests
        assertThrows(TransactionNotFoundException.class, () -> new TransactionService(dataSource).getTransaction("idOfaTransaction"));
    }

    @Test
    @DisplayName("CREATE Transaction - Nominal case")
    void createTransactionTest(@Mock ITransactionDataSource dataSource){
        // Prepare tests
        Transaction transaction = new TransactionBuilder().addTransactions()
                .addTransaction().withIncome(150f).withOutcome(0f).done()
                .addTransaction().withIncome(13.39f).withOutcome(0f).done()
                .addTransaction().withIncome(0f).withOutcome(145.26f).done()
                .done().build();

        when(dataSource.saveTransactions(transaction)).thenReturn(transaction);

        // Run test
        Transaction result = new TransactionService(dataSource).createTransaction(transaction);

        Assertions.assertEquals(1813l, result.getCost(), "Error on cost computation");
        Assertions.assertEquals(1813l, result.getCostAbs(), "Error on cost computation");
        Assertions.assertEquals(TransactionType.INCOME, result.getType(), "Error on cost computation");
    }

    @Test
    @DisplayName("CREATE Transaction - Nominal case - Outcome")
    void createTransactionTestOutcomeType(@Mock ITransactionDataSource dataSource){
        // Prepare tests
        Transaction transaction = new TransactionBuilder().addTransactions()
                .addTransaction().withIncome(0f).withOutcome(145.26f).done()
                .done().build();

        when(dataSource.saveTransactions(transaction)).thenReturn(transaction);

        // Run test
        Transaction result = new TransactionService(dataSource).createTransaction(transaction);

        Assertions.assertEquals(-14526l, result.getCost(), "Error on cost computation");
        Assertions.assertEquals(14526l, result.getCostAbs(), "Error on cost computation");
        Assertions.assertEquals(TransactionType.OUTCOME, result.getType(), "Error on cost computation");
    }

    @Test
    @DisplayName("DELETE Transaction - Nominal Case")
    void deleteTransaction(@Mock ITransactionDataSource dataSource) {

        // Run tests
        new TransactionService(dataSource).deleteTransaction("someId");

        // Assertions
        verify(dataSource, times(1)).deleteTransactions("someId");
    }
}