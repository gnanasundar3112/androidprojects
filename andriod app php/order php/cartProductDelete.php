<?php
include 'connect.php';

$prod_id = $_POST['prod_id'];
$cust_phone = $_POST['cust_phone'];

$sql = "DELETE FROM `cartorder` WHERE `cust_phone`='$cust_phone' and `prod_id`='$prod_id'";
 
  if(mysqli_query($con,$sql)){
    $result['state']="delete";
     echo json_encode($result);
  }
  else
  {
  echo "Deleted";
  }
    
?>
