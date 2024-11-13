<?php

 if($_SERVER['REQUEST_METHOD']=='POST'){

include 'connect.php';
 
$cust_phone = $_POST['cust_phone'];
$cust_pass = $_POST['cust_pass'];
 
 $Sql_Query = "select * from register where cust_phone = '$cust_phone' and cust_pass = '$cust_pass' ";
 
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
