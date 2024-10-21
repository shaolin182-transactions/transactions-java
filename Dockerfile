FROM maven:3.9.2-eclipse-temurin-17-alpine AS build
RUN mkdir -p /workspace
WORKDIR /workspace
COPY . /workspace
RUN mvn -B -f pom.xml clean package -DskipTests

FROM eclipse-temurin:17-alpine

COPY --from=build /workspace/api/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar", "----spring.config.location=/etc/config/application.yaml"]