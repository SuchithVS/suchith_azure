<?php
require_once "config.php";

// Define variables and initialize with empty values
$name = $department = $role = $gender = $date_of_joining = $salary = $performance_score = $projects_completed = $training_hours = $last_promotion_date = "";
$name_err = $department_err = $role_err = $gender_err = $date_of_joining_err = $salary_err = $performance_score_err = $projects_completed_err = $training_hours_err = $last_promotion_date_err = "";

// Processing form data when form is submitted
if($_SERVER["REQUEST_METHOD"] == "POST"){
    // Validate name
    $input_name = trim($_POST["name"]);
    if(empty($input_name)){
        $name_err = "Please enter a name.";
    } elseif(!preg_match("/^[a-zA-Z ]*$/", $input_name)){
        $name_err = "Please enter a valid name.";
    } else{
        $name = $input_name;
    }
    
    // Validate department
    $input_department = trim($_POST["department"]);
    if(empty($input_department)){
        $department_err = "Please enter a department.";
    } else{
        $department = $input_department;
    }
    
    // Validate role
    $input_role = trim($_POST["role"]);
    if(empty($input_role)){
        $role_err = "Please enter a role.";
    } else{
        $role = $input_role;
    }
    
    // Validate gender
    $input_gender = trim($_POST["gender"]);
    if(empty($input_gender)){
        $gender_err = "Please select a gender.";
    } else{
        $gender = $input_gender;
    }
    
    // Validate date of joining
    $input_date_of_joining = trim($_POST["date_of_joining"]);
    if(empty($input_date_of_joining)){
        $date_of_joining_err = "Please enter the date of joining.";
    } else{
        $date_of_joining = $input_date_of_joining;
    }

    // Validate salary
    $input_salary = trim($_POST["salary"]);
    if(empty($input_salary)){
        $salary_err = "Please enter the salary amount.";     
    } elseif(!is_numeric($input_salary)){
        $salary_err = "Please enter a valid number.";
    } else{
        $salary = $input_salary;
    }

    // Validate performance score
    $input_performance_score = trim($_POST["performance_score"]);
    if(empty($input_performance_score)){
        $performance_score_err = "Please enter the performance score.";     
    } elseif(!is_numeric($input_performance_score) || $input_performance_score < 0 || $input_performance_score > 100){
        $performance_score_err = "Please enter a valid score between 0 and 100.";
    } else{
        $performance_score = $input_performance_score;
    }

    // Validate projects completed
    $input_projects_completed = trim($_POST["projects_completed"]);
    if(empty($input_projects_completed)){
        $projects_completed_err = "Please enter the number of projects completed.";     
    } elseif(!ctype_digit($input_projects_completed)){
        $projects_completed_err = "Please enter a valid number.";
    } else{
        $projects_completed = $input_projects_completed;
    }

    // Validate training hours
    $input_training_hours = trim($_POST["training_hours"]);
    if(empty($input_training_hours)){
        $training_hours_err = "Please enter the training hours.";     
    } elseif(!is_numeric($input_training_hours)){
        $training_hours_err = "Please enter a valid number.";
    } else{
        $training_hours = $input_training_hours;
    }

    // Validate last promotion date
    $input_last_promotion_date = trim($_POST["last_promotion_date"]);
    if(empty($input_last_promotion_date)){
        $last_promotion_date_err = "Please enter the last promotion date.";
    } else{
        $last_promotion_date = $input_last_promotion_date;
    }
    
    // Check input errors before inserting in database
    if(empty($name_err) && empty($department_err) && empty($role_err) && empty($gender_err) && empty($date_of_joining_err) && empty($salary_err) && empty($performance_score_err) && empty($projects_completed_err) && empty($training_hours_err) && empty($last_promotion_date_err)){
        // Prepare an insert statement
        $sql = "INSERT INTO employees (name, department, role, gender, date_of_joining, salary, performance_score, projects_completed, training_hours, last_promotion_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
         
        if($stmt = mysqli_prepare($link, $sql)){
            // Bind variables to the prepared statement as parameters
            mysqli_stmt_bind_param($stmt, "sssssdiiis", $param_name, $param_department, $param_role, $param_gender, $param_date_of_joining, $param_salary, $param_performance_score, $param_projects_completed, $param_training_hours, $param_last_promotion_date);
            
            // Set parameters
            $param_name = $name;
            $param_department = $department;
            $param_role = $role;
            $param_gender = $gender;
            $param_date_of_joining = $date_of_joining;
            $param_salary = $salary;
            $param_performance_score = $performance_score;
            $param_projects_completed = $projects_completed;
            $param_training_hours = $training_hours;
            $param_last_promotion_date = $last_promotion_date;
            
            // Attempt to execute the prepared statement
            if(mysqli_stmt_execute($stmt)){
                // Records created successfully. Redirect to landing page
                header("location: index.php");
                exit();
            } else{
                echo "Something went wrong. Please try again later.";
            }
        }
         
        // Close statement
        mysqli_stmt_close($stmt);
    }
    
    // Close connection
    mysqli_close($link);
}
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Add Employee</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style type="text/css">
        body {
            background: linear-gradient(135deg, #1e3c72 0%, #2a5298 100%);
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            font-family: 'Arial', sans-serif;
        }
        .wrapper {
            width: 600px;
            background: rgba(255, 255, 255, 0.1);
            border-radius: 20px;
            box-shadow: 0 8px 32px rgba(31, 38, 135, 0.37);
            backdrop-filter: blur(4px);
            border: 1px solid rgba(255, 255, 255, 0.18);
            padding: 40px;
            color: #fff;
        }
        h2 {
            text-align: center;
            margin-bottom: 30px;
        }
        .form-control {
            background-color: rgba(255, 255, 255, 0.1);
            border: 1px solid rgba(255, 255, 255, 0.3);
            color: #fff;
        }
        .form-control:focus {
            background-color: rgba(255, 255, 255, 0.2);
            color: #fff;
        }
        .btn-primary {
            background: linear-gradient(45deg, #3498db, #2980b9);
            border: none;
        }
        .btn-secondary {
            background: linear-gradient(45deg, #e74c3c, #c0392b);
            border: none;
        }
    </style>
</head>
<body>
    <div class="wrapper">
        <h2>Add Employee</h2>
        <form action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]); ?>" method="post">
            <div class="form-group">
                <label>Name</label>
                <input type="text" name="name" class="form-control <?php echo (!empty($name_err)) ? 'is-invalid' : ''; ?>" value="<?php echo $name; ?>">
                <span class="invalid-feedback"><?php echo $name_err;?></span>
            </div>
            <div class="form-group">
                <label>Department</label>
                <input type="text" name="department" class="form-control <?php echo (!empty($department_err)) ? 'is-invalid' : ''; ?>" value="<?php echo $department; ?>">
                <span class="invalid-feedback"><?php echo $department_err;?></span>
            </div>
            <div class="form-group">
                <label>Role</label>
                <input type="text" name="role" class="form-control <?php echo (!empty($role_err)) ? 'is-invalid' : ''; ?>" value="<?php echo $role; ?>">
                <span class="invalid-feedback"><?php echo $role_err;?></span>
            </div>
            <div class="form-group">
                <label>Gender</label>
                <select name="gender" class="form-control <?php echo (!empty($gender_err)) ? 'is-invalid' : ''; ?>">
                    <option value="">Select Gender</option>
                    <option value="Male" <?php if($gender == "Male") echo "selected"; ?>>Male</option>
                    <option value="Female" <?php if($gender == "Female") echo "selected"; ?>>Female</option>
                    <option value="Other" <?php if($gender == "Other") echo "selected"; ?>>Other</option>
                </select>
                <span class="invalid-feedback"><?php echo $gender_err;?></span>
            </div>
            <div class="form-group">
                <label>Date of Joining</label>
                <input type="date" name="date_of_joining" class="form-control <?php echo (!empty($date_of_joining_err)) ? 'is-invalid' : ''; ?>" value="<?php echo $date_of_joining; ?>">
                <span class="invalid-feedback"><?php echo $date_of_joining_err;?></span>
            </div>
            <div class="form-group">
                <label>Salary</label>
                <input type="text" name="salary" class="form-control <?php echo (!empty($salary_err)) ? 'is-invalid' : ''; ?>" value="<?php echo $salary; ?>">
                <span class="invalid-feedback"><?php echo $salary_err;?></span>
            </div>
            <div class="form-group">
                <label>Performance Score</label>
                <input type="text" name="performance_score" class="form-control <?php echo (!empty($performance_score_err)) ? 'is-invalid' : ''; ?>" value="<?php echo $performance_score; ?>">
                <span class="invalid-feedback"><?php echo $performance_score_err;?></span>
            </div>
            <div class="form-group">
                <label>Projects Completed</label>
                <input type="text" name="projects_completed" class="form-control <?php echo (!empty($projects_completed_err)) ? 'is-invalid' : ''; ?>" value="<?php echo $projects_completed; ?>">
                <span class="invalid-feedback"><?php echo $projects_completed_err;?></span>
            </div>
            <div class="form-group">
                <label>Training Hours</label>
                <input type="text" name="training_hours" class="form-control <?php echo (!empty($training_hours_err)) ? 'is-invalid' : ''; ?>" value="<?php echo $training_hours; ?>">
                <span class="invalid-feedback"><?php echo $training_hours_err;?></span>
            </div>
            <div class="form-group">
                <label>Last Promotion Date</label>
                <input type="date" name="last_promotion_date" class="form-control <?php echo (!empty($last_promotion_date_err)) ? 'is-invalid' : ''; ?>" value="<?php echo $last_promotion_date; ?>">
                <span class="invalid-feedback"><?php echo $last_promotion_date_err;?></span>
            </div>
            <div class="form-group">
                <input type="submit" class="btn btn-primary" value="Submit">
                <a href="index.php" class="btn btn-secondary ml-2">Cancel</a>
            </div>
        </form>
    </div>
</body>
</html>