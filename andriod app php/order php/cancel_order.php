<?php

include 'connect.php';

$cust_phone = $_POST['cust_phone'];
$order_no = $_POST['order_no'];


$stmt = $con->prepare("SELECT a.order_no,a.order_date,a.prod_id,b.prod_name as prod_name,b.tamil_name as tamil_name,
        b.image as image,a.size_id,c.size_name as size_name,a.rate,a.qty,a.amount,status,a.modi_date FROM salesorder 
        as a left join`productmaster` as b on(a.prod_id=b.prod_id) left join `sizemaster` as c on
        (a.size_id=c.size_id) where cust_phone='$cust_phone' and order_no='$order_no'");

$stmt -> execute();
$stmt -> bind_result($order_no,$order_date,$prod_id,$prod_name,$tamil_name,$image,$size_id,$size_name,$rate,$qty,$amount,$status,$modi_date);

$myorders = array();

while($stmt -> fetch()){

    $temp = array();

    $temp['order_no'] = $order_no;
    $temp['order_date'] = $order_date;
    $temp['prod_id'] = $prod_id;
	$temp['prod_name'] = $prod_name;
    $temp['tamil_name'] = $tamil_name;
    $temp['image'] = $image;
	$temp['size_id'] = $size_id;
    $temp['size_name'] = $size_name;
    $temp['rate'] = $rate;
	$temp['qty'] = $qty;
    $temp['amount'] = $amount;
    $temp['status'] = $status;
    $temp['modi_date'] = $modi_date;

    array_push($myorders,$temp);
    }

    echo json_encode($myorders);
?>

