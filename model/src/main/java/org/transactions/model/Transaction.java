package org.transactions.model;

import org.springframework.data.annotation.Id;

import java.util.List;

public class Transaction {

    /**
     * Unique identifier of transactions
     */
    @Id
    private String id;

    private BankAccount from;

    private BankAccount to;

    private List<TransactionDetails> transactions;

    /**
     * Cost in cents
     */
    private Long cost;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Transaction id(String id){
        this.id = id;
        return this;
    }

    public BankAccount getFrom() {
        return from;
    }

    public void setFrom(BankAccount from) {
        this.from = from;
    }

    public BankAccount getTo() {
        return to;
    }

    public void setTo(BankAccount to) {
        this.to = to;
    }

    public List<TransactionDetails> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionDetails> transactions) {
        this.transactions = transactions;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }
}
