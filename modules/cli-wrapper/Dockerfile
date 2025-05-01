FROM maven:3.9.9-eclipse-temurin-21 AS DEPENDENCIES
WORKDIR /app
COPY pom.xml .
COPY modules/cli-wrapper/pom.xml modules/cli-wrapper/pom.xml
COPY modules/core/pom.xml modules/core/pom.xml
COPY modules/rest-api/pom.xml modules/rest-api/pom.xml
COPY modules/webapp/pom.xml modules/webapp/pom.xml

RUN mvn dependency:go-offline -B -e -DskipTests -DexcludeArtifactsIds=podo-generator-core

FROM maven:3.9.9-eclipse-temurin-21 AS BUILDER
LABEL authors="gpspatacean"

WORKDIR /app
COPY --from=DEPENDENCIES /root/.m2 /root/.m2
COPY pom.xml .
COPY modules modules

RUN mvn clean package -pl modules/cli-wrapper -am -B -e -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=BUILDER /app/modules/cli-wrapper/target/*.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
