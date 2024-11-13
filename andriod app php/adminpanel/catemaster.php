<?php

include 'connect.php';

//$image = $_POST['image'];
//$path = 'Images/'.rand(10000, 100000).'.jpg';

//if(file_put_contents($path,base64_decode($image))){} 
     $mcateid='01';
	 $mstr = $con->prepare("SELECT substr(cate_id+101,2,2) as cate_id from catemaster order by cate_id desc limit 1");
	 $mstr -> execute();
	 $mstr -> bind_result($cate_id);
	 while($mstr -> fetch()) {
		 $mcateid = $cate_id;
	  }
	
	$cate_id = $mcateid;
	$cate_name = $_POST["cate_name"];
	$tamil_name = $_POST["tamil_name"];	
	$short_name = $_POST["short_name"];
	$active = $_POST["active"];
	$image = $_POST['image'];
	$crea_date = date("Y-m-d");
    $crea_time = date("h:i:s");
    $modi_date = date("Y-m-d");
    $modi_time = date("h:i:s");
	
	$Sql = "INSERT INTO `catemaster`(`cate_id`,`cate_name`,`tamil_name`,`short_name`,`active`,`image`,`crea_date`,`crea_time`,`modi_date`,`modi_time`) VALUES ('$cate_id','$cate_name','$tamil_name','$short_name','$active','$image','$crea_date','$crea_time','$modi_date','$modi_time')";
	
	$result = mysqli_query($con,$Sql);
    
if($result){    
	echo 'success';
}else{
	echo 'failed';
}


?>