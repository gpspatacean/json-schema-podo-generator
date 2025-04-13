FROM maven:3.9.9-eclipse-temurin-21 AS BUILDER
LABEL authors="gpspatacean"

WORKDIR /app
COPY pom.xml .
COPY modules modules

RUN mvn clean package -pl modules/webapp -am -B -e -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=BUILDER /app/modules/webapp/target/*.jar /app/app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
