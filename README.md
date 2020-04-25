# Simple REST API

## To run postgreSQL in Docker:
docker run --name psql-docker-container -p 5432:5432 -e POSTGRES_DB=testdb -e POSTGRES_PASSWORD=mysecretpassword -d postgres

## To run executable jar:
mvn clean install

java -jar target/store-0.0.1-SNAPSHOT.jar
