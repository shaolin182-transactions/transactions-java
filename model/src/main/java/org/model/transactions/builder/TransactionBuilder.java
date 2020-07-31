package org.model.transactions.builder;

import org.model.transactions.Transaction;
import org.model.transactions.TransactionDetails;

import java.time.OffsetDateTime;
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

    public TransactionBuilder addTransactions(List<TransactionDetails> transactions) {
        instance.setTransactions(transactions);
        return this;
    }

    public TransactionDetailsListBuilder addTransactions() {
        Consumer<List<TransactionDetails>> callback = obj -> instance.setTransactions(obj);
        return new TransactionDetailsListBuilder(this, callback);
    }

    public TransactionBuilder withDate(OffsetDateTime date){
        instance.setDate(date);
        return this;
    }

    public TransactionBuilder withDescription(String description){
        instance.setDescription(description);
        return this;
    }

    public Transaction build(){
        return instance;
    }
}
