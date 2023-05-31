# Project Description

Implement a web service that would handle GET requests to path “weather” by returning the weather data determined by IP
of the request originator. Upon receiving a request, the service should perform a geolocation search using a
non-commercial, 3rd party IP to location provider. Having performed the reverse geo search service should use another
non-commercial, 3rd party service to determine current weather conditions using the coordinates of the IP.

## Clone the repository

1. Clone the repository using the command `git clone https://github.com/Chaklader/WeatherAPI.git`
2. Enter inside the repo: `cd WeatherAPI/`
   <br>
   <br>
   As the IP address to location finder database is shipped with the repo, we need to install Git LFS to fetch it.
   The installation instructions are provided below:
   ###MacOS
   Run the following command to install Git LFS using Homebrew (a popular package manager for macOS):
   <br>
   <br>
   `$ brew install git-lfs`
   ### Windows
   - Download the Git LFS installer for Windows from the official Git LFS website: https://git-lfs.github.com/
   - Run the downloaded installer (.exe file).
   - Follow the installation wizard prompts to complete the installation process.
   ### Linux
   Run the following command to install Git LFS:
   <br>
   <br>
   `$ sudo apt update && sudo apt install git-lfs`
   <br>
   <br>
3. Initialize Git LFS in the cloned repository:
   $ git lfs install

4. Fetch the Git LFS objects:
   <br>
   `$ git lfs fetch`

5. Run the following command to checkout the files with Git LFS:
   <br>
   `$ git lfs checkout`

The above procedure will make sure you have checkout the database `src/main/resources/GeoLite2-City.mmdb` correctly
in the local repository.

## Database

Install PostgreSQL database locally and create a database named `weatherdb` there. Our tables will be in the schema
named `weather` but will be created by Flyway database versioning SQL provided in the `src/main/resources/db/migration/table`
location.

# Run Project

1. Firstly, go inside the project repo and install all the dependencies using the command `$ mvn install -DskipTests`.
   Please, make sure you are using Java 17, for example I use `java version 17.0.6-zulu`
2. The project can be run from the terminal using the command `$ mvn spring-boot:run`
3. Otherwise, Run from a standard IDE such as IntelliJ. Make sure you are using Java 17 in the IDE

## Query Data

I have provided the Postman collection in the root of the project, first run the request `GET Weather Data` that will
fetch the weather data for the IP address and store in the database. Then, we can run other queries for historical analysis.
See that I provided the IP address as header as the request can come from load balancer or proxy and may not indicate the
actual IP address for the client.

## Test Suite

Run the suite `WeatherApiTestSuite.java` and it will run all the tests for the project.

<br>
<br>
<br>
