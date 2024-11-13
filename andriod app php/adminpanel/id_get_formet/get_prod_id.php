<?php

include 'connect.php';

$prod_name = $_POST['prod_name'];

$stmt = $con->prepare("SELECT `prod_id` FROM `productmaster` WHERE prod_name='$prod_name'");

$stmt -> execute();
$stmt -> bind_result($prod_id);

$productmaster = array();

while($stmt -> fetch()){

    $temp = array();

    $temp['prod_id'] = $prod_id;
  
    array_push($productmaster,$temp);
    }

    echo json_encode($productmaster);
?>