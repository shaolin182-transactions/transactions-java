package org.transactions.connector;

import org.model.transactions.BankAccount;
import org.model.transactions.TransactionCategory;

import java.util.Optional;

public interface ICommonDataDatasource {

    Optional<TransactionCategory> findCategoryById(Integer id);

    Optional<BankAccount> findBankAccountById(Integer id);
}
