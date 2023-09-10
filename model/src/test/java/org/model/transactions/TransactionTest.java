package org.model.transactions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.model.transactions.builder.TransactionBuilder;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @ParameterizedTest(name="Validation test on transaction - run #{index} with [{arguments}]")
    @MethodSource(value = "getTransactionDataSet")
    void validNominalCase(List<TransactionDetails> transactions, Integer expectedError) {
        Transaction transaction = new TransactionBuilder()
                .addTransactions(transactions)
                .withDate(OffsetDateTime.now())
                .build();

        Set<ConstraintViolation<Transaction>> constraintViolations = validator.validate(transaction);

        assertEquals(expectedError, constraintViolations.size(), "Error when validating transaction object");
    }

    /**
     * Build data for validation unit tests
     * @return a stream of test data set
     */
    private static Stream<Arguments> getTransactionDataSet() {
        BankAccount aBankAccount = new BankAccount.BankAccountBuilder()
                .withCategory("aCategory")
                .withLabel("aLabel")
                .withId(1)
                .build();

        TransactionDetails aValidTransaction = new TransactionDetails.TransactionDetailsBuilder()
                .withOutcome(123.5f).withIncome(0f)
                .withBankAccount(aBankAccount)
                .withDescription("Some description @ï89")
                .build();
        TransactionDetails aInvalidTransaction = new TransactionDetails.TransactionDetailsBuilder()
                .withOutcome(123.5f).withIncome(10f)
                .withBankAccount(aBankAccount)
                .build();

        List<TransactionDetails> validTransactionList = Stream.of(aValidTransaction).collect(Collectors.toList());
        List<TransactionDetails> inValidTransactionList = Stream.of(aValidTransaction, aInvalidTransaction).collect(Collectors.toList());

        return Stream.of(
                Arguments.of(validTransactionList, 0),
                Arguments.of(Collections.EMPTY_LIST, 1),
                Arguments.of(inValidTransactionList, 1)
        );
    }

    void testEqualsMethod() {
        Transaction obj1 = new TransactionBuilder()
                .addTransactions().addTransaction().withIncome(12f).withOutcome(0f).done().done()
                .build();

        Transaction obj2 = new TransactionBuilder()
                .addTransactions().addTransaction().withIncome(12f).withOutcome(0f).done().done()
                .build();

        Transaction obj3 = new TransactionBuilder()
                .addTransactions().addTransaction().withIncome(12f).withOutcome(0f).done().done()
                .withDate(OffsetDateTime.now())
                .build();

        Assertions.assertEquals(obj1, obj2, "Category with same properties values should be equals");
        Assertions.assertEquals(obj1, obj1, "Same object should be equals");
        Assertions.assertNotEquals(obj1, obj3, "Category with different properties values should be equals");
    }
}