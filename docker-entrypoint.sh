#!/bin/sh

set -e

# Environment variables for development with docker
export OTEL_METRICS_EXPORTER=otlp
export OTEL_EXPORTER_OTLP_METRICS_COMPRESSION=gzip
export OTEL_EXPORTER_OTLP_METRICS_ENDPOINT=http://host.docker.internal:4317
export OTEL_TRACES_EXPORTER=otlp
export OTEL_EXPORTER_OTLP_TRACES_COMPRESSION=gzip
export OTEL_EXPORTER_OTLP_TRACES_ENDPOINT=http://host.docker.internal:4317
export OTEL_LOGS_EXPORTER=none
export OTEL_SERVICE_NAME=billionaire-api

exec java -Dspring.profiles.active=docker -javaagent:opentelemetry-javaagent.jar -jar app.jar
