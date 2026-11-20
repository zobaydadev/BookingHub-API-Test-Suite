# BookingHub API Test Suite

API test suite in Java using Rest Assured and TestNG. It tests the public Restful-Booker API (https://restful-booker.herokuapp.com): authentication, a full booking lifecycle, and negative cases.

## Highlights
- Client layer (`BookingClient`) keeps HTTP details out of the tests
- POJO request and response models with Jackson
- JSON schema validation of the booking response
- Ordered CRUD flow with `dependsOnMethods`
- Negative tests for 404 and 403 behaviour
- CI on every push and a nightly scheduled run with GitHub Actions
- Dockerfile so the suite runs the same way anywhere

## Technology
Java 17, Maven, Rest Assured 5.5, TestNG 7.10, Jackson, json-schema-validator

## Test Coverage
| Area | Tests |
|---|---|
| Auth | valid login returns token, bad login is rejected |
| Booking CRUD | create, get + schema check, search by last name, full update, partial update, delete, deleted booking returns 404 |
| Negative | unknown id returns 404, delete without token is 403, update with bad token is 403 |

## Structure
```
src/main/java/com/qa/api
  config/   ApiConfig          base url and credentials, -D overridable
  client/   BookingClient      one method per endpoint
  models/   Booking, BookingDates
src/test/java/com/qa/api/tests  AuthTest, BookingCrudTest, BookingNegativeTest
src/test/resources/schemas      booking-schema.json
testng.xml
```

## Run
```bash
mvn clean test
mvn clean test -Dapi.base.url=https://restful-booker.herokuapp.com
```
Results are written to `target/surefire-reports`.

## Run with Docker
```bash
docker build -t bookinghub-api-tests .
docker run --rm bookinghub-api-tests
```
