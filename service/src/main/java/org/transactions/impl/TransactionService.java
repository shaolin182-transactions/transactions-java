package org.transactions.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.apache.commons.lang3.StringUtils;
import org.model.transactions.BankAccount;
import org.model.transactions.Transaction;
import org.model.transactions.TransactionCategory;
import org.model.transactions.TransactionDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.transactions.ITransactionService;
import org.transactions.connector.ICommonDataDatasource;
import org.transactions.connector.ITransactionDataSource;
import org.transactions.exception.TransactionBadDataException;
import org.transactions.exception.TransactionNotFoundException;
import org.transactions.exception.TransactionProcessException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TransactionService implements ITransactionService {

    private final ITransactionDataSource transactionDataSource;

    private final ICommonDataDatasource commonDataDatasource;

    private final ObjectMapper mapper;

    @Autowired
    public TransactionService(ITransactionDataSource dataSource, ObjectMapper mapper, ICommonDataDatasource commonDataDatasource){
        this.transactionDataSource = dataSource;
        this.mapper = mapper;
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

    /**
     * Looking for a transaction with given id and apply values from transaction object
     * @param id : identifier of the transaction
     * @param patchOp : operations to apply to existing transaction
     * @return the updated transaction
     */
    @Override
    public Transaction patchTransaction(String id, JsonPatch patchOp)  {
        Transaction origin = getTransaction(id);

        Transaction result;
        try {
            JsonNode patched = patchOp.apply(mapper.convertValue(origin, JsonNode.class));
            result =  mapper.treeToValue(patched, Transaction.class);
        } catch (JsonPatchException | JsonProcessingException e) {
            throw new TransactionProcessException("An exception occurs while processing patch operation", e);
        }

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Transaction>> errors =  validator.validate(result);

        if (!CollectionUtils.isEmpty(errors)){
            throw new TransactionBadDataException();
        }

        return createTransaction(result);
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
