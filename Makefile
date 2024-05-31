.PHONY: clean install test version up down restart upgrade_otel_agent

clean:
	@mvn clean

install:
	@mvn clean install

test:
	@mvn clean test

build:
	@VERSION=$$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout) && \
	docker build . -t "billionaire-api:$$VERSION" --file Dockerfile

version:
	@mvn versions:set -DgenerateBackupPoms=false -DnewVersion=$1

up:
	@docker-compose up -d

down:
	@docker-compose down --remove-orphans --volumes

restart:
	@docker-compose restart

upgrade_otel_agent:
	@echo "Download the OTEL Java Agent..."
	@mkdir -p agents
	@curl -o agents/opentelemetry-javaagent.jar -L https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v1.33.3/opentelemetry-javaagent.jar
	@rm -rf agents/version.txt