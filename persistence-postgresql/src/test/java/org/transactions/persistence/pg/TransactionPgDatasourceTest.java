package org.transactions.persistence.pg;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.model.transactions.BankAccount;
import org.model.transactions.Transaction;
import org.model.transactions.builder.TransactionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import org.transactions.persistence.pg.repositories.BankAccountRepository;
import org.transactions.persistence.pg.repositories.CategoryRepository;
import org.transactions.persistence.pg.repositories.SubTransactionRepository;
import org.transactions.persistence.pg.repositories.TransactionsRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.model.transactions.TransactionCategoryType.COURANTE;

@Testcontainers
@SpringBootTest
@ActiveProfiles("postgresql")
@ExtendWith(SpringExtension.class)
class TransactionPgDatasourceTest {

    @Container
    static final PostgreSQLContainer postgresContainer = new PostgreSQLContainer(DockerImageName.parse("postgres:18-alpine"))
            .withDatabaseName("transactionsdb-test")
            .withUsername("username")
            .withPassword("password");

    @Autowired
    private TransactionPgDatasource transactionPgDatasource;

    @Autowired
    private BankAccountRepository bkRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubTransactionRepository subTransactionRepository;

    @Autowired
    private TransactionsRepository transactionsRepository;

    @DynamicPropertySource
    static void registerResourceServerIssuerProperty(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @BeforeAll
    static void startDb() {
        postgresContainer.start();
    }

    @AfterAll
    static void stopDb() {
        postgresContainer.stop();
    }

    @Test
    @Transactional
    void checkPostgresIntegration() {
        // At the beginning, the database should be empty, so we expect no data
        assertEquals(0, bkRepository.count());
        assertEquals(0, categoryRepository.count());
        assertEquals(0, subTransactionRepository.count());
        assertEquals(0, transactionsRepository.count());

        var transactions = transactionPgDatasource.getAllTransactions();
        assertEquals(0, transactions.size());

        // We save a transaction and then we check that the data is correctly saved in the database
        var savedTransaction = transactionPgDatasource.saveTransactions(transactionPEE());
        var id = savedTransaction.getId();
        assertNotNull(id);
        assertEquals(1, bkRepository.count());
        assertEquals(1, categoryRepository.count());
        assertEquals(1, subTransactionRepository.count());
        assertEquals(1, transactionsRepository.count());

        // We save another transaction with the same category and bank account, then we check that no new category and bank account are created
        transactionPgDatasource.saveTransactions(transactionPEE());
        // We save another transaction with the same category but different bank account, then we check that no new category is created but a new bank account is created
        transactionPgDatasource.saveTransactions(transactionCommun());

        assertEquals(2, bkRepository.count());
        assertEquals(1, categoryRepository.count());
        assertEquals(3, subTransactionRepository.count());
        assertEquals(3, transactionsRepository.count());

        // We update the bank account of the first transaction, then we check that the bank account is updated and no new bank account is created
        savedTransaction.getTransactions().get(0).setBankAccount(new BankAccount.BankAccountBuilder().withCategory("PEE").withId(5).withLabel("BL Label").build());
        transactionPgDatasource.saveTransactions(savedTransaction);

        assertEquals(3, bkRepository.count());
        assertEquals(1, categoryRepository.count());
        assertEquals(3, subTransactionRepository.count());
        assertEquals(3, transactionsRepository.count());

        // We check that we can retrieve the transaction by id and that the retrieved transaction is the same as the saved one
        var tr = transactionPgDatasource.getTransaction(id).get();
        assertEquals(id, tr.getId());
        assertEquals(15456, tr.getCost());
        assertEquals(4, tr.getTransactions().get(0).getCategory().getId());
        assertEquals(5, tr.getTransactions().get(0).getBankAccount().getId());
        assertEquals(0, tr.getTransactions().get(0).getIncome());
        assertEquals(5.5f, tr.getTransactions().get(0).getOutcome());

        transactionPgDatasource.deleteTransactions(id);
        var result = transactionPgDatasource.getTransaction(id);
        Assertions.assertFalse(result.isPresent());

        assertEquals(3, bkRepository.count());
        assertEquals(1, categoryRepository.count());
        assertEquals(2, subTransactionRepository.count());
        assertEquals(2, transactionsRepository.count());
    }

    private Transaction transactionPEE() {
        return new TransactionBuilder()
            .withCost(Long.valueOf(15456))
            .addTransactions()
                .addTransaction()
                    .withCategory().withLabel("Cat1").withId(4).withCategory("ACat").withType(COURANTE).done()
                    .withBankAccount().withCategory("PEE").withId(1).withLabel("PEE_label").done()
                    .withDescription("desc").withIncome(Float.valueOf(0)).withOutcome(Float.valueOf(5.5f)).done()
            .done()
            .build();
    }

    private Transaction transactionCommun() {
        return new TransactionBuilder()
            .withCost(Long.valueOf(15456))
            .addTransactions()
                .addTransaction()
                    .withCategory().withLabel("Cat1").withId(4).withCategory("ACat").withType(COURANTE).done()
                    .withBankAccount().withCategory("Commun").withId(2).withLabel("CMB").done()
                    .withDescription("desc").withIncome(Float.valueOf(0)).withOutcome(Float.valueOf(6.5f)).done()
                .done()
            .build();
    }


}