CREATE DATABASE IF NOT EXISTS mydb;
USE mydb;

CREATE TABLE IF NOT EXISTS employees (
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    department VARCHAR(100) NOT NULL,
    role VARCHAR(100) NOT NULL,
    gender ENUM('Male', 'Female', 'Other') NOT NULL,
    date_of_joining DATE NOT NULL,
    salary DECIMAL(10, 2) NOT NULL,
    performance_score INT,
    projects_completed INT,
    training_hours INT,
    last_promotion_date DATE
);

INSERT INTO employees (name, department, role, gender, date_of_joining, salary, performance_score, projects_completed, training_hours, last_promotion_date)
VALUES
('John Doe', 'IT', 'Developer', 'Male', '2023-01-15', 75000.00, 85, 8, 30, '2023-01-15'),
('Jane Smith', 'HR', 'Manager', 'Female', '2022-05-01', 85000.00, 92, 12, 45, '2022-05-01'),
('Alex Johnson', 'Marketing', 'Specialist', 'Other', '2023-03-10', 65000.00, 78, 6, 25, '2023-03-10'),
('Emily Brown', 'Finance', 'Analyst', 'Female', '2022-09-20', 70000.00, 88, 9, 35, '2022-09-20'),
('Michael Wilson', 'Sales', 'Representative', 'Male', '2023-02-05', 72000.00, 82, 11, 28, '2023-02-05'),
('Sarah Davis', 'IT', 'Project Manager', 'Female', '2022-07-12', 80000.00, 90, 15, 50, '2022-07-12'),
('David Lee', 'Marketing', 'Content Creator', 'Male', '2023-04-18', 68000.00, 75, 7, 20, '2023-04-18'),
('Lisa Taylor', 'HR', 'Recruiter', 'Female', '2022-11-30', 69000.00, 86, 10, 40, '2022-11-30'),
('Robert Martinez', 'Finance', 'Controller', 'Male', '2022-08-25', 82000.00, 89, 13, 38, '2022-08-25'),
('Amanda Clark', 'Sales', 'Manager', 'Female', '2023-01-07', 78000.00, 91, 14, 42, '2023-01-07');