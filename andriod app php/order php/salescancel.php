<?php
include 'connect.php';

$order_no = $_POST['order_no'];
$cust_phone = $_POST['cust_phone'];
$status = $_POST['status'];
$reason = $_POST['reason'];
$crea_date = date("Y-m-d");
$crea_time = date("h:i:s");
$modi_date = date("Y-m-d");
$modi_time = date("h:i:s");


$Sql ="insert into salescancel (acyear,firm_id,order_no,order_date,cust_phone,prod_id,size_id,rate,qty,amount,
       cgstper,sgstper,igstper,cgstamt,sgstamt,igstamt,deli_charg,status,reason,crea_date,crea_time,modi_date,
       modi_time) select acyear,firm_id,order_no,order_date,cust_phone,prod_id,size_id,rate,qty,amount,cgstper,
       sgstper,igstper,cgstamt,sgstamt,igstamt,deli_charg,'$status','$reason',crea_date,crea_time,'$modi_date' ,
       '$modi_time' from salesorder where cust_phone='$cust_phone' and order_no='$order_no'";
$result = mysqli_query($con,$Sql);	   

$Sql ="delete from salesorder where cust_phone='$cust_phone' and order_no='$order_no'";
$result = mysqli_query($con,$Sql);	   

if($result){
    echo "canceled successfully";
}
else{
    echo "Not canceled";
}

mysqli_close($con);
?>