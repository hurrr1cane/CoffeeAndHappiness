# CoffeeAndHappiness

Welcome to the Coffee and Happiness! This project is a comprehensive cafe management system built using Spring Boot, Next.js, and Kotlin for Android. This README.md will provide an in-depth overview of the project's structure, features, and functionality.


## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Roles](#roles)
- [Food Management](#food-management)
- [Cafe Management](#cafe-management)
- [Review System](#review-system)
- [Order System](#order-system)
- [Bonus Points](#bonus-points)
- [News](#news)
- [Technologies Used](#technologies-used)
- [Project Structure](#project-structure)
- [Usage](#usage)
- [Authors](#authors)
- [Contributing](#contributing)
- [License](#license)


## Introduction
The Cafe Application is designed to streamline the operations of a cafe, providing functionality for managing orders, inventory, and customer interactions. It consists of three major components:

1. **Backend Logic (Spring Boot)**: This component handles the server-side logic, including order processing, inventory management, user authentication, and more.
2. **Web Application (Next.js)**: The web interface allows cafe staff to take orders, manage inventory, and view customer information. Users can also interact with the cafe through the website.
3. **Android Application (Kotlin)**: The Android app enables mobile access to the cafe's functionality, allowing staff to process orders on the go, and customers to place orders and explore cafe options.


## Features

### Roles
The application supports three roles:

- **User**: Regular cafe visitors who can place orders, review food, and cafes.
- **Waiter**: Staff members responsible for creating orders, scanning user QR codes, and processing requests.
- **Admin**: Administrators who can manage food items, cafes, news, and more.

### Food Management
- **Menu Display**: Food items are displayed in both the application and website menu sections.

### Cafe Management
- **Institutions Display**: Cafes are displayed as institutions with coordinates on maps. Users can choose between viewing the list of institutions or using the map with cafe markers.

### Review System
- **Rating and Reviews**: Users can rate and write reviews for both food items and cafes. The average rating is displayed as stars in menus and institution details.
- **User Reviews**: Users can view and manage their own reviews on their profile page.

### Order System
- **Waiter-User Interaction**: Waiters can create orders in real-time based on customer requests. Users place orders through waiters who scan their QR codes.
- **Order History**: Users can view their order history in their profile.

### Bonus Points
- **Reward Mechanism**: Users receive bonus points equal to the order price for each order. These points can be used for future orders, with a cost multiplier of x10.

### News
- **News Section**: News and updates are displayed in both the application and website.

## Technologies Used
- **Spring Boot**: Backend logic, RESTful API development, authentication with Spring Security and JWT.
- **Spring Data JPA**: For data access and management.
- **Hibernate**: As the JPA provider.
- **Next.js**: Frontend web application development and menu display.
- **Kotlin**: Android application development with real-time order processing.
- **Database**: MySQL hosted in Azure.
- **Authentication**: User authentication with Spring Security and JWT, including role-based access control.
- **Azure Services**:
  - Azure App Services for hosting both backend and frontend.
  - GitHub Actions for CI/CD for backend.
  - Azure DevOps Pipelines for CI/CD for frontend.
  - Azure Storage for saving images of food, user profile, cafe, and news.
  - Azure Storage for hosting the Android APK file, which can be downloaded from the website.
 


## Authors

- [Tsebulia Denys](https://github.com/d3nnyyy): Backend developer, DevOps.
- [Horak Maksym](https://github.com/hurrr1cane): Android developer.
- [Soloviy Nazariy](https://github.com/N1tingale): Frontend developer.

## License

This project is licensed under the [MIT License](LICENSE).
