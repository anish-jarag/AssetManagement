# 💼 Digital Asset Management System

A **Java-based console application** that helps efficiently manage digital and physical assets within an organization. Built using Object-Oriented Principles with seamless SQL database interaction and robust exception handling.

---

## 📂 Project Structure


---

## ⚙️ Core Functionalities

| Feature                 | Description                                                                 |
|-------------------------|-----------------------------------------------------------------------------|
| ➕ Add Asset            | Add a new asset to the database                                            |
| 🔄 Update Asset         | Modify asset details                                                       |
| ❌ Delete Asset         | Remove asset from system                                                   |
| 📥 Allocate Asset       | Allocate assets to employees                                               |
| 📤 Deallocate Asset     | Deallocate assets and update return date                                   |
| 🛠️ Maintenance          | Log asset maintenance activities (date, cost, description)                 |
| 📅 Reserve Asset        | Reserve an asset for a specific period                                     |
| 🔙 Withdraw Reservation | Cancel a reservation and make asset available again                        |

---

## 🗃️ Database Schema

### 🧑‍💼 `employees`
- `employee_id` (PK)
- `name`
- `department`
- `email`
- `password`

### 🖥️ `assets`
- `asset_id` (PK)
- `name`, `type`
- `serial_number`
-  `purchase_date`
- `location`
- `status`
- `owner_id` (FK → employees)

### 🛠️ `maintenance_records`
- `maintenance_id` (PK)
- `asset_id` (FK)
- `maintenance_date`
- `description`
- `cost`

### 🔄 `asset_allocations`
- `allocation_id` (PK)
- `asset_id`
- `employee_id` (FKs)
- `allocation_date`
- `return_date`

### 📆 `reservations`
- `reservation_id` (PK)
- `asset_id`, `employee_id` (FKs)
- `reservation_date`
- `start_date`
- `end_date`
- `status`

---

## 🚧 Exception Handling

| Exception                     | When It’s Thrown                                                                 |
|------------------------------|----------------------------------------------------------------------------------|
| `AssetNotFoundException`     | Thrown when an asset ID doesn’t exist in the database                           |
| `AssetNotMaintainException`  | Thrown when an asset hasn’t been maintained in the past 2 years                 |

---

## 💻 Technologies Used

- **Java**
- **MySQL / SQL**
- **JDBC**
- **JUnit** (Unit Testing)
- **Object-Oriented Programming**
- **Exception Handling**
- **Collections, Arrays, Loops**

---

## 🔑 How to Run

1. Clone the repository  
   ```bash
   git clone https://github.com/anish-jarag/AssetManagement.git
