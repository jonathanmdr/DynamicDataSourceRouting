# Dynamic Data Source Routing        [![CI](https://github.com/jonathanmdr/RoutingDataSource/actions/workflows/maven.yml/badge.svg)](https://github.com/jonathanmdr/RoutingDataSource/actions/workflows/maven.yml)
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
~$ docker-compose up -d
```
Parar:
```sh
~$ docker-compose down
```
