FROM maven:3.9.9-eclipse-temurin-21 AS BUILDER
LABEL authors="gpspatacean"

WORKDIR /app
COPY pom.xml .
COPY modules modules

RUN mvn clean package -pl modules/rest-api -am -B -e -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=BUILDER /app/modules/rest-api/target/*.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
