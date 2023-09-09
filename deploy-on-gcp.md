# Running a Project on Google Cloud Run with Google Cloud SQL
This readme.md describes the steps necessary to run a Spring Boot project on the Google Cloud Run platform using Google Cloud SQL as the database and authentication via a JSON key file (key.json).

## Table of Contents

- [Preliminary Steps](#preliminary-steps)
- [Google Cloud SQL](#google-cloud-sql)
- [Google Cloud Run Spring Boot](#google-cloud-sql-run-spring-boot)
- [Google Cloud Run Angular](#google-cloud-sql-run-angular)

## Preliminary Steps

Log in to your Google Cloud account: https://console.cloud.google.com/.

Create a new project or select an existing project where you want to deploy your application.

## Google Cloud SQL

[Google Cloud SQL Documentation](#https://cloud.google.com/sql/docs/mysql/)

### Creating a Database
Navigate to the Google Cloud Console: [Link](#https://console.cloud.google.com/)

- Create a new project or select an existing project.

- In the navigation menu, go to "SQL."

- Follow the steps outlined in the "Before you begin" documentation, then create an instance. [(Documentation)](#https://cloud.google.com/sql/docs/mysql/create-instance)

- Choose the appropriate parameters: MySQL, region, database access name, password, and enable the flag:
```
cloudsql_iam_authentication
```

- After creating the database instance, create a new database and give it a name  [(Documentation)](#https://cloud.google.com/sql/docs/mysql/create-manage-databases)

- Create a service account with the editor role and Cloud SQL Instance User role  [(Dokumentacja)](#https://cloud.google.com/iam/docs/service-accounts-create#iam-service-accounts-create-console)

- Create a key for the service account in JSON format; once created, the key will be downloaded. [(Documentation)](#https://cloud.google.com/iam/docs/keys-create-delete)

- Rename it to:
```
key.json
```
- Create a database user. [(Documentation)](#https://cloud.google.com/sql/docs/mysql/create-manage-users)

<details>
<summary style=font-size:18px> Run Locally with a Google Cloud SQL Connection (Windows)</h3></summary>


- Download the Cloud SQL Auth Proxy [(Link)](#https://cloud.google.com/sql/docs/mysql/sql-proxy)

- Rename the file to:
```
  cloud-sql-proxy.exe
```
- Open File Explorer and navigate to:
```
%localappdata%/Google/Cloud SDK
```
- Place the cloud-sql-proxy.exe file in this folder.

- Launch the Google Cloud SDK Shell.

- Log in to your account:
```
gcloud auth login
```
- Then connect using the Google Cloud Auth Proxy:
```
cloud-sql-proxy <INSTANCE_CONNECTION_NAME> --credentials-file="<PATH_TO_SERVICE_ACCOUNT_JSON_FILE>"
```
If port 3306 is already in use, run cmd as an administrator. Find the PID for port and then kill it.
(Replace "----" with the PID for port 3306):
```
netstat -ano | findstr :3306
taskkill /PID ---- /f
```
Then try connecting with the cloud-sql-proxy again.

- Complete application-googlecloud.properties:


```
# .MySQL8Dialect
# Initialize the database since the newly created Cloud SQL database has no tables. The following flag is for Spring Boot 2.5+.
spring.sql.init.mode=always

spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://<DATABASE_HOST>?cloudSqlInstance=<INSTANCE_CONNECTION_NAME>

spring.cloud.gcp.sql.database-name=
spring.cloud.gcp.sql.instance-connection-name=
spring.cloud.gcp.project-id=

# Leave empty for root, uncomment and fill out if you specified a user
spring.datasource.username=<USER_NAME>

# Uncomment if root password is specified
spring.datasource.password=<PASSWORD>
```
- Uncomment `spring.profiles.active` in application.properties and fill out the basic environment variable values:
```
#Uncomment this if you are connecting to Google Cloud SQL
#spring.profiles.active=googlecloud

# Environment Variables
sender.email = ${SENDER_EMAIL:your_email}
sender.password = ${SENDER_PASSWORD:your_password}
cors.allowed-origin-patterns=${CORS_ALLOWED_ORIGIN_PATTERNS:http://localhost:4200}

```
- You can now run the application locally with a Google Cloud SQL connection.
</details>

