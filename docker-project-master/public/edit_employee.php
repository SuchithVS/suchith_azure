<?php
// Include config file
require_once "config.php";

// Define variables and initialize with empty values
$name = $department = $role = $gender = $salary = $performance_score = $projects_completed = $training_hours = $last_promotion_date = "";
$name_err = $department_err = $role_err = $gender_err = $salary_err = $performance_score_err = $projects_completed_err = $training_hours_err = $last_promotion_date_err = "";

// Processing form data when form is submitted
if(isset($_POST["id"]) && !empty($_POST["id"])){
    // Get hidden input value
    $id = $_POST["id"];
    
    // Validate name
    $input_name = trim($_POST["name"]);
    if(empty($input_name)){
        $name_err = "Please enter a name.";
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
    
    // Validate salary
    $input_salary = trim($_POST["salary"]);
    if(empty($input_salary)){
        $salary_err = "Please enter the salary amount.";     
    } elseif(!ctype_digit($input_salary)){
        $salary_err = "Please enter a positive integer value.";
    } else{
        $salary = $input_salary;
    }
    
    // Validate performance_score
    $input_performance_score = trim($_POST["performance_score"]);
    if(empty($input_performance_score)){
        $performance_score_err = "Please enter the performance score.";     
    } elseif(!ctype_digit($input_performance_score) || $input_performance_score < 0 || $input_performance_score > 100){
        $performance_score_err = "Please enter a value between 0 and 100.";
    } else{
        $performance_score = $input_performance_score;
    }
    
    // Validate projects_completed
    $input_projects_completed = trim($_POST["projects_completed"]);
    if(empty($input_projects_completed)){
        $projects_completed_err = "Please enter the number of completed projects.";     
    } elseif(!ctype_digit($input_projects_completed)){
        $projects_completed_err = "Please enter a positive integer value.";
    } else{
        $projects_completed = $input_projects_completed;
    }
    
    // Validate training_hours
    $input_training_hours = trim($_POST["training_hours"]);
    if(empty($input_training_hours)){
        $training_hours_err = "Please enter the training hours.";     
    } elseif(!ctype_digit($input_training_hours)){
        $training_hours_err = "Please enter a positive integer value.";
    } else{
        $training_hours = $input_training_hours;
    }
    
    // Validate last_promotion_date
    $input_last_promotion_date = trim($_POST["last_promotion_date"]);
    if(empty($input_last_promotion_date)){
        $last_promotion_date_err = "Please enter the last promotion date.";     
    } else{
        $last_promotion_date = $input_last_promotion_date;
    }
    
    // Check input errors before inserting in database
    if(empty($name_err) && empty($department_err) && empty($role_err) && empty($gender_err) && empty($salary_err) && empty($performance_score_err) && empty($projects_completed_err) && empty($training_hours_err) && empty($last_promotion_date_err)){
        // Prepare an update statement
        $sql = "UPDATE employees SET name=?, department=?, role=?, gender=?, salary=?, performance_score=?, projects_completed=?, training_hours=?, last_promotion_date=? WHERE id=?";
         
        if($stmt = mysqli_prepare($link, $sql)){
            // Bind variables to the prepared statement as parameters
            mysqli_stmt_bind_param($stmt, "ssssdiiisd", $param_name, $param_department, $param_role, $param_gender, $param_salary, $param_performance_score, $param_projects_completed, $param_training_hours, $param_last_promotion_date, $param_id);
            
            // Set parameters
            $param_name = $name;
            $param_department = $department;
            $param_role = $role;
            $param_gender = $gender;
            $param_salary = $salary;
            $param_performance_score = $performance_score;
            $param_projects_completed = $projects_completed;
            $param_training_hours = $training_hours;
            $param_last_promotion_date = $last_promotion_date;
            $param_id = $id;
            
            // Attempt to execute the prepared statement
            if(mysqli_stmt_execute($stmt)){
                // Records updated successfully. Redirect to landing page
                header("location: index.php");
                exit();
            } else{
                echo "Something went wrong. Please try again later.";
            }

            // Close statement
            mysqli_stmt_close($stmt);
        } else {
            echo "Error preparing statement: " . mysqli_error($link);
        }
    } else {
        echo "Please correct the errors and try again.";
    }
    
    // Close connection
    mysqli_close($link);
} else {
    // Check existence of id parameter before processing further
    if(isset($_GET["id"]) && !empty(trim($_GET["id"]))){
        // Get URL parameter
        $id =  trim($_GET["id"]);
        
        // Prepare a select statement
        $sql = "SELECT * FROM employees WHERE id = ?";
        if($stmt = mysqli_prepare($link, $sql)){
            // Bind variables to the prepared statement as parameters
            mysqli_stmt_bind_param($stmt, "i", $param_id);
            
            // Set parameters
            $param_id = $id;
            
            // Attempt to execute the prepared statement
            if(mysqli_stmt_execute($stmt)){
                $result = mysqli_stmt_get_result($stmt);
    
                if(mysqli_num_rows($result) == 1){
                    /* Fetch result row as an associative array. Since the result set contains only one row, we don't need to use while loop */
                    $row = mysqli_fetch_array($result, MYSQLI_ASSOC);
                    
                    // Retrieve individual field value
                    $name = $row["name"];
                    $department = $row["department"];
                    $role = $row["role"];
                    $gender = $row["gender"];
                    $salary = $row["salary"];
                    $performance_score = $row["performance_score"];
                    $projects_completed = $row["projects_completed"];
                    $training_hours = $row["training_hours"];
                    $last_promotion_date = $row["last_promotion_date"];
                } else{
                    // URL doesn't contain valid id. Redirect to error page
                    header("location: error.php");
                    exit();
                }
            } else{
                echo "Oops! Something went wrong. Please try again later.";
            }

            // Close statement
            mysqli_stmt_close($stmt);
        } else {
            echo "Error preparing statement: " . mysqli_error($link);
        }
        
        // Close connection
        mysqli_close($link);
    } else {
        // URL doesn't contain id parameter. Redirect to error page
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
                    <p>Please edit the input values and submit to update the record.</p>
                    <form action="<?php echo htmlspecialchars(basename($_SERVER['REQUEST_URI'])); ?>" method="post">
                        <div class="form-group <?php echo (!empty($name_err)) ? 'has-error' : ''; ?>">
                            <label>Name</label>
                            <input type="text" name="name" class="form-control" value="<?php echo $name; ?>">
                            <span class="help-block"><?php echo $name_err;?></span>
                        </div>
                        <div class="form-group <?php echo (!empty($department_err)) ? 'has-error' : ''; ?>">
                            <label>Department</label>
                            <input type="text" name="department" class="form-control" value="<?php echo $department; ?>">
                            <span class="help-block"><?php echo $department_err;?></span>
                        </div>
                        <div class="form-group <?php echo (!empty($role_err)) ? 'has-error' : ''; ?>">
                            <label>Role</label>
                            <input type="text" name="role" class="form-control" value="<?php echo $role; ?>">
                            <span class="help-block"><?php echo $role_err;?></span>
                        </div>
                        <div class="form-group <?php echo (!empty($gender_err)) ? 'has-error' : ''; ?>">
                            <label>Gender</label>
                            <select name="gender" class="form-control">
                                <option value="Male" <?php echo ($gender == 'Male') ? 'selected' : ''; ?>>Male</option>
                                <option value="Female" <?php echo ($gender == 'Female') ? 'selected' : ''; ?>>Female</option>
                                <option value="Other" <?php echo ($gender == 'Other') ? 'selected' : ''; ?>>Other</option>
                            </select>
                            <span class="help-block"><?php echo $gender_err;?></span>
                        </div>
                        <div class="form-group <?php echo (!empty($join_date_err)) ? 'has-error' : ''; ?>">
                            <label>Join Date</label>
                            <input type="date" name="join_date" class="form-control" value="<?php echo $join_date; ?>">
                            <span class="help-block"><?php echo $join_date_err;?></span>
                        </div>
                        <div class="form-group <?php echo (!empty($salary_err)) ? 'has-error' : ''; ?>">
                            <label>Salary</label>
                            <input type="text" name="salary" class="form-control" value="<?php echo $salary; ?>">
                            <span class="help-block"><?php echo $salary_err;?></span>
                        </div>
                        <div class="form-group <?php echo (!empty($performance_score_err)) ? 'has-error' : ''; ?>">
                            <label>Performance Score</label>
                            <input type="text" name="performance_score" class="form-control" value="<?php echo $performance_score; ?>">
                            <span class="help-block"><?php echo $performance_score_err;?></span>
                        </div>
                        <div class="form-group <?php echo (!empty($projects_completed_err)) ? 'has-error' : ''; ?>">
                            <label>Projects Completed</label>
                            <input type="text" name="projects_completed" class="form-control" value="<?php echo $projects_completed; ?>">
                            <span class="help-block"><?php echo $projects_completed_err;?></span>
                        </div>
                        <div class="form-group <?php echo (!empty($training_hours_err)) ? 'has-error' : ''; ?>">
                            <label>Training Hours</label>
                            <input type="text" name="training_hours" class="form-control" value="<?php echo $training_hours; ?>">
                            <span class="help-block"><?php echo $training_hours_err;?></span>
                        </div>
                        <div class="form-group <?php echo (!empty($last_promotion_date_err)) ? 'has-error' : ''; ?>">
                            <label>Last Promotion Date</label>
                            <input type="date" name="last_promotion_date" class="form-control" value="<?php echo $last_promotion_date; ?>">
                            <span class="help-block"><?php echo $last_promotion_date_err;?></span>
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