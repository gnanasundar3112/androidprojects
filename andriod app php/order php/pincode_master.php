<?php

 if($_SERVER['REQUEST_METHOD']=='POST'){

include 'connect.php';
 
$pincode =$_POST['pincode'];
 
 $Sql_Query = "Select * from  `pincode_master` where pincode='$pincode'";
 
 $check = mysqli_fetch_array(mysqli_query($con,$Sql_Query));
 
 if(isset($check)){
 
 echo "Delivery Available";
 }
 else{
 echo "Delivery not Available";
 }
 
 }else{
 echo "Check Again";
 }
mysqli_close($con);

?>