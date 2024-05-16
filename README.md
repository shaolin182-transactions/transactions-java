# transactions-java

![](https://github.com/shaolin182/transactions-java/workflows/build/badge.svg)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=org.transactions%3Atransactions-java-server&metric=alert_status)](https://sonarcloud.io/dashboard?id=org.transactions%3Atransactions-java-server)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=org.transactions%3Atransactions-java-server&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=org.transactions%3Atransactions-java-server)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=org.transactions%3Atransactions-java-server&metric=coverage)](https://sonarcloud.io/dashboard?id=org.transactions%3Atransactions-java-server)
![](https://github.com/shaolin182/transactions-java/workflows/deploy/badge.svg)

## Env

### Dev

Env for developing 'transactions-java' module
- Use profile "dev"
- Use configuration from 'src/main/application.yaml' file
- No security on API
- Requires MongoDb Database

```
# From transactions-deployment directory
# Run Mongodb database
docker compose -f docker-compose-dev.yml up -d

# From transactions-java/api directory
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

With 'dev' profile, bean related to security is disabled

## Running in local

Env for testing 'transactions-java' module
- Use profile "local"
- Use configuration from configserver

```
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

## Running in staging

Test all components through docker
- Use profile "local"
- Use configuration from configserver

