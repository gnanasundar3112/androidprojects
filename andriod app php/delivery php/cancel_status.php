<?php

include 'connect.php';

$status = "Cancel";

$stmt = $con->prepare("SELECT order_no,modi_date,status FROM salescancel where status='$status' group by order_no" );

$stmt -> execute();
$stmt -> bind_result($order_no,$modi_date,$status);

$salescancel = array();

while($stmt -> fetch()){

    $temp = array();

	$temp['order_no'] = $order_no;
    $temp['modi_date'] = $modi_date ;
    $temp['status'] = $status;
       
    array_push($salescancel,$temp);
    }
    echo json_encode($salescancel);
?>