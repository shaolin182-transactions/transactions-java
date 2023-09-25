package org.model.constraint;

import org.model.transactions.BankAccount;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BankAccountValidator implements ConstraintValidator<ValidBankAccount, BankAccount> {
    @Override
    public void initialize(ValidBankAccount constraintAnnotation) {
        // No initialization needed for that validator
    }

    @Override
    public boolean isValid(BankAccount bankAccount, ConstraintValidatorContext constraintValidatorContext) {
        int nbFieldSet = bankAccount.getLabel() != null ? 1 : 0;
        nbFieldSet += bankAccount.getCategory() != null ? 1 : 0;

        return nbFieldSet <= 0 || nbFieldSet >= 2;
    }
}
