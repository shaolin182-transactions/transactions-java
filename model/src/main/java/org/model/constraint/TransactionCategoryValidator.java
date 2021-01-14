package org.model.constraint;

import org.model.transactions.TransactionCategory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Check that a category can contains only an id
 * But if other fields are specified, then all fields must be set
 */
public class TransactionCategoryValidator implements ConstraintValidator<ValidTransactionCategory, TransactionCategory> {
    @Override
    public void initialize(ValidTransactionCategory constraintAnnotation) {
        // No initialization needed for that validator
    }

    @Override
    public boolean isValid(TransactionCategory transactionCategory, ConstraintValidatorContext constraintValidatorContext) {
        int nbFieldSet = transactionCategory.getLabel() != null ? 1 : 0;
        nbFieldSet += transactionCategory.getCategory() != null ? 1 : 0;
        nbFieldSet += transactionCategory.getType() != null ? 1 : 0;

        return nbFieldSet <= 0 || nbFieldSet >= 3;
    }
}
