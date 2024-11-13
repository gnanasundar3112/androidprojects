<?php

include 'connect.php';
 
 $cust_phone = $_POST['cust_phone'];
 $cust_pass = $_POST['cust_pass'];

	$mstr = $con->prepare("SELECT cust_id,cust_name,cust_email,cust_phone FROM register where cust_phone = '$cust_phone'" );
	$mstr -> execute();
	$mstr -> bind_result($cust_id,$cust_name,$cust_email,$cust_phone);
	$mcount = array();
	while($mstr -> fetch()){
	    $temp = array();
	    $temp['cust_id'] = $cust_id;
	    $temp['cust_name'] = $cust_name;
	    $temp['cust_email'] = $cust_email;
	    $temp['cust_phone'] = $cust_phone;
	    array_push($mcount,$temp);
	}
	echo json_encode($mcount);

mysqli_close($con);
?>
