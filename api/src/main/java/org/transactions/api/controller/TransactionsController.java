package org.transactions.api.controller;

import com.github.fge.jsonpatch.JsonPatch;
import io.micrometer.observation.annotation.Observed;
import org.model.transactions.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.transactions.ITransactionService;
import org.transactions.api.mapper.TransactionMapper;
import org.transactions.api.server.TransactionsApi;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Observed(name = "transactions.controller",
        contextualName = "transactions-controller",
        lowCardinalityKeyValues = {"layer", "controller"})
public class TransactionsController implements TransactionsApi {

    @Autowired
    private ITransactionService service;

    @Autowired
    TransactionMapper mapper;

    /**
     * @return a list of all transactions found
     */
    public ResponseEntity<List<org.transactions.api.server.model.Transaction>> getAll(){
        List<Transaction> result = service.getAllTransactions();
        var response = result.stream().map(item -> mapper.transactionToRest(item)).collect(Collectors.toList());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * @return a unique transaction thanks to an id
     * See APIExceptionHandler in case of transaction not found
     */
    public ResponseEntity<org.transactions.api.server.model.Transaction> getTransaction(@PathVariable String id) {
        Transaction result = service.getTransaction(id);

        var response = mapper.transactionToRest(result);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Create a new transaction
     * @return transaction updated
     */
    public ResponseEntity<org.transactions.api.server.model.Transaction> createTransaction(org.transactions.api.server.model.Transaction transaction) {
        var request = mapper.transactionFromRest(transaction);
        Transaction result = service.createTransaction(request);
        var response = mapper.transactionToRest(result);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Update transaction with id given in parameter
     * @return transaction updated
     */
    public ResponseEntity<org.transactions.api.server.model.Transaction> updateTransaction(String id, org.transactions.api.server.model.Transaction transaction) {
        var request =  mapper.transactionFromRest(transaction);
        Transaction result = service.saveTransaction(id, request);
        var response = mapper.transactionToRest(result);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Patch a transaction with id given in parameter
     * @return transaction updated
     */
    public ResponseEntity<org.transactions.api.server.model.Transaction> patchTransaction(String id, JsonPatch patchOp) {

        var result = service.patchTransaction(id, patchOp);
        var response = mapper.transactionToRest(result);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Delete a transaction
     * @param id : unique identifier of the transaction
     * @return an HTTP response 204 with no body
     */
    public ResponseEntity<Void> deleteTransaction(String id) {
        service.deleteTransaction(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
