# pseudo-schedulator

Сервис, который может быть использован в качестве планировщика расписания для инициализации 3DS Preparation Flow. 
Описание файла [`docker-compose.yml`](https://github.com/rbkmoney/three-ds-server-compose/blob/master/pseudo-schedulator/docker-compose.yml) для использования данного модуля в качестве примера планировщика расписания для инициализации 3DS Preparation Flow.

## Использование

```bash
docker-compose up -d
```

Вся сборка и запуск сервиса производится внутри докера, локально (`maven`, `openjdk11` и тд) ничего дополнительно устанавливать не нужно. Сборка описана в Dockerfile

Для использования сервиса необходимо собрать `docker image` с помощью `Dockerfile`, который расположен в корневой директории сервиса. Сам `build` сервиса проводится в `docker container` (см. `/three-ds-server-compose/ds-simple-mock/Dockerfile`), поэтому на локальном хосте не обязательно устанавливать необходимые зависимости для сервиса 

