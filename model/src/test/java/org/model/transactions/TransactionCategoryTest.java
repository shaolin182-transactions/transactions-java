package org.model.transactions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.model.transactions.TransactionCategory.TransactionCategoryBuilder;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import java.util.stream.Stream;

import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.junit.jupiter.api.Assertions.*;
import static org.model.transactions.TransactionCategoryType.COURANTE;
import static org.model.transactions.TransactionCategoryType.EXTRA;
import static org.model.transactions.TransactionCategoryType.FIXE;

class TransactionCategoryTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @ParameterizedTest(name="Validation test on category - run #{index} with [{arguments}]")
    @MethodSource("getCategoryDataSet")
    void validTransactionCategory(String category, Integer id, String label, TransactionCategoryType type, Integer expectedError) {
        TransactionCategory transactionCategory = new TransactionCategoryBuilder()
                .withLabel(label)
                .withCategory(category)
                .withId(id)
                .withType(type)
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
                Arguments.of(null, null, null, null, 1),
                Arguments.of(null, 1, null, null, 0),
                Arguments.of("A category type", 1, "A category Label", EXTRA, 0),
                Arguments.of("A category type with accent ad number é1 &", 2, "A category Label with accent ad number à9", EXTRA, 0),
                Arguments.of("A category type", 1000, "A category Label", EXTRA, 1),
                Arguments.of(random(100, true, true), 100, "A category Label", FIXE, 1),
                Arguments.of("A category type", 100, random(100, true, true), COURANTE, 1)
        );
    }

    @Test
    void testEqualsCategoryObject() {
        TransactionCategory cat1 = new TransactionCategoryBuilder()
                .withCategory("someCat").withLabel("label").withId(1)
                .build();

        TransactionCategory  cat2 = new TransactionCategoryBuilder()
                .withCategory("someCat").withLabel("label").withId(1)
                .build();

        TransactionCategory  cat3 = new TransactionCategoryBuilder()
                .withCategory("someDifferentCat").withLabel("label").withId(1)
                .build();

        Assertions.assertEquals(cat1, cat2, "Category with same properties values should be equals");
        Assertions.assertEquals(cat1, cat1, "Same object should be equals");
        Assertions.assertNotEquals(cat1, cat3, "Category with different properties values should be equals");
    }

}