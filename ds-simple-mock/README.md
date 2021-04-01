# ds-simple-mock

[Сервис](https://github.com/rbkmoney/three-ds-server-compose/blob/master/ds-simple-mock/pom.xml), который может быть использован в качестве `DS` для обработки [`3DS Preparation Flow` && `3DS Authentification Flow`](https://github.com/rbkmoney/three-ds-server-compose/blob/master/docs/EMVCo_Protocol_and_Core_Functions_Specification_v2.2.0.pdf) в рамках демонстрации работы [`RBK.money 3D Secure Server`](https://github.com/rbkmoney/three-ds-server)

## Использование

```bash
docker-compose up -d
```

![Demo1](../readme-resources/1_full.gif?raw=true)

Cборка и запуск [сервиса](https://github.com/rbkmoney/three-ds-server-compose/blob/master/ds-simple-mock/pom.xml) полностью автоматизированы и производится внутри докера, дополнительно локально (`maven`, `openjdk11` и тд) ничего устанавливать не нужно. Сборка [сервиса](https://github.com/rbkmoney/three-ds-server-compose/blob/master/ds-simple-mock/pom.xml) описана в [`Dockerfile`](https://github.com/rbkmoney/three-ds-server-compose/blob/master/ds-simple-mock/Dockerfile), запуск описан в [`docker-compose.yml`](https://github.com/rbkmoney/three-ds-server-compose/blob/master/ds-simple-mock/docker-compose.yml)

**Обращаем внимание**, для корректного демо [RBK.money 3D Secure Server](https://github.com/rbkmoney/three-ds-server) порядок запуска:

1. [`docker-compose.yml`](https://github.com/rbkmoney/three-ds-server-compose/blob/master/docker-compose.yml) [three-ds-server macroservice](https://github.com/rbkmoney/three-ds-server-compose) (сам макросервис 3DSS)
2. [`docker-compose.yml`](https://github.com/rbkmoney/three-ds-server-compose/blob/master/ds-simple-mock/docker-compose.yml) [ds-simple-mock](https://github.com/rbkmoney/three-ds-server-compose/tree/master/ds-simple-mock) (пример сервиса, который может быть использован в качестве заглушки для обработки [`PReq` && `AReq` запросов](https://github.com/rbkmoney/three-ds-server-compose/blob/master/docs/EMVCo_Protocol_and_Core_Functions_Specification_v2.2.0.pdf) в DS от [`макросервиса 3DSS`](https://github.com/rbkmoney/three-ds-server-compose))
3. [`docker-compose.yml`](https://github.com/rbkmoney/three-ds-server-compose/blob/master/pseudo-schedulator/docker-compose.yml) [pseudo-schedulator](https://github.com/rbkmoney/three-ds-server-compose/tree/master/pseudo-schedulator) (пример сервиса, который может быть использован в качестве заглушки для инциализации [`PReq/PRes flow`](https://github.com/rbkmoney/three-ds-server-compose/blob/master/docs/EMVCo_Protocol_and_Core_Functions_Specification_v2.2.0.pdf) в [`макросервис 3DSS`](https://github.com/rbkmoney/three-ds-server-compose))

Сервис **НЕ** умеет отдавать отличающиеся `ARes` сообщения (например, в зависимости от приходящего `PAN` в запросе `AReq/ARes flow`), ответы, которые возвращает сервис **статичны**. В стандартной имплементации мока `DS` иммитация стандартной логики `DS` производится по передаваемому PAN в запросе, сервис генерирует успешные и неудачные `ARes` сообщения

## Описание

Сервис имплементирует элементарную логику `DS` — принимает и отвечает **статичными** сообщениями, обозначенных спецификацей `EMVCo`

На данный момент сервис поддерживает обработку 3 провайдеров `{providerId}` — `visa`, `mastercard`, `mir`

`3DS Authentification Flow`
```
-> Request [POST] /{providerId}/DS2/authenticate
{
  "messageType": "AReq",
  "messageVersion": "2.1.0",
  ...
}

<- Response [POST] /{providerId}/DS2/authenticate
{
  "messageType": "ARes",
  "messageVersion": "2.1.0",
  ...
}
```
`3DS Preparation Flow`
```
-> Request [POST] /{providerId}/DS2/authenticate
{
  "messageType": "PReq",
  "messageVersion": "2.1.0",
  ...
}

<- Response [POST] /{providerId}/DS2/authenticate
{
  "messageType": "PRes",
  "messageVersion": "2.1.0",
  ...
}
```

## Конфигурация

Дополнительно настраивать ничего не нужно, но **обратите внимание**, что порты не проброшены наружу, потому что никаких внешних запросов от `3DS Requestor` / `3DSS`-клиента напрямую не производится
