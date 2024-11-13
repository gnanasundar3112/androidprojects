<?php

include 'connect.php';

$status = "Ordered";

$stmt = $con->prepare("SELECT a.order_no,a.order_date,a.cust_phone,b.add_address,b.add_state,b.add_city,b.add_pincode,b.deli_type FROM salesorder as a left join `address` as b on(a.order_no =b.order_no) where status='$status' 
    group by order_no" );

$stmt -> execute();
$stmt -> bind_result($order_no,$order_date,$cust_phone,$add_address,$add_state,$add_city,$add_pincode,$deli_type);

$salesorder = array();

while($stmt -> fetch()){

    $temp = array();

	$temp['order_no'] = $order_no;
    $temp['order_date'] = $order_date ;
    $temp['cust_phone'] = $cust_phone;
   	$temp['add_address'] = $add_address;
    $temp['add_state'] = $add_state ;
    $temp['add_city'] = $add_city;
   	$temp['add_pincode'] = $add_pincode;
    $temp['deli_type'] = $deli_type ;
  
       
    array_push($salesorder,$temp);
    }
    echo json_encode($salesorder);
?>