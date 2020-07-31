package org.model.transactions;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class TransactionDetailsTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @ParameterizedTest(name = "Validation test on transaction detail - run #{index} with [{arguments}]")
    @MethodSource(value = "getTransactionDetailDataSet")
    void validNominalCase(String description, Float outcome, Float income, BankAccount from, Integer expectedError) {
        TransactionDetails transaction = new TransactionDetails.TransactionDetailsBuilder()
                .withBankAccount(from)
                .withDescription(description)
                .withOutcome(outcome)
                .withIncome(income)
                .build();

        Set<ConstraintViolation<TransactionDetails>> constraintViolations = validator.validate(transaction);

        assertEquals(expectedError, constraintViolations.size(), "A validation error occurs");
    }

    /**
     * Build data for validation unit tests
     * @return a stream of test data set
     */
    private static Stream<Arguments> getTransactionDetailDataSet() {
        BankAccount account = new BankAccount.BankAccountBuilder()
                .withCategory("cat").withLabel("label").withId(1).build();

        return Stream.of(
                Arguments.of("A classic ' description, with some special characters like this @.", 10f, 0f, account, 0),
                Arguments.of(null, -10f, null, account, 1),
                Arguments.of(null, null, -10f, account, 1),
                Arguments.of(null, null, null, account, 1),
                Arguments.of(null, 10f, 10f, account, 1)
        );
    }
}