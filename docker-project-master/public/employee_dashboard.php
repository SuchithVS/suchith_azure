<?php
require_once "config.php";

// Enhanced AI insights function
function generateInsights($employee, $allEmployees) {
    $insights = [];
    
    // Performance insight
    if ($employee['performance_score'] >= 90) {
        $insights[] = [
            'title' => 'Performance',
            'text' => "Excellent performer, consider for promotion or bonus.",
            'status' => 'good',
            'chart' => [
                'value' => $employee['performance_score'],
                'max' => 100,
                'threshold' => 90
            ]
        ];
    } elseif ($employee['performance_score'] >= 75) {
        $insights[] = [
            'title' => 'Performance',
            'text' => "Good performer, may benefit from additional responsibilities.",
            'status' => 'good',
            'chart' => [
                'value' => $employee['performance_score'],
                'max' => 100,
                'threshold' => 75
            ]
        ];
    } else {
        $insights[] = [
            'title' => 'Performance',
            'text' => "May need additional support or training to improve performance.",
            'status' => 'bad',
            'chart' => [
                'value' => $employee['performance_score'],
                'max' => 100,
                'threshold' => 75
            ]
        ];
    }

    // Project completion insight
    if ($employee['projects_completed'] > 10) {
        $insights[] = [
            'title' => 'Projects',
            'text' => "High project completion rate, consider for leadership roles.",
            'status' => 'good',
            'chart' => [
                'value' => $employee['projects_completed'],
                'max' => 15,
                'threshold' => 10
            ]
        ];
    } elseif ($employee['projects_completed'] < 5) {
        $insights[] = [
            'title' => 'Projects',
            'text' => "Low project completion rate, may need to review workload or provide assistance.",
            'status' => 'bad',
            'chart' => [
                'value' => $employee['projects_completed'],
                'max' => 15,
                'threshold' => 5
            ]
        ];
    } else {
        $insights[] = [
            'title' => 'Projects',
            'text' => "Average project completion rate, consider ways to improve efficiency.",
            'status' => 'neutral',
            'chart' => [
                'value' => $employee['projects_completed'],
                'max' => 15,
                'threshold' => 7
            ]
        ];
    }

    // Training insight
    if ($employee['training_hours'] < 20) {
        $insights[] = [
            'title' => 'Training',
            'text' => "Consider providing more training opportunities to enhance skills.",
            'status' => 'bad',
            'chart' => [
                'value' => $employee['training_hours'],
                'max' => 40,
                'threshold' => 20
            ]
        ];
    } else {
        $insights[] = [
            'title' => 'Training',
            'text' => "Good training completion. Keep up the good work!",
            'status' => 'good',
            'chart' => [
                'value' => $employee['training_hours'],
                'max' => 40,
                'threshold' => 20
            ]
        ];
    }

    // Retention insight
    $avgSalary = array_sum(array_column($allEmployees, 'salary')) / count($allEmployees);
    if ($employee['salary'] < $avgSalary * 0.9) {
        $insights[] = [
            'title' => 'Retention',
            'text' => "Salary below average. Consider a raise to improve retention.",
            'status' => 'bad',
            'chart' => [
                'value' => $employee['salary'],
                'max' => $avgSalary * 1.2,
                'threshold' => $avgSalary
            ]
        ];
    } else {
        $insights[] = [
            'title' => 'Retention',
            'text' => "Salary at or above average. Good for retention.",
            'status' => 'good',
            'chart' => [
                'value' => $employee['salary'],
                'max' => $avgSalary * 1.2,
                'threshold' => $avgSalary
            ]
        ];
    }

    return $insights;
}

$sql = "SELECT * FROM employees";
$result = mysqli_query($link, $sql);

// Prepare data for charts and analysis
$allEmployees = [];
$departments = [];
$performanceData = [0, 0, 0]; // Low, Medium, High
$projectCompletionData = [];
$salaryData = [];
$trainingData = [];

if ($result) {
    while ($row = mysqli_fetch_array($result)) {
        $allEmployees[] = $row;

        // Count departments
        if (!isset($departments[$row['department']])) {
            $departments[$row['department']] = 0;
        }
        $departments[$row['department']]++;

        // Performance data
        if ($row['performance_score'] < 75) {
            $performanceData[0]++;
        } elseif ($row['performance_score'] < 90) {
            $performanceData[1]++;
        } else {
            $performanceData[2]++;
        }

        // Project completion data
        $projectCompletionData[] = [
            'name' => $row['name'],
            'projects' => $row['projects_completed']
        ];

        // Salary data
        $salaryData[] = [
            'name' => $row['name'],
            'salary' => $row['salary']
        ];

        // Training data
        $trainingData[] = [
            'name' => $row['name'],
            'hours' => $row['training_hours']
        ];
    }
    mysqli_data_seek($result, 0); // Reset result pointer
}

// Calculate overall metrics
$avgSalary = array_sum(array_column($salaryData, 'salary')) / count($salaryData);
$avgPerformance = array_sum(array_column($allEmployees, 'performance_score')) / count($allEmployees);
$totalProjects = array_sum(array_column($allEmployees, 'projects_completed'));

// Identify top performers and those needing improvement
usort($allEmployees, function($a, $b) {
    return $b['performance_score'] - $a['performance_score'];
});
$topPerformers = array_slice($allEmployees, 0, 5);
$needsImprovement = array_slice($allEmployees, -5);
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Employee Management Dashboard</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <style>
        body {
            background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
        }
        .container-fluid {
            max-width: 1400px;
            margin: 0 auto;
        }
        .chart-container {
            position: relative;
            height: 250px;
            width: 100%;
        }
        .insight-chart {
            width: 80px;
            height: 80px;
            display: inline-block;
            margin-right: 10px;
        }
        .insight-text {
            display: inline-block;
            vertical-align: top;
            width: calc(100% - 100px);
        }
        .status-good {
            color: #28a745;
        }
        .status-bad {
            color: #dc3545;
        }
        .status-neutral {
            color: #ffc107;
        }
        .card {
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            transition: all 0.3s ease;
        }
        .card:hover {
            box-shadow: 0 6px 8px rgba(0, 0, 0, 0.15);
            transform: translateY(-5px);
        }
        .employee-card {
            height: 100%;
        }
        #searchInput {
            margin-bottom: 20px;
        }
        #sortSelect {
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <div class="container-fluid mt-5">
        <h2 class="text-center mb-4">Employee Management Dashboard</h2>
        
        <!-- Overall Metrics -->
        <div class="row mb-4">
            <div class="col-md-3">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">Average Salary</h5>
                        <p class="card-text">₹<?php echo number_format($avgSalary, 2); ?></p>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">Average Performance</h5>
                        <p class="card-text"><?php echo number_format($avgPerformance, 2); ?></p>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">Total Projects</h5>
                        <p class="card-text"><?php echo $totalProjects; ?></p>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">Total Employees</h5>
                        <p class="card-text"><?php echo count($allEmployees); ?></p>
                    </div>
                </div>
            </div>
        </div>

        <!-- Charts -->
        <div class="row mb-4">
            <div class="col-md-6 mb-4">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">Department Distribution</h5>
                        <div class="chart-container">
                            <canvas id="departmentChart"></canvas>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-6 mb-4">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">Performance Distribution</h5>
                        <div class="chart-container">
                            <canvas id="performanceChart"></canvas>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-6 mb-4">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">Projects Completed</h5>
                        <div class="chart-container">
                            <canvas id="projectCompletionChart"></canvas>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-6 mb-4">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">Salary Distribution</h5>
                        <div class="chart-container">
                            <canvas id="salaryDistributionChart"></canvas>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Top Performers and Needs Improvement -->
        <div class="row mb-4">
            <div class="col-md-6 mb-4">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">Top Performers</h5>
                        <ul class="list-group">
                            <?php foreach ($topPerformers as $employee): ?>
                                <li class="list-group-item d-flex justify-content-between align-items-center">
                                    <?php echo $employee['name']; ?>
                                    <span class="badge badge-primary badge-pill"><?php echo $employee['performance_score']; ?></span>
                                </li>
                            <?php endforeach; ?>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="col-md-6 mb-4">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">Needs Improvement</h5>
                        <ul class="list-group">
                            <?php foreach ($needsImprovement as $employee): ?>
                                <li class="list-group-item d-flex justify-content-between align-items-center">
                                    <?php echo $employee['name']; ?>
                                    <span class="badge badge-warning badge-pill"><?php echo $employee['performance_score']; ?></span>
                                </li>
                            <?php endforeach; ?>
                        </ul>
                    </div>
                </div>
            </div>
        </div>


        <h2>AI Insights</h2>
        <br>
        <!-- Search and Sort -->
        <div class="row mb-4">
            <div class="col-md-6">
                <input type="text" id="searchInput" class="form-control" placeholder="Search employees...">
            </div>
            <div class="col-md-6">
                <select id="sortSelect" class="form-control">
                    <option value="name">Sort by Name</option>
                    <option value="performance">Sort by Performance</option>
                    <option value="salary">Sort by Salary</option>
                </select>
            </div>
        </div>

        <!-- Employee Cards with AI Insights -->
        <div class="row" id="employeeCards">
            <?php
            if($result && mysqli_num_rows($result) > 0){
                while($row = mysqli_fetch_array($result)){
                    $insights = generateInsights($row, $allEmployees);
                    echo "<div class='col-md-4 mb-4 employee-card-container'>";
                    echo "<div class='card employee-card'>";
                    echo "<div class='card-body'>";
                    echo "<h5 class='card-title'>" . $row['name'] . "</h5>";
                    echo "<p class='card-text'>Department: " . $row['department'] . "</p>";
                    echo "<p class='card-text'>Role: " . $row['role'] . "</p>";
                    echo "<p class='card-text'>Salary: ₹" . number_format($row['salary'], 2) . "</p>";
                    echo "<p class='card-text'>Performance Score: " . $row['performance_score'] . "</p>";
                    echo "<h6 class='mt-4'>AI Insights:</h6>";
                    foreach ($insights as $index => $insight) {
                        echo "<div class='insight-container mb-3'>";
                        echo "<div class='insight-chart'>";
                        echo "<canvas id='insightChart" . $row['id'] . "-" . $index . "'></canvas>";
                        echo "</div>";
                        echo "<div class='insight-text'>";
                        echo "<p class='status-" . $insight['status'] . "'><strong>" . $insight['title'] . ":</strong> " . $insight['text'] . "</p>";
                        echo "</div>";
                        echo "</div>";
                        
                        // Generate chart for each insight
                        echo "<script>";
                        echo "new Chart(document.getElementById('insightChart" . $row['id'] . "-" . $index . "'), {";
                        echo "    type: 'doughnut',";
                        echo "    data: {";
                        echo "        datasets: [{";
                        echo "            data: [" . $insight['chart']['value'] . ", " . ($insight['chart']['max'] - $insight['chart']['value']) . "],";
                        echo "            backgroundColor: ['" . ($insight['status'] == 'good' ? '#28a745' : ($insight['status'] == 'bad' ? '#dc3545' : '#ffc107')) . "', '#e9ecef']";
                        echo "        }]";
                        echo "    },";
                        echo "    options: {";
                        echo "        cutout: '70%',";
                        echo "        responsive: true,";
                        echo "        maintainAspectRatio: false,";
                        echo "        legend: { display: false },";
                        echo "        tooltips: { enabled: false },";
                        echo "        animation: { duration: 2000 }";
                        echo "    }";
                        echo "});";
                        echo "</script>";
                    }
                    echo "</div>";
                    echo "</div>";
                    echo "</div>";
                }
                mysqli_free_result($result);
            } else{
                echo "<p class='lead'><em>No records were found.</em></p>";
            }
            mysqli_close($link);
            ?>
        </div>
        <a href="index.php" class="btn btn-primary mb-5">Back to Home</a>
    </div>

    <script>
        // Department Distribution Chart (Doughnut)
        var ctxDepartment = document.getElementById('departmentChart').getContext('2d');
        new Chart(ctxDepartment, {
            type: 'doughnut',
            data: {
                labels: <?php echo json_encode(array_keys($departments)); ?>,
                datasets: [{
                    data: <?php echo json_encode(array_values($departments)); ?>,
                    backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0', '#9966FF']
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                title: {
                    display: true,
                    text: 'Employee Distribution by Department'
                },
                animation: {
                    duration: 2000
                }
            }
        });

        // Performance Distribution Chart (Doughnut)
        var ctxPerformance = document.getElementById('performanceChart').getContext('2d');
        new Chart(ctxPerformance, {
            type: 'doughnut',
            data: {
                labels: ['Low', 'Medium', 'High'],
                datasets: [{
                    data: <?php echo json_encode($performanceData); ?>,
                    backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56']
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                title: {
                    display: true,
                    text: 'Performance Distribution'
                },
                animation: {
                    duration: 2000
                }
            }
        });

        // Project Completion Chart (Bar)
        var ctxProject = document.getElementById('projectCompletionChart').getContext('2d');
        new Chart(ctxProject, {
            type: 'bar',
            data: {
                labels: <?php echo json_encode(array_column($projectCompletionData, 'name')); ?>,
                datasets: [{
                    label: 'Projects Completed',
                    data: <?php echo json_encode(array_column($projectCompletionData, 'projects')); ?>,
                    backgroundColor: 'rgba(75, 192, 192, 0.6)',
                    borderColor: 'rgba(75, 192, 192, 1)',
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    y: {
                        beginAtZero: true
                    }
                },
                title: {
                    display: true,
                    text: 'Projects Completed by Employee'
                },
                animation: {
                    duration: 2000
                }
            }
        });

        // Salary Distribution Chart (Bar)
        var ctxSalary = document.getElementById('salaryDistributionChart').getContext('2d');
        new Chart(ctxSalary, {
            type: 'bar',
            data: {
                labels: <?php echo json_encode(array_column($salaryData, 'name')); ?>,
                datasets: [{
                    label: 'Salary',
                    data: <?php echo json_encode(array_column($salaryData, 'salary')); ?>,
                    backgroundColor: 'rgba(255, 159, 64, 0.6)',
                    borderColor: 'rgba(255, 159, 64, 1)',
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    y: {
                        beginAtZero: true
                    }
                },
                title: {
                    display: true,
                    text: 'Salary Distribution'
                },
                animation: {
                    duration: 2000
                }
            }
        });

        // Search and Sort functionality
        $(document).ready(function() {
            $('#searchInput').on('keyup', function() {
                var value = $(this).val().toLowerCase();
                $('.employee-card-container').filter(function() {
                    $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
                });
            });

            $('#sortSelect').on('change', function() {
                var sortBy = $(this).val();
                var $wrapper = $('#employeeCards');
                
                $wrapper.find('.employee-card-container').sort(function(a, b) {
                    if (sortBy === 'name') {
                        return $(a).find('.card-title').text().localeCompare($(b).find('.card-title').text());
                    } else if (sortBy === 'performance') {
                        return parseFloat($(b).find('.card-text:contains("Performance Score")').text().split(': ')[1]) - 
                               parseFloat($(a).find('.card-text:contains("Performance Score")').text().split(': ')[1]);
                    } else if (sortBy === 'salary') {
                        return parseFloat($(b).find('.card-text:contains("Salary")').text().replace(/[^0-9.-]+/g,"")) - 
                               parseFloat($(a).find('.card-text:contains("Salary")').text().replace(/[^0-9.-]+/g,""));
                    }
                }).appendTo($wrapper);
            });
        });
    </script>
</body>
</html>

