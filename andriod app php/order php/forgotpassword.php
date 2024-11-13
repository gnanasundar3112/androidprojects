<?php

include 'connect.php';

$cust_phone = $_POST['cust_phone'];
$cust_pass =$_POST['cust_pass'];

$Sql = "Select * from  `register` where cust_phone='$cust_phone'";
$result1 = mysqli_query($con,$Sql);
if($result1){
    $row = mysqli_num_rows($result1);
    if ($row>0) {
        $Sql = "UPDATE `register` SET `cust_pass`='$cust_pass' WHERE cust_phone='$cust_phone'";
        $result = mysqli_query($con,$Sql);
        if($result){
            echo "Password changed successfully";
        }
        else{
            echo "Password not changed";
        }
    }   
    else {
        echo "There is no user in this phone number";        
    }  
}    

mysqli_close($con);
?>
