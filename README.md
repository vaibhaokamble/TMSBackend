# 📌 Task Management System (Backend)

A scalable and secure RESTful backend application built using **Java, Spring Boot, and MySQL** to manage tasks efficiently. This project provides APIs for task creation, assignment, tracking, and status management with proper authentication and layered architecture.

---

## 🚀 Features

- Create, update, delete, and fetch tasks
- Task status management (Pending, In Progress, Completed)
- Set task priorities and deadlines
- Employee/User task assignment
- RESTful API architecture
- Authentication & Authorization
- Global Exception Handling
- Input Validation using Jakarta Validation
- Layered Architecture (Controller → Service → Repository)
- Database integration with JPA & Hibernate
- API testing using Postman

---

## 🛠️ Tech Stack

| Technology | Usage |
|------------|-------|
| Java | Programming Language |
| Spring Boot | Backend Framework |
| Spring MVC | REST API Development |
| Spring Data JPA | Database Operations |
| Hibernate | ORM Framework |
| MySQL | Relational Database |
| Maven | Build Tool |
| Lombok | Boilerplate Reduction |
| Postman | API Testing |
| Git & GitHub | Version Control |

---

## 📂 Project Structure

```bash
taskManagement/
│── src/
│   ├── main/
│   │   ├── java/com/organization/taskManagement/
│   │   │   ├── Controller/
│   │   │   ├── Service/
│   │   │   ├── Repository/
│   │   │   ├── DTO/
│   │   │   ├── Model/
│   │   │   ├── Enums/
│   │   │   ├── Config/
│   │   │   ├── Exception/
│   │   │   └── Mappers/
│   │   │
│   │   └── resources/
│   │       ├── application.yaml
│   │
│── pom.xml
│── README.md
```

---

## ⚙️ Installation & Setup

### 1️⃣ Clone Repository

```bash
git clone https://github.com/vaibhaokamble/TMSBackend.git
```

### 2️⃣ Navigate to Project

```bash
cd taskManagement
```

### 3️⃣ Configure Database

Update `application.properties`

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/tsm_db
spring.datasource.username=root
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

### 4️⃣ Run the Application

Using Maven:

```bash
mvn spring-boot:run
```

Application will start on:

```bash
http://localhost:8080
```

---

## 🔗 API Endpoints

### Task APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/tasks` | Create Task |
| GET | `/tasks` | Get All Tasks |
| GET | `/tasks/{id}` | Get Task By ID |
| PUT | `/tasks/{id}` | Update Task |
| DELETE | `/tasks/{id}` | Delete Task |

---

## 📦 Sample Request JSON

```json
{
  "title": "Complete Backend API",
  "description": "Develop task management APIs",
  "status": "IN_PROGRESS",
  "priority": "HIGH",
  "deadline": "2026-05-20"
}
```

---

## 🧪 API Testing

Use **Postman** to test APIs.

Example:

```http
POST http://localhost:8080/tasks
```

---

## 🔐 Authentication

- Spring Security based authentication
- Protected APIs
- Role-based authorization support

---

## 🚨 Exception Handling

Custom exception handling implemented using:

- `@ControllerAdvice`
- `@ExceptionHandler`

Provides meaningful API responses for errors and validations.

---

## 📈 Future Enhancements

- JWT Authentication
- Swagger/OpenAPI Documentation
- Email Notifications
- Task Comments & Attachments
- Docker Deployment
- Microservices Architecture

---

## 👨‍💻 Author

### Vaibhao Kamble

Java Full Stack Developer

### Skills

- Java
- Spring Boot
- REST APIs
- MySQL
- Microservices
- AWS

---

## ⭐ Support

If you like this project, give it a ⭐ on GitHub and support the repository.
