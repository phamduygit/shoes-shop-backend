# Overview Shoes Shope Project

1. Admin website - React
- Deployed website: https://pmdshoesshop.netlify.app/
- Source: https://github.com/phamduygit/shoes-shop-admin-page
2. Client mobile application - Flutter
- APK file for Android: https://drive.google.com/file/d/1_6BrRpuQNwS4yxJ3uPHBHtd5UzEJqJ2o/view?usp=sharing
- Source: https://github.com/phamduygit/shoes-shop-app
3. Backend application - Spring boot
- Deployed AWS Cloud: https://ec2-13-213-11-93.ap-southeast-1.compute.amazonaws.com/swagger-ui/index.html
- DNS server: https://pmdshoesshop.online/swagger-ui/index.html
- Source: https://github.com/phamduygit/shoes-shop-backend

# About Backend Server - Spring boot
This is the admin website used to manage products, orders, users, ads, brands...
## 1. Overview
- Deployed AWS Cloud: https://ec2-13-213-11-93.ap-southeast-1.compute.amazonaws.com/swagger-ui/index.html
- DNS server: https://pmdshoesshop.online/swagger-ui/index.html
- Postman collection: https://drive.google.com/file/d/1Vv7ZQo-x9x_ocBD872NMTldFUpIV7ZZZ/view?usp=sharing

## 2. Techology and dabatabase
- Tech: Spring boot, Spring data JPA, Spring Security, JWT
- Database: MySQL
- Hosting and tool: AWS, Docker

## 3. Database Schema
![Shoes app](https://res.cloudinary.com/dvhhz53rr/image/upload/v1690461639/Shoes_app_rnuurj.png)


## 3. How to build project
In the project directory, you can run:

### Prerequisite
- Installed Java 17
- Installed MySQL and created database in mysql server
- Config file application.properties mapping with url mysql server

### `./mvnm spring-boot:run`
Runs the app in the development mode.\
Open [http://localhost:8080](http://localhost:8080) to view it in the browser.
Otherwise you can import [JSONFile](https://github.com/phamduygit/shoes-shop-backend/blob/main/Shoes%20Shop.postman_collection.json) to postman to test all of API.


