# Demo Project for Online Stream #39 - Spring Security
Demo project for online YouTube stream #39 about Spring Security

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

Get the exchange rate using general simple `x-api-key`:

```shell
curl -H 'x-api-key: valid-key' http://localhost:8080/forex/rate?from=USD&to=EUR
```

Get the exchange rate using client's key `x-client-api-key`:

```shell
curl -H 'x-client-api-key: key1' http://localhost:8080/forex/rate?from=USD&to=EUR
curl -H 'x-client-api-key: key3' http://localhost:8080/forex/rate?from=USD&to=UAH
curl -H 'x-client-api-key: key4' http://localhost:8080/forex/rate?from=USD&to=CHF
```


### Reference Documentation
For further reference, please consider the following sections:

* [Spring Security](https://docs.spring.io/spring-boot/docs/3.1.0/reference/htmlsingle/#web.security)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.1.0/reference/htmlsingle/#data.sql.jpa-and-spring-data)

### Guides
The following guides illustrate how to use some features concretely:

* [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
* [Spring Boot and OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
* [Authenticating a User with LDAP](https://spring.io/guides/gs/authenticating-ldap/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
