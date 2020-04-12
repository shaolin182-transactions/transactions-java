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

class TransactionCategoryTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void validNominalCase() {
        TransactionCategory category = new TransactionCategory.TransactionCategoryBuilder()
                .withCategory("A bank Account Category")
                .withId(1)
                .withLabel("A bank Account Label")
                .build();

        Set<ConstraintViolation<TransactionCategory>> constraintViolations = validator.validate(category);

        assertEquals(0, constraintViolations.size(), "A validation error occurs");
    }

    @Test
    public void validErrorOnId() {
        TransactionCategory category = new TransactionCategory.TransactionCategoryBuilder()
                .withId(513)
                .withCategory(random(50, true, true))
                .withLabel(random(50, true, true))
                .build();

        Set<ConstraintViolation<TransactionCategory>> constraintViolations = validator.validate(category);

        assertEquals(1, constraintViolations.size(), "Error on validate object");
    }

    @Test
    public void validErrorOnCategory() {
        TransactionCategory category = new TransactionCategory.TransactionCategoryBuilder()
                .withCategory(random(100, true, true))
                .withLabel(random(50, true, true))
                .withId(51)
                .build();

        Set<ConstraintViolation<TransactionCategory>> constraintViolations = validator.validate(category);

        assertEquals(1, constraintViolations.size(), "Error on validate object");
    }

    @Test
    public void validErrorOnLabel() {
        TransactionCategory category = new TransactionCategory.TransactionCategoryBuilder()
                .withLabel(random(100, true, true))
                .withCategory(random(50, true, true))
                .withId(51)
                .build();

        Set<ConstraintViolation<TransactionCategory>> constraintViolations = validator.validate(category);

        assertEquals(1, constraintViolations.size(), "Error on validate object");
    }

}