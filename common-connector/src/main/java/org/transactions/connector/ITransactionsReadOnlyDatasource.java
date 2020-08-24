package org.transactions.connector;

import org.model.transactions.Transaction;

import java.util.List;
import java.util.Optional;

public interface ITransactionsReadOnlyDatasource {

    List<Transaction> getAllTransactions();

    Optional<Transaction> getTransaction(String id);

}
