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

import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.junit.jupiter.api.Assertions.*;

class TransactionCategoryTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @ParameterizedTest(name="Validation test on category - run #{index} with [{arguments}]")
    @MethodSource("getCategoryDataSet")
    public void validTransactionCategory(String category, Integer id, String label, Integer expectedError) {
        TransactionCategory transactionCategory = new TransactionCategory.TransactionCategoryBuilder()
                .withLabel(label)
                .withCategory(category)
                .withId(id)
                .build();

        Set<ConstraintViolation<TransactionCategory>> constraintViolations = validator.validate(transactionCategory);

        assertEquals(expectedError, constraintViolations.size(), "Error when validating TransactionCategory object");
    }

    /**
     * Build data for validation unit tests
     * @return a stream of test data set
     */
    private static Stream<Arguments> getCategoryDataSet() {
        return Stream.of(
                Arguments.of("A category type", 1, "A category Label", 0),
                Arguments.of("A category type", 1000, "A category Label", 1),
                Arguments.of(random(100, true, true), 100, "A category Label", 1),
                Arguments.of("A category type", 100, random(100, true, true), 1)
        );
    }

}