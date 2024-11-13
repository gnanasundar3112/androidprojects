<?php
include 'connect.php';

$prod_id = $_POST['prod_id'];

$sql = "DELETE FROM `productmaster` WHERE `prod_id`='$prod_id'";
 
  if(mysqli_query($con,$sql)){
    $result['productmaster']="delete";
     echo json_encode($result);
  }
  else
  {
  echo "Deleted";
  }
    
?>
