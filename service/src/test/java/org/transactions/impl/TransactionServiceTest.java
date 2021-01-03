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

    @Mock
    private ITransactionDataSource transactionDataSource;

    @Mock
    private ICommonDataDatasource commonDataDatasource;

    @AfterEach
    private void reset() {
        Mockito.reset(transactionDataSource, commonDataDatasource);
    }

    @Test
    @DisplayName("GET All Transactions - Nominal case")
    void getAllTransactions() {
        // Prepare test
        when(transactionDataSource.getAllTransactions()).thenReturn(new ArrayList<Transaction>());

        // Run method to test
        List<Transaction> result = new TransactionService(transactionDataSource, null, commonDataDatasource).getAllTransactions();

        // Assertions
        verify(transactionDataSource, times(1)).getAllTransactions();
    }

    @Test
    @DisplayName("GET Transaction - Nominal Case")
    void getTransaction() {
        // Prepare test
        Transaction transaction = new Transaction().id("IdForTest");
        Optional<Transaction> opt = Optional.of(transaction);

        when(transactionDataSource.getTransaction(anyString())).thenReturn(opt);

        // Run Tests
        Transaction result = new TransactionService(transactionDataSource, null, commonDataDatasource).getTransaction("IdForTest");

        // Assertions
        assertEquals(transaction, result, "Wrong transaction retrieved");
        verify(transactionDataSource, times(1)).getTransaction(anyString());
    }

    @Test
    @DisplayName("GET Transaction - Throw Exception")
    void getTransactionException() {
        // Prepare tests
        when(transactionDataSource.getTransaction(anyString())).thenReturn(Optional.empty());

        // Assertions & Run tests
        TransactionService service = new TransactionService(transactionDataSource, null, commonDataDatasource);
        assertThrows(TransactionNotFoundException.class, () -> service.getTransaction("idOfaTransaction"));
    }

    @Test
    @DisplayName("CREATE Transaction - Nominal case")
    void createTransactionTest(){
        // Prepare tests
        Transaction transaction = new TransactionBuilder().addTransactions()
                .addTransaction().withIncome(150f).withOutcome(0f).done()
                .addTransaction().withIncome(13.39f).withOutcome(0f).done()
                .addTransaction().withIncome(0f).withOutcome(145.26f).done()
                .done().build();

        when(transactionDataSource.saveTransactions(transaction)).thenReturn(transaction);

        // Run test
        Transaction result = new TransactionService(transactionDataSource, null, commonDataDatasource).createTransaction(transaction);

        Assertions.assertEquals(1813l, result.getCost(), "Error on cost computation");
        Assertions.assertEquals(1813l, result.getCostAbs(), "Error on cost computation");
        Assertions.assertEquals(TransactionType.INCOME, result.getType(), "Error on cost computation");
    }

    @Test
    @DisplayName("CREATE Transaction - Nominal case - Outcome")
    void createTransactionTestOutcomeType(){
        // Prepare tests
        Transaction transaction = new TransactionBuilder().addTransactions()
                .addTransaction().withIncome(0f).withOutcome(145.26f)
                    .withBankAccount().withCategory("cat").withLabel("label").withId(1).done()
                    .withCategory().withType(EXTRA).withCategory("cat").withId(2).withLabel("label").done()
                    .done()
                .done().build();

        when(transactionDataSource.saveTransactions(transaction)).thenReturn(transaction);

        // Run test
        Transaction result = new TransactionService(transactionDataSource, null, commonDataDatasource).createTransaction(transaction);

        Assertions.assertEquals(-14526l, result.getCost(), "Error on cost computation");
        Assertions.assertEquals(14526l, result.getCostAbs(), "Error on cost computation");
        Assertions.assertEquals(TransactionType.OUTCOME, result.getType(), "Error on cost computation");
    }

    @Test
    @DisplayName("CREATE Transaction - Nominal case - Only id provided")
    void createTransactionOnlyId(){
        // Prepare tests
        Transaction transaction = new TransactionBuilder().addTransactions()
                .addTransaction().withIncome(0f).withOutcome(145.26f)
                .withBankAccount().withId(1).done()
                .withCategory().withId(2).done()
                .done()
                .done().build();

        when(transactionDataSource.saveTransactions(transaction)).thenReturn(transaction);
        when(commonDataDatasource.findCategoryById(2)).thenReturn(Optional.of(new TransactionCategory.TransactionCategoryBuilder().withId(2).withCategory("cat").withLabel("label").withType(FIXE).build()));
        when(commonDataDatasource.findBankAccountById(1)).thenReturn(Optional.of(new BankAccount.BankAccountBuilder().withId(1).withCategory("cat").withLabel("label").build()));

        // Run test
        Transaction result = new TransactionService(transactionDataSource, , null, commonDataDatasource).createTransaction(transaction);

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, result.getTransactions().get(0).getBankAccount().getId(), "Error on bank account data")  ,
                () -> Assertions.assertEquals("cat", result.getTransactions().get(0).getBankAccount().getCategory(), "Error on bank account data"),
                () -> Assertions.assertEquals("label", result.getTransactions().get(0).getBankAccount().getLabel(), "Error on bank account data"),
                () -> Assertions.assertEquals(2, result.getTransactions().get(0).getCategory().getId(), "Error on category data")  ,
                () -> Assertions.assertEquals("cat", result.getTransactions().get(0).getCategory().getCategory(), "Error on category data"),
                () -> Assertions.assertEquals("label", result.getTransactions().get(0).getCategory().getLabel(), "Error on category data"),
                () -> Assertions.assertEquals(FIXE, result.getTransactions().get(0).getCategory().getType(), "Error on category data")
        );

    }

    @Test
    @DisplayName("DELETE Transaction - Nominal Case")
    void deleteTransaction() {

        // Run tests
        new TransactionService(transactionDataSource, null, commonDataDatasource).deleteTransaction("someId");

        // Assertions
        verify(transactionDataSource, times(1)).deleteTransactions("someId");
    }
}