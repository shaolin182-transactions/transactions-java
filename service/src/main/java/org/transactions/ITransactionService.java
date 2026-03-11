package org.transactions;

import org.transactions.model.Transaction;

import java.util.List;

/**
 * Expose methods for interacting with our service layer
 * Each clients should implements this interface
 */
public interface ITransactionService {

    List<Transaction> getAllTransactions();

    Transaction getTransaction(String id);

}
