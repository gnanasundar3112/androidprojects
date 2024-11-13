<?php

include 'connect.php';

	$user_id = $_POST["user_id"];
    $user_name = $_POST["user_name"];
	$user_password = $_POST["user_password"];	
	$user_phone = $_POST["user_phone"];
	$aadhar_no = $_POST["aadhar_no"];
	$user_address = $_POST["user_address"];
	$user_image = $_POST["user_image"];
	$aadhar_image = $_POST["aadhar_image"];
	$bike_image = $_POST["bike_image"];
	$active = $_POST["active"];
    $modi_date = date("Y-m-d");
    $modi_time = date("h:i:s");
	
	$sql = ("UPDATE `user_register` SET `user_name`='$user_name',`user_password`='$user_password',
	        `user_phone`='$user_phone',`aadhar_no`='$aadhar_no',`user_address`='$user_address',
	        `user_image`='$user_image',`aadhar_image`='$aadhar_image',`bike_image`='$bike_image',
	        `active`='$active',`modi_date`='$modi_date',`modi_time`='$modi_time' 
	        WHERE user_id=$user_id");
	$result = mysqli_query($con,$sql);

	if($result){
		echo "Update Successfully";
		mysqli_close($con);
	}else{
		echo "failed";
    }
	

?>


