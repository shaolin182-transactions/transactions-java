package org.model.transactions;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

public class Transaction {

    /**
     * Unique identifier of transactions
     */
    private String id;

    @Valid
    @NotNull
    private BankAccount from;

    @Valid
    private BankAccount to;

    @Valid
    @NotEmpty
    private List<TransactionDetails> transactions;

    @NotNull
    private OffsetDateTime date;

    /**
     * Cost in cents
     */
    private Long cost;

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

    public BankAccount getFrom() {
        return from;
    }

    public void setFrom(BankAccount from) {
        this.from = from;
    }

    public BankAccount getTo() {
        return to;
    }

    public void setTo(BankAccount to) {
        this.to = to;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return id.equals(that.id) &&
                from.equals(that.from) &&
                Objects.equals(to, that.to) &&
                Objects.equals(transactions, that.transactions) &&
                date.equals(that.date) &&
                Objects.equals(cost, that.cost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, from, to, transactions, date, cost);
    }
}
