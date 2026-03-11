package org.transactions.persistence;

import org.model.transactions.BankAccount;
import org.model.transactions.TransactionCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.transactions.connector.ICommonDataDatasource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class CommonDataDatasource implements ICommonDataDatasource {

    private MongoTemplate mongoTemplate;

    private List<TransactionCategory> categories;

    private List<BankAccount> bankAccounts;

    @Autowired
    public CommonDataDatasource(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Optional<TransactionCategory> findCategoryById(Integer id) {
        if (CollectionUtils.isEmpty(categories)){
            getAllCategories();
        }

        return categories.stream()
                .filter(item -> id.equals(item.getId()))
                .findFirst();
    }

    @Override
    public Optional<BankAccount> findBankAccountById(Integer id) {
        if (CollectionUtils.isEmpty(bankAccounts)){
            getAllBankAccount();
        }

        return bankAccounts.stream()
                .filter(item -> id.equals(item.getId()))
                .findFirst();
    }

    private void getAllCategories() {
        categories = mongoTemplate.getCollection("transaction")
                .distinct("transactions.category", TransactionCategory.class)
                .into(new ArrayList<>());
    }

    private void getAllBankAccount() {
        bankAccounts =  mongoTemplate.getCollection("transaction")
                .distinct("transactions.bankAccount", BankAccount.class)
                .into(new ArrayList<>());
    }
}
