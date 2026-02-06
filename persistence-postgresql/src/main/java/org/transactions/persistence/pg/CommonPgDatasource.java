package org.transactions.persistence.pg;

import org.model.transactions.BankAccount;
import org.model.transactions.TransactionCategory;
import org.model.transactions.TransactionCategoryType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.transactions.connector.ICommonDataDatasource;
import org.transactions.persistence.pg.repositories.BankAccountRepository;
import org.transactions.persistence.pg.repositories.CategoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Component
@Profile("postgresql")
public class CommonPgDatasource implements ICommonDataDatasource {

    private List<TransactionCategory> categories;

    private List<BankAccount> bankAccounts;

    private final BankAccountRepository bkRepository;
    private final CategoryRepository catRepository;

    @Autowired
    public CommonPgDatasource(BankAccountRepository bankAccountRepository, CategoryRepository categoryRepository){
        this.bkRepository = bankAccountRepository;
        this.catRepository = categoryRepository;
    }

    @Override
    public Optional<TransactionCategory> findCategoryById(Integer id) {

        if (CollectionUtils.isEmpty(categories)){
            refresh();
        }
        return categories.stream()
            .filter(item -> id.equals(item.getId()))
            .findFirst();
    }

    @Override
    public Optional<BankAccount> findBankAccountById(Integer id) {
        if (CollectionUtils.isEmpty(bankAccounts)){
            refresh();
        }

        return bankAccounts.stream()
                .filter(item -> id.equals(item.getId()))
                .findFirst();
    }

    @Override
    public void refresh() {
        categories = StreamSupport.stream(catRepository.findAll().spliterator(), false)
                .map(entity -> new TransactionCategory.TransactionCategoryBuilder().withCategory(entity.getCategory()).withId(entity.getId()).withType(TransactionCategoryType.valueOf(entity.getType())).withLabel(entity.getLabel()).build())
                .toList();

        bankAccounts = StreamSupport.stream(bkRepository.findAll().spliterator(), false)
                .map(entity -> new BankAccount.BankAccountBuilder().withCategory(entity.getCategory()).withId(entity.getId()).withLabel(entity.getLabel()).build())
                .toList();;
    }
}
