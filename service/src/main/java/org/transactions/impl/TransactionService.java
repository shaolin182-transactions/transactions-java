package org.transactions.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.transactions.ITransactionService;
import org.transactions.connector.ITransactionDataSource;
import org.transactions.exception.TransactionNotFoundException;
import org.model.transactions.Transaction;

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
        return null;
    }

    @Override
    public Transaction createTransaction(Transaction transaction) {
        return null;
    }

    @Override
    public Transaction deleteTransaction(String id) {
        return null;
    }
}
