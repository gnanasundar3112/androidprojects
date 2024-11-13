<?php

include 'connect.php';

	$eff_date = $_POST["eff_date"];
	$effdate = explode('/',$eff_date);
	$date = $effdate[2].'-'.$effdate[1].'-'.$effdate[0];
	$prod_id = $_POST["prod_id"];	
	$size_id  = $_POST["size_id"];
	$crea_date = date("Y-m-d");
    $crea_time = date("h:i:s");
    $modi_date = date("Y-m-d");
    $modi_time = date("h:i:s");


	$Sql = "INSERT INTO `productsize`(`eff_date`,`prod_id`,`size_id`,`crea_date`,`crea_time`,`modi_date`,`modi_time`)
	VALUES ('$date','$prod_id','$size_id','$crea_date','$crea_time','$modi_date','$modi_time')";
	
	$result = mysqli_query($con,$Sql);
	
	if($result){
		echo "success";
	
	}else{
		echo 'failed';
    }
		mysqli_close($con);

?>


