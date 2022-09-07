#!/bin/bash

set -e

exec java -jar /app.jar && \
  -javaagent:/opentelemetry-javaagent.jar && \
  -Dotel.traces.exporter=otlp && \
  -Dotel.resource.attributes=service.name=billionaire-api && \
  -Dotel.exporter.otlp.endpoint=http://localhost:14268 && \
  -Dmaster.datasource.host=master-db && \
  -Dslave.datasource.host=slave-db