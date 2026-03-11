package org.transactions.model.builder;

import org.transactions.model.BankAccount;
import org.transactions.model.Transaction;
import org.transactions.model.TransactionDetails;

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

    public BankAccount.BankAccountBuilder withBankAccountFrom() {
        Consumer<BankAccount> callback = obj -> { instance.setFrom(obj);};
        return new BankAccount.BankAccountBuilder(this, callback);
    }

    public BankAccount.BankAccountBuilder withBankAccountTo() {
        Consumer<BankAccount> callback = obj -> { instance.setTo(obj);};
        return new BankAccount.BankAccountBuilder(this, callback);
    }

    public TransactionDetailsListBuilder addTransactions() {
        Consumer<List<TransactionDetails>> callback = obj -> {instance.setTransactions(obj);};
        return new TransactionDetailsListBuilder(this, callback);
    }

    public Transaction build(){
        return instance;
    }
}
