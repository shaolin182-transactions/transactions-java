package org.transactions.persistence.pg.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "bank_accounts")
public class BankAccountEntity {

    @Id
    private Integer id;

    private String category;

    private String label;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
