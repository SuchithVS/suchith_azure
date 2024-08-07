<?php
require_once "config.php";
$sql = "SELECT * FROM employees";
$result = mysqli_query($link, $sql);
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin Management</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <style>
        body {
            background: linear-gradient(135deg, #1e3c72 0%, #2a5298 100%);
            min-height: 100vh;
            margin: 0;
            padding: 20px 0;
            font-family: 'Arial', sans-serif;
        }
        .container {
            background: rgba(255, 255, 255, 0.1);
            border-radius: 20px;
            box-shadow: 0 8px 32px rgba(31, 38, 135, 0.37);
            backdrop-filter: blur(4px);
            border: 1px solid rgba(255, 255, 255, 0.18);
            padding: 40px;
            width: 100%;
            max-width: 800px;
        }
        .card {
            background: rgba(255, 255, 255, 0.1);
            border: none;
            border-radius: 15px;
            padding: 20px;
            margin-bottom: 20px;
            transition: all 0.3s ease;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            color: #ffffff;
        }
        .card:hover {
            transform: translateY(-5px);
            box-shadow: 0 6px 20px rgba(0, 0, 0, 0.2);
            background: rgba(255, 255, 255, 0.2);
        }
        .card-title {
            font-weight: bold;
            font-size: 24px;
            margin-bottom: 15px;
        }
        .card-text {
            margin-bottom: 10px;
            font-size: 16px;
        }
        .btn-custom, .btn-danger {
            border: none;
            color: #fff;
            transition: all 0.3s ease;
            padding: 8px 16px;
            font-size: 14px;
            border-radius: 5px;
            width: 100px;
            text-align: center;
            margin-right: 10px;
        }
        .btn-custom {
            background: linear-gradient(45deg, #3498db, #2980b9);
        }
        .btn-danger {
            background: linear-gradient(45deg, #e74c3c, #c0392b);
        }
        .btn-custom:hover, .btn-danger:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
        }
        .action-buttons {
            display: flex;
            justify-content: flex-start;
            margin-top: 15px;
        }
        h1 {
            color: #fff;
            text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
            font-size: 2.5rem;
            margin-bottom: 30px;
            text-align: center;
        }
        #searchInput {
            background-color: rgba(255, 255, 255, 0.1);
            border: 1px solid rgba(255, 255, 255, 0.3);
            color: #fff;
            margin-bottom: 20px;
        }
        #sortSelect {
            background-color: rgba(255, 255, 255, 0.1);
            border: 1px solid rgba(255, 255, 255, 0.3);
            color: #fff;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <div class="container mt-5">
        <h1>Admin Management</h1>
        <a href="add_employee.php" class="btn btn-custom mb-4"><i class="fas fa-user-plus mr-2"></i>Add New Employee</a>
        <input type="text" id="searchInput" class="form-control" placeholder="Search employees...">
        <select id="sortSelect" class="form-control">
            <option value="name">Sort by Name</option>
            <option value="id">Sort by ID</option>
            <option value="department">Sort by Department</option>
            <option value="role">Sort by Role</option>
        </select>
        <div id="employeeCards">
            <?php
            if($result && mysqli_num_rows($result) > 0){
                while($row = mysqli_fetch_array($result)){
                    echo "<div class='card mb-4' data-name='" . strtolower($row['name']) . "' data-id='" . $row['id'] . "' data-department='" . strtolower($row['department']) . "' data-role='" . strtolower($row['role']) . "'>";
                    echo "<h5 class='card-title'>" . $row['name'] . "</h5>";
                    echo "<p class='card-text'><i class='fas fa-id-badge mr-2'></i>ID: " . $row['id'] . "</p>";
                    echo "<p class='card-text'><i class='fas fa-building mr-2'></i>Department: " . $row['department'] . "</p>";
                    echo "<p class='card-text'><i class='fas fa-user-tie mr-2'></i>Role: " . $row['role'] . "</p>";
                    echo "<div class='action-buttons'>";
                    echo "<a href='edit_employee.php?id=". $row['id'] ."' class='btn btn-custom'><i class='fas fa-edit mr-1'></i>Edit</a>";
                    echo "<a href='delete_employee.php?id=". $row['id'] ."' class='btn btn-danger'><i class='fas fa-trash-alt mr-1'></i>Delete</a>";
                    echo "</div>";
                    echo "</div>";
                }
                mysqli_free_result($result);
            } else{
                echo "<p class='lead text-white'><em>No records were found.</em></p>";
            }
            mysqli_close($link);
            ?>
        </div>
        <a href="index.php" class="btn btn-custom mt-3"><i class="fas fa-home mr-2"></i>Back to Home</a>
    </div>

    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script>
        $(document).ready(function() {
            // Search functionality
            $("#searchInput").on("keyup", function() {
                var value = $(this).val().toLowerCase();
                $("#employeeCards .card").filter(function() {
                    $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
                });
            });

            // Sort functionality
            $("#sortSelect").on("change", function() {
                var sortBy = $(this).val();
                var cards = $("#employeeCards .card").get();
                cards.sort(function(a, b) {
                    var aVal = $(a).data(sortBy);
                    var bVal = $(b).data(sortBy);
                    if (sortBy === 'id') {
                        return parseInt(aVal) - parseInt(bVal);
                    } else {
                        return aVal.localeCompare(bVal);
                    }
                });
                $("#employeeCards").html(cards);
            });
        });
    </script>
</body>
</html>