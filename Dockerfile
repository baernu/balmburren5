
#FROM maven:3-alpine
#
#WORKDIR /balmburren
#COPY . .
#RUN mvn clean install
#
#CMD mvn spring-boot:run


FROM eclipse-temurin:17-jdk-focal

WORKDIR /balmburren

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline

COPY src ./src

CMD ["./mvnw", "spring-boot:run"]



EXPOSE 8082



# The base image on which we would build our image


#FROM openjdk:11-jdk-slim as build
#WORKDIR /workspace/app
#
#COPY mvnw .
#COPY .mvn .mvn
#COPY pom.xml .
#COPY src src
#
#RUN ./mvnw install -DskipTests
#
#FROM openjdk:11-jdk-slim
#VOLUME /tmp
#ARG JAR_FILE=/workspace/app/target/*.jar
#COPY --from=build ${JAR_FILE} app.jar
#ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
