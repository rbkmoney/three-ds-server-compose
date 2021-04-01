# RBKMoneyAuthenticationRequest

Актуальная модель в виде `java-файла` для выполнения `POST HTTP json-запроса` в [`макросервис 3DSS`](https://github.com/rbkmoney/three-ds-server-compose)) тут [RBKMoneyAuthenticationRequest.java](https://raw.githubusercontent.com/rbkmoney/three-ds-server-domain-lib/master/src/main/java/com/rbkmoney/threeds/server/domain/root/rbkmoney/RBKMoneyAuthenticationRequest.java)

# Примеры

Отдельно примеры запросов находятся в папке [samples](https://github.com/rbkmoney/three-ds-server-compose/tree/master/samples)

# Описание полей

| Data Element                                               | Field Name                              |
|------------------------------------------------------------|-----------------------------------------|
| 3DS Method Completion Indicator                            | threeDSCompInd                          |
| 3DS Requestor Authentication Indicator                     | threeDSRequestorAuthenticationInd       |
| 3DS Requestor Authentication Information                   | threeDSRequestorAuthenticationInfo      |
| 3DS Requestor Authentication Method Verification Indicator | threeDSReqAuthMethodInd                 |
| 3DS Requestor Decoupled Max Time                           | threeDSRequestorDecMaxTime              |
| 3DS Requestor Decoupled Request Indicator                  | threeDSRequestorDecReqInd               |
| 3DS Requestor ID                                           | threeDSRequestorID                      |
| 3DS Requestor Name                                         | threeDSRequestorName                    |
| 3DS Requestor Prior Transaction Authentication Information | threeDSRequestorPriorAuthenticationInfo |
| 3DS Requestor URL                                          | threeDSRequestorURL                     |
| 3DS Server Reference Number                                | threeDSServerRefNumber                  |
| 3DS Server Operator ID                                     | threeDSServerOperatorID                 |
| 3DS Server Transaction ID                                  | threeDSServerTransID                    |
| 3DS Server URL                                             | threeDSServerURL                        |
| 3RI Indicator                                              | threeRIInd                              |
| Account Type                                               | acctType                                |
| Acquirer BIN                                               | acquirerBIN                             |
| Acquirer Merchant ID                                       | acquirerMerchantID                      |
| Address Match Indicator                                    | addrMatch                               |
| Broadcast Information                                      | broadInfo                               |
| Browser Accept Headers                                     | browserAcceptHeader                     |
| Browser IP Address                                         | browserIP                               |
| Browser Java Enabled                                       | browserJavaEnabled                      |
| Browser JavaScript Enabled                                 | browserJavascriptEnabled                |
| Browser Language                                           | browserLanguage                         |
| Browser Screen Color Depth                                 | browserColorDepth                       |
| Browser Screen Height                                      | browserScreenHeight                     |
| Browser Screen Width                                       | browserScreenWidth                      |
| Browser Time Zone                                          | browserTZ                               |
| Browser User-Agent                                         | browserUserAgent                        |
| Card/Token Expiry Date                                     | cardExpiryDate                          |
| Cardholder Account Information                             | acctInfo                                |
| Cardholder Account Number                                  | acctNumber                              |
| Cardholder Account Identifier                              | acctID                                  |
| Cardholder Billing Address City                            | billAddrCity                            |
| Cardholder Billing Address Country                         | billAddrCountry                         |
| Cardholder Billing Address Line 1                          | billAddrLine1                           |
| Cardholder Billing Address Line 2                          | billAddrLine2                           |
| Cardholder Billing Address Line 3                          | billAddrLine3                           |
| Cardholder Billing Address Postal Code                     | billAddrPostCode                        |
| Cardholder Billing Address State                           | billAddrState                           |
| Cardholder Email Address                                   | email                                   |
| Cardholder Home Phone Number                               | homePhone                               |
| Cardholder Mobile Phone Number                             | mobilePhone                             |
| Cardholder Name                                            | cardholderName                          |
| Cardholder Shipping Address City                           | shipAddrCity                            |
| Cardholder Shipping Address Country                        | shipAddrCountry                         |
| Cardholder Shipping Address Line 1                         | shipAddrLine1                           |
| Cardholder Shipping Address Line 2                         | shipAddrLine2                           |
| Cardholder Shipping Address Line 3                         | shipAddrLine3                           |
| Cardholder Shipping Address Postal Code                    | shipAddrPostCode                        |
| Cardholder Shipping Address State                          | shipAddrState                           |
| Cardholder Work Phone Number                               | workPhone                               |
| Device Channel                                             | deviceChannel                           |
| Device Information                                         | deviceInfo                              |
| Device Rendering Options Supported                         | deviceRenderOptions                     |
| DS Reference Number                                        | dsReferenceNumber                       |
| DS Transaction ID                                          | dsTransID                               |
| DS URL                                                     | dsURL                                   |
| EMV Payment Token Indicator                                | payTokenInd                             |
| EMV Payment Token Source                                   | payTokenSource                          |
| Instalment Payment Data                                    | purchaseInstalData                      |
| Merchant Category Code                                     | mcc                                     |
| Merchant Country Code                                      | merchantCountryCode                     |
| Merchant Name                                              | merchantName                            |
| Merchant Risk Indicator                                    | merchantRiskIndicator                   |
| Message Category                                           | messageCategory                         |
| Message Extension                                          | messageExtension                        |
| Message Type                                               | messageType                             |
| Message Version Number                                     | messageVersion                          |
| Notification URL                                           | notificationURL                         |
| Purchase Amount                                            | purchaseAmount                          |
| Purchase Currency                                          | purchaseCurrency                        |
| Purchase Currency Exponent                                 | purchaseExponent                        |
| Purchase Date & Time                                       | purchaseDate                            |
| Recurring Expiry                                           | recurringExpiry                         |
| Recurring Frequency                                        | recurringFrequency                      |
| Transaction Type                                           | transType                               |
| Whitelist Status                                           | whiteListStatus                         |
| Whitelist Status Source                                    | whiteListStatusSource                   |
| 3DS Requestor Challenge Indicator                          | threeDSRequestorChallengeInd            |
