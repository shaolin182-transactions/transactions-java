package org.transactions.persistence.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.model.transactions.Transaction;

@Repository("mongoTransactionsRepository")
public interface TransactionsRepository extends MongoRepository <Transaction, String> {
}
