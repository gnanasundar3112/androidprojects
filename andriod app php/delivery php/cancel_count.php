<?php
include 'connect.php';

$status = "Cancel";

$mstr = $con->prepare("SELECT count(*) as row_count FROM salescancel where status='$status' group by status");
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
