# Web-store

This is a web store project created in Java using Spring Boot and Angular. The website is fully responsive, allowing users to browse and search for products, create an account with email verification, log in, add their shipping address, make purchases, view orders, and reset their password (with a password reset link sent to their email). The admin can add, modify, or delete categories, subcategories, products, and producers. I am continually expanding my knowledge and developing the project in my spare time.

Project deployed on GCP: [Link](https://toysland-hixicpf4xa-lm.a.run.app/)

## Table of contents
- [Technologies](#technologies)
- [How to run](#how-to-run)
- [Features](#features)
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
- Lombok
- Mapstruct
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
- [Run to locally](ścieżka/do/twego/pliku)


## Features

- Login/Sign-up with a confirmed email
- Product pagination 
- Buy the products
- Search products
- View orders
- Modification account data
- Admin board - modification: category, subcategory, product, producer
- Reset password system

### To-Do

- Home page - new offerts and promotions (currently working on it)
- Sort and fillter product
- Message box
- Newsletter
- New admin board with more options
- Better angular pagination
- ...

<div align="right">
  <a href="#web-store">Back to top</a>
</div>

## Spring boot API Reference

<details>
<summary>Category</summary>

| Method | Path | Permision | Description |
| ---------- | ---------- | ---------- | ---------- |
| `GET` | `/api/v1/categories`| Everyone  | Get all categories |
| `POST` | `/api/v1/categories` | Admin | Add category | 
| `PUT` | `/api/v1/categories/{id}` | Admin | Update category | 
| `DELETE` | `/api/v1/categories/{id}` | Admin |Delete category |

</details>

<details>
<summary>Subategory</summary>

| Method | Path | Permision | Description |
| ---------- | ---------- | ---------- | ---------- |
| `GET` | `/api/v1/categories/subcategories` | Everyone | Get all subcategories | 
| `GET` | `/api/v1/categories/subcategories/{subCategoryId}` | Everyone | Get subcategory by id| 
| `POST` | `/api/v1/categories/{categoryId}/subcategories` | Admin | Add subcategory |
| `PUT` | `/api/v1/categories/{categoryId}/subcategories/{subCategoryId}` | Admin | Update subcategory |
| `DELETE` | `/api/v1/categories/subcategories/{subCategoryId}` |Admin | Delete subcategory | 

</details>

<details>
<summary>Producer</summary>

| Method | Path | Permision | Description |
| ---------- | ---------- | ---------- | ---------- |
| `GET` | `/api/v1/producers` | Admin | Get all producers |
| `POST` | `/api/v1/producers` | Admin | Add producer |
| `PUT` | `/api/v1/producers/{id}` | Admin | Update producer by id |
| `DELETE` | `/api/v1/producers/{id}` | Admin | Delte producer by id |

</details>


<details>
<summary>Poruduct</summary>

| Method | Path | Permision | Description |
| ---------- | ---------- | ---------- | ---------- |
| `GET` | `/api/v1/subcategories/products/types` | Admin |Get all product types | 
| `GET` | `/api/v1/subcategories/products` | Admin | Get all products |
| `GET` | `/api/v1/subcategories/{subcategoryId}/products` | Everyone |Get all products by subcategory id with params (page, size, sort) |
| `GET` | `/api/v1/subcategories//{subcategoryId}/products/quantity` | Everyone | Get products quantity by subcategory id|
| `POST` | `/api/v1/subcategories/{subcategoryId}/producers/{producerId}/products` | Admin |Add product by subcategory id and producer id | 
| `PUT` | `/api/v1/subcategories/{subcategoryId}/producers/{producerId}/products/{productId}` | Admin |Update product by subcategory id, producer id and product id|
| `DELETE` | `/api/v1/subcategories/products/{productId}` | Admin |Delete product by id |
| `GET` | `/api/v1/products/search` | Everyone |Get search products with params (text, page, size, sort) |
| `GET` | `/api/v1/products/search/quantity` | Everyone | Get search products quantity with params (text)|

</details>

<details>
<summary>Account</summary>

| Method | Path | Permision | Description |
| ---------- | ---------- | ---------- | ---------- |
| `POST` | `/api/v1/registration` | Everyone | Account registration send confirm link to email | 
| `GET` | `/api/v1/registration/confirm` | Everyone | Account enabled and confrim registration token with params (token) | 
| `POST` | `/api/v1/login` | Everyone | Login | 
| `POST` | `/api/v1/logout` | User, Admin | Logout | 
| `GET` | `/api/v1/accounts/{accountId}` | User, Admin | Get account data by account id with authorization | 
| `PUT` | `/api/v1/accounts/{accountId}` | User, Admin  | Update account data by account id with authorization | 
| `DELETE` | `/api/v1/accounts/{accountId}` | User, Admin  | Delete account by account id data with authorization |
| `GET` | `api/v1/accounts/{email}/reset-password` | Everyone | Send reset password link to email |
| `PATCH` | `api/v1/accounts/reset-password/confirm` | User, Admin | Setup new password and confirm token with params (password, token) |

</details>

<details>
<summary>Account address</summary>

| Method | Path | Permision | Description |
| ---------- | ---------- | ---------- | ---------- |
| `GET` | `/api/v1/accounts/{accountId}/address` | User, Admin | Get account address data by account with authorization | 
| `POST` | `/api/v1/accounts/{accountId}/address` | User, Admin | Add account address data by account with authorization | 
| `PUT` | `/api/v1/accounts/{accountId}/address` | User, Admin | Update account address data by account with authorization | 

</details>

<details>
<summary>Order</summary>

| Method | Path | Permision | Description |
| ---------- | ---------- | ---------- | ---------- |
| `GET` | `/api/v1/accounts/{accountId}/orders` | User, Admin | Get account all orders by account with authorization | 
| `GET` | `/api/v1/accounts/{accountId}/orders/{orderId}` | User, Admin | Get account order by account id and order id with authorization | 
| `POST` | `/api/v1/accounts/{accountId}/orders` | User, Admin | Add order to account by account id with authorization | 
| `PUT` | `/api/v1/accounts/{accountId}/orders` | User, Admin | Update order in account by account id with authorization | 
| `DELETE` | `/api/v1/accounts/{accountId}/orders` | User, Admin | Delete order from account by account id with authorization | 

</details>

<div align="right">
  <a href="#web-store">Back to top</a>
</div>

## Screenshot

![App Screenshot](https://ik.imagekit.io/glowacki/Zrzut_ekranu_2023-07-12_102052.png?updatedAt=1689150176647)
### Admin Board

![App Screenshot](https://ik.imagekit.io/glowacki/Zrzut%20ekranu%202023-08-18%20123044.png?updatedAt=1692354731249)

### Database diagram

![App Screenshot](https://ik.imagekit.io/glowacki/Zrzut_ekranu_2023-07-12_192353.png?updatedAt=1689182679226)

### Tests
![App Screenshot](https://ik.imagekit.io/glowacki/Zrzut%20ekranu%202023-08-09%20002003.png?updatedAt=1691533356035)

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

<div style="display: flex; justify-content: right;">
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
