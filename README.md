# java-explore-with-me

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
