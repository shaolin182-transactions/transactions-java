package org.transactions.persistence.pg.converters;

import org.apache.commons.lang3.StringUtils;
import org.model.transactions.Transaction;
import org.model.transactions.TransactionDetails;
import org.springframework.stereotype.Component;
import org.transactions.persistence.pg.entities.BankAccountEntity;
import org.transactions.persistence.pg.entities.CategoryEntity;
import org.transactions.persistence.pg.entities.SubTransactionEntity;
import org.transactions.persistence.pg.entities.TransactionEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class TransactionConverter {

    public TransactionEntity convert(Transaction aTransaction) {
        if (aTransaction == null) {
            return null;
        }
        var entity = new TransactionEntity();
        if (StringUtils.isNotBlank(aTransaction.getId())){
            entity.setId(UUID.fromString(aTransaction.getId()));
        }
        entity.setCost(aTransaction.getCost());
        entity.setCostAbs(aTransaction.getCostAbs());
        entity.setDate(aTransaction.getDate());
        entity.setDescription(aTransaction.getDescription());
        if (aTransaction.getType() != null){
            entity.setType(aTransaction.getType().name());
        }

        entity.setTransactionsDetails(convert(aTransaction.getTransactions()));
        return entity;
    }

    private List<SubTransactionEntity> convert(List<TransactionDetails> transactionDetails){

        if (transactionDetails == null){
            return List.of();
        }

        return transactionDetails.stream().map(t -> {
            var entity = new SubTransactionEntity();
            entity.setId(t.getId());
            entity.setCost(t.getCost());
            entity.setCostAbs(t.getCostAbs());
            entity.setDescription(t.getDescription());
            entity.setIncome(t.getIncome());
            entity.setOutcome(t.getOutcome());

            if (t.getCategory() != null){
                var categoryEntity = new CategoryEntity();
                categoryEntity.setId(t.getCategory().getId());
                categoryEntity.setCategory(t.getCategory().getCategory());
                categoryEntity.setLabel(t.getCategory().getLabel());
                categoryEntity.setType(t.getCategory().getType().name());
                entity.setCategory(categoryEntity);
            }
            if (t.getBankAccount() != null){
                var bankAccountEntity = new BankAccountEntity();
                bankAccountEntity.setId(t.getBankAccount().getId());
                bankAccountEntity.setCategory(t.getBankAccount().getCategory());
                bankAccountEntity.setLabel(t.getBankAccount().getLabel());
                entity.setBankAccount(bankAccountEntity);
            }
            return entity;
        }).collect(Collectors.toCollection(ArrayList::new));

    }
}
