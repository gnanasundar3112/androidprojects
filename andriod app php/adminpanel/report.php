<?php

include 'connect.php';

$stmt = $con->prepare("SELECT a.cate_id,b.cate_name as cate_name,a.prod_name,a.tamil_name,a.rate,a.image,a.stock_type FROM productmaster as a left join `catemaster` as b on(a.cate_id=b.cate_id)" );

$stmt -> execute();
$stmt -> bind_result($prod_id,$prod_name,$tamil_name,$rate,$image,$stock_type);

$products = array();

while($stmt -> fetch()){

    $temp = array();

    $temp['cate_name'] = $cate_name;
    $temp['prod_name'] = $prod_name;
    $temp['tamil_name'] = $tamil_name;
    $temp['rate'] = $rate;
    $temp['image'] = $image;
    $temp['stock_type'] = $stock_type;

    array_push($products,$temp);
    }

    echo json_encode($products);
?>


