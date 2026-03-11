package org.transactions.model;

import org.transactions.model.builder.TransactionDetailsListBuilder;

import java.util.function.Consumer;

public class TransactionDetails {

    /**
     * Category of the transaction
     * Use for statistics purpose
     */
    private TransactionCategory category;

    private Float income;

    private Float outcome;

    private String description;

    private TransactionDetails() {

    }

    public TransactionCategory getCategory() {
        return category;
    }

    public Float getIncome() {
        return income;
    }

    public Float getOutcome() {
        return outcome;
    }

    public String getDescription() {
        return description;
    }

    public static class TransactionDetailsBuilder {

        private TransactionDetails instance;

        private TransactionDetailsListBuilder parentBuilder;

        private Consumer<TransactionDetails> callback;

        public TransactionDetailsBuilder(TransactionDetailsListBuilder parentBuilder, Consumer<TransactionDetails> callback){
            this.parentBuilder = parentBuilder;
            this.callback = callback;
            this.instance = new TransactionDetails();
        }

        public TransactionDetailsBuilder withIncome(Float income){
            instance.income = income;
            return this;
        }

        public TransactionDetailsBuilder withOutcome(Float outcome){
            instance.outcome = outcome;
            return this;
        }

        public TransactionDetailsBuilder withDescription(String description){
            instance.description = description;
            return this;
        }

        public TransactionCategory.TransactionCategoryBuilder withCategory(){
            Consumer<TransactionCategory> callback = obj -> { instance.category = obj;};
            return new TransactionCategory.TransactionCategoryBuilder(this, callback);
        }

        public TransactionDetailsListBuilder done(){
            callback.accept(instance);
            return parentBuilder;
        }
    }
}
