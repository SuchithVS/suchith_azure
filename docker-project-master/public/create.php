<?php
require_once "config.php";

$name = $email = $address = $department = $position = $hire_date = $salary = $skills = $education = $certifications = "";
$name_err = $email_err = $address_err = $department_err = $position_err = $hire_date_err = $salary_err = "";

if($_SERVER["REQUEST_METHOD"] == "POST"){
    // Validate name
    $input_name = trim($_POST["name"]);
    if(empty($input_name)){
        $name_err = "Please enter a name.";
    } elseif(!filter_var($input_name, FILTER_VALIDATE_REGEXP, array("options"=>array("regexp"=>"/^[a-zA-Z\s]+$/")))){
        $name_err = "Please enter a valid name.";
    } else{
        $name = $input_name;
    }
    
    // Validate email
    $input_email = trim($_POST["email"]);
    if(empty($input_email)){
        $email_err = "Please enter an email.";
    } elseif(!filter_var($input_email, FILTER_VALIDATE_EMAIL)){
        $email_err = "Please enter a valid email.";
    } else{
        $email = $input_email;
    }
    
    // Validate address
    $input_address = trim($_POST["address"]);
    if(empty($input_address)){
        $address_err = "Please enter an address.";
    } else{
        $address = $input_address;
    }
    
    // Validate department
    $input_department = trim($_POST["department"]);
    if(empty($input_department)){
        $department_err = "Please enter a department.";
    } else{
        $department = $input_department;
    }
    
    // Validate position
    $input_position = trim($_POST["position"]);
    if(empty($input_position)){
        $position_err = "Please enter a position.";
    } else{
        $position = $input_position;
    }
    
    // Validate hire date
    $input_hire_date = trim($_POST["hire_date"]);
    if(empty($input_hire_date)){
        $hire_date_err = "Please enter a hire date.";
    } else{
        $hire_date = $input_hire_date;
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
    
    $skills = trim($_POST["skills"]);
    $education = trim($_POST["education"]);
    $certifications = trim($_POST["certifications"]);

    // Check input errors before inserting in database
    if(empty($name_err) && empty($email_err) && empty($address_err) && empty($department_err) && empty($position_err) && empty($hire_date_err) && empty($salary_err)){
        $sql = "INSERT INTO employees (name, email, address, department, position, hire_date, salary, skills, education, certifications) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        if($stmt = mysqli_prepare($link, $sql)){
            mysqli_stmt_bind_param($stmt, "ssssssssss", $param_name, $param_email, $param_address, $param_department, $param_position, $param_hire_date, $param_salary, $param_skills, $param_education, $param_certifications);
            
            $param_name = $name;
            $param_email = $email;
            $param_address = $address;
            $param_department = $department;
            $param_position = $position;
            $param_hire_date = $hire_date;
            $param_salary = $salary;
            $param_skills = $skills;
            $param_education = $education;
            $param_certifications = $certifications;
            
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
}
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Create Record</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.css">
    <style type="text/css">
        .wrapper{ width: 500px; margin: 0 auto; }
    </style>
</head>
<body>
    <div class="wrapper">
        <div class="container-fluid">
            <div class="row">
                <div class="col-md-12">
                    <div class="page-header">
                        <h2>Create Record</h2>
                    </div>
                    <p>Please fill this form and submit to add employee record to the database.</p>
                    <form action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]); ?>" method="post">
                        <div class="form-group <?php echo (!empty($name_err)) ? 'has-error' : ''; ?>">
                            <label>Name</label>
                            <input type="text" name="name" class="form-control" value="<?php echo $name; ?>">
                            <span class="help-block"><?php echo $name_err;?></span>
                        </div>
                        <div class="form-group <?php echo (!empty($email_err)) ? 'has-error' : ''; ?>">
                            <label>Email</label>
                            <input type="email" name="email" class="form-control" value="<?php echo $email; ?>">
                            <span class="help-block"><?php echo $email_err;?></span>
                        </div>
                        <div class="form-group <?php echo (!empty($address_err)) ? 'has-error' : ''; ?>">
                            <label>Address</label>
                            <textarea name="address" class="form-control"><?php echo $address; ?></textarea>
                            <span class="help-block"><?php echo $address_err;?></span>
                        </div>
                        <div class="form-group <?php echo (!empty($department_err)) ? 'has-error' : ''; ?>">
                            <label>Department</label>
                            <input type="text" name="department" class="form-control" value="<?php echo $department; ?>">
                            <span class="help-block"><?php echo $department_err;?></span>
                        </div>
                        <div class="form-group <?php echo (!empty($position_err)) ? 'has-error' : ''; ?>">
                            <label>Position</label>
                            <input type="text" name="position" class="form-control" value="<?php echo $position; ?>">
                            <span class="help-block"><?php echo $position_err;?></span>
                        </div>
                        <div class="form-group <?php echo (!empty($hire_date_err)) ? 'has-error' : ''; ?>">
                            <label>Hire Date</label>
                            <input type="date" name="hire_date" class="form-control" value="<?php echo $hire_date; ?>">
                            <span class="help-block"><?php echo $hire_date_err;?></span>
                        </div>
                        <div class="form-group <?php echo (!empty($salary_err)) ? 'has-error' : ''; ?>">
                            <label>Salary</label>
                            <input type="text" name="salary" class="form-control" value="<?php echo $salary; ?>">
                            <span class="help-block"><?php echo $salary_err;?></span>
                        </div>
                        <div class="form-group">
                            <label>Skills</label>
                            <textarea name="skills" class="form-control"><?php echo $skills; ?></textarea>
                        </div>
                        <div class="form-group">
                            <label>Education</label>
                            <textarea name="education" class="form-control"><?php echo $education; ?></textarea>
                        </div>
                        <div class="form-group">
                            <label>Certifications</label>
                            <textarea name="certifications" class="form-control"><?php echo $certifications; ?></textarea>
                        </div>
                        <input type="submit" class="btn btn-primary" value="Submit">
                        <a href="index.php" class="btn btn-default">Cancel</a>
                    </form>
                </div>
            </div>        
        </div>
    </div>
</body>
</html>