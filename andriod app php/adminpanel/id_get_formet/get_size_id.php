<?php

include 'connect.php';

$size_name = $_POST['size_name'];

$stmt = $con->prepare("SELECT size_id FROM sizemaster where size_name='$size_name'" );

$stmt -> execute();
$stmt -> bind_result($size_id);

$sizemaster = array();

while($stmt -> fetch()){

    $temp = array();

    $temp['size_id'] = $size_id;
  
    array_push($sizemaster,$temp);
    }

    echo json_encode($sizemaster);
?>