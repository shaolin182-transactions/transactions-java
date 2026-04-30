package org.transactions.api.mapper;

import org.mapstruct.Mapper;
import org.model.transactions.BankAccount;
import org.model.transactions.Transaction;
import org.model.transactions.TransactionCategory;
import org.model.transactions.TransactionDetails;
import org.transactions.api.server.model.Category;
import org.transactions.api.server.model.TransactionDetail;

@Mapper(componentModel = "spring", uses = CommonMappers.class)
public interface TransactionMapper {

    // Transaction
    Transaction transactionFromRest(org.transactions.api.server.model.Transaction transaction);
    org.transactions.api.server.model.Transaction transactionToRest(Transaction transaction);

    // Bank Account
    BankAccount bankAccountFromRest(org.transactions.api.server.model.BankAccount bankAccount);
    org.transactions.api.server.model.BankAccount bankAccountToRest(BankAccount bankAccount);

    // Category
    TransactionCategory categoryFromRest(Category category);
    Category categoryToRest(TransactionCategory transactionCategory);

    // TransactionDetails
    TransactionDetails detailsFromRest(TransactionDetail transactionDetails);
    TransactionDetail detailsToRest(TransactionDetails transactionDetails);


}
