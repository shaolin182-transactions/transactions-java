package org.transactions.persistence.pg.repositories;

import org.springframework.data.repository.CrudRepository;
import org.transactions.persistence.pg.entities.SubTransactionEntity;

import java.util.UUID;

public interface SubTransactionRepository extends CrudRepository<SubTransactionEntity, UUID> {
}
