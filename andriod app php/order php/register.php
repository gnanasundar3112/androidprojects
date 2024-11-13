<?php

include 'connect.php';

$cust_name = $_POST['cust_name'];
$cust_email = $_POST['cust_email'];
$cust_phone = $_POST['cust_phone'];
$cust_pass =$_POST['cust_pass'];
$Image =$_POST['image'];


$sql = "SELECT * FROM register WHERE cust_phone = '$cust_phone'";
$result = $con->query($sql);
if ($result->num_rows == 1) {
        echo "This Mobile number is already Registered";
} else{
    $Sql = "INSERT INTO `register`( `cust_name`, `cust_email`, `cust_phone`, `cust_pass`,`image`) VALUES ('$cust_name','$cust_email','$cust_phone','$cust_pass','$Image')";

    $result = mysqli_query($con,$Sql);
    if($result){
        echo "Register successfully";
    }
    else{
        echo "Not register";
    }
}

mysqli_close($con);
?>