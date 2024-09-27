# Running the Project Locally with MySQL, Spring Boot, and Angular
This guide will walk you through the steps to run your project locally on your development machine. The project consists of three main components: a MySQL database, a Spring Boot backend, and an Angular frontend.

## Table of Contents

- [Prerequisites](#prerequisites)

- [Running in docker](#Running-in-docker)

## Prerequisites
Before you begin, make sure you have the following installed on your machine:

- [Git Bash](https://git-scm.com/downloads)

- [Docker](https://www.docker.com/products/docker-desktop/)

 - Integrated Development Environment (IDE) of your choice, such as [IntelliJ IDEA](https://www.jetbrains.com/idea/download/?section=windows) for Java and [Visual Studio Code](https://code.visualstudio.com/download) for Angular

Also, you need a google account for the email sender host.

<div align="right">
  <a href="#running-the-project-locally-with-mysql-spring-boot-and-angular">Back to top</a>
</div>

## Running in docker

- Clone project using URL:
```
git clone https://github.com/Glowka96/web-store.git
```

### Setting up Spring Boot
```
# Environment Variables
sender.email = ${SENDER_EMAIL:your_email}
sender.password = ${SENDER_PASSWORD:your_password}
cors.allowed-origin-patterns=${CORS_ALLOWED_ORIGIN_PATTERNS:http://localhost:4200}
email.confirmation.link = ${EMAIL_CONFIRMATION_LINK:http://localhost:4200/registration/confirm?token=}
reset-password.confirmation.link = ${RESET_PASSWORD_CONFIRMATION_LINK:http://localhost:4200/reset-password/confirm?token=}
shipment.address = ${SHIPMENT_ADDRESS:your_shipment_address}
account.image.url = ${ACCOUNT_IMAGE_URL:https://i.imgur.com/a23SANX.png}
secret.key = ${SECRET_KEY:5970337336763979244226452948404D6351665468576D5A7134743777217A25}
admin.email = ${ADMIN_EMAIL:admin@admin.com}
admin.password = ${ADMIN_PASSWORD:Password123!}
```
How to create a Google-generated app password [link](https://support.google.com/accounts/answer/185833?hl=en).
Use it for `SENDER_PASSWORD`.

How to create a secret key [link](https://dev.to/tkirwa/generate-a-random-jwt-secret-key-39j4).

If you're running Angular on default port you don't need change cross origination patterns, email confirmation link and password reset confirmation link.

- Open the pom.xml and comment or delete these: 

```
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>com.google.cloud</groupId>
                <artifactId>spring-cloud-gcp-dependencies</artifactId>
                <version>${spring-cloud-gcp-dependencies.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
```
```
        <dependency>
            <groupId>com.google.cloud</groupId>
            <artifactId>spring-cloud-gcp-starter-sql-mysql</artifactId>
        </dependency>
```
  Uncomment this:
```
<!--        <dependency>-->
<!--            <groupId>com.mysql</groupId>-->
<!--            <artifactId>mysql-connector-j</artifactId>-->
<!--            <version>${mysql-connector.version}</version>-->
<!--            <scope>runtime</scope>-->
<!--        </dependency>-->
```

<div align="right">
  <a href="#running-the-project-locally-with-mysql-spring-boot-and-angular">Back to top</a>
</div>

### Setting up Angular

- If you change spring boot port, then open environment.ts in the Angular project and set that port.

  ![app image](https://ik.imagekit.io/glowacki/environment.png?updatedAt=1695389288197)


### Creating .env file

Create `.env` file in root directory and add these environment variables:
```
MYSQLDB_USER=root
MYSQLDB_PASSWORD=123456
MYSQLDB_DATABASE=your-database-name
MYSQLDB_LOCAL_PORT=3307
MYSQLDB_DOCKER_PORT=3306

SPRING_LOCAL_PORT=8080
SPRING_DOCKER_PORT=8080
```

### Running project

- Open root directory in terminal and run docker compose
```
docker-compose up --build
``` 

<div align="right">
  <a href="#running-the-project-locally-with-mysql-spring-boot-and-angular">Back to top</a>
</div>