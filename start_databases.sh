#!/bin/bash

docker container stop master-db || true && \
docker container rm master-db || true && \
docker container run --name master-db \
 -e MYSQL_ROOT_PASSWORD=root \
 -e MYSQL_DATABASE=billionaires \
 -p 3306:3306 \
 -v "$PWD/data.sql":/docker-entrypoint-initdb.d/dump.sql \
 -d mysql:8.0.25 --character-set-server=utf8 --collation-server=utf8_unicode_ci

docker container stop slave-db || true && \
docker container rm slave-db || true && \
docker container run --name slave-db \
 -e MYSQL_ROOT_PASSWORD=root \
 -e MYSQL_DATABASE=billionaires \
 -p 3307:3306 \
 -v "$PWD/data.sql":/docker-entrypoint-initdb.d/dump.sql \
 -d mysql:8.0.25 --character-set-server=utf8 --collation-server=utf8_unicode_ci