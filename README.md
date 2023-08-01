# Web-store

This is simple web-store created with java and angular with database

![App Screenshot](https://ik.imagekit.io/glowacki/Zrzut_ekranu_2023-07-12_102052.png?updatedAt=1689150176647)
## Introduction 
I started working on this project after finishing sdacademy course. I wanted to consolidate knowledge from the course, test my knowlage in practice. While working on the project I got to know a lot of new solutions that were not on the course.

## Technologies
**Java:** 17

**Angular:** 16.1.3

**MySQL:** 8.0.32 



## Features

- Login/Sign-up with a confirmed email
- Product pagination 
- Buy the products
- Search products
- View orders
- Modification account data
- Admin board - modification: category, subcategory, product, producer

### To-Do

- Refresh JWT token
- Sort and fillter product
- Message box
- Newsletter
- Better angular pagination
- Home page - new offerts and promotions
- ...

## API Reference

| Method        | Path           | Permision  | Description |
| ------------- |:-------------| -----------| -----------|
| `GET` | `/api/v1/categories`| User  | Get all categories |
| `POST` | `/api/v1/categories` |Admin | Add category | 
| `PUT` | `/api/v1/categories/{id}` |Admin | Update category | 
| `DELETE` | `/api/v1/categories/{id}` | Admin |Delete category | 
| `GET` | `/api/v1/categories/subcategories` |User | Get all subcategories | 
| `GET` | `/api/v1/categories/subcategories/{subCategoryId}` |User | Get subcategory by id | 
| `POST` | `/api/v1/categories/{categoryId}/subcategories` | Admin | Add subcategory |
| `PUT` | `/api/v1/categories/{categoryId}/subcategories/{subCategoryId}` | Admin | Update subcategory |
| `DELETE` | `/api/v1/categories/subcategories/{subCategoryId}` |Admin | Delete subcategory | 
| `GET` | `/api/v1/producers` | Admin | Get all producers |
| `POST` | `/api/v1/producers` | Admin | Add producer |
| `PUT` | `/api/v1/producers/{id}` | Admin | Update producer by id |
| `DELETE` | `/api/v1/producers/{id}` | Admin | Delte producer by id |
| `GET` | `/api/v1/subcategories/products/types` | User |Get all product types | 
| `GET` | `/api/v1/subcategories/products` | Admin | Get all products |
| `GET` | `/api/v1/subcategories/{subcategoryId}/products` |  Admin |Get all products by subcategory id with params |
| `GET` | `/api/v1/subcategories//{subcategoryId}/products/quantity` |  Admin | Get products quantity by subcategory id|
| `POST` | `/api/v1/subcategories/{subcategoryId}/producers/{producerId}/products` | Admin |Add product by subcategory id and producer id | 
| `PUT` | `/api/v1/subcategories/{subcategoryId}/producers/{producerId}/products/{productId}` |  Admin |Update product by subcategory id and producer id |
| `DELETE` | `/api/v1/subcategories/products/{productId}` |  Admin |Delete product by id |
| `GET` | `/api/v1/products/search/{text}` |  User |Get search products with params |
| `GET` | `/api/v1/products/search/quantity` | User | Get search products quantity|
| `POST` | `/api/v1/registration` | User | Account registration | 
| `GET` | `/api/v1/registration/confirm` | User | Confrim registration token | 
| `POST` | `/api/v1/login` | User | Login | 
| `POST` | `/api/v1/logout` | User | Logout | 
| `GET` | `/api/v1/accounts/{accountId}` | User | Get account data by account id with authorization | 
| `PUT` | `/api/v1/accounts/{accountId}` | User | Update account data by account id with authorization | 
| `DELETE` | `/api/v1/accounts/{accountId}` | User | Delete account by account id data with authorization | 
| `GET` | `/api/v1/accounts/{accountId}/address` | User | Get account address data by account with authorization | 
| `POST` | `/api/v1/accounts/{accountId}/address` | User | Add account address data by account with authorization | 
| `PUT` | `/api/v1/accounts/{accountId}/address` | User | Update account address data by account with authorization | 
| `GET` | `/api/v1/accounts/{accountId}/orders` | User | Get account all orders by account with authorization | 
| `GET` | `/api/v1/accounts/{accountId}/orders/{orderId}` | User | Get account order by account id and order id with authorization | 
| `POST` | `/api/v1/accounts/{accountId}/orders` | User | Add order to account by account id with authorization | 
| `PUT` | `/api/v1/accounts/{accountId}/orders` | User | Update order in account by account id with authorization | 
| `DELETE` | `/api/v1/accounts/{accountId}/orders` | User | Delete order from account by account id with authorization | 



## Database diagram

![App Screenshot](https://ik.imagekit.io/glowacki/Zrzut_ekranu_2023-07-12_192353.png?updatedAt=1689182679226)

## Tests
![App Screenshot](https://ik.imagekit.io/glowacki/Tests.jpg?updatedAt=1689219890363)
