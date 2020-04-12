package org.model.transactions;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.junit.jupiter.api.Assertions.*;

class BankAccountTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void validNominalCase() {
        BankAccount bankAccount = new BankAccount.BankAccountBuilder()
                .withCategory("A bank Account Category")
                .withId(1)
                .withLabel("A bank Account Label")
                .build();

        Set<ConstraintViolation<BankAccount>> constraintViolations = validator.validate(bankAccount);

        assertEquals(0, constraintViolations.size(), "A validation error occurs");
    }

    @Test
    public void validErrorOnId() {
        BankAccount bankAccount = new BankAccount.BankAccountBuilder()
                .withCategory(random(50, true, true))
                .withId(1000)
                .withLabel(random(50, true, true))
                .build();

        Set<ConstraintViolation<BankAccount>> constraintViolations = validator.validate(bankAccount);

        assertEquals(1, constraintViolations.size(), "Error on validate object");
    }

    @Test
    public void validErrorOnCategory() {
        BankAccount bankAccount = new BankAccount.BankAccountBuilder()
                .withLabel(random(50, true, true))
                .withId(100)
                .withCategory(random(100, true, true))
                .build();

        Set<ConstraintViolation<BankAccount>> constraintViolations = validator.validate(bankAccount);

        assertEquals(1, constraintViolations.size(), "Error on validate object");
    }

    @Test
    public void validErrorOnLabel() {
        BankAccount bankAccount = new BankAccount.BankAccountBuilder()
                .withLabel(random(100, true, true))
                .withId(100)
                .withCategory(random(50, true, true))
                .build();

        Set<ConstraintViolation<BankAccount>> constraintViolations = validator.validate(bankAccount);

        assertEquals(1, constraintViolations.size(), "Error on validate object");
    }
}