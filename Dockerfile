FROM openjdk:22
EXPOSE 8080
ADD target/balmburren.jar balmburren.jar
ENTRYPOINT ["java", "-jar", "/balmburren.jar"]