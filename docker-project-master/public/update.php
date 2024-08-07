<?php
require_once "config.php";

$name = $address = $salary = $employee_id = $date_of_birth = $gender = $phone = $email = $emergency_contact = $job_title = $department = $reporting_manager = $start_date = $employment_type = $qualifications = $skills = $goals = $performance_ratings = $achievements = $training_completed = $work_schedule = $attendance_records = $leave_balances = $career_aspirations = $training_needs = $mentorship_preferences = $feedback = $survey_results = $communication_logs = "";
$name_err = $address_err = $salary_err = $employee_id_err = "";

if(isset($_POST["id"]) && !empty($_POST["id"])){
    $id = $_POST["id"];

    $input_name = trim($_POST["name"]);
    $input_address = trim($_POST["address"]);
    $input_salary = trim($_POST["salary"]);
    $input_employee_id = trim($_POST["employee_id"]);
    
    $name = $input_name;
    $address = $input_address;
    $salary = $input_salary;
    $employee_id = $input_employee_id;
    $date_of_birth = trim($_POST["date_of_birth"]);
    $gender = trim($_POST["gender"]);
    $phone = trim($_POST["phone"]);
    $email = trim($_POST["email"]);
    $emergency_contact = trim($_POST["emergency_contact"]);
    $job_title = trim($_POST["job_title"]);
    $department = trim($_POST["department"]);
    $reporting_manager = trim($_POST["reporting_manager"]);
    $start_date = trim($_POST["start_date"]);
    $employment_type = trim($_POST["employment_type"]);
    $qualifications = trim($_POST["qualifications"]);
    $skills = trim($_POST["skills"]);
    $goals = trim($_POST["goals"]);
    $performance_ratings = trim($_POST["performance_ratings"]);
    $achievements = trim($_POST["achievements"]);
    $training_completed = trim($_POST["training_completed"]);
    $work_schedule = trim($_POST["work_schedule"]);
    $attendance_records = trim($_POST["attendance_records"]);
    $leave_balances = trim($_POST["leave_balances"]);
    $career_aspirations = trim($_POST["career_aspirations"]);
    $training_needs = trim($_POST["training_needs"]);
    $mentorship_preferences = trim($_POST["mentorship_preferences"]);
    $feedback = trim($_POST["feedback"]);
    $survey_results = trim($_POST["survey_results"]);
    $communication_logs = trim($_POST["communication_logs"]);

    if(empty($name_err) && empty($address_err) && empty($salary_err) && empty($employee_id_err)){
        $sql = "UPDATE employees SET name=?, address=?, salary=?, employee_id=?, date_of_birth=?, gender=?, phone=?, email=?, emergency_contact=?, job_title=?, department=?, reporting_manager=?, start_date=?, employment_type=?, qualifications=?, skills=?, goals=?, performance_ratings=?, achievements=?, training_completed=?, work_schedule=?, attendance_records=?, leave_balances=?, career_aspirations=?, training_needs=?, mentorship_preferences=?, feedback=?, survey_results=?, communication_logs=? WHERE id=?";

        if($stmt = mysqli_prepare($link, $sql)){
            mysqli_stmt_bind_param($stmt, "ssssssssssssssssssssssssssssssi", $name, $address, $salary, $employee_id, $date_of_birth, $gender, $phone, $email, $emergency_contact, $job_title, $department, $reporting_manager, $start_date, $employment_type, $qualifications, $skills, $goals, $performance_ratings, $achievements, $training_completed, $work_schedule, $attendance_records, $leave_balances, $career_aspirations, $training_needs, $mentorship_preferences, $feedback, $survey_results, $communication_logs, $id);

            if(mysqli_stmt_execute($stmt)){
                header("location: index.php");
                exit();
            } else{
                echo "Something went wrong. Please try again later.";
            }
        }
        mysqli_stmt_close($stmt);
    }
    mysqli_close($link);
} else{
    if(isset($_GET["id"]) && !empty(trim($_GET["id"]))){
        $id = trim($_GET["id"]);

        $sql = "SELECT * FROM employees WHERE id = ?";
        if($stmt = mysqli_prepare($link, $sql)){
            mysqli_stmt_bind_param($stmt, "i", $param_id);
            $param_id = $id;

            if(mysqli_stmt_execute($stmt)){
                $result = mysqli_stmt_get_result($stmt);

                if(mysqli_num_rows($result) == 1){
                    $row = mysqli_fetch_array($result, MYSQLI_ASSOC);
                    
                    $name = $row["name"];
                    $address = $row["address"];
                    $salary = $row["salary"];
                    $employee_id = $row["employee_id"];
                    $date_of_birth = $row["date_of_birth"];
                    $gender = $row["gender"];
                    $phone = $row["phone"];
                    $email = $row["email"];
                    $emergency_contact = $row["emergency_contact"];
                    $job_title = $row["job_title"];
                    $department = $row["department"];
                    $reporting_manager = $row["reporting_manager"];
                    $start_date = $row["start_date"];
                    $employment_type = $row["employment_type"];
                    $qualifications = $row["qualifications"];
                    $skills = $row["skills"];
                    $goals = $row["goals"];
                    $performance_ratings = $row["performance_ratings"];
                    $achievements = $row["achievements"];
                    $training_completed = $row["training_completed"];
                    $work_schedule = $row["work_schedule"];
                    $attendance_records = $row["attendance_records"];
                    $leave_balances = $row["leave_balances"];
                    $career_aspirations = $row["career_aspirations"];
                    $training_needs = $row["training_needs"];
                    $mentorship_preferences = $row["mentorship_preferences"];
                    $feedback = $row["feedback"];
                    $survey_results = $row["survey_results"];
                    $communication_logs = $row["communication_logs"];
                } else{
                    header("location: error.php");
                    exit();
                }
            } else{
                echo "Oops! Something went wrong. Please try again later.";
            }
        }
        mysqli_stmt_close($stmt);
        mysqli_close($link);
    } else{
        header("location: error.php");
        exit();
    }
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Update Record</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.css">
    <style type="text/css">
        .wrapper{
            width: 500px;
            margin: 0 auto;
        }
    </style>
</head>
<body>
    <div class="wrapper">
        <div class="container-fluid">
            <div class="row">
                <div class="col-md-12">
                    <div class="page-header">
                        <h2>Update Record</h2>
                    </div>
                    <p>Please edit the input values and submit to update the employee record.</p>
                    <form action="<?php echo htmlspecialchars(basename($_SERVER['REQUEST_URI'])); ?>" method="post">
                        <div class="form-group <?php echo (!empty($name_err)) ? 'has-error' : ''; ?>">
                            <label>Name</label>
                            <input type="text" name="name" class="form-control" value="<?php echo $name; ?>">
                            <span class="help-block"><?php echo $name_err;?></span>
                        </div>
                        <div class="form-group <?php echo (!empty($address_err)) ? 'has-error' : ''; ?>">
                            <label>Address</label>
                            <textarea name="address" class="form-control"><?php echo $address; ?></textarea>
                            <span class="help-block"><?php echo $address_err;?></span>
                        </div>
                        <div class="form-group <?php echo (!empty($salary_err)) ? 'has-error' : ''; ?>">
                            <label>Salary</label>
                            <input type="text" name="salary" class="form-control" value="<?php echo $salary; ?>">
                            <span class="help-block"><?php echo $salary_err;?></span>
                        </div>
                        <div class="form-group <?php echo (!empty($employee_id_err)) ? 'has-error' : ''; ?>">
                            <label>Employee ID</label>
                            <input type="text" name="employee_id" class="form-control" value="<?php echo $employee_id; ?>">
                            <span class="help-block"><?php echo $employee_id_err;?></span>
                        </div>
                        <div class="form-group">
                            <label>Date of Birth</label>
                            <input type="date" name="date_of_birth" class="form-control" value="<?php echo $date_of_birth; ?>">
                        </div>
                        <div class="form-group">
                            <label>Gender</label>
                            <input type="text" name="gender" class="form-control" value="<?php echo $gender; ?>">
                        </div>
                        <div class="form-group">
                            <label>Phone</label>
                            <input type="text" name="phone" class="form-control" value="<?php echo $phone; ?>">
                        </div>
                        <div class="form-group">
                            <label>Email</label>
                            <input type="text" name="email" class="form-control" value="<?php echo $email; ?>">
                        </div>
                        <div class="form-group">
                            <label>Emergency Contact</label>
                            <input type="text" name="emergency_contact" class="form-control" value="<?php echo $emergency_contact; ?>">
                        </div>
                        <div class="form-group">
                            <label>Job Title</label>
                            <input type="text" name="job_title" class="form-control" value="<?php echo $job_title; ?>">
                        </div>
                        <div class="form-group">
                            <label>Department</label>
                            <input type="text" name="department" class="form-control" value="<?php echo $department; ?>">
                        </div>
                        <div class="form-group">
                            <label>Reporting Manager</label>
                            <input type="text" name="reporting_manager" class="form-control" value="<?php echo $reporting_manager; ?>">
                        </div>
                        <div class="form-group">
                            <label>Start Date</label>
                            <input type="date" name="start_date" class="form-control" value="<?php echo $start_date; ?>">
                        </div>
                        <div class="form-group">
                            <label>Employment Type</label>
                            <input type="text" name="employment_type" class="form-control" value="<?php echo $employment_type; ?>">
                        </div>
                        <div class="form-group">
                            <label>Qualifications</label>
                            <textarea name="qualifications" class="form-control"><?php echo $qualifications; ?></textarea>
                        </div>
                        <div class="form-group">
                            <label>Skills</label>
                            <textarea name="skills" class="form-control"><?php echo $skills; ?></textarea>
                        </div>
                        <div class="form-group">
                            <label>Goals</label>
                            <textarea name="goals" class="form-control"><?php echo $goals; ?></textarea>
                        </div>
                        <div class="form-group">
                            <label>Performance Ratings</label>
                            <textarea name="performance_ratings" class="form-control"><?php echo $performance_ratings; ?></textarea>
                        </div>
                        <div class="form-group">
                            <label>Achievements</label>
                            <textarea name="achievements" class="form-control"><?php echo $achievements; ?></textarea>
                        </div>
                        <div class="form-group">
                            <label>Training Completed</label>
                            <textarea name="training_completed" class="form-control"><?php echo $training_completed; ?></textarea>
                        </div>
                        <div class="form-group">
                            <label>Work Schedule</label>
                            <textarea name="work_schedule" class="form-control"><?php echo $work_schedule; ?></textarea>
                        </div>
                        <div class="form-group">
                            <label>Attendance Records</label>
                            <textarea name="attendance_records" class="form-control"><?php echo $attendance_records; ?></textarea>
                        </div>
                        <div class="form-group">
                            <label>Leave Balances</label>
                            <textarea name="leave_balances" class="form-control"><?php echo $leave_balances; ?></textarea>
                        </div>
                        <div class="form-group">
                            <label>Career Aspirations</label>
                            <textarea name="career_aspirations" class="form-control"><?php echo $career_aspirations; ?></textarea>
                        </div>
                        <div class="form-group">
                            <label>Training Needs</label>
                            <textarea name="training_needs" class="form-control"><?php echo $training_needs; ?></textarea>
                        </div>
                        <div class="form-group">
                            <label>Mentorship Preferences</label>
                            <textarea name="mentorship_preferences" class="form-control"><?php echo $mentorship_preferences; ?></textarea>
                        </div>
                        <div class="form-group">
                            <label>Feedback</label>
                            <textarea name="feedback" class="form-control"><?php echo $feedback; ?></textarea>
                        </div>
                        <div class="form-group">
                            <label>Survey Results</label>
                            <textarea name="survey_results" class="form-control"><?php echo $survey_results; ?></textarea>
                        </div>
                        <div class="form-group">
                            <label>Communication Logs</label>
                            <textarea name="communication_logs" class="form-control"><?php echo $communication_logs; ?></textarea>
                        </div>
                        <input type="hidden" name="id" value="<?php echo $id; ?>"/>
                        <input type="submit" class="btn btn-primary" value="Submit">
                        <a href="index.php" class="btn btn-default">Cancel</a>
                    </form>
                </div>
            </div>        
        </div>
    </div>
</body>
</html>
