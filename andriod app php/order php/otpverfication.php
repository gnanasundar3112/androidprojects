<?php

 if($_SERVER['REQUEST_METHOD']=='POST'){

include 'connect.php';
 
$cust_phone = $_POST['cust_phone'];
$otp = $_POST['otp'];


 $Sql_Query = "select * from register where cust_phone = '$cust_phone' and otp = '$otp' ";
 
 $check = mysqli_fetch_array(mysqli_query($con,$Sql_Query));

 if(isset($check)){
      echo "Successfully login";
 }
 else{
 echo "Invalid OTP";
 }
 
 }else{
 echo "Check Again";
 }

mysqli_close($con);
?>
