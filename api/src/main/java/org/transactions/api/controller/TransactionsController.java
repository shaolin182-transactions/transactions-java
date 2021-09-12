package org.transactions.api.controller;

import com.github.fge.jsonpatch.JsonPatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.transactions.ITransactionService;
import org.model.transactions.Transaction;

import javax.validation.Valid;
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
    public ResponseEntity<List<Transaction>> getAll(@AuthenticationPrincipal Jwt token){
        List<Transaction> result = service.getAllTransactions();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * @return a unique transaction thanks to an id
     * See APIExceptionHandler in case of transaction not found
     */
    @GetMapping(value = "/transactions/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Transaction> getTransaction(@PathVariable String id, @AuthenticationPrincipal Jwt token) {
        Transaction result = service.getTransaction(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Create a new transaction
     * @return transaction updated
     */
    @PostMapping(value = "/transactions", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Transaction> createTransaction(@Valid @RequestBody Transaction transaction, @AuthenticationPrincipal Jwt token) {
        Transaction result = service.createTransaction(transaction);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    /**
     * Update transaction with id given in parameter
     * @return transaction updated
     */
    @PutMapping(value = "/transactions/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Transaction> updateTransaction(@PathVariable String id, @Valid @RequestBody Transaction transaction, @AuthenticationPrincipal Jwt token) {
        Transaction result = service.saveTransaction(id, transaction);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Patch a transaction with id given in parameter
     * @return transaction updated
     */
    @PatchMapping(value = "/transactions/{id}", produces = APPLICATION_JSON_VALUE, consumes = "application/json-patch+json")
    public ResponseEntity<Transaction> patchTransaction(@PathVariable String id, @RequestBody JsonPatch patchOp, @AuthenticationPrincipal Jwt token) {
        Transaction result = service.patchTransaction(id, patchOp);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Delete a transaction
     * @param id : unique identifier of the transaction
     * @return an HTTP response 204 with no body
     */
    @DeleteMapping(value= "/transactions/{id}")
    public ResponseEntity<Transaction> deleteTransaction(@PathVariable String id, @AuthenticationPrincipal Jwt token) {
        service.deleteTransaction(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
