package org.transactions;

import org.model.transactions.Transaction;

import java.util.List;

/**
 * Expose methods for interacting with our service layer
 * Each clients should implements this interface
 */
public interface ITransactionService {

    List<Transaction> getAllTransactions();

    Transaction getTransaction(String id);

    Transaction saveTransaction(String id, Transaction transaction);

    Transaction createTransaction(Transaction transaction);

    Transaction deleteTransaction(String id);

}
