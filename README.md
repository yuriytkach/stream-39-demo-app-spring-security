# Demo Project for Online Stream #39-40 - Spring Security
Demo project for online YouTube stream #39 and #40 about Spring Security

## Access to Online Stream on YouTube

To get a link to online stream on YouTube please do the following:

- :moneybag: Make any donation to support my volunteering initiative to help Ukrainian Armed Forces by means described on [my website](https://www.yuriytkach.com/volunteer)
- :email: Write me an [email](mailto:me@yuriytkach.com) indicating donation amount and time
- :tv: I will reply with the link to the stream on YouTube.

Thank you in advance for your support! Слава Україні! :ukraine:

# Run the app

First, you need to start the Postgres database using provided `docker-compose.yml` file:

```shell
docker-compose up
```

Then, you can start the application using Gradle:

```shell
./gradlew bootRun
```

# Test the app

You can test the app using provided `curl` commands:

Get the list of supported currencies:

```shell
curl http://localhost:8080/forex/currencies
```

## Simple API Key

Get the exchange rate using general simple `x-api-key`:

```shell
curl 'http://localhost:8080/forex?from=USD&to=EUR' -H 'x-api-key: valid-key'
```

## Client API Key

Get the exchange rate using client's key `x-client-api-key`:

```shell
curl 'http://localhost:8080/forex?from=USD&to=EUR' -H 'x-client-api-key: key1'
curl 'http://localhost:8080/forex?from=USD&to=UAH' -H 'x-client-api-key: key3'
curl 'http://localhost:8080/forex?from=USD&to=CHF' -H 'x-client-api-key: key4'
```

## JWT Authentication

First, obtain the JWT token by supplying username and password in JSON format to the auth endpoint:

```shell
curl -X POST http://localhost:8080/auth -H 'Content-Type: application/json' \
-d '{"username": "user1", "password": "qwerty"}'
```

Then use the received token to access the protected endpoint:

```shell
curl 'http://localhost:8080/forex?from=USD&to=EUR' -H 'Authorization: Bearer <jwt token>'
```

JWT token is valid for 2 minutes. This can be changed in `application.yml` file.

### Reference Documentation
For further reference, please consider the following sections:

* [Spring Security](https://docs.spring.io/spring-boot/docs/3.1.0/reference/htmlsingle/#web.security)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.1.0/reference/htmlsingle/#data.sql.jpa-and-spring-data)
* [JWT.io](https://jwt.io/)

### Guides
The following guides illustrate how to use some features concretely:

* [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
* [Spring Boot and OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
* [Authenticating a User with LDAP](https://spring.io/guides/gs/authenticating-ldap/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
