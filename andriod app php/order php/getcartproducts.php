<?php

include 'connect.php';

$mcustphone = $_POST['cust_phone'];

$stmt = $con->prepare("SELECT a.cust_phone,a.prod_id,b.prod_name as prod_name,b.tamil_name as tamil_name,b.image,a.rate,c.size_name as size_name,a.qty,a.amount FROM cartorder as a left join `productmaster` as b on(a.prod_id =b.prod_id) left join `sizemaster` as c on(a.size_id =c.size_id) where a.cust_phone='$mcustphone'");

$stmt -> execute();
$stmt -> bind_result($cust_phone,$prod_id,$prod_name,$tamil_name,$image,$rate,$size_name,$qty,$amount);

$cartorder = array();

while($stmt -> fetch()){

    $temp = array();
    
    $temp['cust_phone'] = $cust_phone;
    $temp['prod_id'] = $prod_id;
    $temp['prod_name'] = $prod_name;
    $temp['tamil_name'] = $tamil_name;
    $temp['image'] = $image;
    $temp['rate'] = $rate;
    $temp['gram'] = $size_name;
    $temp['qty'] = $qty;
    $temp['amount'] = $amount;

    array_push($cartorder,$temp);
    }

    echo json_encode($cartorder);
?>