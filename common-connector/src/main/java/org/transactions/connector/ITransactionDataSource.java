package org.transactions.connector;

import org.model.transactions.Transaction;

import java.util.List;
import java.util.Optional;

public interface ITransactionDataSource {

    List<Transaction> getAllTransactions();

    Optional<Transaction> getTransaction(String id);

    void deleteTransactions(String id);

    Transaction saveTransactions(Transaction aTransaction);
}
