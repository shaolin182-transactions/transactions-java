package org.model.transactions;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.model.transactions.builder.TransactionBuilder;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
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
    public void validNominalCase(List<TransactionDetails> transactions, BankAccount bankAccountFrom, Integer expectedError) {
        Transaction transaction = new TransactionBuilder()
                .addTransactions(transactions)
                .withBankAccountFrom(bankAccountFrom)
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
                .withOutcome(123.5f).withIncome(0f).build();
        TransactionDetails aInvalidTransaction = new TransactionDetails.TransactionDetailsBuilder()
                .withOutcome(123.5f).withIncome(10f).build();

        List<TransactionDetails> validTransactionList = Stream.of(aValidTransaction).collect(Collectors.toList());
        List<TransactionDetails> inValidTransactionList = Stream.of(aValidTransaction, aInvalidTransaction).collect(Collectors.toList());

        return Stream.of(
                Arguments.of(validTransactionList, aBankAccount, 0),
                Arguments.of(validTransactionList, null, 1),
                Arguments.of(Collections.EMPTY_LIST, aBankAccount, 1),
                Arguments.of(inValidTransactionList, aBankAccount, 1)
        );
    }
}