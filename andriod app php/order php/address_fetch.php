<?php

include 'connect.php';
 
 $cust_phone = $_POST['cust_phone'];

	$mstr = $con->prepare("SELECT add_name,add_mobile,add_address,add_state,add_city,add_pincode FROM address where cust_phone = '$cust_phone' group by cust_phone " );
	$mstr -> execute();
	$mstr -> bind_result($add_name,$add_mobile,$add_address,$add_state,$add_city,$add_pincode);
	$mcount = array();
	while($mstr -> fetch()){
	    $temp = array();
	    $temp['add_name'] = $add_name;
	    $temp['add_mobile'] = $add_mobile;
	    $temp['add_address'] = $add_address;
	    $temp['add_state'] = $add_state;
		$temp['add_city'] = $add_city;
		$temp['add_pincode'] = $add_pincode;
		
	    array_push($mcount,$temp);
	}
	echo json_encode($mcount);

mysqli_close($con);
?>
