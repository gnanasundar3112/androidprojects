<?php
include 'connect.php';
include 'vstrading.php';

$myyyy = $GLOBALS['yyyy'];
$mfirm = $GLOBALS['mfirm'];
$myearmark = $GLOBALS['myearmark'];
$mfirmmark = $GLOBALS['mfirmmark'];
$morderno =$myearmark.$mfirmmark."00000001";
$order_date = date("Y-m-d");
$acyear= acyear("$order_date");
$add_name = $_POST['add_name'];
$add_mobile = $_POST['add_mobile'];
$add_address = $_POST['add_address'];
$add_state = $_POST['add_state'];
$add_city = $_POST['add_city'];
$add_pincode = $_POST['add_pincode'];
$deli_man = "";
$cust_phone = $_POST['cust_phone'];
$status = $_POST['status'];
$deli_type = $_POST['deli_type'];
$crea_date = date("Y-m-d");
$crea_time = date("h:i:s");
$modi_date = date("Y-m-d");
$modi_time = date("h:i:s");

 $mstr = $con->prepare("SELECT concat(substr(substr(order_no,3,8)+100000001,2,8)) as order_no from address where 
         acyear='$myyyy' and firm_id='$mfirm' order by order_no desc limit 1");
 $mstr -> execute();
 $mstr -> bind_result($order_no);
 while($mstr -> fetch()) {
     $morderno = $myearmark.$mfirmmark.$order_no;
  }

$Sql = "INSERT INTO `address`(`acyear`, `firm_id`, `order_no`, `order_date`, `add_name`, `add_mobile`, `add_address`,
		`add_state`, `add_city`, `add_pincode`, `cust_phone`, `deli_type`,`crea_date`,`crea_time`,`modi_date`,
		`modi_time`) VALUES ('$myyyy','$mfirm','$morderno','$order_date','$add_name','$add_mobile','$add_address',
		'$add_state','$add_city','$add_pincode','$cust_phone','$deli_type','$crea_date','$crea_time','$modi_date','$modi_time')";
$result = mysqli_query($con,$Sql);
  
$Sql ="insert into salesorder (acyear,firm_id,order_no,order_date,cust_phone,prod_id,size_id,rate,qty,amount,
       cgstper,sgstper,igstper,cgstamt,sgstamt,igstamt,deli_charg,status,deli_man,crea_date,crea_time,modi_date,modi_time) select '$myyyy' as acyear,'$mfirm' as firm_id,'$morderno' as order_no,
       '$order_date' as order_date,cust_phone,prod_id,size_id,rate,qty,amount,cgstper,sgstper,igstper,cgstamt,
       sgstamt,igstamt,deli_charg,'$status','$deli_man','$crea_date' as crea_date,'$crea_time' as crea_time,'$modi_date' as modi_date,'$modi_time' as modi_time 
       from cartorder  where cust_phone='$cust_phone'";
$result = mysqli_query($con,$Sql);	   

$Sql ="delete from cartorder where cust_phone='$cust_phone'";
$result = mysqli_query($con,$Sql);	   

if($result){
    echo "Ordered successfully";
}
else{
    echo "Not Ordered";
}

mysqli_close($con);
?>