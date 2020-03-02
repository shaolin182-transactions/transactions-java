package org.transactions.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.transactions.ITransactionService;
import org.transactions.model.Transaction;

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
}
