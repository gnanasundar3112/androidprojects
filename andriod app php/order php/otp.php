<?php
include 'connect.php';
$cust_phone = $_POST['cust_phone'];	

$sql="update register set otp=substr(floor(rand()*999999)+1000000,2,7) where cust_phone=$cust_phone";
$result = mysqli_query($con,$sql);
if($result){
	echo "otp sent sucessfully";
}
else{
	echo "otp not generated";
}

mysqli_close($con);
?>
