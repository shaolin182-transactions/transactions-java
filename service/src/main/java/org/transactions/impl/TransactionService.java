package org.transactions.impl;

import org.model.transactions.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.transactions.ITransactionService;
import org.transactions.connector.ITransactionDataSource;
import org.transactions.exception.TransactionNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService implements ITransactionService {

    private ITransactionDataSource transactionDataSource;

    @Autowired
    public TransactionService(ITransactionDataSource dataSource){
        this.transactionDataSource = dataSource;
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionDataSource.getAllTransactions();
    }

    @Override
    public Transaction getTransaction(String id) {
        Optional<Transaction> result = transactionDataSource.getTransaction(id);
        return result.orElseThrow(TransactionNotFoundException::new);
    }

    @Override
    public Transaction saveTransaction(String id, Transaction transaction) {

        return createTransaction(transaction);
    }

    @Override
    public Transaction createTransaction(Transaction transaction) {

        transaction.computeDynamicFields();
        transaction.getTransactions().stream().forEach(aTransaction -> aTransaction.computeDynamicFields());

        // Persist in database
        return transactionDataSource.saveTransactions(transaction);
    }

    @Override
    public void deleteTransaction(String id) {
        transactionDataSource.deleteTransactions(id);
    }
}
