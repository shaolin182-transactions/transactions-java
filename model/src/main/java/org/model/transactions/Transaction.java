package org.model.transactions;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

public class Transaction {

    /**
     * Unique identifier of transactions
     */
    private String id;

    @Size(max = 512)
    @Pattern(regexp = "^[\\p{L}0-9_@./# &,'-]*$")
    private String description;

    @Valid
    @NotEmpty
    private List<TransactionDetails> transactions;

    @NotNull
    private OffsetDateTime date;

    /**
     * Cost in cents
     */
    private Long cost;

    /**
     * Absolute value of cost field
     */
    private Long costAbs;

    /**
     * Type of transaction (income or outcome)
     */
    private TransactionType type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Transaction id(String id){
        this.id = id;
        return this;
    }

    public List<TransactionDetails> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionDetails> transactions) {
        this.transactions = transactions;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public OffsetDateTime getDate() {
        return date;
    }

    public void setDate(OffsetDateTime date) {
        this.date = date;
    }

    public Long getCostAbs() {
        return costAbs;
    }

    public void setCostAbs(Long costAbs) {
        this.costAbs = costAbs;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(description, that.description) &&
                Objects.equals(transactions, that.transactions) &&
                Objects.equals(date, that.date) &&
                Objects.equals(cost, that.cost) &&
                Objects.equals(costAbs, that.costAbs) &&
                type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, transactions, date, cost, costAbs, type);
    }

    public void computeDynamicFields() {
        // Compute total cost in cent
        Float tempCost = transactions.stream().map(item -> item.getIncome() - item.getOutcome()).reduce(0f, Float::sum);
        cost = (long) Math.round(tempCost * 100);

        // Compute absolute value of cost
        costAbs =  Math.abs(cost);

        // Compute transaction type property
        if (cost < 0){
            type = TransactionType.OUTCOME;
        } else {
            type = TransactionType.INCOME;
        }
    }
}
