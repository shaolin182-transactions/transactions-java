package org.transactions.persistence.pg.repositories;

import org.springframework.data.repository.CrudRepository;
import org.transactions.persistence.pg.entities.CategoryEntity;

public interface CategoryRepository extends CrudRepository<CategoryEntity, Integer> {
}
