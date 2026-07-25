

https://github.com/user-attachments/assets/99999cb8-0926-497e-9032-52d321348e34



<img width="3073" height="1366" alt="mermaid-diagram" src="https://github.com/user-attachments/assets/fadce330-6d29-4f72-a3ac-e6eddf71121a" />
<img width="1388" height="482" alt="image" src="https://github.com/user-attachments/assets/6d0a19da-5e14-4d98-803a-6646a2d44fc7" />

## Project Workflow

### 1. User Authentication

* Users can register and log in through the React application.
* Passwords are securely hashed using **BCrypt** before being stored in **PostgreSQL**.
* After successful authentication, the server issues a **JWT** stored in an **HTTP-Only Cookie**.
* Protected endpoints require authentication and enforce **role-based authorization** (e.g., `USER` and `ADMIN`).

### 2. Product Management

* Users can browse and search products.
* Dynamic filtering and searching are implemented using **QueryDSL**, allowing efficient and flexible database queries.

### 3. Shopping Cart

* Shopping cart data and frequently accessed user information are stored in **Redis** to improve application performance and reduce database load.
* This enables faster cart operations and a better user experience.

### 4. Order Processing

* When a user places an order, the order information is first saved in **PostgreSQL**.
* The application then publishes an **Order Created** event to **RabbitMQ** for asynchronous processing.

### 5. Asynchronous Event Handling

* A **RabbitMQ Listener** consumes the order event.
* After receiving the event, it:

  * Clears the user's shopping cart stored in Redis.
  * Sends a real-time notification to the client through **WebSocket**.

### 6. Real-Time Notifications

* The React frontend maintains a **WebSocket** connection with the server.
* When an order is successfully processed, the client immediately receives a notification without refreshing the page.

### 7. Order Management

* **Regular users** can view only their own orders and order details.
* **Administrators** can view all orders in the system and filter them using various criteria such as customer, status, or date.
* 
### 8. Docker Deployment

The application and its required services are containerized using **Docker**.

The complete application environment can be started using **Docker Compose**, allowing the backend, frontend, Kafka, database, and other required services to run together in a consistent environment.


To stop the application:

docker compose down
---

### Technologies Used

* **Frontend:** React, Vite, TypeScript
* **Backend:** Spring Boot, POM
* **Database:** PostgreSQL
* **Caching:** Redis
* **Message Broker:** RabbitMQ
* **Real-Time Communication:** WebSocket (STOMP)
* **Dynamic Queries:** QueryDSL
* **Authentication:** JWT with HTTP-Only Cookies
* **Authorization:** Role-Based Access Control (RBAC)
* **Password Security:** BCrypt
