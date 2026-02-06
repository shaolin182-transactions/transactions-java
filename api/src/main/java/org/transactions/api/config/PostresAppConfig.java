package org.transactions.api.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@Profile("postgresql")
@EnableJpaRepositories("org.transactions.persistence.pg.repositories")
@EntityScan("org.transactions.persistence.pg.entities")
public class PostresAppConfig {
}
