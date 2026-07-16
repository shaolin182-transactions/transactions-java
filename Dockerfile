FROM maven:3.9.2-eclipse-temurin-17-alpine@sha256:a60d968f8143fe4662365b07a69e86b39c98e0130a80a32cfaec5fabd9056d6d AS build
RUN mkdir -p /workspace
WORKDIR /workspace
COPY . /workspace
RUN mvn -B -f pom.xml clean package -DskipTests

FROM eclipse-temurin:17-alpine@sha256:638937c54b6d63f0973a20501973e7c433a36b1f22262bd2b25afa7be5ff8c4a

COPY --from=build /workspace/api/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-Dlogging.config=/etc/config/log4j2.yaml", "-jar", "/app.jar", "----spring.config.location=/etc/config/application.yaml"]