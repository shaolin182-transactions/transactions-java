package org.model.transactions;

import java.util.function.Consumer;

public class TransactionCategory {

    /**
     * Unique identifier of a category
     */
    private Integer id;

    /**
     * Parent Category
     */
    private String category;

    /**
     * Category label
     */
    private String label;

    /**
     * Private constructor - New TransactionCategory object must be created through builder
     */
    private TransactionCategory() {

    }

    /**
     * Used for categorize categories
     */
    private TransactionCategoryType type;

    public Integer getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getLabel() {
        return label;
    }

    public TransactionCategoryType getType() {
        return type;
    }

    public static class TransactionCategoryBuilder {

        private TransactionDetails.TransactionDetailsBuilder parentBuilder;

        private Consumer<TransactionCategory> callback;

        private TransactionCategory instance;

        public TransactionCategoryBuilder(TransactionDetails.TransactionDetailsBuilder parentBuilder, Consumer<TransactionCategory> callback){
            this.parentBuilder = parentBuilder;
            this.callback = callback;
            this.instance = new TransactionCategory();
        }

        public TransactionCategoryBuilder withId(Integer id){
            instance.id = id;
            return this;
        }

        public TransactionCategoryBuilder withCategory(String category){
            instance.category = category;
            return this;
        }

        public TransactionCategoryBuilder withLabel(String label){
            instance.label = label;
            return this;
        }

        public TransactionCategoryBuilder withType(TransactionCategoryType type){
            instance.type = type;
            return  this;
        }

        public TransactionDetails.TransactionDetailsBuilder done(){
            callback.accept(instance);
            return parentBuilder;
        }
    }
}
