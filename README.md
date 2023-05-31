# match
Playing around with Use Case Driven Development

# building the application
To build the application, a running Docker enviroment is needed since the integration tests in this repo make use of Testcontainers. This addition spins up a Docker container with a PostgreSql database, which is being used while running the integration tests.

# running locally
To run this application locally make sure a Docker environment is running. As the application starts up, the docker-compose file (located in the "local" directory) runs automatically using spring-docker-compose. This will spin up a PostgreSql database.
