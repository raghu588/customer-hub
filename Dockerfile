FROM openjdk:17-jdk-alpine
EXPOSE 8080
ADD build/libs/customer-0.0.1-SNAPSHOT.jar customer-hub.jar
ENTRYPOINT ["java","-jar","/customer-hub.jar"]