<?php

include 'connect.php';

$size_name = $_POST['size_name'];

$stmt = $con->prepare("SELECT size_id,size_name,factor FROM sizemaster where size_name='$size_name'");

$stmt -> execute();
$stmt -> bind_result($size_id,$size_name,$factor);

$size_factor = array();

while($stmt -> fetch()){

    $temp = array();
    $temp['size_id'] = $size_id;
    $temp['size_name'] = $size_name;
    $temp['factor'] = $factor;
  
  
    array_push($size_factor,$temp);
    }

    echo json_encode($size_factor);
?>