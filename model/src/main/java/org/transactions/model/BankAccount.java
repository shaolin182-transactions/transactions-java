package org.transactions.model;

import org.transactions.model.builder.TransactionBuilder;

import java.util.function.Consumer;

public class BankAccount {

    /**
     * Unique identifier of a bank account
     */
    private Integer id;

    /**
     * Categorize bank account
     */
    private String category;

    /**
     * Bank account label
     */
    private String label;

    /**
     * Private constructor - New BankAccount object must be created through builder
     */
    private BankAccount(){

    }

    public Integer getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getLabel() {
        return label;
    }

    /**
     * Create new instance of BankAccount object
     */
    public static class BankAccountBuilder {

        private TransactionBuilder parentBuilder;

        private Consumer<BankAccount> callback;

        private BankAccount instance;

        public BankAccountBuilder(TransactionBuilder parentBuilder, Consumer<BankAccount> callback){
            this.parentBuilder = parentBuilder;
            this.callback = callback;
            this.instance = new BankAccount();
        }

        public BankAccountBuilder withId(Integer id){
            instance.id = id;
            return this;
        }

        public BankAccountBuilder withCategory(String category){
            instance.category = category;
            return this;
        }

        public BankAccountBuilder withLabel(String label){
            instance.label = label;
            return this;
        }

        public TransactionBuilder done(){
            callback.accept(instance);
            return parentBuilder;
        }
    }

}
