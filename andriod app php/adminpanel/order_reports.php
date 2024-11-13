<?php

include 'connect.php';

$from_date = $_POST['from_date'];
$fromdate = explode('/',$from_date);
$From_date = $fromdate[2].'-'.$fromdate[1].'-'.$fromdate[0];

$to_date = $_POST['to_date'];
$todate = explode('/',$to_date);
$Todate = $todate[2].'-'.$todate[1].'-'.$todate[0];
$status =  $_POST['status'];

if ($status=='All') {
    $mstatus='';
}else{
    $mstatus=" and status='$status'";
}


$stmt = $con->prepare("SELECT order_no,order_date,cust_phone,qty,amount,status FROM `salescancel` where order_date>='$From_date' and order_date<='$Todate' $mstatus UNION ALL SELECT order_no,order_date,cust_phone,qty,amount,status FROM `salesorder` where order_date>='$From_date' and order_date<='$Todate' $mstatus ORDER BY order_no" );

$stmt -> execute();
$stmt -> bind_result($order_no,$order_date,$cust_phone,$qty,$amount,$status);

$reports = array();

while($stmt -> fetch()){

    $temp = array();

    $temp['order_no'] = $order_no;
    $temp['order_date'] = $order_date;
    $temp['cust_phone'] = $cust_phone;
    $temp['qty'] = $qty;
    $temp['amount'] = $amount;
    $temp['status'] = $status;
    array_push($reports,$temp);
    }

    echo json_encode($reports);
?>