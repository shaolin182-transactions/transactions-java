FROM openjdk:11-jdk

ARG JAR_FILE=api/target/*.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app.jar"]