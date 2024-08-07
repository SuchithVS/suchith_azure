<?php
require_once "config.php";

$sql = "SELECT * FROM employees";
$result = mysqli_query($link, $sql);

header('Content-Type: text/csv');
header('Content-Disposition: attachment; filename="employee_details.csv"');

$output = fopen('php://output', 'w');

fputcsv($output, array('Name', 'ID', 'Department', 'Role', 'Salary', 'Performance Score', 'Projects Completed', 'Training Hours', 'Last Promotion Date'));

while ($row = mysqli_fetch_assoc($result)) {
    fputcsv($output, array(
        $row['name'],
        $row['id'],
        $row['department'],
        $row['role'],
        '₹' . number_format($row['salary'], 2),
        $row['performance_score'],
        $row['projects_completed'],
        $row['training_hours'],
        $row['last_promotion_date']
    ));
}

fclose($output);
mysqli_close($link);
?>