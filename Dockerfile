#FROM openjdk:22
#EXPOSE 8080
#ADD target/balmburren.jar balmburren.jar
#ENTRYPOINT ["java", "-jar", "/balmburren.jar"]

# FROM maven:3.8.2-jdk-8 # for Java 8
FROM maven:3-alpine

WORKDIR /balmburren
COPY . .
RUN mvn clean install

CMD mvn spring-boot:run
