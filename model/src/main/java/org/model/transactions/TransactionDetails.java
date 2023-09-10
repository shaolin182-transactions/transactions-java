package org.model.transactions;

import org.model.constraint.ValidIncomeOutcome;
import org.model.transactions.builder.TransactionDetailsListBuilder;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.util.Objects;
import java.util.function.Consumer;

@ValidIncomeOutcome
public class TransactionDetails {

    /**
     * Category of the transaction
     * Use for statistics purpose
     */
    @Valid
    private TransactionCategory category;

    @PositiveOrZero
    private Float income;

    @PositiveOrZero
    private Float outcome;

    @Size(max = 512)
    @Pattern(regexp = "^[\\p{L}0-9_@./# &,'-]*$")
    private String description;

    @Valid
    @NotNull
    private BankAccount bankAccount;

    /**
     * Cost in cents
     */
    private Long cost;

    /**
     * Absolute value of cost field
     */
    private Long costAbs;

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

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public Long getCostAbs() {
        return costAbs;
    }

    public void setCostAbs(Long costAbs) {
        this.costAbs = costAbs;
    }

    public void setCategory(TransactionCategory category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionDetails that = (TransactionDetails) o;
        return Objects.equals(category, that.category) &&
                Objects.equals(income, that.income) &&
                Objects.equals(outcome, that.outcome) &&
                Objects.equals(description, that.description) &&
                Objects.equals(bankAccount, that.bankAccount) &&
                Objects.equals(cost, that.cost) &&
                Objects.equals(costAbs, that.costAbs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, income, outcome, description, bankAccount, cost, costAbs);
    }

    public static class TransactionDetailsBuilder {

        private TransactionDetails instance;

        private TransactionDetailsListBuilder parentBuilder;

        private Consumer<TransactionDetails> callback;

        public TransactionDetailsBuilder() {
            this.instance = new TransactionDetails();
        }

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
            Consumer<TransactionCategory> callbackMethod = obj -> instance.category = obj;
            return new TransactionCategory.TransactionCategoryBuilder(this, callbackMethod);
        }

        public TransactionDetailsBuilder withCategory(TransactionCategory category){
            instance.category = category;
            return this;
        }

        public TransactionDetailsListBuilder done(){
            callback.accept(instance);
            return parentBuilder;
        }

        public TransactionDetailsBuilder withBankAccount(BankAccount bankAccount) {
            instance.setBankAccount(bankAccount);
            return this;
        }

        public BankAccount.BankAccountBuilder withBankAccount() {
            Consumer<BankAccount> callbackMethod = obj -> instance.setBankAccount(obj);
            return new BankAccount.BankAccountBuilder(this, callbackMethod);
        }

        public TransactionDetails build() {
            return instance;
        }
    }

    /**
     * Compute some fields
     */
    public void computeDynamicFields() {
        // Compute total cost in cent
        cost = (long) Math.round((income - outcome) * 100);

        // Compute absolute value of cost
        costAbs =  Math.abs(cost);
    }


}
