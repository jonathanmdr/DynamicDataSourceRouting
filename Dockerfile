FROM maven:3.9.4-amazoncorretto-21 AS build

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src src
RUN mvn package -DskipTests

FROM azul/zulu-openjdk:21 AS release

ARG OTEL_AGENT_VERSION=v1.33.3

WORKDIR /

COPY --from=build /target/*.jar app.jar

COPY docker-entrypoint.sh .

RUN useradd billionaire && \
    chmod +x docker-entrypoint.sh && \
    apt-get update && \
    apt-get install -y curl && \
    curl -fsSL -o opentelemetry-javaagent.jar https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/${OTEL_AGENT_VERSION}/opentelemetry-javaagent.jar && \
    apt-get purge -y --auto-remove curl && \
    rm -rf /var/lib/apt/lists/*

USER billionaire

EXPOSE 8080

ENTRYPOINT ["/docker-entrypoint.sh"]
