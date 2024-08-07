<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Employee Management System</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <style>
        body {
            background: linear-gradient(135deg, #1e3c72 0%, #2a5298 100%);
            min-height: 100vh;
            margin: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            font-family: 'Arial', sans-serif;
            perspective: 1000px;
        }
        .container {
            background: rgba(255, 255, 255, 0.1);
            border-radius: 20px;
            box-shadow: 0 8px 32px rgba(31, 38, 135, 0.37);
            backdrop-filter: blur(4px);
            border: 1px solid rgba(255, 255, 255, 0.18);
            padding: 40px;
        }
        .card {
            background: rgba(255, 255, 255, 0.1);
            border: none;
            border-radius: 15px;
            padding: 30px;
            margin: 20px;
            transition: all 0.3s ease;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            height: 400px;
            display: flex;
            flex-direction: column;
            justify-content: space-between;
            align-items: center;
            text-align: center;
            position: relative;
            overflow: hidden;
        }
        .card:hover {
            transform: translateY(-10px) scale(1.02);
            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.3);
            background: rgba(255, 255, 255, 0.2);
        }
        .card::before {
            content: '';
            position: absolute;
            top: -50%;
            left: -50%;
            width: 200%;
            height: 200%;
            background: radial-gradient(circle, rgba(255,255,255,0.3) 0%, transparent 70%);
            transform: scale(0);
            transition: transform 0.5s ease;
        }
        .card:hover::before {
            transform: scale(1);
        }
        .card-title {
            font-weight: bold;
            font-size: 24px;
            color: #ffffff;
            margin: 20px 0;
            transition: all 0.3s ease;
        }
        .card:hover .card-title {
            transform: scale(1.05);
        }
        .card-text {
            font-size: 16px;
            color: #e6e6e6;
            margin: 15px 0;
            flex-grow: 1;
            transition: all 0.3s ease;
        }
        .card:hover .card-text {
            color: #ffffff;
        }
        .btn {
            padding: 12px 25px;
            font-size: 16px;
            border-radius: 50px;
            background: linear-gradient(45deg, #3498db, #2980b9);
            border: none;
            color: #fff;
            transition: all 0.3s ease;
            margin-top: 20px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
            width: 100%;
            position: relative;
            overflow: hidden;
        }
        .btn:hover {
            background: linear-gradient(45deg, #2980b9, #3498db);
            transform: translateY(-3px);
            box-shadow: 0 6px 20px rgba(0, 0, 0, 0.3);
        }
        .btn::after {
            content: '';
            position: absolute;
            top: -50%;
            left: -50%;
            width: 200%;
            height: 200%;
            background: radial-gradient(circle, rgba(255,255,255,0.3) 0%, transparent 70%);
            transform: scale(0);
            transition: transform 0.5s ease;
        }
        .btn:hover::after {
            transform: scale(1);
        }
        h1 {
            color: #fff;
            text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
            font-size: 3rem;
            margin-bottom: 40px;
            text-align: center;
        }
        .card-icon {
            font-size: 64px;
            color: #ffffff;
            margin-bottom: 20px;
            transition: all 0.3s ease;
        }
        .card:hover .card-icon {
            transform: scale(1.1) rotate(10deg);
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Employee Management System</h1>
        <div class="row">
            <div class="col-md-4">
                <div class="card">
                    <i class="fas fa-user-tie card-icon"></i>
                    <h5 class="card-title">Employee Details</h5>
                    <p class="card-text">Access comprehensive employee details, including personal information, job roles, and performance metrics.</p>
                    <a href="employee_details.php" class="btn">View Details</a>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card">
                    <i class="fas fa-cogs card-icon"></i>
                    <h5 class="card-title">Admin Management</h5>
                    <p class="card-text">Manage employee records efficiently by adding, editing, and deleting information for effective management.</p>
                    <a href="admin_management.php" class="btn">Manage Employees</a>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card">
                    <i class="fas fa-chart-line card-icon"></i>
                    <h5 class="card-title">AI Dashboard</h5>
                    <p class="card-text">Gain insights into employee performance, attendance, and training records. Enhance productivity and satisfaction.</p>
                    <a href="employee_dashboard.php" class="btn">View Dashboard</a>
                </div>
            </div>
        </div>
    </div>
</body>
</html>