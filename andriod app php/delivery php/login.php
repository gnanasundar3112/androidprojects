<?php

 if($_SERVER['REQUEST_METHOD']=='POST'){

include 'connect.php';
 
$user_name = $_POST['user_name'];
$user_password = $_POST['user_password'];
 
 $Sql_Query = "select * from user_register where user_name = '$user_name' and user_password = '$user_password' ";
 
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
