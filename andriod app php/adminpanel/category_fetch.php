<?php

include 'connect.php';
	
$sql = "SELECT * FROM `catemaster`";

if(!$con->query($sql)){
	echo "Error in connecting to Database.";
}else{
	$result = $con->query($sql);
    if($result->num_rows > 0){
        $return_arr['catemaster'] = array();
	       while($row = $result->fetch_array()){
	       array_push($return_arr['catemaster'],array(
	          'cate_id'=>$row['cate_id'],
	          'cate_name'=>$row['cate_name']));
		    }
	    echo json_encode($return_arr);
	    }
   }
?>

