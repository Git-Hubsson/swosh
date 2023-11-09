# Swosh Banking System
## Overview
The Swosh Banking System is a comprehensive Java-based application designed to simulate banking operations. This robust system is structured to provide distinct functionalities for administrators and users, integrating seamlessly with a SQL database to manage user accounts, transactions, and administrative tasks.

## Features
User Management: Securely manage user information, including adding, updating, and deleting user details.

Account Operations: Administer bank accounts with features to add, delete, and retrieve account information based on user IDs.

Transaction Processing: Facilitate financial transactions between accounts and provide detailed transaction histories.

Administrative Controls: Specialized administrative view for managing users, accounts, and viewing transaction logs.

Security: Incorporates password hashing and verification for enhanced security. Also, to ensure the security and integrity of database interactions, the application extensively utilizes prepared statements. This practice is pivotal in preventing SQL injection attacks, thereby safeguarding sensitive user data and maintaining robust database security.

Database Integration: Utilizes a robust SQL database for storing and managing all user, account, and transaction data.


## Modules
### Models:

Account 

Transaction

User
### Services:

AccountService: Manages account-related operations.
DatabaseManager: Handles database connections and configurations.
PasswordManager: Provides secure password management.
SwoshTableCreator: Initializes database tables.
TransactionService: Handles all aspects of financial transactions.
UserService: Facilitates user-related services.
### Views:

AdminView: Interface for administrators with comprehensive system control.
UserView: Interface for standard users focusing on personal banking operations.
### Main:

The application's entry point, handling initial setup and user authentication.
## Getting Started
Clone the repository.
Ensure Java and SQL database setup.
Run Main.java to start the application.
Follow on-screen instructions to navigate through the system.
