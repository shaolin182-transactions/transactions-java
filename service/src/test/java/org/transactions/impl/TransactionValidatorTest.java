package org.transactions.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.model.transactions.BankAccount;
import org.model.transactions.Transaction;
import org.model.transactions.TransactionCategory;
import org.model.transactions.TransactionCategoryType;
import org.model.transactions.builder.TransactionBuilder;
import org.transactions.ITransactionValidator;
import org.transactions.connector.ICommonDataDatasource;
import org.transactions.exception.TransactionBadDataException;

import java.time.OffsetDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@Tag("UnitTest")
class TransactionValidatorTest {

    @Mock
    ICommonDataDatasource commonDataDatasource;

    @BeforeEach
    private void reset() {
        Mockito.reset(commonDataDatasource);
    }

    @Test
    void noError() {
        Transaction transaction = new TransactionBuilder()
                .addTransactions()
                .addTransaction()
                .withCategory().withId(1).withCategory("CatZ").withLabel("LabZ").withType(TransactionCategoryType.EXTRA).done()
                .withBankAccount().withId(2).withCategory("BKZ").withLabel("LabZ").done()
                .withIncome(100f).withDescription("someDesc")
                .done()
                .done().withDate(OffsetDateTime.now()).build();

        TransactionCategory category = new TransactionCategory.TransactionCategoryBuilder().withId(1).withCategory("CatZ").withLabel("LabZ").withType(TransactionCategoryType.EXTRA).build();
        BankAccount bk = new BankAccount.BankAccountBuilder().withId(2).withCategory("BKZ").withLabel("LabZ").build();
        Mockito.when(commonDataDatasource.findBankAccountById(2)).thenReturn(Optional.of(bk));
        Mockito.when(commonDataDatasource.findCategoryById(1)).thenReturn(Optional.of(category));

        ITransactionValidator validator = new TransactionValidator(commonDataDatasource);
        validator.validate(transaction);

        Assertions.assertTrue(true, "No error should occurs");
    }

    @Test
    void errorOnTransaction() {
        Transaction transaction = new TransactionBuilder().build();

        ITransactionValidator validator = new TransactionValidator(commonDataDatasource);
        Assertions.assertThrows(TransactionBadDataException.class, () -> validator.validate(transaction), "A validation error should occurs as category and bank account does not exist in datasource");
    }

    @Test
    void validateUnknownData() {
        Transaction transaction = new TransactionBuilder()
                .addTransactions()
                    .addTransaction()
                        .withCategory().withId(1).withCategory("CatZ").withLabel("LabZ").withType(TransactionCategoryType.EXTRA).done()
                        .withBankAccount().withId(2).withCategory("BKZ").withLabel("LabZ").done()
                        .withIncome(100f).withDescription("someDesc")
                        .done()
                    .done().withDate(OffsetDateTime.now()).build();


        ITransactionValidator validator = new TransactionValidator(commonDataDatasource);
        validator.validate(transaction);

        Assertions.assertTrue(true, "No error should occurs if data is unknown");
    }

    @Test
    void validateIncorrectDataOnBK() {
        Transaction transaction = new TransactionBuilder()
                .addTransactions()
                .addTransaction()
                .withCategory().withId(1).withCategory("CatZ").withLabel("LabZ").withType(TransactionCategoryType.EXTRA).done()
                .withBankAccount().withId(2).withCategory("BKZ").withLabel("LabZ").done()
                .withIncome(100f).withDescription("someDesc")
                .done()
                .done().withDate(OffsetDateTime.now()).build();


        BankAccount bk = new BankAccount.BankAccountBuilder().withId(2).withCategory("BK").withLabel("Lab").build();
        Mockito.when(commonDataDatasource.findBankAccountById(2)).thenReturn(Optional.of(bk));

        ITransactionValidator validator = new TransactionValidator(commonDataDatasource);

        Assertions.assertThrows(TransactionBadDataException.class, () -> validator.validate(transaction), "A validation error should occurs as category and bank account does not exist in datasource");
    }

    @Test
    void validateIncorrectDataOnCategory() {
        Transaction transaction = new TransactionBuilder()
                .addTransactions()
                .addTransaction()
                .withCategory().withId(1).withCategory("CatZ").withLabel("LabZ").withType(TransactionCategoryType.EXTRA).done()
                .withBankAccount().withId(2).withCategory("BKZ").withLabel("LabZ").done()
                .withIncome(100f).withDescription("someDesc")
                .done()
                .done().withDate(OffsetDateTime.now()).build();


        TransactionCategory category = new TransactionCategory.TransactionCategoryBuilder().withId(1).withCategory("CatZ").withLabel("Lab").withType(TransactionCategoryType.EXTRA).build();
        BankAccount bk = new BankAccount.BankAccountBuilder().withId(2).withCategory("BKZ").withLabel("LabZ").build();
        Mockito.when(commonDataDatasource.findBankAccountById(2)).thenReturn(Optional.of(bk));
        Mockito.when(commonDataDatasource.findCategoryById(1)).thenReturn(Optional.of(category));

        ITransactionValidator validator = new TransactionValidator(commonDataDatasource);

        Assertions.assertThrows(TransactionBadDataException.class, () -> validator.validate(transaction), "A validation error should occurs as category and bank account does not exist in datasource");
    }
}