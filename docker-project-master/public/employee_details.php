<?php
require_once "config.php";

$sql = "SELECT * FROM employees";
$result = mysqli_query($link, $sql);

$allEmployees = [];
if ($result) {
    while ($row = mysqli_fetch_array($result)) {
        $allEmployees[] = $row;
    }
    mysqli_data_seek($result, 0); // Reset result pointer
}
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Employee Details</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="https://cdn.datatables.net/1.10.24/css/dataTables.bootstrap4.min.css">
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
            max-width: 1200px;
        }
        .btn-details {
            background: linear-gradient(45deg, #3498db, #2980b9);
            border: none;
            color: #fff;
            transition: all 0.3s ease;
        }
        .btn-details:hover {
            background: linear-gradient(45deg, #2980b9, #3498db);
            transform: translateY(-2px);
        }
        h1 {
            color: #fff;
            text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
            font-size: 2.5rem;
            margin-bottom: 30px;
            text-align: center;
        }
        .download-buttons {
            margin-top: 20px;
            text-align: center;
        }
        .download-buttons .btn {
            margin: 0 10px;
        }
        #employeeTable {
            color: #fff;
        }
        #employeeTable_wrapper {
            color: #fff;
        }
        #employeeTable_filter input {
            color: #fff;
            background-color: rgba(255, 255, 255, 0.1);
            border: 1px solid rgba(255, 255, 255, 0.3);
        }
        #employeeTable_length select {
            color: #fff;
            background-color: rgba(255, 255, 255, 0.1);
            border: 1px solid rgba(255, 255, 255, 0.3);
        }
        .dataTables_info, .dataTables_paginate {
            color: #fff !important;
        }
        .page-link {
            background-color: rgba(255, 255, 255, 0.1);
            border-color: rgba(255, 255, 255, 0.3);
            color: #fff;
        }
        .page-item.active .page-link {
            background-color: #3498db;
            border-color: #3498db;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Employee Details</h1>
        <table id="employeeTable" class="table table-striped table-bordered" style="width:100%">
            <thead>
                <tr>
                    <th>Name</th>
                    <th>Employee ID</th>
                    <th>Department</th>
                    <th>Role</th>
                    <th>Salary</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <?php
                if($result && mysqli_num_rows($result) > 0){
                    while($row = mysqli_fetch_array($result)){
                        echo "<tr>";
                        echo "<td>" . $row['name'] . "</td>";
                        echo "<td>" . $row['id'] . "</td>";
                        echo "<td>" . $row['department'] . "</td>";
                        echo "<td>" . $row['role'] . "</td>";
                        echo "<td>₹" . number_format($row['salary'], 2) . "</td>";
                        echo "<td><button class='btn btn-details btn-sm' onclick='showDetails(" . $row['id'] . ")'>View Details</button></td>";
                        echo "</tr>";
                    }
                    mysqli_free_result($result);
                }
                mysqli_close($link);
                ?>
            </tbody>
        </table>
        <div class="download-buttons">
            <a href="generate_pdf.php" class="btn btn-details">Download PDF</a>
            <a href="generate_excel.php" class="btn btn-details">Download Excel</a>
        </div>
        <div class="text-center mt-4">
            <a href="index.php" class="btn btn-details">Back to Home</a>
        </div>
    </div>

    <!-- Modal for employee details -->
    <div class="modal fade" id="employeeModal" tabindex="-1" role="dialog" aria-labelledby="employeeModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="employeeModalLabel">Employee Details</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body" id="employeeModalBody">
                    <!-- Employee details will be loaded here -->
                </div>
            </div>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="https://cdn.datatables.net/1.10.24/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/1.10.24/js/dataTables.bootstrap4.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <script>
        $(document).ready(function() {
            $('#employeeTable').DataTable({
                "pageLength": 10,
                "lengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]]
            });
        });

        function showDetails(employeeId) {
            $.ajax({
                url: 'get_employee_details.php',
                type: 'GET',
                data: { id: employeeId },
                success: function(response) {
                    $('#employeeModalBody').html(response);
                    $('#employeeModal').modal('show');
                },
                error: function() {
                    alert('Error fetching employee details');
                }
            });
        }
    </script>
</body>
</html>