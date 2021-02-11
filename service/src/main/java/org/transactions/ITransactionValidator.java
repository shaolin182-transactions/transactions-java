package org.transactions;

import org.model.transactions.Transaction;

public interface ITransactionValidator {

    void validate(Transaction transaction);
}
