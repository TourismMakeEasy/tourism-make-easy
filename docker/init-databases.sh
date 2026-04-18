#!/bin/bash
# Creates isolated databases for each microservice on first PostgreSQL startup.
# Database names are passed as environment variables from docker-compose.yml.
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE DATABASE ${CENTRAL_DB:-central_db};
    CREATE DATABASE ${INVENTORY_DB:-inventory_db};
    CREATE DATABASE ${RESERVATION_DB:-reservation_db};
    CREATE DATABASE ${RESORT_DB:-resort_db};
EOSQL
