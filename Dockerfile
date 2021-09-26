FROM maven:3.6.3-jdk-11-slim AS build
RUN mkdir -p /workspace
WORKDIR /workspace
COPY . /workspace
RUN mvn -B -f pom.xml clean package -DskipTests

FROM openjdk:11-jdk

ENV TARGET_ENV=dev
ENV CONFIG_SERVER=http://localhost:8888

COPY --from=build /workspace/api/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar", "--spring.profiles.active=${TARGET_ENV}", "--spring.cloud.config.uri=${CONFIG_SERVER}"]