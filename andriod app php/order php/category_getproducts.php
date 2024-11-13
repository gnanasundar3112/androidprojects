<?php

include 'connect.php';

$stmt = $con->prepare("SELECT cate_id,cate_name,tamil_name,image FROM catemaster" );

$stmt -> execute();
$stmt -> bind_result($cate_id,$cate_name,$tamil_name,$image);

$category = array();

while($stmt -> fetch()){

    $temp = array();

	$temp['cate_id'] = $cate_id;
    $temp['cate_name'] = $cate_name ;
    $temp['tamil_name'] = $tamil_name;
    $temp['image'] = $image;
       
    array_push($category,$temp);
    }

    echo json_encode($category);
?>