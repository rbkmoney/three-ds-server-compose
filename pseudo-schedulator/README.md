# pseudo-schedulator

[Сервис](https://github.com/rbkmoney/three-ds-server-compose/blob/master/pseudo-schedulator/pom.xml), который может быть использован в качестве планировщика расписания для инициализации `3DS Preparation Flow` в рамках демонстрации работы [RBK.money 3D Secure Server](https://github.com/rbkmoney/three-ds-server)

## Использование

```bash
docker-compose up -d
```

Cборка и запуск [сервиса](https://github.com/rbkmoney/three-ds-server-compose/blob/master/pseudo-schedulator/pom.xml) полностью автоматизированы и производится внутри докера, дополнительно локально (`maven`, `openjdk11` и тд) ничего устанавливать не нужно. Сборка [сервиса](https://github.com/rbkmoney/three-ds-server-compose/blob/master/pseudo-schedulator/pom.xml) описана в [Dockerfile](https://github.com/rbkmoney/three-ds-server-compose/blob/master/pseudo-schedulator/Dockerfile), запуск описан в [`docker-compose.yml`](https://github.com/rbkmoney/three-ds-server-compose/blob/master/pseudo-schedulator/docker-compose.yml)

**Обращаем внимание**, для корректного демо [RBK.money 3D Secure Server](https://github.com/rbkmoney/three-ds-server) порядок запуска:

1. [`docker-compose.yml`](https://github.com/rbkmoney/three-ds-server-compose/blob/master/docker-compose.yml) [three-ds-server macroservice](https://github.com/rbkmoney/three-ds-server-compose) (сам макросервис 3DSS)
2. [`docker-compose.yml`](https://github.com/rbkmoney/three-ds-server-compose/blob/master/ds-simple-mock/docker-compose.yml) [ds-simple-mock](https://github.com/rbkmoney/three-ds-server-compose/tree/master/ds-simple-mock) (простой сервис, который может быть использован в качестве заглушки для обработки запросов в DS от [3DSS](https://github.com/rbkmoney/three-ds-server))
4. [`docker-compose.yml`](https://github.com/rbkmoney/three-ds-server-compose/blob/master/pseudo-schedulator/docker-compose.yml) [pseudo-schedulator](https://github.com/rbkmoney/three-ds-server-compose/tree/master/pseudo-schedulator) (простой сервис, который может быть использован в качестве заглушки для обработки `PREQ/PRES flow` в [3DSS](https://github.com/rbkmoney/three-ds-server))

**При работе [pseudo-schedulator](https://github.com/rbkmoney/three-ds-server-compose/tree/master/pseudo-schedulator) могут вознить ошибки**, если для запросов от [сервиса](https://github.com/rbkmoney/three-ds-server-compose/blob/master/pseudo-schedulator/pom.xml) нет потребителя, то есть нет работающего [макросервиса 3DSS](https://github.com/rbkmoney/three-ds-server-compose) и [pseudo-schedulator](https://github.com/rbkmoney/three-ds-server-compose/tree/master/pseudo-schedulator) не куда стучаться с запросами

## Описание



## Конфигурация





