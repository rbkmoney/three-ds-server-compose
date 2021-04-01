# three-ds-server-compose

Репозиторий с макросервисом [RBK.money 3D Secure Server](https://github.com/rbkmoney/three-ds-server) для запуска внутри [Docker](https://hub.docker.com/r/rbkmoney/three-ds-server)

## Сокращения
```
Directory Server = DS
3D Secure Server = 3DSS
```

## Использование

```bash
docker-compose up -d
```

![Demo2](./readme-resources/2_full.gif?raw=true)

Запуск [макросервиса](https://github.com/rbkmoney/three-ds-server/blob/master/pom.xml) полностью автоматизирован и производится внутри докера, образ скачивается напрямую из [репозитория `rbkmoney` на `Docker Hub`](https://hub.docker.com/r/rbkmoney/three-ds-server).

**Обращаем внимание**, для корректного демо [RBK.money 3D Secure Server](https://github.com/rbkmoney/three-ds-server) порядок запуска:

1. [`docker-compose.yml`](https://github.com/rbkmoney/three-ds-server-compose/blob/master/docker-compose.yml) [three-ds-server macroservice](https://github.com/rbkmoney/three-ds-server-compose/blob/master/README.md) (сам макросервис 3DSS)
2. [`docker-compose.yml`](https://github.com/rbkmoney/three-ds-server-compose/blob/master/ds-simple-mock/docker-compose.yml) [ds-simple-mock](https://github.com/rbkmoney/three-ds-server-compose/blob/master/ds-simple-mock/README.md) (пример сервиса, который может быть использован в качестве заглушки для обработки [`PReq` && `AReq` запросов](https://github.com/rbkmoney/three-ds-server-compose/blob/master/docs/EMVCo_Protocol_and_Core_Functions_Specification_v2.2.0.pdf) в DS от [`макросервиса 3DSS`](https://github.com/rbkmoney/three-ds-server-compose/blob/master/README.md))
3. [`docker-compose.yml`](https://github.com/rbkmoney/three-ds-server-compose/blob/master/pseudo-schedulator/docker-compose.yml) [pseudo-schedulator](https://github.com/rbkmoney/three-ds-server-compose/blob/master/pseudo-schedulator/README.md) (пример сервиса, который может быть использован в качестве заглушки для инциализации [`PReq/PRes flow`](https://github.com/rbkmoney/three-ds-server-compose/blob/master/docs/EMVCo_Protocol_and_Core_Functions_Specification_v2.2.0.pdf) в [`макросервис 3DSS`](https://github.com/rbkmoney/three-ds-server-compose/blob/master/README.md))

## Описание

`3DSS` имплементирует требование по спецификации `EMVCo` к `3D Secure` взаимодействию, поддерживает только аутентификацию из вебсайта (`Browser-based`)

![alt text](./readme-resources/flow.jpg "3D Secure Processing Flow - Browser-based")

### 3DS Versioning

Перед прохождением `3DS Authentification Flow` `3DS Requestor` должен версионировать `PAN` плательщика. Результат версионирования подтверждает, что данный `PAN` является участником `3DS 2.0` и имеет право пройти аутентитфикацию `3DS Authentification Flow`, в протитвном случае результатом версионирования будет `HTTP 404 NOT FOUND`

Пример
```
-> Request [POST] http://three-ds-server:8080/versioning
Content-Type=application/json
{
  "accountNumber": "1234567890"
}

<- Response [POST] http://three-ds-server:8080/versioning
{
  "threeDsServerTransId": "bc9f0b90-1041-47f0-94df-d692170ea0d7",
  "dsProviderId": "visa",
  "acsStartProtocolVersion": "2.1.0",
  "acsEndProtocolVersion": "2.1.0",
  "dsStartProtocolVersion": "2.1.0",
  "dsEndProtocolVersion": "2.1.0",
  "threeDsMethodUrl": "url"
}
```
```
-> Request [POST] http://three-ds-server:8080/versioning
Content-Type=application/json
{
  "accountNumber": "9234567890"
}

<- Response [POST] http://three-ds-server:8080/versioning
HTTP 404 NOT FOUND
```

### 3DS Method

Дополнительный эндпоинт, который не обязателен для использования в `3DS Requestor`, используется для сборки HTML шаблона, который необходим при проведении `3DS Method` между `3DS Requestor` и [`ACS`]((https://github.com/rbkmoney/three-ds-server-compose/blob/master/docs/EMVCo_Protocol_and_Core_Functions_Specification_v2.2.0.pdf))

Пример
```
-> Request [POST] http://three-ds-server:8080/three-ds-method
Content-Type=application/json
{
  "threeDsMethodData": {
    "threeDSServerTransID": "1",
    "threeDSMethodNotificationURL": "url1"
  },
  "threeDsMethodUrl": "url2"
}

<- Response [POST] http://three-ds-server:8080/three-ds-method
{
  "htmlThreeDsMethodData": "...",
  "threeDsServerTransId": "1"
}
```
Где `htmlThreeDsMethodData`
 
```html
<!DOCTYPE html>
<html>
<body>

<h2>RBK.money 3D Secure Method Form</h2>

<form id="rbkMoneyThreeDsMethodForm" name="ThreeDsMethodForm"
      action="url2"
      method="POST">
    <input type="hidden"
           name="threeDSMethodData"
           value="eyJ0aHJlZURTU2VydmVyVHJhbnNJRCI6IjEiLCJ0aHJlZURTTWV0aG9kTm90aWZpY2F0aW9uVVJMIjoidXJsMSJ9"
    />
</form>

<script>
    document.getElementById("rbkMoneyThreeDsMethodForm").submit()
</script>
</body>
</html>
```

**Обратите внимание**, `3DSS` не проводит `3DS Method`, его проводит `3DS Requestor` напрямую с [`ACS`]((https://github.com/rbkmoney/three-ds-server-compose/blob/master/docs/EMVCo_Protocol_and_Core_Functions_Specification_v2.2.0.pdf))

Конечным результатом проведения `3DS Method` является определение параметра [`ThreeDsMethodCompletionInd`](https://github.com/rbkmoney/three-ds-server-domain-lib/blob/master/src/main/java/com/rbkmoney/threeds/server/domain/root/rbkmoney/RBKMoneyAuthenticationRequest.java#L46), который используется при проведении аутентификации. 

`3DS Method` можно провести без использования `3DSS`, самостоятельно собрав подобный шаблон `htmlThreeDsMethodData`, описанный здесь

#### 3DS Authentication Flow

Для прохождения аутентификации, запрос должен быть отправлен на `http://three-ds-server:8080/sdk`, `Content-Type=application/json`, `"messageType": "RBKMONEY_AUTHENTICATION_REQUEST"`

Описание модели запроса при прохождении `3DS Authentification Flow` здесь — [`RBKMoneyAuthenticationRequest`](https://github.com/rbkmoney/three-ds-server-compose/blob/master/RBKMoneyAuthenticationRequest.md)

Актуальная модель в виде `java-файла` для получения `POST HTTP json-ответа` в [`макросервис 3DSS`](https://github.com/rbkmoney/three-ds-server-compose) тут [RBKMoneyAuthenticationRequest.java](https://raw.githubusercontent.com/rbkmoney/three-ds-server-domain-lib/master/src/main/java/com/rbkmoney/threeds/server/domain/root/rbkmoney/RBKMoneyAuthenticationResponse.java)

Актуальная модель ответа находятся по пути https://github.com/rbkmoney/three-ds-server-domain-lib/blob/master/src/main/java/com/rbkmoney/threeds/server/domain/root/rbkmoney/RBKMoneyAuthenticationResponse.java

Ответ: 

```json
{
  "messageType": "RBKMONEY_AUTHENTICATION_RESPONSE",
  "messageVersion": "2.1.0",
  "threeDSServerTransID": "5201a899-749a-4300-841b-24a870565b51",
  "transStatus": "Y",
  "dsReferenceNumber": "DSServerRef123456",
  "acsReferenceNumber": "ACSRefNum1234",
  "acsTransID": "77bbb905-1ac1-464d-9b5f-3f5bdc43ffe4",
  "dsTransID": "1d042cf8-a44c-4111-a057-723b95e403d2",
  "authenticationValue": "AAABBZEEBgAAAAAAAAQGAAAAAAA=",
  "acsOperatorID": "00000014",
  "eci": "05"
}

```

Также может вместо ответа вернуться сообщение об ошибке с описанием

Ошибка

```json
{
  "messageType": "Erro",
  "messageVersion": "2.1.0",
  "errorCode": "404",
  "errorComponent": "A",
  "errorDescription": "Permanent system failure",
  "errorDetail": "Database not available"
}
```

Актуальная модель сообщения об ошибке находятся по пути https://github.com/rbkmoney/three-ds-server-domain-lib/blob/master/src/main/java/com/rbkmoney/threeds/server/domain/root/emvco/Erro.java

В остальных случаях вернется соотвествующий HTTP код ошибки 

## Предварительное конфигурирование окружения перед использованием `docker-compose.yml`

`3DSS` является клиентом для `DS`, и использует `DS` при выполенении запросов, обозначенных спецификацей `EMVCo` (ссылка ниже), поэтому для корректной работы `3DSS` необходима настройка обоих доменов

### **(обязательно)** Настройка домена совместимости (домен `DS`)

При запуске `3DSS` попытается выполнить запрос на обновление карточных диапазонов `PReq/PRes flow` (описание ниже, п.3), поэтому перед запуском `3DSS` необходимо убедиться, что есть доступ к активному `DS`, который готов принимать HTTP запросы (т.е поднятый и работающий сервис)

При отсутствии активного `DS` можно воспользоваться сервисом `ds-simple-mock`, который расположен в:

```
three-ds-server-compose  
│
└───ds-simple-mock
    │
    └───...
```

#### (необязательно) Использование `ds-simple-mock` в качестве `DS`

##### Описание

`ds-simple-mock` имплементирует элементарную логику `DS` — принимает и отвечает сообщениями, обозначенных спецификацей `EMVCo`

На данный момент поддерживает 4 запроса:
- POST /visa/DS2/authenticate {AReq} --> ARes
- POST /visa/DS2/authenticate {PReq} --> PRes
- POST /mastercard/DS2/authenticate {AReq} --> ARes
- POST /mastercard/DS2/authenticate {PReq} --> PRes

Сервис **НЕ** умеет отдавать отличающиеся ARes сообщения, ответы, которые возвращает сервис статичны. В стандартной имплементации мока `DS` иммитация стандартной логики `DS` производится по передаваемому PAN в запросе, сервис генерирует успешные и неудачные ARes сообщения

##### Использование

Для использования сервиса необходимо собрать `docker image` с помощью `Dockerfile`, который расположен в корневой директории сервиса. Сам `build` сервиса проводится в `docker container` (см. `/three-ds-server-compose/ds-simple-mock/Dockerfile`), поэтому на локальном хосте не обязательно устанавливать необходимые зависимости для сервиса (`maven`, `openjdk11` и тд)

Собрать и запустить сервис:

```bash
cd ds-simple-mock/
docker-compose up -d
```

![Demo1](./readme-resources/1_full.gif?raw=true)

После запуска сервис висит на `http://localhost:8081`

### **(обязательно)** Настройка домена эквайера (домен `3DSS`)

#### Опции для настройки сервиса `3DSS` в `docker-compose.yml`

`3DSS` может быть скофигурирован для работы с: 
- `DS visa`
- `DS mastercard`
- одновременно со всеми `DS`

1. **(обязательно)** Указать в директории `/three-ds-server-compose/three-ds-server/cert` ключи `visa.p12`, `mastercard.p12`, которые используются `3DSS` для соединения с `DS`

Ключи будут использоваться параметрами:

```yaml
client.ds.ssl.visa.trust-store: file:/opt/three-ds-server/cert/visa.p12
client.ds.ssl.mastercard.trust-store: file:/opt/three-ds-server/cert/mastercard.p12
```

Указать пароли для ключей:
 
```yaml
client.ds.ssl.visa.trust-store-password: {{password}}
client.ds.ssl.mastercard.trust-store-password: {{password}}
```

Изменить (при необходимости) volume

```yaml
volumes:
  - ./three-ds-server/cert:/opt/three-ds-server/cert/:ro
```

2 **(обязательно)** Указать адреса `DS visa` && `DS mastercard`

```yaml
environment.visa.ds-url: http://host.docker.internal:8081/visa/DS2/authenticate
environment.mastercard.ds-url: http://host.docker.internal:8081/mastercard/DS2/authenticate
```

При таких значениях параметров `ds-url` `3DSS` будет обращаться к `http://localhost:8081` как к сервису `DS` 

3 (не обязательно) Настройка расписания для обновления карточных диапазонов (`PReq/PRes flow`)

Описание `PReq/PRes flow` можно найти в открытой спецификации `EMVCo`:

```
three-ds-server-compose  
│
└───docs
    │
    └───EMVCo_Protocol_and_Core_Functions_Specification_v2.2.0.pdf
        │
        └───5.6 PReq/PRes Message Handling Requirements (page 119)
```

Выключить обновление карточных диапазонов:

```yaml
rbkmoney-preparation-flow.scheduler.enabled: "false" ("true" по умолчанию) 
```

Задать расписание обновления карточных диапазонов:

```yaml
rbkmoney-preparation-flow.scheduler.schedule.cron: "0 0 * * * ?" (обновлять каждый час — по умолчанию) 
```

Выключить обновление карточных диапазонов для определеннного `DS`:

```yaml
rbkmoney-preparation-flow.scheduler.ds-provider.mastercard.enabled: "false" ("true" по умолчанию) 
rbkmoney-preparation-flow.scheduler.ds-provider.visa.enabled: "true" ("true" по умолчанию) 

```

## Запуск `3DSS` с использованием `docker-compose.yml`

Собрать и запустить сервис:

```bash
docker-compose up -d
```

![Demo2](./readme-resources/2_full.gif?raw=true)

После запуска сервис висит на `http://localhost:8080`

## Тестирование `3DSS`

Для проведения Authentification Flow (в соотвествии с спецификацией `EMVCo`) в `3DSS` отправляется POST HTTP запрос на `http://three-ds-server:8080/sdk`:
- `Content-Type=application/json`
- `"messageType": "RBKMONEY_AUTHENTICATION_REQUEST"`
- остальные параметры JSON должны быть заполнены в соотвествии с структурой https://github.com/rbkmoney/three-ds-server-domain-lib/blob/master/src/main/java/com/rbkmoney/threeds/server/domain/root/rbkmoney/RBKMoneyAuthenticationRequest.java

Для теста в `3DSS` посылаются 2 запроса, у одного `"acctNumber": "2201010000000000"`, у второго `"acctNumber": "4012000000001001"`. В данном тесте в качестве сервиса `DS` используется собственный мок-сервис `ds-simple-mock` (описание см. выше)

`3DSS` (с помощью мок-сервиса `DS`) настроен так, что `"acctNumber": "2201010000000000"` ассоциирует с `mastercard` , `"acctNumber": "4012000000001001"` ассоциирует с `visa`, и отправляет запрос в соотвествующий (`mastercard`/`visa`) `DS` (`PReq/PRes flow`)

![Demo3](./readme-resources/test.gif?raw=true)
