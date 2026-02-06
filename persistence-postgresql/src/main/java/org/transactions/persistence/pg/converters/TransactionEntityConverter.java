package org.transactions.persistence.pg.converters;

import org.model.transactions.Transaction;
import org.model.transactions.TransactionCategoryType;
import org.model.transactions.TransactionDetails;
import org.model.transactions.TransactionType;
import org.springframework.stereotype.Component;
import org.transactions.persistence.pg.entities.SubTransactionEntity;
import org.transactions.persistence.pg.entities.TransactionEntity;

import java.util.List;

/**
 * Handle conversion between TransactionEntity and Transaction model
 */
@Component
public class TransactionEntityConverter {

    public Transaction convert(TransactionEntity entityTransaction){
        if(entityTransaction == null){
            return null;
        }

        Transaction transaction = new Transaction();
        transaction.setId(entityTransaction.getId().toString());
        transaction.setDescription(entityTransaction.getDescription());
        transaction.setDate(entityTransaction.getDate());
        transaction.setCost(entityTransaction.getCost());
        transaction.setCostAbs(entityTransaction.getCostAbs());

        if (entityTransaction.getType() != null) {
            transaction.setType(TransactionType.valueOf(entityTransaction.getType()));
        }

        transaction.setTransactions(convert(entityTransaction.getTransactionsDetails()));
        return transaction;
    }

    private List<TransactionDetails> convert(List<SubTransactionEntity> subTransactionEntities){
        if (subTransactionEntities == null){
            return List.of();
        }

            return subTransactionEntities.stream().map(e -> {
                var builder  = new TransactionDetails.TransactionDetailsBuilder();
                builder.withDescription(e.getDescription())
                        .withId(e.getId())
                        .withIncome(e.getIncome())
                        .withOutcome(e.getOutcome());

                if (e.getCategory() != null){
                    builder.withCategory()
                            .withLabel(e.getCategory().getLabel())
                            .withCategory(e.getCategory().getCategory())
                            .withId(e.getCategory().getId()).done();
                    if (e.getCategory().getType() != null){
                        builder.withCategory().withType(TransactionCategoryType.valueOf(e.getCategory().getType()));
                    }
                }

                if (e.getBankAccount() != null){
                    builder.withBankAccount()
                            .withLabel(e.getBankAccount().getLabel())
                            .withCategory(e.getBankAccount().getCategory())
                            .withId(e.getBankAccount().getId()).done();
                }

                return builder.build();
            }).toList();
    }
}
