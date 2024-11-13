<?php

include 'connect.php';

$stmt = $con->prepare("select user_id,user_name,user_password,user_phone,aadhar_no,user_address,user_image,aadhar_image,bike_image,active from user_register");
$stmt -> execute();
$stmt -> bind_result($user_id,$user_name,$user_password,$user_phone,$aadhar_no,$user_address,$user_image,$aadhar_image,$bike_image,$active);
$user_register = array();

while($stmt -> fetch()){
    $temp = array();
    
    $temp['user_id'] = $user_id;
    $temp['user_name'] = $user_name;
    $temp['user_password'] = $user_password;
    $temp['user_phone'] = $user_phone;
    $temp['aadhar_no'] = $aadhar_no;
    $temp['user_address'] = $user_address;
    $temp['user_image'] = $user_image;
    $temp['aadhar_image'] = $aadhar_image;
    $temp['bike_image'] = $bike_image;
    $temp['active'] = $active;
    array_push($user_register,$temp);

    }
    echo json_encode($user_register);
?>


