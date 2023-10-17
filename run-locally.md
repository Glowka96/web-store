# Running the Project Locally with MySQL, Spring Boot, and Angular
This guide will walk you through the steps to run your project locally on your development machine. The project consists of three main components: a MySQL database, a Spring Boot backend, and an Angular frontend.

## Table of Contents

- [Prerequisites](#prerequisites)

- [Setting up MySQL Database](#setting-up-mysql-database)

- [Setting up Spring Boot](#setting-up-spring-boot)

- [Setting up Angular](#setting-up-angular)

## Prerequisites
Before you begin, make sure you have the following installed on your machine:

- [MySQL](https://dev.mysql.com/downloads/installer/)

- [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/javase-downloads.html)

- [Node.js](https://nodejs.org/en/download/)

- [Angular CLI](https://angular.io/cli)

- Integrated Development Environment (IDE) of your choice, such as [IntelliJ IDEA](https://www.jetbrains.com/idea/download/?section=windows) for Java and [Visual Studio Code](https://code.visualstudio.com/download) for Angular

## Setting Up MySQL Database

- Install and set up MySQL on your local machine if you haven't already.

- Create a new MySQL database for your project.

- Configure the database connection in your Spring Boot application.properties: 

```
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/your_databas
spring.datasource.username=your_database_user
spring.datasource.password=your_database_user_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

<div align="right">
  <a href="#running-the-project-locally-with-mysql-spring-boot-and-angular">Back to top</a>
</div>

## Setting up Spring Boot

- Open the WebStoreSpring directory in your chosen IDE.

- Complete environment variables in application.properties

```
# Environment Variables
sender.email = ${SENDER_EMAIL:your_email}
sender.password = ${SENDER_PASSWORD:your_password}
cors.allowed-origin-patterns=${CORS_ALLOWED_ORIGIN_PATTERNS:http://localhost:4200}
email.confirmation.link = ${EMAIL_CONFIRMATION_LINK:http://localhost:4200/registration/confirm?token=}
reset-password.confirmation.link = ${RESET_PASSWORD_CONFIRMATION_LINK:http://localhost:4200/reset-password/confirm?token=}
shipment.address = ${SHIPMENT_ADDRESS:your_shipment_address}
```
How to create a Google-generated app password [link](https://support.google.com/accounts/answer/185833?hl=en).
Use it for `SENDER_PASSWORD`.

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

- Load maven changes

- Build and run your Spring Boot application.

Your Spring Boot application should start and connect to the MySQL database.

<div align="right">
  <a href="#running-the-project-locally-with-mysql-spring-boot-and-angular">Back to top</a>
</div>

## Setting up Angular

- Open the WebStoreAngular directory in your chosen IDE.

- Open a terminal and install project dependencies by running:

```
npm install
```

- If you change spring boot port, then open environment.ts in the Angular project and set that port.

  ![app image](https://ik.imagekit.io/glowacki/environment.png?updatedAt=1695389288197)


- Start the Angular development server:

```
ng serve -o
```

This will compile your Angular application and open it in your browser.


<div align="right">
  <a href="#running-the-project-locally-with-mysql-spring-boot-and-angular">Back to top</a>
</div>
