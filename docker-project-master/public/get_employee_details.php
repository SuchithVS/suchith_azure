<?php
require_once "config.php";

if(isset($_GET['id'])) {
    $id = mysqli_real_escape_string($link, $_GET['id']);
    $sql = "SELECT * FROM employees WHERE id = $id";
    $result = mysqli_query($link, $sql);

    if($result && mysqli_num_rows($result) > 0) {
        $row = mysqli_fetch_assoc($result);
        echo "<p><strong>Name:</strong> " . $row['name'] . "</p>";
        echo "<p><strong>Employee ID:</strong> " . $row['id'] . "</p>";
        echo "<p><strong>Department:</strong> " . $row['department'] . "</p>";
        echo "<p><strong>Role:</strong> " . $row['role'] . "</p>";
        echo "<p><strong>Salary:</strong> ₹" . number_format($row['salary'], 2) . "</p>";
        echo "<p><strong>Performance Score:</strong> " . $row['performance_score'] . "</p>";
        echo "<p><strong>Projects Completed:</strong> " . $row['projects_completed'] . "</p>";
        echo "<p><strong>Training Hours:</strong> " . $row['training_hours'] . "</p>";
        echo "<p><strong>Last Promotion Date:</strong> " . $row['last_promotion_date'] . "</p>";
    } else {
        echo "Employee not found.";
    }
    mysqli_close($link);
} else {
    echo "Invalid request.";
}
?>