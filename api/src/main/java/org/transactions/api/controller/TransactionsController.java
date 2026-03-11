package org.transactions.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.transactions.ITransactionService;
import org.model.transactions.Transaction;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class TransactionsController {

    @Autowired
    private ITransactionService service;

    /**
     * @return a list of all transactions found
     */
    @GetMapping(value = "/transactions", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Transaction>> getAll(){
        List<Transaction> result = service.getAllTransactions();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * @return a unique transaction thanks to an id
     * See APIExceptionHandler in case of transaction not found
     */
    @GetMapping(value = "/transactions/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Transaction> getTransaction(@PathVariable String id) {
        Transaction result = service.getTransaction(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Create a new transaction
     * @return transaction updated
     */
    @PostMapping(value = "/transactions", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        Transaction result = service.createTransaction(null);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    /**
     * Update transaction with id given in parameter
     * @return transaction updated
     */
    @PutMapping(value = "/transactions/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Transaction> updateTransaction(@PathVariable String id, @RequestBody Transaction transaction) {
        Transaction result = service.saveTransaction(id, null);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Delete a transaction
     * @param id : unique identifier of the transaction
     * @return an HTTP response 204 with no body
     */
    @DeleteMapping(value= "/transactions/{id}")
    public ResponseEntity<Transaction> deleteTransaction(@PathVariable String id) {
        service.deleteTransaction(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
