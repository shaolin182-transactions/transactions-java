package org.transactions.persistence.pg.repositories;

import org.springframework.data.repository.CrudRepository;
import org.transactions.persistence.pg.entities.TransactionEntity;

import java.util.UUID;

public interface TransactionsRepository extends CrudRepository<TransactionEntity, UUID> {
}
