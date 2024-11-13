<?php

include 'connect.php';

$item_total = $_POST['item_total'];
$delivery = $_POST['delivery'];
$tax = $_POST['tax'];
$total_price = $_POST['total_price'];
$cust_phone = $_POST['cust_phone'];

$Sql = "INSERT INTO `cartprice`( `item_total`, `delivery`, `tax`, `total_price`,`user_id`) VALUES ('$item_total','$delivery','$tax','$total_price','$user_id') ON DUPLICATE KEY UPDATE item_total='$item_total',delivery='$delivery',tax='$tax',total_price='$total_price',cust_phone='$cust_phone'";


$result = mysqli_query($con,$Sql);

if($result){
    echo "Checkout";
}
else{
    echo "Not Added";
}

mysqli_close($con);
?>