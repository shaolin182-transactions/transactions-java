package org.transactions.persistence.pg.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.transactions.persistence.pg.entities.TransactionEntity;

import java.util.UUID;

@Repository("pgTransactionsRepository")
public interface TransactionsRepository extends CrudRepository<TransactionEntity, UUID> {
}
