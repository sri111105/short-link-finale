# URL Shortener Service

A simple URL Shortener built with Spring Boot, MySQL, and Vanilla JS.

## Prerequisites

- Java 8 or higher
- Maven
- MySQL Database

## Setup

1.  **Database Configuration**:
    The application connects to a MySQL database named `url_shortener`.
    Calculated credentials in `src/main/resources/application.properties`:
    - Username: `root`
    - Password: `root@123` (Please update this if your MySQL password is different)

    The application will automatically create the `url_shortener` database if it doesn't exist (assuming the user has permissions).

2.  **Build and Run**:
    This project includes a setup script to automatically install Maven (locally) and run the application.
    
    **Option 1: Automatic Setup (Recommended)**
    Run the PowerShell script in the project root:
    ```powershell
    .\setup_run.ps1
    ```

    **Option 2: Manual (If you have Maven installed)**
    ```sh
    mvn spring-boot:run
    ```

3.  **Usage**:
    - Open your browser and go to `http://localhost:8080`.
    - Enter a long URL to shorten it.
    - Click the short URL to test the redirection.
    - View the statistics in the dashboard below.
