<?php
include 'connect.php';

$status = "Ordered";

$mstr = $con->prepare("SELECT count(*) as row_count FROM salesorder where status='$status' group by status");
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
