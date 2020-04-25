package org.model.transactions.builder;

import org.model.transactions.BankAccount;
import org.model.transactions.Transaction;
import org.model.transactions.TransactionDetails;

import java.util.List;
import java.util.function.Consumer;

public class TransactionBuilder {

    private Transaction instance;

    public TransactionBuilder() {
        instance = new Transaction();
    }

    public TransactionBuilder withId(String id){
        instance.setId(id);
        return this;
    }

    public TransactionBuilder withCost(Long cost){
        instance.setCost(cost);
        return this;
    }

    public TransactionBuilder withBankAccountFrom(BankAccount bankAccount) {
        instance.setFrom(bankAccount);
        return this;
    }

    public BankAccount.BankAccountBuilder withBankAccountFrom() {
        Consumer<BankAccount> callback = obj -> { instance.setFrom(obj);};
        return new BankAccount.BankAccountBuilder(this, callback);
    }

    public TransactionBuilder withBankAccountTo(BankAccount bankAccount) {
        instance.setTo(bankAccount);
        return this;
    }

    public BankAccount.BankAccountBuilder withBankAccountTo() {
        Consumer<BankAccount> callback = obj -> { instance.setTo(obj);};
        return new BankAccount.BankAccountBuilder(this, callback);
    }

    public TransactionBuilder addTransactions(List<TransactionDetails> transactions) {
        instance.setTransactions(transactions);
        return this;
    }

    public TransactionDetailsListBuilder addTransactions() {
        Consumer<List<TransactionDetails>> callback = obj -> {instance.setTransactions(obj);};
        return new TransactionDetailsListBuilder(this, callback);
    }

    public Transaction build(){
        return instance;
    }
}
