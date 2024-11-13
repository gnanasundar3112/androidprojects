<?php

include 'connect.php';

	 $muser_id ='000000000000001';
	 
	 $mstr = $con->prepare("SELECT substr(user_id+1000000000000001,2,15) as user_id from `user_register` order by user_id desc limit 1");
	 $mstr -> execute();
	 $mstr -> bind_result($user_id);
	 while($mstr -> fetch()) {
		 $muser_id = $user_id;
		 }
	
	$user_id = $muser_id;
    $user_name = $_POST["user_name"];
	$user_password = $_POST["user_password"];	
	$user_phone = $_POST["user_phone"];
	$aadhar_no = $_POST["aadhar_no"];
	$user_address = $_POST["user_address"];
	$user_image = $_POST["user_image"];
	$aadhar_image = $_POST["aadhar_image"];
	$bike_image = $_POST["bike_image"];
	$active = $_POST["active"];
	$crea_date = date("Y-m-d");
    $crea_time = date("h:i:s");
    $modi_date = date("Y-m-d");
    $modi_time = date("h:i:s");
	
$sql = "INSERT INTO `user_register`(`user_id`,`user_name`,`user_password`,`user_phone`,`aadhar_no`,`user_address`,`user_image`,`aadhar_image`,`bike_image`,`active`,`crea_date`,`crea_time`,`modi_date`,`modi_time`) 
	VALUES ('$user_id','$user_name','$user_password','$user_phone','$aadhar_no','$user_address','$user_image','$aadhar_image','$bike_image','$active','$crea_date','$crea_time','$modi_date','$modi_time')";
	
	$result = mysqli_query($con,$sql);

if($result){
	    echo "success";
		mysqli_close($con);
	}else{
		echo 'failed';
    }

?>