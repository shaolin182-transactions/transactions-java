package org.model.transactions;

import org.model.transactions.TransactionDetails.TransactionDetailsBuilder;

import javax.validation.constraints.*;
import java.util.Objects;
import java.util.function.Consumer;

public class BankAccount {

    /**
     * Unique identifier of a bank account
     */
    @Positive
    @Max(value = 512)
    @NotNull
    private Integer id;

    /**
     * Categorize bank account
     */
    @Size(max = 64)
    @Pattern(regexp = "^[a-zA-Z0-9/ ,'-]*$")
    @NotNull
    private String category;

    /**
     * Bank account label
     */
    @Size(max = 64)
    @Pattern(regexp = "^[a-zA-Z0-9/ ,'-]*$")
    @NotNull
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankAccount that = (BankAccount) o;
        return id.equals(that.id) &&
                category.equals(that.category) &&
                label.equals(that.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, category, label);
    }

    /**
     * Create new instance of BankAccount object
     */
    public static class BankAccountBuilder {

        private TransactionDetailsBuilder parentBuilder;

        private Consumer<BankAccount> callback;

        private BankAccount instance;

        public BankAccountBuilder(){
            this.instance = new BankAccount();
        }

        public BankAccountBuilder(TransactionDetailsBuilder parentBuilder, Consumer<BankAccount> callback){
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

        public TransactionDetailsBuilder done(){
            callback.accept(instance);
            return parentBuilder;
        }

        public BankAccount build(){
            return instance;
        }
    }

}
