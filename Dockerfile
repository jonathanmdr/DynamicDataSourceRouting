FROM maven:3.9.11-eclipse-temurin-25-alpine AS build

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src src
RUN mvn package -DskipTests

FROM eclipse-temurin:25-jre-alpine-3.22 AS release

ARG OTEL_AGENT_VERSION=v1.58.0

WORKDIR /

COPY --from=build /target/*.jar app.jar
COPY docker-entrypoint.sh .
COPY .otel/otel.jar .

RUN addgroup -S billionaire && \
    adduser -S -G billionaire billionaire && \
    chmod +x docker-entrypoint.sh && \
    rm -rf /var/cache/apk/* /tmp/* /var/tmp/*

USER billionaire:billionaire

EXPOSE 8080

ENTRYPOINT ["/docker-entrypoint.sh"]
