<?php

include 'connect.php';
	
$sql = "SELECT * FROM `productmaster`";

if(!$con->query($sql)){
	echo "Error in connecting to Database.";
}else{
	$result = $con->query($sql);
    if($result->num_rows > 0){
        $return_arr['productmaster'] = array();
	       while($row = $result->fetch_array()){
	       array_push($return_arr['productmaster'],array(
	          'prod_id'=>$row['prod_id'],
	          'prod_name'=>$row['prod_name']));
		    }
	    echo json_encode($return_arr);
	    }
   }
?>