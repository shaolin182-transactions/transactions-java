package org.model.transactions;

import org.model.constraint.ValidIncomeOutcome;
import org.model.transactions.builder.TransactionDetailsListBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
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
    @Pattern(regexp = "^[a-zA-Z0-9_@./# &,'-]*$")
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

        TransactionDetailsBuilder() {
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
            Consumer<TransactionCategory> callback = obj -> { instance.category = obj;};
            return new TransactionCategory.TransactionCategoryBuilder(this, callback);
        }

        public TransactionDetailsListBuilder done(){
            callback.accept(instance);
            return parentBuilder;
        }

        TransactionDetails build() {
            return instance;
        }
    }
}
