package org.transactions.api.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.model.transactions.TransactionCategoryType;
import org.model.transactions.TransactionDetails;
import org.model.transactions.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.transactions.api.server.model.BankAccount;
import org.transactions.api.server.model.Category;
import org.transactions.api.server.model.Transaction;
import org.transactions.api.server.model.TransactionDetail;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Check that mapping is correct between data from REST endpoints and model objets
 */
@SpringBootTest
class TransactionMapperTest {

    @Autowired
    TransactionMapper mapper;

    @Test
    @DisplayName("should convert REST data to model object")
    void testFromRestToModel() {
        var bk = new BankAccount();
        bk.setId(1l);
        bk.setCategory("bkCategory");
        bk.setLabel("bkLabel");

        var category = new Category();
        category.setId(2l);
        category.setCategory("category");
        category.setLabel("label");
        category.setType(Category.TypeEnum.EXTRA);

        var details = new TransactionDetail();
        details.setCategory(category);
        details.setDescription("description");
        details.setBankAccount(bk);
        details.setCost(1235l);
        details.setCostAbs(13545l);
        details.setIncome(-1235.5f);
        details.setOutcome(425.5f);


        var restTransaction = new Transaction();
        restTransaction.setId("someId");
        restTransaction.setCost(12356l);
        restTransaction.setCostAbs(12354668l);
        restTransaction.setDescription("Some description");
        restTransaction.setType(Transaction.TypeEnum.OUTCOME);
        restTransaction.setDate(OffsetDateTime.now());
        restTransaction.setTransactions(List.of(details));

        var result = mapper.transactionFromRest(restTransaction);
        var resultDetails = result.getTransactions().get(0);

        Assertions.assertAll(
                () -> Assertions.assertEquals(restTransaction.getId(), result.getId()),
                () -> Assertions.assertEquals(restTransaction.getCost(), result.getCost()),
                () -> Assertions.assertEquals(restTransaction.getCostAbs(), result.getCostAbs()),
                () -> Assertions.assertEquals(TransactionType.OUTCOME, result.getType()),
                () -> Assertions.assertEquals(restTransaction.getDate(), result.getDate()),
                () -> Assertions.assertEquals(restTransaction.getDescription(), result.getDescription()),
                () -> Assertions.assertEquals(restTransaction.getDate(), result.getDate()),

                () -> Assertions.assertEquals(1235l, resultDetails.getCost()),
                () -> Assertions.assertEquals(13545l, resultDetails.getCostAbs()),
                () -> Assertions.assertEquals(-1235.5f, resultDetails.getIncome()),
                () -> Assertions.assertEquals(425.5f, resultDetails.getOutcome()),
                () -> Assertions.assertEquals("description", resultDetails.getDescription()),
                () -> Assertions.assertEquals(1, resultDetails.getBankAccount().getId()),
                () -> Assertions.assertEquals("bkCategory", resultDetails.getBankAccount().getCategory()),
                () -> Assertions.assertEquals("bkLabel", resultDetails.getBankAccount().getLabel()),
                () -> Assertions.assertEquals(2, resultDetails.getCategory().getId()),
                () -> Assertions.assertEquals("category", resultDetails.getCategory().getCategory()),
                () -> Assertions.assertEquals("label", resultDetails.getCategory().getLabel()),
                () -> Assertions.assertEquals(TransactionCategoryType.EXTRA, resultDetails.getCategory().getType())

        );
    }

    @Test
    void testMappingCategory() {
        var category = new Category();
        category.setId(2l);
        category.setCategory("category");
        category.setLabel("label");
        category.setType(Category.TypeEnum.EXTRA);

        var result = mapper.categoryFromRest(category);

        Assertions.assertAll(
                () -> Assertions.assertEquals(2, result.getId()),
                () -> Assertions.assertEquals("category", result.getCategory()),
                () -> Assertions.assertEquals("label", result.getLabel()),
                () -> Assertions.assertEquals(TransactionCategoryType.EXTRA, result.getType())
        );
    }
}
