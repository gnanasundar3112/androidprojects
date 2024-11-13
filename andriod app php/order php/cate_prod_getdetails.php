<?php

include 'connect.php';

$cate_id = $_POST['cate_id'];

$stmt = $con->prepare("SELECT prod_id,prod_name,tamil_name,rate,image,stock_type FROM productmaster where cate_id='$cate_id'" );

$stmt -> execute();
$stmt -> bind_result($prod_id,$prod_name,$tamil_name,$rate,$image,$stock_type);

$products = array();

while($stmt -> fetch()){

    $temp = array();

    $temp['prod_id'] = $prod_id;
    $temp['prod_name'] = $prod_name;
    $temp['tamil_name'] = $tamil_name;
    $temp['rate'] = $rate;
    $temp['image'] = $image;
    $temp['stock_type'] = $stock_type;

    array_push($products,$temp);
    }

    echo json_encode($products);
?>