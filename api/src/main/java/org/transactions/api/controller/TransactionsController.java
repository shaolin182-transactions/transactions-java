package org.transactions.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.transactions.ITransactionService;

@RestController
public class TransactionsController {

    @Autowired
    private ITransactionService service;

    @GetMapping("/transactions")
    public void getAll(){
        service.getAllTransactions();
    }
}
