FROM openjdk:21-slim
COPY ./target/*.jar /app/tariffs-service.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","/app/tariffs-service.jar"]