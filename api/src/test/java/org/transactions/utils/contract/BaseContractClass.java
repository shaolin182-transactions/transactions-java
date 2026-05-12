package org.transactions.utils.contract;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.transactions.api.controller.TransactionsController;
import org.transactions.api.mapper.TransactionMapper;
import org.transactions.api.server.model.BankAccount;
import org.transactions.api.server.model.Category;
import org.transactions.api.server.model.Transaction;
import org.transactions.api.server.model.TransactionDetail;
import org.transactions.impl.TransactionService;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BaseContractClass {

    @Mock
    TransactionService service;

    @Mock
    TransactionMapper mapper;

    @BeforeEach
    public void setup() {
        RestAssuredMockMvc.standaloneSetup(new TransactionsController(service, mapper));

        // Create data
        Transaction expectedTransaction = createTransaction();

        // Configure Mock
        when(mapper.transactionToRest(Mockito.any())).thenReturn(expectedTransaction);
    }

    private Transaction createTransaction() {
        var bk = new BankAccount();
        bk.setId(14l);
        bk.setCategory("Commun");
        bk.setLabel("PEE");

        var category = new Category();
        category.setId(11l);
        category.setCategory("Maison");
        category.setLabel("Assurances");
        category.setType(Category.TypeEnum.FIXE);

        var details = new TransactionDetail();
        details.setCategory(category);
        details.setDescription("description");
        details.setBankAccount(bk);
        details.setCost(-127687l);
        details.setCostAbs(127687l);
        details.setIncome(0f);
        details.setOutcome(1276.87f);


        var restTransaction = new Transaction();
        restTransaction.setId("someId");
        restTransaction.setCost(-127687l);
        restTransaction.setCostAbs(127687l);
        restTransaction.setDescription("Some description");
        restTransaction.setDate(OffsetDateTime.of(2026,5,11,14,13,16,0, ZoneOffset.UTC));
        restTransaction.setTransactions(List.of(details));

        return restTransaction;
    }


}
