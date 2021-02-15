package org.transactions.impl;

import org.model.transactions.Transaction;
import org.model.transactions.TransactionDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.transactions.ITransactionValidator;
import org.transactions.connector.ICommonDataDatasource;
import org.transactions.exception.TransactionBadDataException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

@Component
public class TransactionValidator implements ITransactionValidator {

    private final ICommonDataDatasource commonDatasource;

    @Autowired
    public TransactionValidator(ICommonDataDatasource commonDataDatasource){
        this.commonDatasource = commonDataDatasource;
    }

    public void validate(Transaction transaction) {

        // Validate java annotation on each fields
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Transaction>> errors =  validator.validate(transaction);

        if (!CollectionUtils.isEmpty(errors)){
            throw new TransactionBadDataException();
        }

        // Check that bank account data and category data match those in database
        transaction.getTransactions().forEach(this::validSubTransaction);
    }

    /**
     * Validate that bank account and category data are equals to those that already exists un database
     * @param transactionDetails : current transaction
     */
    private void validSubTransaction(TransactionDetails transactionDetails) {
        if (transactionDetails.getBankAccount() != null && transactionDetails.getBankAccount().getCategory() != null){
            commonDatasource.findBankAccountById(transactionDetails.getBankAccount().getId())
                    .ifPresent(data -> checkEqualityObject(data, transactionDetails.getBankAccount()));
        }

        if (transactionDetails.getCategory() != null && transactionDetails.getCategory().getCategory() != null){
            commonDatasource.findCategoryById(transactionDetails.getCategory().getId())
                    .ifPresent(data -> checkEqualityObject(data, transactionDetails.getCategory()));
        }
    }

    /**
     * Thrown an exception if two object are not equals
     * @param o1
     * @param o2
     */
    private void checkEqualityObject(Object o1, Object o2) {
        if (!o1.equals(o2)) {
            throw new TransactionBadDataException();
        }
    }
}
