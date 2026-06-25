# Recipe Management API

## Overview

This project is a RESTful API developed as part of an exam assignment. The application provides user authentication and authorization, recipe management, recipe categorization, rating functionality, filtering capabilities, and paginated responses.

## Features

### Authentication

* User registration
* User login
* Secure cookie based authentication
* Password hashing and validation

### Authorization

* Role-based access control
* Protected endpoints accessible only to authenticated users
* Permission management for recipe creation, updates, and deletion

### Recipe Management

* Create recipes
* Retrieve recipes
* Delete recipes

### Recipe Categories

* Create and manage recipe categories
* Assign categories to recipes
* Retrieve recipes by category

### Recipe Ratings

* Add ratings to recipes
* Calculate and display average recipe ratings
* View recipe ratings

### Filtering

Recipes can be filtered by:

* Rating
* Category name
* Recipe name

### Pagination

* Paginated recipe listing
* Configurable page size and page number parameters

## Technologies Used

*  Spring Boot 
* Hibernate
* Cookie based Authentication
* H2 in memory database

## Running the Project

1. Clone the repository.
2. Configure the database connection.
3. Run database migrations.
4. Start the application.
5. Access the API through Swagger or check for Postman Json file.

## Project Structure

* Authentication & Authorization
* Recipe Management
* Category Management
* Rating System
* Filtering & Pagination
* Database Layer
* API Controllers

