FROM maven:3.8.5-openjdk-17 AS build

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src src
RUN mvn package

FROM openjdk:17.0.2 AS release

COPY --from=build /target/*.jar /app.jar

ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar /

COPY docker-entrypoint.sh /

RUN chmod +x /docker-entrypoint.sh

ENTRYPOINT ["/docker-entrypoint.sh"]
