package org.model.constraint;

import org.model.transactions.TransactionDetails;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Check that a transaction detail cannot have both fields 'income' and 'outcome' set
 */
public class IncomeOutComeValidator implements ConstraintValidator<ValidIncomeOutcome, TransactionDetails> {
    @Override
    public void initialize(ValidIncomeOutcome constraintAnnotation) {
        // No initialization needed for that validator
    }

    @Override
    public boolean isValid(TransactionDetails transaction, ConstraintValidatorContext constraintValidatorContext) {

        if ((transaction.getIncome() == null && transaction.getOutcome() == null)
            || (transaction.getIncome() != null && transaction.getOutcome() != null && transaction.getIncome() != 0 && transaction.getOutcome() != 0)) {
            // Both properties are not set
            return false;
        }

        return true;
    }

}
