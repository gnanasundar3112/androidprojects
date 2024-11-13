<?php

include 'connect.php';

	$cate_id = $_POST["cate_id"];
	$cate_name = $_POST["cate_name"];
	$tamil_name = $_POST["tamil_name"];	
	$short_name = $_POST["short_name"];
	$active = $_POST["active"];
	$image = $_POST["image"];
    $modi_date = date("Y-m-d");
    $modi_time = date("h:i:s");
	
	$sql = "UPDATE `catemaster` SET `cate_name`='$cate_name',`tamil_name`='$tamil_name',`short_name`='$short_name',`active`='$active',`image`='$image',`modi_date`='$modi_date',`modi_time`='$modi_time' WHERE cate_id=$cate_id";

	$result = mysqli_query($con,$sql);
	
	if($result){
		echo "Update Successfully";
	
	}else{
		echo 'failed';
    }
	mysqli_close($con);

?>


