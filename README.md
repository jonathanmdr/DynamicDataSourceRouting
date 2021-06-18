# RoutingDataSource        [![CI](https://github.com/jonathanmdr/RoutingDataSource/actions/workflows/maven.yml/badge.svg)](https://github.com/jonathanmdr/RoutingDataSource/actions/workflows/maven.yml)
Exemplo de uma API Rest desenvolvida em Java com Spring, o intuíto é apresentar uma solução para trabalhar com múltiplas fontes de dados, neste exemplo trabalharemos com apenas duas, sendo uma base de dados principal e outra base de dados réplica.
</br>
A ideia é fazer com que a aplicação direcione as transações `READ_ONLY` para a base de dados réplica e que as transações `READ_WRITE` sejam direcionadas para a base de dados principal.
</br>
Este modelo arquitetural consiste em diminuir a concorrência de transações da API com o banco de dados, isso nos permite alguns ganhos em ambientes de alta escala.
</br>
Este mesmo modelo com algumas pequenas mudanças também nos permite trabalharmos com um modelo Multitenancy baseando-se no conceito de que cada cliente que se comunica com a API possuí uma base de dados única, podendo ainda incrementar o uso de uma base de dados principal juntamente com uma réplica para cada cliente.
</br>

[![node](https://img.shields.io/badge/AdoptOpenJDK-11.0.11+9-red.svg)](https://adoptopenjdk.net/)
[![node](https://img.shields.io/badge/Spring_Boot-2.5.1-green.svg)](https://spring.io/)
[![node](https://img.shields.io/badge/MySQL-8.0.25-blue.svg)](https://www.mysql.com/)


## Arquitetura
[![node](https://github.com/jonathanmdr/RoutingDataSource/blob/master/docs/replication-databases.png)](https://github.com/jonathanmdr/RoutingDataSource/blob/master)

## Inicializando as bases de dados
> :warning: É necessário ter instalado `docker` e `docker-compose`.

#### Execute os comandos abaixo:

Inicializar:
```sh
~$ docker-compose up
```
Parar:
```sh
~$ docker-compose down
```
