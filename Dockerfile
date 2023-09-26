FROM maven:3.9.4-amazoncorretto-21 AS build

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src src
RUN mvn package

FROM azul/zulu-openjdk:21 AS release

COPY --from=build /target/*.jar /app.jar

ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v1.30.0/opentelemetry-javaagent.jar /

COPY docker-entrypoint.sh /

RUN chmod +x /docker-entrypoint.sh

ENTRYPOINT ["/docker-entrypoint.sh"]
