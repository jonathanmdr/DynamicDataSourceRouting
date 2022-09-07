FROM maven:3.8.5-openjdk-17 AS build

COPY . .

RUN mvn --batch-mode package

FROM openjdk:17.0.2

ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v1.17.0/opentelemetry-javaagent.jar .

COPY --from=build /target/multidatasources*.jar /app.jar

COPY docker-entrypoint.sh .

RUN chmod +x /docker-entrypoint.sh

ENTRYPOINT ["/docker-entrypoint.sh"]