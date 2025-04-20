# 📚 Library Management System

A modular **Java console-based application** to manage books and users in a library. It supports multiple roles (Students and Librarians), user authentication, book borrowing/returning, and CSV-based file handling for persistent storage.

---

## ✨ Features

### 👤 User Roles
- **Students:** Register, log in, borrow & return books, view borrowed books.  
- **Librarians:** Add books, view all books, approve pending librarian applications.

### 🗃 Data Persistence
- Load and save books and student records using **CSV files**.
- Auto-save and load on application start and exit.

### 🔐 Authentication
- Email-password based login system.
- Admin librarian account is pre-registered.

### 🔎 Search Functionality
- Search books by **title** or **author**.

---

## 🛠 Tech Stack

- **Language:** Java  
- **Concepts Used:** OOP, Inheritance, Exception Handling, File I/O, Collections (Map, List)  
- **Tools:** IntelliJ IDEA, OpenJDK

---

## 📁 Project Structure

LibraryManagementSystem/ 
├── LibraryMain.java # Main driver class 
├── Library.java # Core logic (users, books, file handling) 
├── User.java # Abstract base class 
├── Student.java # Student logic 
├── Librarian.java # Librarian logic 
├── Book.java # Book model 
├── books.csv # Stores all books 
├── students.csv # Stores all registered students


---

## 📂 How to Run

1. Clone or download the project.
2. Ensure `books.csv` and `students.csv` are in the **project root directory**.
3. Compile and run `LibraryMain.java`.

---

## 📝 Sample Admin Credentials

Email: admin@library.com
Password: admin123
