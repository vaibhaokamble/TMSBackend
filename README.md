# 🚀 Task Management System - Spring Boot Backend

A professional Task Management Backend Application built using Java Spring Boot following enterprise-level architecture and REST API best practices.

This project allows organizations and teams to manage employees, tasks, comments, and task assignments efficiently.

Built using:
- Java
- Spring Boot
- Spring Data JPA
- Hibernate
- MySQL
- Maven
- Lombok

Layered architecture with Controller, Service, Repository, DTO, and Entity separation is considered a recommended Spring Boot backend structure. :contentReference[oaicite:0]{index=0}

---

# ✨ Features

## 👨‍💼 Employee Management
- Employee Registration
- Employee Role Management
- Employee Designation Handling

## 📋 Task Management
- Create Task
- Update Task
- Delete Task
- Get Task By ID
- Get All Tasks
- Assign Task to Employee
- Enum-based Task Status

## 💬 Comment Management
- Add Comments to Tasks
- OneToMany & ManyToOne Relationship Mapping

## 🛡️ Backend Best Practices
- DTO Layer
- Layered Architecture
- Exception Handling
- RESTful APIs
- JPA/Hibernate Relationships
- Lombok Integration

---

# 🛠️ Tech Stack

| Technology | Version |
|---|---|
| Java | 21 |
| Spring Boot | 4.x |
| Spring Data JPA | Latest |
| Hibernate | Latest |
| MySQL | 8+ |
| Maven | Latest |
| Lombok | Latest |

---

# 📂 Project Structure

```bash
src/main/java/com/organization/taskManagement
│
├── Config
├── Controller
├── DTO
├── enums
├── Mapper
├── Model
├── Repos
├── Services
└── TaskManagementApplication.java
