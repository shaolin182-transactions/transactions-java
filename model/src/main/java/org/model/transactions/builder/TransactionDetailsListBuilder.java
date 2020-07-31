package org.model.transactions.builder;

import org.model.transactions.TransactionDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TransactionDetailsListBuilder {

    private List<TransactionDetails> list;

    private TransactionBuilder parentBuilder;

    private Consumer<List<TransactionDetails>> callback;

    public TransactionDetailsListBuilder() {

    }

    public TransactionDetailsListBuilder(TransactionBuilder parentBuilder, Consumer<List<TransactionDetails>> callback) {
        this.parentBuilder = parentBuilder;
        this.callback = callback;
        list = new ArrayList<TransactionDetails>();
    }

    public TransactionDetails.TransactionDetailsBuilder addTransaction() {
        Consumer<TransactionDetails> callbackMethod = obj ->  list.add(obj);
        return new TransactionDetails.TransactionDetailsBuilder(this, callbackMethod);
    }

    public TransactionBuilder done(){
        callback.accept(list);
        return parentBuilder;
    }
}
