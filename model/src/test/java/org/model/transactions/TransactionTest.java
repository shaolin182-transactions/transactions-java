package org.model.transactions;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.model.transactions.builder.TransactionBuilder;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void validNominalCase() {
        Transaction transaction = new TransactionBuilder()
                .addTransactions()
                    .addTransaction()
                        .withCategory().withId(1).withCategory("aCategory").withLabel("aLabel").done()
                        .withIncome(0f).withOutcome(123.5f).done()
                    .done()
                    .withBankAccountFrom().withCategory("aCategory").withId(1).withLabel("aLabel").done()
                .build();

        Set<ConstraintViolation<Transaction>> constraintViolations = validator.validate(transaction);

        assertEquals(0, constraintViolations.size(), "A validation error occurs");
    }

    @Test
    public void validMissingBankAccount() {
        Transaction transaction = new TransactionBuilder()
                .addTransactions()
                    .addTransaction()
                        .withCategory().withId(1).withCategory("aCategory").withLabel("aLabel").done()
                        .withIncome(0f).withOutcome(123.5f).done()
                    .done()
                .build();

        Set<ConstraintViolation<Transaction>> constraintViolations = validator.validate(transaction);

        assertEquals(1, constraintViolations.size(), "Bank Account cannot be empty");
    }

    @Test
    public void validEmptyTransactionList() {
        Transaction transaction = new TransactionBuilder()
                .withBankAccountFrom().withCategory("aCategory").withId(1).withLabel("aLabel").done()
                .build();

        Set<ConstraintViolation<Transaction>> constraintViolations = validator.validate(transaction);

        assertEquals(1, constraintViolations.size(), "Bank Account cannot be empty");
    }



}