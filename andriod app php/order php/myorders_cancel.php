<?php

include 'connect.php';

$cust_phone = $_POST['cust_phone'];

$stmt = $con->prepare("SELECT a.order_no,a.order_date,a.prod_id,b.image as image,a.status FROM salescancel 
                        as a left join `productmaster` as b on(a.prod_id=b.prod_id) 
                        where cust_phone='$cust_phone' group by order_no");

$stmt -> execute();
$stmt -> bind_result($order_no,$order_date,$prod_id,$image,$status);

$myorders = array();

while($stmt -> fetch()){

    $temp = array();

    $temp['order_no'] = $order_no;
    $temp['order_date'] = $order_date;
    $temp['image'] = $image;
     $temp['status'] = $status;

    array_push($myorders,$temp);
    }

    echo json_encode($myorders);
?>
