<?php
require_once "config.php";

$sql = "SELECT * FROM employees";
$result = mysqli_query($link, $sql);

header("Content-Type: text/html");
header("Content-Disposition: attachment; filename=employee_details.html");

echo "<html><head><title>Employee Details</title></head><body>";
echo "<table border='1'>";
echo "<tr>
        <th>Name</th>
        <th>ID</th>
        <th>Department</th>
        <th>Role</th>
        <th>Salary</th>
        <th>Performance Score</th>
        <th>Projects Completed</th>
        <th>Training Hours</th>
        <th>Last Promotion Date</th>
      </tr>";

while($row = mysqli_fetch_assoc($result)) {
    echo "<tr>";
    echo "<td>" . $row['name'] . "</td>";
    echo "<td>" . $row['id'] . "</td>";
    echo "<td>" . $row['department'] . "</td>";
    echo "<td>" . $row['role'] . "</td>";
    echo "<td>₹" . number_format($row['salary'], 2) . "</td>";
    echo "<td>" . $row['performance_score'] . "</td>";
    echo "<td>" . $row['projects_completed'] . "</td>";
    echo "<td>" . $row['training_hours'] . "</td>";
    echo "<td>" . $row['last_promotion_date'] . "</td>";
    echo "</tr>";
}

echo "</table></body></html>";

mysqli_close($link);
?>