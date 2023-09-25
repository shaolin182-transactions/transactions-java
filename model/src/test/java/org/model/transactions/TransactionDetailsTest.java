package org.model.transactions;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Tag("UnitTest")
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

    void testEqualsMethod() {
        TransactionDetails obj1 = new TransactionDetails.TransactionDetailsBuilder()
                .withIncome(0f).withOutcome(45.2f).withDescription("somedesc")
                .withBankAccount().withCategory("cat").withId(1).withLabel("label").done()
                .withCategory().withCategory("cat").withId(1).withLabel("label").done()
                .build();

        TransactionDetails obj2 = new TransactionDetails.TransactionDetailsBuilder()
                .withIncome(0f).withOutcome(45.2f).withDescription("somedesc")
                .withBankAccount().withCategory("cat").withId(1).withLabel("label").done()
                .withCategory().withCategory("cat").withId(1).withLabel("label").done()
                .build();

        TransactionDetails obj3 = new TransactionDetails.TransactionDetailsBuilder()
                .withIncome(0f).withOutcome(45.2f).withDescription("someOtherdesc")
                .withBankAccount().withCategory("cat").withId(1).withLabel("label").done()
                .withCategory().withCategory("cat").withId(1).withLabel("label").done()
                .build();

        Assertions.assertEquals(obj1, obj2, "Category with same properties values should be equals");
        Assertions.assertEquals(obj1, obj1, "Same object should be equals");
        Assertions.assertNotEquals(obj1, obj3, "Category with different properties values should be equals");
    }
}