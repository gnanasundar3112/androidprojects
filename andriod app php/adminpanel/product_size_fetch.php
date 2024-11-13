<?php

include 'connect.php';

$stmt = $con->prepare("SELECT a.eff_date,a.prod_id,b.prod_name,a.size_id,c.size_name FROM productsize as a left join `productmaster` as b on(a.prod_id=b.prod_id) left join `sizemaster` as c on(a.size_id=c.size_id)" );

$stmt -> execute();
$stmt -> bind_result($eff_date,$prod_id,$prod_name,$size_id,$size_name);

$productsize = array();

while($stmt -> fetch()){

    $temp = array();

    $temp['eff_date'] = $eff_date;
    $temp['prod_name'] = $prod_name;
    $temp['prod_id'] = $prod_id;
    $temp['size_id'] = $size_id;
    $temp['size_name'] = $size_name;
  
    array_push($productsize,$temp);
    }

    echo json_encode($productsize);
?>