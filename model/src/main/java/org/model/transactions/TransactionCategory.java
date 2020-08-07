package org.model.transactions;

import javax.validation.constraints.*;
import java.util.Objects;
import java.util.function.Consumer;

public class TransactionCategory {

    /**
     * Unique identifier of a category
     */
    @Positive
    @Max(value = 512)
    @NotNull
    private Integer id;

    /**
     * Parent Category
     */
    @Size(max = 64)
    @Pattern(regexp = "^[\\p{L}0-9/ ,'-]*$")
    @NotNull
    private String category;

    /**
     * Category label
     */
    @Size(max = 64)
    @Pattern(regexp = "^[\\p{L}0-9/ ,'-]*$")
    @NotNull
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionCategory that = (TransactionCategory) o;
        return id.equals(that.id) &&
                category.equals(that.category) &&
                label.equals(that.label) &&
                type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, category, label, type);
    }

    public static class TransactionCategoryBuilder {

        private TransactionDetails.TransactionDetailsBuilder parentBuilder;

        private Consumer<TransactionCategory> callback;

        private TransactionCategory instance;

        public TransactionCategoryBuilder(){
            this.instance = new TransactionCategory();
        }

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

        public TransactionCategory build(){
            return instance;
        }
    }
}
