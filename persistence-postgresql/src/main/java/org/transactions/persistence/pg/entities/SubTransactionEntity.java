package org.transactions.persistence.pg.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity(name = "transaction_details")
public class SubTransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private CategoryEntity category;

    private Float income;

    private Float outcome;

    private String description;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "bank_account_id", referencedColumnName = "id")
    private BankAccountEntity bankAccount;

    private Long cost;

    private Long costAbs;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    public Float getIncome() {
        return income;
    }

    public void setIncome(Float income) {
        this.income = income;
    }

    public Float getOutcome() {
        return outcome;
    }

    public void setOutcome(Float outcome) {
        this.outcome = outcome;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BankAccountEntity getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccountEntity bankAccount) {
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
}
