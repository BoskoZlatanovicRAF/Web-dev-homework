# Web Programming Homework Collection

This repository contains six homework assignments from a Web Development course, each demonstrating different aspects of web development from basic threading to advanced web applications with authentication.

## Homework 1: Multi-threaded Student Defense Simulation

**Purpose:** Demonstrates Java threading and concurrency concepts by simulating student defense sessions.

**Key Features:**
- Implements a multi-threaded simulation where professors and assistants evaluate students
- Uses Java's concurrency utilities like `BlockingQueue`, `CyclicBarrier`, and `AtomicInteger`
- Simulates student defense sessions with randomized arrival and defense times
- Calculates and reports average scores across all evaluated students

The program simulates a 5-second window where students arrive for defense sessions, which are handled concurrently by multiple professor threads and an assistant thread.

## Homework 2: Simple Chat Application

**Purpose:** Implements a client-server chat application using Java sockets.

**Key Features:**
- Multi-threaded server that handles multiple client connections
- Username verification to prevent duplicate usernames
- Message broadcasting to all connected clients
- Message censoring for specific words
- Message history storage (up to 100 messages)

The application allows multiple clients to connect to a central server, send messages that are broadcast to all users, and implements basic content moderation through word censoring.

## Homework 3: Basic Web Server with Quote Management

**Purpose:** Creates a simple HTTP server from scratch that manages and displays quotes.

**Key Features:**
- Custom HTTP server implementation in Java
- MVC architecture with controllers, services, and repositories
- Quote submission and display functionality
- Integration with a secondary "Quote of the Day" microservice
- Request/response handling for GET and POST methods

The application allows users to submit quotes through a form and displays all submitted quotes along with a randomly selected "Quote of the Day" provided by a separate service.

## Homework 4: Weekly Meal Selection System

**Purpose:** Demonstrates Java Servlets, session management, and form handling.

**Key Features:**
- Servlet-based web application
- User authentication (admin access)
- Weekly meal selection system by day
- Session management to track user selections
- Order counting and reporting for administrators

The system allows users to select meals for each day of the week and saves these selections. Administrators can view aggregate data about meal selections.

## Homework 5: RESTful Blog Platform

**Purpose:** Implements a RESTful API for a blog platform using the Jersey framework.

**Key Features:**
- Jersey-based RESTful API
- In-memory repository pattern
- Blog post creation and listing
- Comment functionality
- Input validation
- Frontend with JavaScript for interaction

Users can create posts, view post listings, read full posts, and add comments through a responsive web interface that communicates with the backend via REST APIs.

## Homework 6: Secure Blog Platform with Authentication

**Purpose:** Extends Homework 5 to add security features and database persistence.

**Key Features:**
- JWT-based authentication system
- MySQL database integration
- Protected API endpoints with authorization filters
- User login functionality
- RESTful API for posts and comments
- SPA (Single Page Application) frontend

This more advanced version of the blog platform adds security through JWT tokens, persists data in a MySQL database, and implements proper authentication and authorization for API access.

## Technologies Used Across Projects

- **Java** - Core language for all assignments
- **Concurrency** - Threads, Atomic variables, Barriers
- **Networking** - Sockets, HTTP, TCP/IP
- **Web** - Servlets, REST APIs, HTTP request/response
- **Frontend** - HTML, CSS, JavaScript
- **Data Storage** - In-memory collections, MySQL
- **Security** - JWT, password hashing
- **Build Tools** - Maven

Each homework builds on concepts from previous assignments, gradually introducing more advanced web development techniques.
