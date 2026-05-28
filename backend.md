# Backend API Endpoints Documentation

This document lists all the backend API endpoints for the Task Management System.

**Base URL:** `http://localhost:5004`

---

## 1. Authentication
  
### Register Employee
- **URL:** `/auth/register`
- **Method:** `POST`
- **Request Body:**
  ```json
  {
    "name": "Firstname Lastname",
    "email": "user@koderztech.com",
    "employeeId": "KDZ123",
    "password": "Password123!",
    "role": "EMPLOYEE", // or ADMIN
    "designation": "DEVELOPER" // or relevant designation
  }
  ```
- **Response:** `ApiResponseDTO<EmployeeRegistrationResponseDTO>`

### Login
- **URL:** `/auth/login`
- **Method:** `POST`
- **Request Body:**
  ```json
  {
    "employeeId": "KDZ123",
    "password": "Password123!",
    "role": "EMPLOYEE"
  }
  ```
- **Response:** `ApiResponseDTO<EmployeeRegistrationResponseDTO>`

---

## 2. Employees

### Get All Employees (Paginated)
- **URL:** `/employees`
- **Method:** `GET`
- **Query Parameters:**
  - `page` (default: 0)
  - `size` (default: 10)
  - `sortBy` (default: "id")
- **Response:** `Page<EmployeeRegistrationResponseDTO>`

### Update Employee
- **URL:** `/update/{employeeId}`
- **Method:** `PATCH`
- **Path Variable:** `employeeId` (e.g., KDZ123)
- **Request Body:**
  ```json
  {
    "name": "Updated Name",
    "email": "updated@koderztech.com",
    "role": "MANAGER",
    "designation": "SENIOR_DEVELOPER"
  }
  ```
- **Response:** `ApiResponseDTO<EmployeeRegistrationResponseDTO>`

### Delete Employee
- **URL:** `/{id}`
- **Method:** `DELETE`
- **Path Variable:** `id` (Long database ID)
- **Response:** `ApiResponseDTO`

---

## 3. Tasks

### Create Task
- **URL:** `/task`
- **Method:** `POST`
- **Request Body:**
  ```json
  {
    "title": "Task Title",
    "description": "Task Description",
    "teamId": "Team A",
    "assignedToId": "KDZ123",
    "dueDate": "2023-12-31",
    "status": "NEW" // NEW ASSIGN DONE
  }
  ```
- **Response:** `ApiResponseDTO<TaskResponseDTO>`

### Get Task By ID
- **URL:** `/task/{id}`
- **Method:** `GET`
- **Path Variable:** `id` (Long database ID)
- **Response:** `ApiResponseDTO<TaskResponseDTO>`

### Get All Tasks
- **URL:** `/tasks`
- **Method:** `GET`
- **Response:** `ApiResponseDTO<List<TaskResponseDTO>>`

### Update Task
- **URL:** `/task/{id}`
- **Method:** `PUT`
- **Path Variable:** `id` (Long database ID)
- **Request Body:** `TaskRequestDTO` (same as Create Task)
- **Response:** `ApiResponseDTO<TaskResponseDTO>`

### Delete Task
- **URL:** `/task/{id}`
- **Method:** `DELETE`
- **Path Variable:** `id` (Long database ID)
- **Response:** `ApiResponseDTO`

---

## 4. Comments

### Add Comment to Task
- **URL:** `/tasks/{id}/comments`
- **Method:** `POST`
- **Path Variable:** `id` (Task database ID)
- **Request Body:**
  ```json
  {
    "message": "This is a comment",
    "employeeId": "KDZ123",
    "name": "Employee Name"
  }
  ```
- **Response:** `ApiResponseDTO<CommentCreateResponse>`

---

## 5. Analytics

### Get Analytics Overview
- **URL:** `/analytics/overview`
- **Method:** `GET`
- **Response:** `ApiResponseDTO<AnalyticsResponseDTO>`

---

## Data Structures (DTOs)

### ApiResponseDTO
```json
{
  "success": true,
  "message": "Success message",
  "data": { ... } // Actual data object
}
```

### EmployeeRegistrationResponseDTO
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@koderztech.com",
  "employeeId": "KDZ001",
  "role": "EMPLOYEE",
  "designation": "DEVELOPER"
}
```

### TaskResponseDTO
```json
{
  "id": 1,
  "title": "Task 1",
  "description": "Description",
  "teamId": "Team A",
  "assignedToId": "KDZ001",
  "dueDate": "2023-12-31",
  "status": "TODO",
  "createdAt": "2023-01-01T00:00:00",
  "updatedAt": "2023-01-01T00:00:00"
}
```
