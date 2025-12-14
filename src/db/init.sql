
-- Admins Table
CREATE TABLE admins (
  admin_id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(255) UNIQUE,
  password VARCHAR(255),
  email VARCHAR(255) UNIQUE,
  created_at DATETIME,
  updated_at DATETIME
);

-- Users Table
CREATE TABLE users (
  user_id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(255) UNIQUE,
  password VARCHAR(255),
  email VARCHAR(255) UNIQUE,
  role VARCHAR(50), -- 'organizer', 'attendee'
  gender VARCHAR(20),
  created_at DATETIME,
  updated_at DATETIME
);

-- Categories Table
CREATE TABLE categories (
  category_id INT AUTO_INCREMENT PRIMARY KEY,
  category_name VARCHAR(255),
  created_at DATETIME,
  updated_at DATETIME
);

-- Events Table
CREATE TABLE events (
  event_id INT AUTO_INCREMENT PRIMARY KEY,
  event_name VARCHAR(255),
  date DATETIME,
  location VARCHAR(255),
  description TEXT,
  total_seats INT,
  availablity BOOLEAN,
  organizer_id INT,
  category_id INT,
  created_at DATETIME,
  updated_at DATETIME,
  FOREIGN KEY (organizer_id) REFERENCES users(user_id),
  FOREIGN KEY (category_id) REFERENCES categories(category_id)
);

-- Registrations Table
CREATE TABLE registrations (
  registration_id INT AUTO_INCREMENT PRIMARY KEY,
  event_id INT,
  attendee_id INT,
  registration_time DATETIME,
  confirmation_code VARCHAR(100) UNIQUE,
  status VARCHAR(20) DEFAULT 'confirmed',
  attended BOOLEAN DEFAULT FALSE,
  created_at DATETIME,
  updated_at DATETIME,
  UNIQUE (event_id, attendee_id),
  FOREIGN KEY (event_id) REFERENCES events(event_id),
  FOREIGN KEY (attendee_id) REFERENCES users(user_id)
);

-- Notifications Table
CREATE TABLE notifications (
  notification_id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT,
  admin_id INT,
  event_id INT,
  message TEXT,
  sent_time DATETIME,
  read_status BOOLEAN,
  created_at DATETIME,
  updated_at DATETIME,
  FOREIGN KEY (user_id) REFERENCES users(user_id),
  FOREIGN KEY (admin_id) REFERENCES admins(admin_id),
  FOREIGN KEY (event_id) REFERENCES events(event_id)
);
