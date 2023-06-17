# Dynamic Data Source Routing

[![CI](https://github.com/jonathanmdr/RoutingDataSource/actions/workflows/maven.yml/badge.svg)](https://github.com/jonathanmdr/RoutingDataSource/actions/workflows/maven.yml)
[![CodeFactor](https://www.codefactor.io/repository/github/jonathanmdr/dynamicdatasourcerouting/badge)](https://www.codefactor.io/repository/github/jonathanmdr/dynamicdatasourcerouting)

Exemplo de uma API REST desenvolvida em Java com Spring, o intuito é apresentar uma solução arquitetural para dimensionar operações de leitura com banco de dados, neste exemplo trabalharemos com duas bases de dados, uma base de dados principal e uma réplica.
</br>
</br>
A ideia é fazer com que a aplicação direcione as transações definidas como `READ_ONLY` para a base de dados réplica e que as transações `READ_WRITE` sejam direcionadas para a base de dados principal.
</br>
</br>
As imagens e configurações contidas no arquivo `docker-compose.yaml` são específicas para lidar com a replicação de dados.
</br>
</br>
Esta arquitetura consiste em diminuir a concorrência entre transações da API com o banco de dados, isso nos permite alguns ganhos em ambientes de alta escala dado que podemos tranquilamente escalar horizontalmente mais nós de réplica quando necessário.
</br>
</br>

[![node](https://img.shields.io/badge/AdoptOpenJDK-17-red.svg)](https://adoptopenjdk.net/)
[![node](https://img.shields.io/badge/Spring_Boot-3.1.0-green.svg)](https://spring.io/)
[![node](https://img.shields.io/badge/MySQL-8.0.28-blue.svg)](https://www.mysql.com/)


## Arquitetura
[![node](https://github.com/jonathanmdr/RoutingDataSource/blob/master/docs/replication-databases.png)](https://github.com/jonathanmdr/RoutingDataSource/blob/master/docs/replication-databases.png)

## Documentação
> :information_source: Após inicializar a aplicação, acesse a documentação clicando neste [link](http://localhost:8080/).

## Inicializando as bases de dados
> :warning: É necessário ter instalado `docker` e `docker-compose`.

#### Execute os comandos abaixo:

Inicializar:
```sh
make up
```
Parar:
```sh
make down
```
## OpenTelemetry
> O OpenTelemetry foi incluso no projeto utilizando a estratégia de coletores como agent.

Variáveis de ambiente:
```shell
OTEL_TRACES_EXPORTER=jaeger
OTEL_EXPORTER_OTLP_TRACES_ENDPOINT=http://localhost:14250
OTEL_METRICS_EXPORTER=none
OTEL_SERVICE_NAME=billionaire-api
```

Argumentos de VM:
```shell
-javaagent:./agents/otel/opentelemetry-javaagent.jar
```