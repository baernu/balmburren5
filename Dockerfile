
#FROM maven:3-alpine
#
#WORKDIR /balmburren
#COPY . .
#RUN mvn clean install
#
#CMD mvn spring-boot:run


FROM eclipse-temurin:17-jdk-focal

WORKDIR /backend

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline

COPY src ./src

CMD ["./mvnw", "spring-boot:run"]

EXPOSE 8082