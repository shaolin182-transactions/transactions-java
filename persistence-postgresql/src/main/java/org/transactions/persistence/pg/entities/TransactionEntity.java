package org.transactions.persistence.pg.entities;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Entity(name = "transactions")
public class TransactionEntity {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    private String description;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "transaction_id", referencedColumnName = "id")
    private List<SubTransactionEntity> transactionsDetails;

    private OffsetDateTime date;

    private Long cost;

    private Long costAbs;

    private String type;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<SubTransactionEntity> getTransactionsDetails() {
        return transactionsDetails;
    }

    public void setTransactionsDetails(List<SubTransactionEntity> transactionsDetails) {
        this.transactionsDetails = transactionsDetails;
    }

    public OffsetDateTime getDate() {
        return date;
    }

    public void setDate(OffsetDateTime date) {
        this.date = date;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @PrePersist
    /**
     * We cannot use @GeneratedValue annotation for generating id because
     * some transaction will be migrated from other sources, and they already have an id
     *
     * So, as ew transactions will not have an id, we use this method to initialize the id
     */
    public void setUUID(){
        if (id == null){
            id = UUID.randomUUID();
        }
    }
}
