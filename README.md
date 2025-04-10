# Web-store

This is a web store project created in Java using Spring Boot and Angular. The website is fully responsive, allowing users to browse and search for products, create an account with email verification, log in, add their shipping address, make purchases, view orders, and reset their password (with a password reset link sent to their email). The admin can add, modify, or delete categories, subcategories, products, and producers. I am continually expanding my knowledge and developing the project in my spare time.

Project deployed on GCP: [Link](https://webstore-j5caa4355a-lm.a.run.app/)

## Table of contents
- [Technologies](#technologies)
- [How to run](#how-to-run)
- [Features](#features)
- [Changelogs](#changelogs)
- [Api Reference](#spring-boot-api-reference)
- [Screenshot](#screenshot)
- [Plugins](#plugins)
- [Credits](#credits)
- [License](#license)
- [Contact](#contact)

## Technologies
**Java:** 17
 
<details>
<summary>Depedencies:</summary>

- Spring Boot Starter
- Spring Boot Starter Data JPA
- Spring Boot Starter Validation
- Spring Boot Starter Web
- Spring Boot Starter Test
- Spring Boot Starter Mail 
- Spring Boot Starter Security
- Spring Security Test
- Make-it-easy
- Lombok
- Passay
- Jsonwebtoken
- Annotations

Locally:
- Mysql Connector J

GCP: 
- Spring Cloud GCP Depedencies (dependencyMenagement)
- Spring Cloud GCP Starter SQL MySQL

</details>
<br>

**Angular:** 16.1.3

**MySQL:** 8.0.32 

## How to run

- [Deploy on GCP](deploy-on-gcp.md)
- [Run locally](run-locally.md)

## Features

- Login/Sign-up with a confirmed email
- Products pagination 
- Product and Order Page
- Sort products
- Buy the products
- Search products
- View orders
- Modification account data
- Admin board - modification: category, subcategory, product, producer, product type, promotion, delivery type
- Reset password system- send email with a link
- Change email system with option to restore old email - send email to old;
- Newsletter system with confirmation users and usubscribe;
- Product available notification system with confirmation and usubscribe;

### Currently working on:
 
 - New UX/UI
 - Implement systems form backend to frontend

### To-Do

- Fillter products
- Send order email
- Refactoring a monolith to microservices 
- New UX/UI
- ...

<div align="right">
  <a href="#web-store">Back to top</a>
</div>

## Changelogs

<details>
<summary>Newsletter branch:</summary>

  - Add ShedulerService to check your newsletters and send them when they are due  
  - Add unit and integration tests for new systems
  - Add new link providers
  - Add new EmailTypes
  - Add missed URI to SecurityConfig
  - Add registrations controllers
  - Add unsubscriber controllers
  - Add ApplicationEventPublisher for public event to notify product subscribers 
  - Add ProductQuantityRequest, ProductAvailableEvent
  - Add ProductNameView - projection
  - Change Map<String, Object> in return type to ResponseMessageDto 
  - Add generic repository and service for RemovalTokens;
  - Add RemovalToken interface
  - Add Removals token for unsubscriber notifications
  - Add NotificationExpirationManager 
  - Rebuild Email Strategy getMessage() - now return String.format() - for in some case takes multiple arguments
  - Rename NotificationStrategy/Type to EmailStrategy/Type
  - Clean code, simplify names;
  - Change some directories, correct directory names
  - Add abastract generic services for sending emails with tokens/confirmed entities
  - Add extends services for abstract repositories and service
  - Add abstract generic repositories and services for confirmation tokens; 
  - Add helper service for TokenDetails;
  - Add BaseConfToken, OwnerConfToken, Subscriber interface for generic type in abstract services;
  - Rebuild ConfirmationToken - now is seprate for Account, NewsletterSubscriber and ProductSubscriber
  - TokenDetails is Embeddable, using in conf tokes
  - To separte TokenDetails from ConfirmationToken
  - Add entities: ProductSubscriber, ProductSubscription, NewsletterSubscriber, NewslettereMessage
  - ErrorResponse convert to record
</details>


<details>
<summary>Change email branch:</summary>
  
  - Add test of new methods
  - Rebuild ShipmentService - more details in exception - clean code
  - Add restoreEmail system
  - Rebuild NotificationStrategy
  - Rebuild email sender - now is async and return void
  - Moved validation password of change password to aspect
  - Use aspect for validation data and send email to old account email
  - Add change email system
  - Add annotion for Account Aspect
</details>

<details>
<summary>Logger and mapper branch:</summary>

- Add logs for services
- Convert dto classes to records
- Remove SortDirectionType
- Add DiscountRandomizer
- Add reset password method.
- Rebuild mappers.
- Remove Mapstruct dependency.

</details>

<details>
<summary>Discount branch:</summary>

- Add info about subcategory id of product in Product DTOS
- Add unit and integrations test
- Rebuilding caluclated shipment price system
- Add Discount

</details>


<details>
<summary>Clean code branch:</summary>

- Add missed validation annotations for DTO's fields
- Correct DTO names
- Correct validation annotations
- Add providers for enivoroment variables 
- Use @Transactional for setup methods
- Simplify method names
- Remove unnecessary squares and naming of parameters
- Simplify delete entity methods - previously entity was found before delete 
- Remove ResponseEntity<> from Controllers and GlobalExceptionHandler
- Rename ProductPricePromotion to Promotion
- Use asserts from junit.jupiter instead of assertThat in some cases 
- Remove comments (//given, //when, //then) form tests
- Separating integration tests

</details>

<details>
<summary>Integrations and unit test branch:</summary>

- Add changelogs
- Add docker-compose.yml for whole project
- Add admin account initializer 
- Update PageProductsService
- Delete SortByType and SortDirection converters 
- Add PageProductsOptions for @RequestParam in controllers, SortByType and SortDirection in one field
- Change find Order's products - now one query for find all products
- Update EmailSenderStrategy to NotificationStrategy
- Add Make-It-Easy dependency for test builders
- Add integration tests
- Change relationship beetween Account and AccountAddress - unidirectional association (@MapsId)
- Add Maven Surefire and Failsafe
- Add Testcontainers

</details>


<div align="right">
  <a href="#web-store">Back to top</a>
</div>


## Spring boot API Reference

[Postman documentation](https://documenter.getpostman.com/view/23641360/2sA2xiXCKr#intro)

<div align="right">
  <a href="#web-store">Back to top</a>
</div>

## Screenshot

![app image](https://ik.imagekit.io/glowacki/Zrzut_ekranu_2023-07-12_102052.png?updatedAt=1711179688638)

### Products Page

![app image](https://ik.imagekit.io/glowacki/products.png?updatedAt=1711181592363)

### Product Page
![app image](https://ik.imagekit.io/glowacki/Zrzut%20ekranu%202024-03-23%20085842.png?updatedAt=1711180738622)

### Admin Board

![app image](https://ik.imagekit.io/glowacki/Zrzut%20ekranu%202023-08-18%20123044.png?updatedAt=1692354731249)

### Account Page

![app image](https://ik.imagekit.io/glowacki/Zrzut%20ekranu%202024-03-23%20085732.png?updatedAt=1711180738487)

### Order Page
![app image](https://ik.imagekit.io/glowacki/Zrzut%20ekranu%202024-03-23%20085819.png?updatedAt=1711180738571)


### Database diagram

![app image](https://ik.imagekit.io/glowacki/Zrzut_ekranu_2023-07-12_192353.png?updatedAt=1711179630302)

### Tests
![app imaget](https://ik.imagekit.io/glowacki/Zrzut%20ekranu%202023-08-09%20002003.png?updatedAt=1711181500231)

<div align="right">
    <a href="#web-store">Back to top</a>
</div>

## Plugins

  **IntellIJ IDEA**: Google Cloud Code, SonarLint, Mapstruct Support, Docker

  **VSC**: Angular Language Service, Prettier - Code formatter, Docker

<div align="right">
    <a href="#web-store">Back to top</a>
</div>

## Credits

I used the following tutorials: 

https://www.youtube.com/watch?v=QwQuro7ekvc

https://www.youtube.com/watch?v=KxqlJblhzfI

Amigoscode https://github.com/amigoscode

<div align="right">
  <a href="#web-store">Back to top</a>
</div>

## License
MIT License

Copyright (c) 2023 Sebastian Głowacki

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

<div align="right">
    <a href="#web-store">Back to top</a>
</div>

## Contact

Sebastian Głowacki - glowackisebastian.it@gmail.com

<div align="right">
    <a href="#web-store">Back to top</a>
</div>
