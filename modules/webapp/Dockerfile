FROM maven:3.9.9-eclipse-temurin-21 AS dependencies
WORKDIR /app
COPY pom.xml .
COPY modules/cli-wrapper/pom.xml modules/cli-wrapper/pom.xml
COPY modules/core/pom.xml modules/core/pom.xml
COPY modules/rest-api/pom.xml modules/rest-api/pom.xml
COPY modules/webapp/pom.xml modules/webapp/pom.xml

RUN mvn dependency:go-offline -B -e -DskipTests -DexcludeArtifactsIds=podo-generator-core

FROM maven:3.9.9-eclipse-temurin-21 AS builder
LABEL authors="gpspatacean"

WORKDIR /app
COPY --from=dependencies /root/.m2 /root/.m2
COPY pom.xml .
COPY modules modules

RUN mvn clean package -pl modules/webapp -am -B -e -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app

RUN useradd -m -s /usr/sbin/nologin appuser
USER appuser

COPY --from=builder /app/modules/webapp/target/*.jar /app/app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
