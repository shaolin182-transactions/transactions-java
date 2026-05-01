package org.transactions.persistence.pg;

import org.model.transactions.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.transactions.connector.ITransactionDataSource;
import org.transactions.connector.ITransactionsReadOnlyDatasource;
import org.transactions.persistence.pg.converters.TransactionConverter;
import org.transactions.persistence.pg.converters.TransactionEntityConverter;
import org.transactions.persistence.pg.entities.BankAccountEntity;
import org.transactions.persistence.pg.entities.CategoryEntity;
import org.transactions.persistence.pg.entities.SubTransactionEntity;
import org.transactions.persistence.pg.repositories.BankAccountRepository;
import org.transactions.persistence.pg.repositories.CategoryRepository;
import org.transactions.persistence.pg.repositories.TransactionsRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Component("postgresDatasource")
@Profile("postgresql")
public class TransactionPgDatasource implements ITransactionDataSource, ITransactionsReadOnlyDatasource  {

    private final TransactionsRepository repository;
    private final CategoryRepository categoryRepository;
    private final BankAccountRepository bkRepository;
    private final TransactionEntityConverter transactionEntityConverter;
    private final TransactionConverter transactionConverter;

    private List<CategoryEntity> categories;

    private List<BankAccountEntity> bankAccounts;

    @Autowired
    public TransactionPgDatasource(TransactionsRepository repository, CategoryRepository categoryRepository, BankAccountRepository bkRepository, TransactionEntityConverter transactionEntityConverter, TransactionConverter transactionConverter){
        this.repository = repository;
        this.categoryRepository = categoryRepository;
        this.bkRepository = bkRepository;
        this.transactionEntityConverter = transactionEntityConverter;
        this.transactionConverter = transactionConverter;
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
            .map(transactionEntityConverter::convert)
            .toList();
    }

    @Override
    public Optional<Transaction> getTransaction(String id) {
        var result = repository.findById(UUID.fromString(id));
        if (result.isPresent()){
            return Optional.of(transactionEntityConverter.convert(result.get()));
        }
        return Optional.empty();
    }

    @Override
    public void deleteTransactions(String id) {
        Assert.hasText(id, "id must not be null or empty");

        repository.deleteById(UUID.fromString(id));
    }

    @Override
    public Transaction saveTransactions(Transaction aTransaction) {
        var entity = transactionConverter.convert(aTransaction);

        // For each sub transaction entity, we check that bank account and category exist
        for (SubTransactionEntity details : entity.getTransactionsDetails()) {

            if (details.getCategory() != null){
                var cat = findCategoryById(details.getCategory().getId());
                if (cat.isPresent()){
                    details.setCategory(cat.get());
                }
            }

            if (details.getBankAccount() != null) {
                var bk = findBankAccountById(details.getBankAccount().getId());
                if (bk.isPresent()) {
                    details.setBankAccount(bk.get());
                }
            }
        }

        var result = repository.save(entity);
        if (result != null) {
            return transactionEntityConverter.convert(result);
        }
        return null;
    }

    private Optional<CategoryEntity> findCategoryById(Integer id) {
        categories = StreamSupport.stream(categoryRepository.findAll().spliterator(), false).toList();

        return categories.stream()
                .filter(item -> id.equals(item.getId()))
                .findFirst();
    }

    private Optional<BankAccountEntity> findBankAccountById(Integer id) {
        bankAccounts = StreamSupport.stream(bkRepository.findAll().spliterator(), false).toList();
        return bankAccounts.stream()
                .filter(item -> id.equals(item.getId()))
                .findFirst();
    }
}
