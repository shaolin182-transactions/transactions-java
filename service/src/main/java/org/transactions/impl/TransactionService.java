package org.transactions.impl;

import org.apache.commons.lang3.StringUtils;
import org.model.transactions.BankAccount;
import org.model.transactions.Transaction;
import org.model.transactions.TransactionCategory;
import org.model.transactions.TransactionDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.transactions.ITransactionService;
import org.transactions.connector.ICommonDataDatasource;
import org.transactions.connector.ITransactionDataSource;
import org.transactions.exception.TransactionBadDataException;
import org.transactions.exception.TransactionNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService implements ITransactionService {

    private ITransactionDataSource transactionDataSource;

    private ICommonDataDatasource commonDataDatasource;

    @Autowired
    public TransactionService(ITransactionDataSource dataSource, ICommonDataDatasource commonDataDatasource ){
        this.transactionDataSource = dataSource;
        this.commonDataDatasource = commonDataDatasource;
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

        // Handle incomplete category and bankAccount
        transaction.getTransactions().forEach(this::completeInformation);

        // Compute some fields
        transaction.computeDynamicFields();
        transaction.getTransactions().forEach(TransactionDetails::computeDynamicFields);

        // Persist in database
        return transactionDataSource.saveTransactions(transaction);
    }

    @Override
    public void deleteTransaction(String id) {
        transactionDataSource.deleteTransactions(id);
    }

    /**
     * Check if some information are missing on category and bankAccount and add them
     */
    private void completeInformation(TransactionDetails detailTransaction) {
        if (detailTransaction.getCategory() != null && StringUtils.isEmpty(detailTransaction.getCategory().getCategory())){
            // Missing category information, only id is provided
            TransactionCategory category = commonDataDatasource.findCategoryById(detailTransaction.getCategory().getId())
                    .orElseThrow(TransactionBadDataException::new);

            detailTransaction.setCategory(category);
        }

        if (detailTransaction.getBankAccount() != null && StringUtils.isEmpty(detailTransaction.getBankAccount().getCategory())){
            // Missing bank account information, only id is provided
            BankAccount result = commonDataDatasource.findBankAccountById(detailTransaction.getBankAccount().getId())
                    .orElseThrow(TransactionBadDataException::new);
            detailTransaction.setBankAccount(result);
        }
    }
}
