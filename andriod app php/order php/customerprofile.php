<?php

include 'connect.php';

$mcustphone = $_POST['cust_phone'];

$stmt = $con->prepare("select cust_id,cust_name,cust_email,cust_phone from register where cust_phone='$mcustphone'");
$stmt -> execute();
$stmt -> bind_result($cust_id,$cust_name,$cust_email,$cust_phone);
$register = array();

while($stmt -> fetch()){
    $temp = array();
    $temp['cust_id'] = $cust_id;
    $temp['cust_name'] = $cust_name;
    $temp['cust_email'] = $cust_email;
    $temp['cust_phone'] = $cust_phone;
    array_push($register,$temp);

    }

    echo json_encode($register);
?>
