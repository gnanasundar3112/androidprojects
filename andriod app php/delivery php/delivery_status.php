<?php

include 'connect.php';

$status = "Delivered";

$stmt = $con->prepare("SELECT order_no,modi_date,status FROM salesorder where status='$status' group by order_no" );

$stmt -> execute();
$stmt -> bind_result($order_no,$modi_date,$status);

$salesorder = array();

while($stmt -> fetch()){

    $temp = array();

	$temp['order_no'] = $order_no;
    $temp['modi_date'] = $modi_date ;
    $temp['status'] = $status;
       
    array_push($salesorder,$temp);
    }
    echo json_encode($salesorder);
?>