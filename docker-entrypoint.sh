#!/bin/sh

set -e

exec java -jar /app.jar \
  && -javaagent:./opentelemetry-javaagent.jar \
  && -Dotel.traces.exporter=jaeger \
  && -Dotel.metrics.exporter=prometheus \
  && -Dotel.exporter.prometheus.port="${PROMETHEUS_PORT}" \
  && -Dotel.exporter.prometheus.host="${PROMETHEUS_HOST}" \
  && -Dotel.exporter.jaeger.endpoint="${JAEGER_ENDPOINT}" \
  && -Dotel.resource.attributes=service.name="${APPLICATION_NAME}"