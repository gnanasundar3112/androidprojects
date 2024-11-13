<?php

include 'connect.php';

$order_no = $_POST["order_no"];

$stmt = $con->prepare("SELECT  b.prod_name as prod_name,c.size_name as size_name,a.rate,a.qty,a.amount 
                        FROM salesorder as a left join`productmaster` as b on(a.prod_id=b.prod_id) 
                        left join `sizemaster` as c on(a.size_id=c.size_id) where order_no='$order_no' 
                        UNION ALL SELECT b.prod_name as prod_name,c.size_name as size_name,a.rate,a.qty,
                        a.amount FROM salescancel as a left join`productmaster` as b on(a.prod_id=b.prod_id) 
                        left join `sizemaster` as c on(a.size_id=c.size_id) where order_no='$order_no'");

$stmt -> execute();
$stmt -> bind_result($prod_name,$size_name,$rate,$qty,$amount);

$reports_detail = array();

while($stmt -> fetch()){

    $temp = array();

	$temp['prod_name'] = $prod_name;
    $temp['size_name'] = $size_name;
    $temp['rate'] = $rate;
	$temp['qty'] = $qty;
    $temp['amount'] = $amount;

    array_push($reports_detail,$temp);
    }

    echo json_encode($reports_detail);
?>

