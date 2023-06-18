#!/bin/sh

set -e

# Environment variables for development with docker
export OTEL_TRACES_EXPORTER=jaeger
export OTEL_EXPORTER_OTLP_TRACES_ENDPOINT=http://host.docker.internal:14250
export OTEL_METRICS_EXPORTER=none
export OTEL_SERVICE_NAME=billionaire-api

exec java -Dspring.profiles.active=docker -javaagent:opentelemetry-javaagent.jar -jar app.jar