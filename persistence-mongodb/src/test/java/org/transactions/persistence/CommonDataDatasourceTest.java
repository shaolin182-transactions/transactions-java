package org.transactions.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.model.transactions.BankAccount;
import org.model.transactions.BankAccount.BankAccountBuilder;
import org.model.transactions.Transaction;
import org.model.transactions.TransactionCategory;
import org.model.transactions.TransactionCategory.TransactionCategoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.transactions.TestApplication;
import org.transactions.persistence.config.MongoConfig;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import static org.model.transactions.TransactionCategoryType.COURANTE;
import static org.model.transactions.TransactionCategoryType.EXTRA;

@ExtendWith({SpringExtension.class})
@DataMongoTest
@ContextConfiguration(classes = {TestApplication.class, MongoConfig.class})
class CommonDataDatasourceTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    private static ResourceLoader resourceLoader = new DefaultResourceLoader();

    private static ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void insertSomeData() throws IOException {

        Resource resource = resourceLoader.getResource("classpath:data/sample_file.json");
        List<Transaction> transactions = mapper.readValue(resource.getFile(), mapper.getTypeFactory().constructCollectionType(List.class, Transaction.class));

        transactions.forEach(item -> mongoTemplate.save(item, "transaction"));
    }

    @AfterEach
    void deleteData() {
        mongoTemplate.dropCollection("transaction");
    }

    @ParameterizedTest
    @MethodSource("dataBankAccount")
    void findBankAccountById(Integer bankAccountId, BankAccount expected) {
        BankAccount result = new CommonDataDatasource(mongoTemplate).findBankAccountById(bankAccountId).orElse(null);
        Assertions.assertEquals(expected, result);
    }

    private static Stream<Arguments> dataBankAccount() {
        return Stream.of(
                Arguments.of(21, new BankAccountBuilder().withId(21).withLabel("CMB").withCategory("Commun").build()),
                Arguments.of(2, new BankAccountBuilder().withId(2).withCategory("category 2A").withLabel("label bank account 2A").build()),
                Arguments.of(3, new BankAccountBuilder().withId(3).withCategory("category 3B").withLabel("label bank account 3B").build()),
                Arguments.of(4, new BankAccountBuilder().withId(4).withCategory("category 3C").withLabel("label bank account 3C").build()),
                Arguments.of(1, null));
    }

    @ParameterizedTest
    @MethodSource("dataCategory")
    void findCategoryById(Integer bankAccountId, TransactionCategory expected) {
        TransactionCategory result = new CommonDataDatasource(mongoTemplate).findCategoryById(bankAccountId).orElse(null);
        Assertions.assertEquals(expected, result);
    }

    private static Stream<Arguments> dataCategory() {
        return Stream.of(
                Arguments.of(17, new TransactionCategoryBuilder().withId(17).withCategory("Autres").withLabel("Cadeaux").withType(EXTRA).build()),
                Arguments.of(14, new TransactionCategoryBuilder().withId(14).withCategory("Alimentaire").withLabel("Supermarché").withType(COURANTE).build()),
                Arguments.of(3, new TransactionCategoryBuilder().withId(3).withCategory("category 3C").withLabel("label category 3C").withType(COURANTE).build()),
                Arguments.of(1, null));
    }
}