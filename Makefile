.PHONY: clean install test version up down restart otel-agent

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

otel-agent:
	@echo "Update local version for OTEL Java Agent..."
	@mkdir -p .otel
	@curl -o .otel/otel.jar -L https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar
	@curl --progress-bar -sL https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases | grep -oE 'releases/tag/v[0-9]+\.[0-9]+\.[0-9]+' | cut -d'/' -f3 | sort -V | tail -n 1 > .otel/version.txt
	@VERSION=$$(cat .otel/version.txt) && echo "OTEL Java Agent local was updated to $$VERSION"
	@rm -rf .otel/version.txt