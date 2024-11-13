<?php

include 'connect.php';

$user_id = $_POST['user_id'];

$stmt = $con->prepare("select add_id,add_name,add_mobile,add_address,add_city,add_state,add_pincode,deli_type,user_id from address where user_id='$user_id'");
$stmt -> execute();
$stmt -> bind_result($add_id,$add_name,$add_mobile,$add_address,$add_city,$add_state,$add_pincode,$deli_type,$user_id);
$address = array();

while($stmt -> fetch()){
    $temp = array();
    $temp['add_id'] = $add_id;
    $temp['add_mobile'] = $add_name;
    $temp['add_mobile'] = $add_mobile;
    $temp['add_address'] = $add_address;
    $temp['add_city'] = $add_city;
    $temp['add_state'] = $add_state;
    $temp['add_pincode'] = $add_pincode;
    $temp['deli_type'] = $deli_type;
    $temp['user_id'] = $user_id;
    array_push($address,$temp);

    }

    echo json_encode($address);
?>


