.PHONY: install test up down upgrade_otel_agent

install:
	@mvn clean install

test:
	@mvn clean test

up:
	@docker-compose up -d

down:
	@docker-compose down --remove-orphans --volumes

upgrade_otel_agent:
	@echo "Update local version for OTEL Java Agent..."
	@mkdir -p agents
	@curl -o agents/otel/opentelemetry-javaagent.jar -L https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar
	@curl -sL https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases | grep -oE 'releases/tag/v[0-9]+\.[0-9]+\.[0-9]+' | cut -d'/' -f3 | sort -V | tail -n 1 > agents/otel/version.txt
	@VERSION=$$(cat agents/otel/version.txt) && echo "OTEL Java Agent local was updated to $$VERSION"
	@rm -rf agents/otel/version.txt