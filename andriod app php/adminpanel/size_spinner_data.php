<?php

include 'connect.php';
	
$sql = "SELECT * FROM `sizemaster`";

if(!$con->query($sql)){
	echo "Error in connecting to Database.";
}else{
	$result = $con->query($sql);
    if($result->num_rows > 0){
        $return_arr['sizemaster'] = array();
	       while($row = $result->fetch_array()){
	       array_push($return_arr['sizemaster'],array(
	          'size_id'=>$row['size_id'],
	          'size_name'=>$row['size_name']));
		    }
	    echo json_encode($return_arr);
	    }
   }
?>