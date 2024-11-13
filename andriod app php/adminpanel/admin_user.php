<?php

 if($_SERVER['REQUEST_METHOD']=='POST'){

include 'connect.php';
 
$user_name = $_POST['user_name'];
$user_pass = $_POST['user_pass'];
 
 $Sql_Query = "select * from admin_user where user_name = '$user_name' and user_pass = '$user_pass' ";
 
 $check = mysqli_fetch_array(mysqli_query($con,$Sql_Query));

 if(isset($check)){
      echo "Successfully login";
 }
 else{
 echo "Invalid Username or Password Please Try Again";
 }
 
 }else{
 echo "Check Again";
 }

mysqli_close($con);
?>
