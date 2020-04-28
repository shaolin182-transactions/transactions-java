package org.transactions.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.transactions.connector.ITransactionDataSource;
import org.model.transactions.Transaction;
import org.transactions.persistence.repositories.TransactionsRepository;

import java.util.List;
import java.util.Optional;

@Component
public class TransactionDatasource implements ITransactionDataSource {

    @Autowired
    private TransactionsRepository repository;

    @Override
    public List<Transaction> getAllTransactions() {
        return repository.findAll();
    }

    @Override
    public Optional<Transaction> getTransaction(String id) {
        return repository.findById(id);
    }

    @Override
    public void deleteTransactions(String id) {
        repository.deleteById(id);
    }

    @Override
    public Transaction saveTransactions(Transaction aTransaction) {
        return repository.save(aTransaction);
    }
}
