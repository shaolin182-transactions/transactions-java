package org.transactions.persistence.pg;

import org.apache.commons.lang3.StringUtils;
import org.model.transactions.BankAccount;
import org.model.transactions.TransactionCategory;
import org.model.transactions.TransactionCategoryType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.transactions.connector.ICommonDataDatasource;
import org.transactions.persistence.pg.entities.BankAccountEntity;
import org.transactions.persistence.pg.entities.CategoryEntity;
import org.transactions.persistence.pg.repositories.BankAccountRepository;
import org.transactions.persistence.pg.repositories.CategoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Component("posgresCommonDatasource")
@Profile("postgresql")
public class CommonPgDatasource implements ICommonDataDatasource {

    private static final Logger log = LoggerFactory.getLogger(CommonPgDatasource.class);

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
    public void saveCategory(TransactionCategory category) {

        if (category == null){
            return;
        }

        if (findCategoryById(category.getId()).isEmpty()){
            var categoryEntity = new CategoryEntity();
            categoryEntity.setId(category.getId());
            categoryEntity.setCategory(category.getCategory());
            categoryEntity.setLabel(category.getLabel());
            if (category.getType() != null){
                categoryEntity.setType(category.getType().name());
            }
            catRepository.save(categoryEntity);

            refresh();
        }
    }

    @Override
    public void saveBankAccount(BankAccount bankAccount) {

        if (findBankAccountById(bankAccount.getId()).isEmpty()){
            var bankAccountEntity = new BankAccountEntity();
            bankAccountEntity.setId(bankAccount.getId());
            bankAccountEntity.setCategory(bankAccount.getCategory());
            bankAccountEntity.setLabel(bankAccount.getLabel());
            bkRepository.save(bankAccountEntity);

            refresh();
        }
    }

    @Override
    public void refresh() {
        categories = StreamSupport.stream(catRepository.findAll().spliterator(), false)
                .map(entity -> buildCategory(entity))
                .toList();

        bankAccounts = StreamSupport.stream(bkRepository.findAll().spliterator(), false)
                .map(entity -> new BankAccount.BankAccountBuilder().withCategory(entity.getCategory()).withId(entity.getId()).withLabel(entity.getLabel()).build())
                .toList();;
    }

    private static TransactionCategory buildCategory(CategoryEntity entity) {
        var builder = new TransactionCategory.TransactionCategoryBuilder();

        builder
                .withCategory(entity.getCategory())
                .withId(entity.getId())
                .withLabel(entity.getLabel());

        if (StringUtils.isNotEmpty(entity.getType())){
            try {
                builder.withType(TransactionCategoryType.valueOf(entity.getType()));
            } catch (IllegalArgumentException e) {
                log.atWarn().setCause(e).log("Invalid category type {} for category id {}, category will be ignored", entity.getType(), entity.getId());
            }
        }

        return builder.build();
    }
}
