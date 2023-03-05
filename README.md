# java-explore-with-me

## UPD: Feature

Branch: `feature_subscriptions`.

Pull request: https://github.com/terekhovmv/java-explore-with-me/pull/4

Swagger specs are embedded and available via `<server>/swagger-ui/index.html` ( e.g. http://localhost:8080/swagger-ui/index.html ).
See sections:
* Private: Subscriptions
* Public: Subscriptions

## Components

### `ewm-service`

*Depends on:* `ewm-service-contract`, `stats-client`

Main API of Explore-with-me application.

### `ewm-service-contract`

Explore-with-me service contract built on Swagger specs.

### `stats-service`

*Depends on:* `stats-dto`

Statistics service for collecting the information about APIs called. Can be used in any other environments.

### `stats-client`

*Depends on:* `stats-dto`

The module for embedding into the app for sending the information to statistics service (non blocking).

### `stats-dto`

Common statistics module used both on service and client sides.
