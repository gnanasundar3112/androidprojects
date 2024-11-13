<?php

include 'connect.php';
	
	$cate_id = $_POST["cate_id"];

	 $mprodid= $cate_id.'00001';
	 
	 $mstr = $con->prepare("SELECT substr(prod_id+10000001,2,7) as prod_id from productmaster where left(prod_id,2)=$cate_id order by prod_id desc limit 1");
	 $mstr -> execute();
	 $mstr -> bind_result($prod_id);
	 while($mstr -> fetch()) {
		 $mprodid = $prod_id;
	  }
	
	$prod_id = $mprodid;
	$prod_name = $_POST["prod_name"];
	$tamil_name = $_POST["tamil_name"];	
	$short_name = $_POST["short_name"];
	$rate = $_POST["rate"];	
	$active = $_POST["active"];
	$image = $_POST["image"];
	$stock_type = $_POST["stock_type"];
	$crea_date = date("Y-m-d");
    $crea_time = date("h:i:s");
    $modi_date = date("Y-m-d");
    $modi_time = date("h:i:s");
	
	$Sql = "INSERT INTO `productmaster`(`prod_id`,`prod_name`,`tamil_name`,`short_name`,`rate`,`active`,`image`,`cate_id`,`stock_type`,`crea_date`,`crea_time`,`modi_date`,`modi_time`) 
	VALUES ('$prod_id','$prod_name','$tamil_name','$short_name','$rate','$active','$image','$cate_id','$stock_type','$crea_date','$crea_time','$modi_date','$modi_time')";
	
	$result = mysqli_query($con,$Sql);
	
	if($result){
		echo "success";
		mysqli_close($con);
	}else{
		echo 'failed';
    }
	

?>