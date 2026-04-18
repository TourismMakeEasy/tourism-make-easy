#!/bin/bash
# Creates isolated databases for each microservice on first PostgreSQL startup.
# This script is mounted into /docker-entrypoint-initdb.d/ and runs automatically.
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE DATABASE central_db;
    CREATE DATABASE inventory_db;
    CREATE DATABASE reservation_db;
    CREATE DATABASE resort_db;
EOSQL
