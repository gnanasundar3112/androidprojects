<?php
include 'connect.php';

$cust_phone = $_POST['cust_phone'];

$mstr = $con->prepare("SELECT count(*) as row_count FROM cartorder where cust_phone='$cust_phone' group by cust_phone");
$mstr -> execute();
$mstr -> bind_result($row_count);
$mcount = array();
while($mstr -> fetch()){
    $temp = array();
    $temp['row_count'] = $row_count;
    array_push($mcount,$temp);
}
echo json_encode($mcount);
?>
