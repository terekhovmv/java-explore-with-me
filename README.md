# java-explore-with-me

## Components

### `ewm-server`

*Depends on:* `stats-client`

Main API of Explore--with-me application.

### `stats-server`

*Depends on:* `stats-dto`

Statistics server for collecting the information about APIs called. Can be used in any other environments.

### `stats-client`

*Depends on:* `stats-dto`

The module for embedding into the app for sending the information to statistics server (non blocking).

### `stats-dto`

Common statistics module used both on server and client sides.
