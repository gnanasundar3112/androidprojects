<?php

include 'connect.php';

$stmt = $con->prepare("SELECT b.cate_name,a.prod_id,a.prod_name,a.tamil_name,a.short_name,a.rate,a.image,a.stock_type,a.active FROM productmaster as a
left join `catemaster` as b ON(a.cate_id=b.cate_id)");

$stmt -> execute();
$stmt -> bind_result($cate_name,$prod_id,$prod_name,$tamil_name,$short_name,$rate,$image,$stock_type,$active);

$products = array();

while($stmt -> fetch()){

    $temp = array();
  
    $temp['cate_name'] = $cate_name;
    $temp['prod_id'] = $prod_id;
    $temp['prod_name'] = $prod_name;
    $temp['tamil_name'] = $tamil_name;
    $temp['short_name'] = $short_name;
    $temp['rate'] = $rate;
    $temp['image'] = $image;
    $temp['stock_type'] = $stock_type;
    $temp['active'] = $active;

    array_push($products,$temp);
    }

    echo json_encode($products);
?>