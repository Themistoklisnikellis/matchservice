Match Service

This service is part of a microservices-based betting application.
It manages match data and odds, packaged with Docker for easy setup (make sure Docker is installed).

1. Clone the repository

git clone https://github.com/Themistoklisnikellis/matchservice.git

2. Setup Environment File

cd matchservice

copy .env.example .env

3. Run the Application

docker-compose up --build

Once the containers are up, the API will be available at: http://localhost:8080
