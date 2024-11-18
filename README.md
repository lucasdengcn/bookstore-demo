# Bookstore Demo Application

## Background

This is a demo application for online bookstore, providing browsing, purchasing book via end user's browser etc.

It is built on top of Spring Boot framework, since Spring Boot is a great framework for building microservices quickly and easily,
and also the Spring Community is very active and has a lot of great resources and outstanding solutions for common problems.

To simplify demo, here putting book service and cart service into one project.

## Tech Stack

- Java 21
- Spring Boot 3.3.4
- Spring Data JPA
- Spring Event
- Hibernate
- H2 Database
- Lombok
- JUnit5
- Mockito
- Mapstruct
- Swagger UI
- Gradle 8+
- Flyway
- RFC-9457
- Version Catalog
- Checkstyle
- Spotless
- Testcontainers

## Error Response

follow [RFC-9457](https://www.rfc-editor.org/rfc/rfc9457.html) specification to response with standard error output, it is clean and easier to align with Frontend or other integration point.

## API Specification

* The api specification is generated by Swagger UI. You can access it via [API Specification](http://localhost:8080/swagger-ui/index.html)
* API Versioning on URL path is supported.

## API Usage

* export above Api Specification into JSON file and import into Postman or other similar tools
* checkout each API and try it out
* you can also try it out via curl or other tools
* API explanation as below:
- `/books/v1/`: create a book with title and price etc.
- `/books/v1/{id}`: get a book by id, if not found then return 404.
- `/books/v1/{index}/{size}`: get a page of books with index and size.
- `/carts/v1/book/`: add a book into current user's cart, will return cart id.
- `/carts/v1/{id}/book/`: incr or decr a book's purchased amount from current user's cart by cart id.
- `/carts/v1/books/`: get all books from current user's cart.
- `/checkouts/v1/summary`: get a summary (total price) of current user's cart.

## How to run the application?

1. Clone this repository
2. Run the following command in the root directory of the project

if you have installed Gradle in your environment, then just run:

```shell
gradle clean bootRun
```

otherwise, then run

```shell
./gradlew clean bootRun
```

## How to run unit test?

1. Clone this repository
2. Run the following command in the root directory of the project

```shell
gradle clean test
```

otherwise, then run

```shell
./gradlew clean test
```

3. Test Report will be generated under

```shell
build/reports/tests/test/index.html
```

## How to run unit test with coverage?

1. Clone this repository
2. Run the following command in the root directory of the project

```shell
gradle clean jacocoTestReport
```

3.Coverage report will be generated under

```shell
build/reports/jacoco/test/index.html
```

4.Report example as following

![Report](./code-coverage.png)

## Actuator

http://localhost:8080/actuator
http://localhost:8080/actuator/refresh
http://localhost:8080/actuator/health

## Redis

https://github.com/redis/lettuce/wiki/Connection-Pooling

## Capabilities

- Global Exception Handler
- Entity Convertor
- Distributed Lock
- Custom Metrics
- Auto reload yaml file and refresh properties bean
- Checkstyle (https://checkstyle.sourceforge.io/google_style.html)
- Spotless https://github.com/diffplug/spotless/tree/main/plugin-gradle#requirements
- API Doc https://springdoc.org/
- Testcontainers https://testcontainers.com/guides/testing-spring-boot-rest-api-using-testcontainers/