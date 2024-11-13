<?php

include 'connect.php';
	
$sql = "SELECT * FROM `statemaster`";

if(!$con->query($sql)){
	echo "Error in connecting to Database.";
}else{
	$result = $con->query($sql);
    if($result->num_rows > 0){
        $return_arr['statemaster'] = array();
	       while($row = $result->fetch_array()){
	       array_push($return_arr['statemaster'],array(
	          'state_id'=>$row['state_id'],
	          'state_name'=>$row['state_name'],
			  'short_name' =>$row['short_name']));
		    }
	    echo json_encode($return_arr);
	    }
   }
?>