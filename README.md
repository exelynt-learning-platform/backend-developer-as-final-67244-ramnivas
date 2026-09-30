# backend-developer-as-final-67244-ramnivas
Final Project Assignment - This repository contains the complete final project code and documentation.

Vehicle Booking System API
A RESTful backend service for managing users, vehicles, and bookings. Built with Spring Boot, it provides authentication, user booking flows, and admin management features.

🚀 About the Project
The Vehicle Booking System allows:

Users: Register, log in, and book vehicles.

Admins: Manage vehicles, view users, and oversee bookings.

Global Access: View available vehicles without authentication.

The API is organized into:

Authentication → /login, /register

User Calls → /auth/user/... (book, cancel, view bookings)

Admin Calls → /auth/admin/... (add/update vehicles, view users/bookings)

Global Calls → /vehicle (list all vehicles)

⚙️ Tech Stack
Backend: Java, Spring Boot

Security: Spring Security, JWT Authentication

Database: (configure your choice, e.g., MySQL/PostgreSQL)

Build Tool: Maven/Gradle

🛠️ Setup Instructions
1. Clone the repository
bash
git clone "copy the link from the github repo"
cd vehicleBookingSystem
2. Configure application properties
Edit src/main/resources/application.properties:

properties
spring.datasource.url=jdbc:mysql://localhost:3306/vehicle_booking
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
jwt.secret=your-secret-key
3. Build and run
bash
mvn clean install
mvn spring-boot:run
Server runs at:

Code
http://localhost:8080
🔑 Authentication
Public endpoints: /login, /register

Protected endpoints: Require JWT Bearer token in Authorization header

Example login request:

json
POST /login
{
  "email": "user@example.com",
  "password": "yourpassword"
}
Response: JWT token to use in subsequent requests.

📚 API Endpoints
User
POST /register → Register new user

POST /login → Authenticate and get JWT

Admin (JWT required)
POST /auth/admin/addVehicle → Add vehicle

POST /auth/admin/updateVehicle → Update vehicle

GET /auth/admin/users → Get all users

GET /auth/admin/bookings → Get all bookings

User Calls (JWT required)
POST /auth/user/bookVehicle → Book a vehicle

POST /auth/user/cancelBooking/{bookingId} → Cancel booking

GET /auth/user/bookedVehicles → View user’s booked vehicles

Global (no auth required)
GET /vehicle → Get all available vehicles

✅ Example Usage
Book a Vehicle
json
POST /auth/user/bookVehicle
Authorization: Bearer <jwt-token>
{
  "vehicleId": 6,
  "deliveryDate": "2026-10-07"
}
Response: Booking confirmation with booking ID and vehicle details.

📌 Notes
Ensure database is running before starting the app.

JWT tokens expire based on configuration (maxAge in cookie).

Admin endpoints require an admin role.