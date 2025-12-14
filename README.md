# 📦 Event Management System - Java + MySQL

This is a local desktop application using **Java Swing** for the UI and **MySQL** (via **XAMPP**) for the database.

---

## 📁 Project Folder Structure


---

## ⚙️ How to Run the Project Locally

### ✅ Step 1: Install Requirements

- Java JDK 8+
- VS Code (or any IDE)
- XAMPP
- MySQL JDBC Driver (already included in `lib/`)

---

### ✅ Step 2: Set Up MySQL

1. Start **MySQL** using **XAMPP**.
2. Open **phpMyAdmin**.
3. Import the SQL file located at `src/db/init.sql` to create the database and tables.

> **Database name used:** `events_system`

---

### ✅ Step 3: Compile the Code

From the root of the project directory, run:

```bash
javac -cp "lib/*" -d out src/db/DBConnection.java src/db/DBTest.java
```

### ✅ Step 4: Run the Test (Check DB Connection)
```bash
java -cp "lib/*;out" db.DBTest
```

### 🛠️ Java Main App Entry Point
```bash
javac -cp "lib/*" -d out src/Main.java
java -cp "lib/*;out" Main
```

### 🔁 Notes for Team

- Always use lib/* to include external JARs (e.g., JDBC connector).

- Never commit the out/ folder to Git.

- Update init.sql if the database schema changes.

- Keep GUI logic in the auth/ and ui/ directories.

- Place data access classes (DAOs) in db/.


### Task we have to fix and finish 

- in user page and events management the events aviliable not scrolling and the design not good

- in home page the card of event not show currectly the date and time and location




---