# E-HUB — Supermarket Employee Assistant

## About the Project

**E-HUB is an Android application designed to improve communication and coordination between supermarket employees and departments.**

In a supermarket, employees sometimes need to request products or items from another department. E-HUB aims to make this process easier by providing a shared product search and a central place to create, send, and manage these requests.

The application is being developed as a private tool for supermarket employees. It uses its own database and operates independently of the supermarket's existing proprietary software and database.

## The Problem E-HUB Aims to Solve

When an employee needs something from another department, communication can take time and requests can be overlooked or forgotten.

E-HUB aims to make this process more organized by allowing employees to:

- Find products quickly using a shared product database.
- Send requests directly to the department responsible for handling them.
- View requests assigned to their department in one place.
- Track which requests have been completed.

The goal is to reduce unnecessary communication, improve coordination, and help employees handle their daily tasks more efficiently.

## How It Will Work

### 1. Product Search

Employees will be able to search for products using their product codes or descriptions. Search results will include the product name, code, and a link to its online product page when available.

Product search is intended to be available across the application's departments.

### 2. Customer Support

The Customer Support screen will allow employees to create requests by selecting a product or entering its code manually.

A request can include:
- Product code and description, when available
- Requested quantity, including decimal quantities when needed
- Location information
- An optional message
- The destination department

The employee can then send the request to the appropriate department.

### 3. Department Requests

The receiving department will have a shared list of incoming requests assigned to it.

Employees will be able to view the requested items and mark requests as completed. This will help the department keep track of outstanding work.

The planned departments are:

- **Customer Support (Εξυπηρέτηση)**
- **Warehouse (Αποθήκη)**
- **Produce (Μανάβικο)**
- **Refrigeration (Ψυγεία)**

### 4. Employee Accounts

A future stage will introduce employee registration, administrator approval, and login.

Each employee will have a consistent identity within the application. Employees will be able to select their active department without creating a separate account for each department.

The planned account system will help associate requests and completed work with the employees responsible for them.

## Technology

E-HUB is being developed using:

- **Kotlin** — Android application logic
- **XML layouts** — Android user interface
- **Android Studio** — development environment
- **Supabase and PostgreSQL** — online database and backend services

The backend is designed to be independent of the Android user interface, allowing the database or backend technology to be changed in the future if needed.

## Development Status

E-HUB is currently under active development.

The shared product search has been implemented and connected to the online product database. The Customer Support request workflow and department request screens are being developed and tested.

Employee accounts, final access permissions, and production security are planned for later stages. Features described as planned above should not be considered complete until implemented and tested.

## Project Goal

The goal of E-HUB is to build a practical, easy-to-use tool that helps supermarket employees find products, communicate requests between departments, and organize their daily work.

The project is being developed incrementally, with the aim of testing the complete workflow before preparing the application for real-world use.
