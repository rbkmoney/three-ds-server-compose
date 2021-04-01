# pseudo-schedulator

[Сервис](https://github.com/rbkmoney/three-ds-server-compose/blob/master/pseudo-schedulator/pom.xml), который может быть использован в качестве планировщика расписания для инициализации [`3DS Preparation Flow`](https://github.com/rbkmoney/three-ds-server-compose/blob/master/docs/EMVCo_Protocol_and_Core_Functions_Specification_v2.2.0.pdf) в рамках демонстрации работы [`RBK.money 3D Secure Server`](https://github.com/rbkmoney/three-ds-server)

## Использование

```bash
docker-compose up -d
```

Cборка и запуск [сервиса](https://github.com/rbkmoney/three-ds-server-compose/blob/master/pseudo-schedulator/pom.xml) полностью автоматизированы и производится внутри докера, дополнительно локально (`maven`, `openjdk11` и тд) ничего устанавливать не нужно. Сборка [сервиса](https://github.com/rbkmoney/three-ds-server-compose/blob/master/pseudo-schedulator/pom.xml) описана в [`Dockerfile`](https://github.com/rbkmoney/three-ds-server-compose/blob/master/pseudo-schedulator/Dockerfile), запуск описан в [`docker-compose.yml`](https://github.com/rbkmoney/three-ds-server-compose/blob/master/pseudo-schedulator/docker-compose.yml)

**Обращаем внимание**, для корректного демо [RBK.money 3D Secure Server](https://github.com/rbkmoney/three-ds-server) порядок запуска:

1. [`docker-compose.yml`](https://github.com/rbkmoney/three-ds-server-compose/blob/master/docker-compose.yml) [three-ds-server macroservice](https://github.com/rbkmoney/three-ds-server-compose) (сам макросервис 3DSS)
2. [`docker-compose.yml`](https://github.com/rbkmoney/three-ds-server-compose/blob/master/ds-simple-mock/docker-compose.yml) [ds-simple-mock](https://github.com/rbkmoney/three-ds-server-compose/tree/master/ds-simple-mock) (пример сервиса, который может быть использован в качестве заглушки для обработки [`PReq` && `AReq` запросов](https://github.com/rbkmoney/three-ds-server-compose/blob/master/docs/EMVCo_Protocol_and_Core_Functions_Specification_v2.2.0.pdf) в DS от [`макросервиса 3DSS`](https://github.com/rbkmoney/three-ds-server-compose))
3. [`docker-compose.yml`](https://github.com/rbkmoney/three-ds-server-compose/blob/master/pseudo-schedulator/docker-compose.yml) [pseudo-schedulator](https://github.com/rbkmoney/three-ds-server-compose/tree/master/pseudo-schedulator) (пример сервиса, который может быть использован в качестве заглушки для инциализации [`PReq/PRes flow`](https://github.com/rbkmoney/three-ds-server-compose/blob/master/docs/EMVCo_Protocol_and_Core_Functions_Specification_v2.2.0.pdf) в [`макросервис 3DSS`](https://github.com/rbkmoney/three-ds-server-compose))

**При работе [pseudo-schedulator](https://github.com/rbkmoney/three-ds-server-compose/tree/master/pseudo-schedulator) могут вознить ошибки**, если для запросов от [сервиса](https://github.com/rbkmoney/three-ds-server-compose/blob/master/pseudo-schedulator/pom.xml) нет потребителя, то есть нет работающего [`макросервиса 3DSS`](https://github.com/rbkmoney/three-ds-server-compose) и [pseudo-schedulator](https://github.com/rbkmoney/three-ds-server-compose/tree/master/pseudo-schedulator) не куда стучаться с запросами

## Описание

Сервис отправляет запрос по собственному расписанию крона в [`макросервис 3DSS`](https://github.com/rbkmoney/three-ds-server-compose) с целью инициализации [`3DS Preparation Flow`](https://github.com/rbkmoney/three-ds-server-compose/blob/master/docs/EMVCo_Protocol_and_Core_Functions_Specification_v2.2.0.pdf)

Пример 
```
-> Request [POST] http://three-ds-server-storage:8022/three-ds-server-storage/rest/preparation-flow
{
  "providerId": "visa",
  "messageVersion": "2.1.0"
}

<- Response [POST] http://three-ds-server-storage:8022/three-ds-server-storage/rest/preparation-flow
HTTP 200 OK
```

Пример расписания крона
```
0 0 * * * ?
```
в данном случае крон устанавливает отправку запроса каждый час в ```**:00```

## Конфигурация

При необходимости параметры сервиса можно редактировать в [`docker-compose.yml`](https://github.com/rbkmoney/three-ds-server-compose/blob/master/pseudo-schedulator/docker-compose.yml) в [`services.schedulator.environment`](https://github.com/rbkmoney/three-ds-server-compose/blob/master/pseudo-schedulator/docker-compose.yml#L10)

Настройка адреса клиента и таймауты
```json
client.three-ds-server-storage.url: http://three-ds-server-storage:8022/three-ds-server-storage/rest/preparation-flow
client.three-ds-server-storage.read-timeout: 5000
client.three-ds-server-storage.connect-timeout: 5000
```
в данном случае, `http://three-ds-server-storage:8022` адрес контейнера [three-ds-server-storage](https://github.com/rbkmoney/three-ds-server-compose/blob/217511fd6f0c162b69c9d293cc83f4cadded9163/docker-compose.yml#L19)

Настройка для включения расписания крона, если параметр в значении `"false"`, то запрос в `3DSS` будет произведен **1 раз при старте** сервиса
```json
preparation-flow.scheduler.enabled: "false"
```

Настройка расписания [cron](https://www.freeformatter.com/cron-expression-generator-quartz.html)
```json
preparation-flow.scheduler.schedule.cron: "0 0 * * * ?"
```

Настройка инициализации отдельный пройвадеров, при `mastercard.enabled: "false"` инициализация [`PREQ/PRES flow`](https://github.com/rbkmoney/three-ds-server-compose/blob/master/docs/EMVCo_Protocol_and_Core_Functions_Specification_v2.2.0.pdf) будет активна только для `visa`, `mir`, для `mastercard` выключена

Параметр `message-version: 2.1.0` настривает версию сообщения `PReq`, возможную в рамках спецификации `EMVCo`
```json
preparation-flow.ds-provider.mastercard.enabled: "true"
preparation-flow.ds-provider.mastercard.message-version: 2.1.0      
preparation-flow.ds-provider.visa.enabled: "true"
preparation-flow.ds-provider.visa.message-version: 2.1.0
preparation-flow.ds-provider.mir.enabled: "true"
preparation-flow.ds-provider.mir.message-version: 2.1.0
```
