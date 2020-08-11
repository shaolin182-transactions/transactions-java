package org.model.transactions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.model.transactions.BankAccount.BankAccountBuilder;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;
import java.util.stream.Stream;

import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.junit.jupiter.api.Assertions.*;

class BankAccountTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @ParameterizedTest(name="Validation test on bank account - run #{index} with [{arguments}]")
    @MethodSource("getBankAccountDataSet")
    void validBankAccount(String category, Integer id, String label, Integer expectedError) {
        BankAccount bankAccount = new BankAccountBuilder()
                .withLabel(label)
                .withId(id)
                .withCategory(category)
                .build();

        Set<ConstraintViolation<BankAccount>> constraintViolations = validator.validate(bankAccount);

        assertEquals(expectedError, constraintViolations.size(), "Error when validating BankAccount object");
    }

    /**
     * Build data for validation unit tests
     * @return a stream of test data set
     */
    private static Stream<Arguments> getBankAccountDataSet() {
        return Stream.of(
          Arguments.of("A bank Account Category à8", 1, "A bank Account Label ë3", 0),
          Arguments.of("A bank Account Category", 1000, "A bank Account Label", 1),
          Arguments.of(random(100, true, true), 100, "A bank Account Label", 1),
          Arguments.of("A bank Account Category", 100, random(100, true, true), 1)
        );
    }

    @Test
    void testEqualsBankAccountObject() {
        BankAccount bkAcc1 = new BankAccountBuilder()
                .withCategory("someCat").withLabel("label").withId(1)
                .build();

        BankAccount bkAcc2 = new BankAccountBuilder()
                .withCategory("someCat").withLabel("label").withId(1)
                .build();

        BankAccount bkAcc3 = new BankAccountBuilder()
                .withCategory("someCat").withLabel("label").withId(2)
                .build();

        Assertions.assertEquals(bkAcc1, bkAcc2, "Category with same properties values should be equals");
        Assertions.assertEquals(bkAcc1, bkAcc1, "Same object should be equals");
        Assertions.assertNotEquals(bkAcc1, bkAcc3, "Category with different properties values should be equals");
    }
}