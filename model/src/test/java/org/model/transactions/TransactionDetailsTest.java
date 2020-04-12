package org.model.transactions;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TransactionDetailsTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void IncomeOutcomeBothSet() {
        TransactionDetails transaction = new TransactionDetails.TransactionDetailsBuilder()
                .withOutcome(10f)
                .withIncome(10f)
                .build();

        Set<ConstraintViolation<TransactionDetails>> constraintViolations = validator.validate(transaction);

        assertEquals(1, constraintViolations.size(), "Error on validating transaction detail");
        assertEquals("One of the properties 'income' and 'outcome' must be set", constraintViolations.iterator().next().getMessage(), "Wrong error message");
    }

    @Test
    public void IncomeOutcomeNoneSet() {
        TransactionDetails transaction = new TransactionDetails.TransactionDetailsBuilder().build();

        Set<ConstraintViolation<TransactionDetails>> constraintViolations = validator.validate(transaction);

        assertEquals(1, constraintViolations.size(), "Error on validating transaction detail");
        assertEquals("One of the properties 'income' and 'outcome' must be set", constraintViolations.iterator().next().getMessage(), "Wrong error message");
    }

    @Test
    public void IncomeNegative() {
        TransactionDetails transaction = new TransactionDetails.TransactionDetailsBuilder()
                .withIncome(-10f)
                .build();

        Set<ConstraintViolation<TransactionDetails>> constraintViolations = validator.validate(transaction);

        assertEquals(1, constraintViolations.size(), "Error on validating transaction detail");
        assertNotNull(constraintViolations.iterator().next().getMessage(), "Wrong error message");
    }

    @Test
    public void OutcomeNegative() {
        TransactionDetails transaction = new TransactionDetails.TransactionDetailsBuilder()
                .withOutcome(-10f)
                .build();

        Set<ConstraintViolation<TransactionDetails>> constraintViolations = validator.validate(transaction);

        assertEquals(1, constraintViolations.size(), "Error on validating transaction detail");
        assertNotNull(constraintViolations.iterator().next().getMessage(), "Wrong error message");
    }

    @Test
    public void validNominalCase() {
        TransactionDetails transaction = new TransactionDetails.TransactionDetailsBuilder()
                .withDescription("A classic ' description, with some special characters like this @.")
                .withOutcome(10f)
                .withIncome(0f)
                .build();

        Set<ConstraintViolation<TransactionDetails>> constraintViolations = validator.validate(transaction);

        assertEquals(0, constraintViolations.size(), "A validation error occurs");
    }
}