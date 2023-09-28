# Running a Project on Google Cloud Run with Google Cloud SQL
This readme.md describes the steps necessary to run a project on the Google Cloud Run platform using Google Cloud SQL as the database and authentication via a JSON key file (key.json).

## Table of Contents

- [Preliminary Steps](#preliminary-steps)
- [Google Cloud SQL](#google-cloud-sql)
- [Google Artifact Registry](#google-artifact-registry)
- [Google Cloud Run](#google-artifact-registry)
    - [Google Cloud Run Spring Boot](#google-cloud-run-spring-boot)
    - [Google Cloud Run Angular](#google-cloud-run-angular)

## Preliminary Steps

Log in to your Google Cloud account: [Link](https://console.cloud.google.com/).

Create a new project or select an existing project where you want to deploy your application.

## Google Cloud SQL

[Google Cloud SQL Documentation](https://cloud.google.com/sql/docs/mysql/)

### Creating a Database
Navigate to the Google Cloud Console: [Link](https://console.cloud.google.com/)

- In the navigation menu, go to "SQL".

- Follow the steps outlined in the "Before you begin" documentation, then create an instance. [(Documentation)](https://cloud.google.com/sql/docs/mysql/create-instance)

- Choose the appropriate parameters: MySQL 8.0, region, database access name, password, and enable the flag:
```
cloudsql_iam_authentication
```

- After creating the database instance, create a new database and give it a name.  [(Documentation)](https://cloud.google.com/sql/docs/mysql/create-manage-databases)

- Create a service account with the editor role and Cloud SQL Instance User role.  [(Documentation)](https://cloud.google.com/iam/docs/service-accounts-create#iam-service-accounts-create-console)

- Create a key for the service account in JSON format. Once created, the key will be downloaded. [(Documentation)](https://cloud.google.com/iam/docs/keys-create-delete)

- Create a database user. [(Documentation)](https://cloud.google.com/sql/docs/mysql/create-manage-users)

<details>
<summary style=font-size:18px> Run Locally with a Google Cloud SQL Connection (Windows)</summary>


- Download the Cloud SQL Auth Proxy: [(Link)](https://cloud.google.com/sql/docs/mysql/sql-proxy)

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

spring.datasource.url=jdbc:mysql://<DATABASE_HOST>?cloudSqlInstance=<INSTANCE_CONNECTION_NAME>

spring.cloud.gcp.sql.database-name=${DATABASE_NAME:your_datase_name}
spring.cloud.gcp.sql.instance-connection-name=${INSTANCE_CONNECTION_NAME:your_instance_connection_name}
spring.cloud.gcp.project-id=${PROJECT_ID:your_project_id}

spring.datasource.username=${DATABASE_USER_NAME:your_database_user_name}

spring.datasource.password=${DATABASE_USER_PASSWORD:your_dabatase_user_password}
```
- Uncomment `spring.profiles.active` in application.properties and fill out the basic environment variable values:
```
#Uncomment this if you are connecting to Google Cloud SQL
#spring.profiles.active=googlecloud

# Environment Variables
sender.email = ${SENDER_EMAIL:your_email}
sender.password = ${SENDER_PASSWORD:your_password}
cors.allowed-origin-patterns=${CORS_ALLOWED_ORIGIN_PATTERNS:http://localhost:4200}
email.confirmation.link = ${EMAIL_CONFIRMATION_LINK:http://localhost:4200/registration/confirm?token=}
reset-password.confirmation.link = ${RESET_PASSWORD_CONFIRMATION_LINK:http://localhost:4200/reset-password/confirm?token=}

```
How to create a Google-generated app password: [link](https://support.google.com/accounts/answer/185833?hl=en)
Use it for `SENDER_PASSWORD`

- You can now run the application locally with a Google Cloud SQL connection.
</details>

<div align="right">
  <a href="#running-a-project-on-google-cloud-run-with-google-cloud-sql">Back to top</a>
</div>

## Google Artifact Registry
Instructions for creating docked images in the Artifact Registry.
You'll need this later if you are not deploying a project from a GitHub.

- Follow the instructions in: [Documentation](https://cloud.google.com/artifact-registry/docs/docker/store-docker-container-images#windows)

Follow the steps below for Angular and Spring Boot:

- Open cmd and navigate to the directory where the Dockerfile is located.

- Runs the following commands: [Documentation](https://cloud.google.com/artifact-registry/docs/docker/pushing-and-pulling)
  ```
  docker build -t <YOUR IMAGE_NAME> .
  ```
  ```
  docker tag <LOCAL_IMAGE_NAME> <LOCATION>-docker.pkg.dev/<PROJECT_ID>/<REPOSITORY_NAME/<YOUR_IMAGE_NAME>:[IMAGE_TAG]
  ```
  ```
  docker push <LOCATION>-docker.pkg.dev/<PROJECT ID>/<REPOSITORY_NAME>/<YOUR IMAGE_NAME>:[IMAGE_TAG]
  ```

<div align="right">
  <a href="#running-a-project-on-google-cloud-run-with-google-cloud-sql">Back to top</a>
</div>


## Google Cloud Run
Navigate to the Google Cloud Console: [Link](https://console.cloud.google.com/)

Follow the steps below for Angular and Spring Boot:

- In the navigation menu, go to "Cloud Run".

- Click "Create service".

- You have two options for how you want to deploy a Docker image:

  ![image](https://github.com/Glowka96/web-store/assets/107603098/bf698b6a-b997-4fb8-9cdb-b6507c1dab54)

  If you choose first option, follow the steps from:
  [Google Artifact Registry](#google-artifact-registry)

  <details>
    <summary>  If you choose second option, follow the steps bellow:</summary>

    - Select GitHut on Repository Provider, authorize access in github, then select the project repository

    - Select Dockerfile build type, then set the source location:
        - for Spring:
      ```
      WebStoreSpring/Dockerfile
      ```
        - for Angular
      ```
      WebStroreAngular/Dockerfile
      ```
  </details>

<div align="right">
  <a href="#running-a-project-on-google-cloud-run-with-google-cloud-sql">Back to top</a>
</div>


### Google Cloud Run Spring Boot

- Set the service options and add environment variables as in the image - region and maximum number of instances can be diffrent

![App Screenshot](https://ik.imagekit.io/glowacki/SetupGCPRUN.png?updatedAt=1695910895556)

<details>
  <summary>Environment variables</summary>

```
SENDER_EMAIL
```
```
SENDER_PASSWORD
```
```
CORS_ALLOWED_ORIGIN_PATTERNS
```
```
EMAIL_CONFIRMATION_LINK
```
```
RESET_PASSWORD_CONFIRMATION_LINK
```
```
DATABASE_NAME
```
```
INSTANCE_CONNECTION_NAME
```
```
PROJECT_ID
```
```
DATABASE_USER_NAME
```
```
DATABASE_USER_PASSWORD
```

Cross origination patterns, email confirmation link and password reset completed after creating the service for Angular. Or you already know the URL of the domain you want to use. (Domain mapping for services)

Change `localhost:4200` to your angular service domain.

EMAIL_CONFIRMATION_LINK `http://localhost:4200/registration/confirm?token=`

RESET_PASSWORD_CONFIRMATION_LINK `http://localhost:4200/reset-password/confirm?token=`

How to create a Google-generated app password [link](https://support.google.com/accounts/answer/185833?hl=en)
Use it for `SENDER_PASSWORD`

</details>

- Add a Google Cloud SQL connection

  ![app image](https://ik.imagekit.io/glowacki/Zrzut%20ekranu%202023-09-16%20094138.png?updatedAt=1694850693566)

- Select the service account you created earlier.

  ![app image](https://ik.imagekit.io/glowacki/Zrzut%20ekranu%202023-09-16%20094226.png?updatedAt=1694850693583)

- Click "Create".

<div align="right">
  <a href="#running-a-project-on-google-cloud-run-with-google-cloud-sql">Back to top</a>
</div>

### Google Cloud Run Angular

- Open environment.prod.ts in the Angular project and set the spring service domain

  ![app image](https://ik.imagekit.io/glowacki/environment.prod.ts.png?updatedAt=1695206362652)

- Save change - push on GitHub or build docker image

- Select the new version docker image

- Set the service options as in the image - region and maximum number of instances can be diffrent

![app image](https://ik.imagekit.io/glowacki/SetupGCPRUN_ANGULAR.png?updatedAt=1695206264653)

- Click "Create".
  
- If you didn't set the environment variables(cross origination patterns, email confirmation link and password reset confirmation link) when create spring boot service, follow these steps:

    - Open spring service in Google Cloud Run, then go to `YAML`

    - Update the values of environment variables and save them

  ![app image](https://ik.imagekit.io/glowacki/YAML.png?updatedAt=1695292279498)

<div align="right">
  <a href="#running-a-project-on-google-cloud-run-with-google-cloud-sql">Back to top</a>
</div>


