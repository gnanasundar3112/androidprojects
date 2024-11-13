<?php

include 'connect.php';

$order_no = $_POST['order_no'];
$cust_phone = $_POST['cust_phone'];
$status = $_POST['status'];
$deli_man = $_POST['deli_man'];


$Sql = "update salesorder set status='$status',deli_man='$deli_man' where order_no = '$order_no' and cust_phone = '$cust_phone'";
$result = mysqli_query($con,$Sql);

if($result){
    echo "successfully";
}
else{
    echo "Not Delivered";
}
mysqli_close($con);
?>   