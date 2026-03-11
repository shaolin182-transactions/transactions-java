package org.model.transactions.builder;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.model.transactions.Transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.model.transactions.TransactionCategoryType.COURANTE;
import static org.model.transactions.TransactionCategoryType.EXTRA;

class TransactionBuilderTest {

    @Test
    @DisplayName("Build a full transaction object")
    void build(){
        Transaction transaction = new TransactionBuilder()
                .withId("idTransaction")
                .withCost(Long.valueOf(15456))
                .withBankAccountFrom().withCategory("PEE").withId(1).withLabel("PEE_label").done()
                .withBankAccountTo().withCategory("Commun").withId(3).withLabel("Commun_label").done()
                .addTransactions()
                    .addTransaction()
                        .withCategory().withLabel("Cat1").withId(4).withCategory("ACat").withType(COURANTE).done()
                        .withDescription("desc").withIncome(Float.valueOf(0)).withOutcome(Float.valueOf(5.5f)).done()
                    .addTransaction()
                        .withCategory().withLabel("Cat2").withId(78).withCategory("BCat").withType(EXTRA).done()
                        .withDescription("desc2").withIncome(Float.valueOf(4.8f)).withOutcome(Float.valueOf(0)).done()
                .done()
                .build();

        // Main Properties
        assertEquals("idTransaction", transaction.getId());
        assertEquals(Long.valueOf(15456), transaction.getCost());

        // Bank Account From
        assertEquals("PEE", transaction.getFrom().getCategory());
        assertEquals(1, transaction.getFrom().getId());
        assertEquals("PEE_label", transaction.getFrom().getLabel());

        // Bank Account To
        assertEquals("Commun", transaction.getTo().getCategory());
        assertEquals(3, transaction.getTo().getId());
        assertEquals("Commun_label", transaction.getTo().getLabel());

        // Transactions list
        assertEquals(2, transaction.getTransactions().size());

        // Transactions list - Item 1
        assertEquals("desc", transaction.getTransactions().get(0).getDescription());
        assertEquals(Float.valueOf(0), transaction.getTransactions().get(0).getIncome());
        assertEquals(Float.valueOf(5.5f), transaction.getTransactions().get(0).getOutcome());
        assertEquals(4, transaction.getTransactions().get(0).getCategory().getId());
        assertEquals("Cat1", transaction.getTransactions().get(0).getCategory().getLabel());
        assertEquals("ACat", transaction.getTransactions().get(0).getCategory().getCategory());
        assertEquals(COURANTE, transaction.getTransactions().get(0).getCategory().getType());

        // Transactions list - Item 2
        assertEquals("desc2", transaction.getTransactions().get(1).getDescription());
        assertEquals(Float.valueOf(4.8f), transaction.getTransactions().get(1).getIncome());
        assertEquals(Float.valueOf(0), transaction.getTransactions().get(1).getOutcome());
        assertEquals(78, transaction.getTransactions().get(1).getCategory().getId());
        assertEquals("Cat2", transaction.getTransactions().get(1).getCategory().getLabel());
        assertEquals("BCat", transaction.getTransactions().get(1).getCategory().getCategory());
        assertEquals(EXTRA, transaction.getTransactions().get(1).getCategory().getType());
    }
}