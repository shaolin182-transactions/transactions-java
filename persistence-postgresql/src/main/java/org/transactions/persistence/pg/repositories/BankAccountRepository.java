package org.transactions.persistence.pg.repositories;

import org.springframework.data.repository.CrudRepository;
import org.transactions.persistence.pg.entities.BankAccountEntity;

public interface BankAccountRepository extends CrudRepository<BankAccountEntity, Integer> {
}
