package org.transactions.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(scanBasePackages="org.transactions")
@EnableMongoRepositories("org.transactions.persistence.repositories")
@PropertySource("classpath:application.properties")
@PropertySource({"classpath:application-${envTarget:dev}.properties"})
public class ServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}
