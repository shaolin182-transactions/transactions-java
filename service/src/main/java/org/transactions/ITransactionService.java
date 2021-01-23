package org.transactions;

import com.github.fge.jsonpatch.JsonPatch;
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

    Transaction patchTransaction(String id, JsonPatch transaction);

    Transaction createTransaction(Transaction transaction);

    void deleteTransaction(String id);

}
