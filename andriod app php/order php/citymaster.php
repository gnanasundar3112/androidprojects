<?php

include 'connect.php';
	
$mstate = $_GET['state_name'];

$sql = "SELECT * FROM `citymaster` WHERE state_name='$mstate'";

if(!$con->query($sql)){
	echo "Error in connecting to Database.";
}else{
	$result = $con->query($sql);
    if($result->num_rows > 0){
        $return_arr['citymaster'] = array();
	       while($row = $result->fetch_array()){
	       array_push($return_arr['citymaster'],array(
	          'city_id'=>$row['city_id'],
	          'city_name'=>$row['city_name'],
			  'short_name' =>$row['short_name']));
		    }
	    echo json_encode($return_arr);
	    }
   }
?>