<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Employee Management System</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        .card {
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <div class="container mt-5">
        <h1 class="text-center mb-4">Employee Management System</h1>
        <div class="row">
            <div class="col-md-4">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">Employee Details</h5>
                        <p class="card-text">View all employee details.</p>
                        <a href="employee_details.php" class="btn btn-primary">View Details</a>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">Admin Management</h5>
                        <p class="card-text">Manage employee records.</p>
                        <a href="admin_management.php" class="btn btn-primary">Manage Employees</a>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">Employee Dashboard</h5>
                        <p class="card-text">View employee insights and analysis.</p>
                        <a href="employee_dashboard.php" class="btn btn-primary">View Dashboard</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>