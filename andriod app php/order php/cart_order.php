<?php

include 'connect.php';
include 'functions.php';
include 'main.php';

$firm_id = "1";
$ref_date = date("Y-m-d");
$acyear= acyear("$ref_date");
$cust_phone = $_POST['cust_phone'];
$prod_id = $_POST['prod_id'];
$size_id = $_POST['size_id'];
$rate = $_POST['rate'];
$qty = $_POST['qty'];
$amount = $_POST['amount'];
$crea_date = date("Y-m-d");
$crea_time = date("h:i:s");
$modi_date = date("Y-m-d");
$modi_time = date("h:i:s");


$Sql = "INSERT INTO `cartorder`(`acyear`,`firm_id`,`ref_date`,`cust_phone`,`prod_id`,`size_id`,`rate`,
       `qty`,`amount`,`crea_date`,`crea_time`,`modi_date`,`modi_time`) VALUES ('$acyear','$firm_id',
       '$ref_date','$cust_phone','$prod_id','$size_id','$rate','$qty','$amount','$crea_date','$crea_time',
       '$modi_date','$modi_time') ON DUPLICATE KEY UPDATE ref_date='$ref_date',size_id='$size_id',rate='$rate',
	   qty='$qty',amount='$amount',modi_date='$modi_date',
	   modi_time='$modi_time'";

$result = mysqli_query($con,$Sql);


if($result){
    echo "Added to cart";
}
else{
    echo "Not Added";
    mysqli_error($result);

}
mysqli_close($con);
?>