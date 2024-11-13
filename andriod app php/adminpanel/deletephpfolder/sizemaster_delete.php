<?php
include 'connect.php';

$eff_date = $_POST['eff_date'];
$prod_id = $_POST['prod_id'];
$size_id = $_POST['size_id'];

$sql = "DELETE FROM `productsize` WHERE `eff_date`='$eff_date' and `prod_id`='$prod_id' and `size_id`='$size_id'";
 
  if(mysqli_query($con,$sql)){
    $result['productsize']="delete";
     echo json_encode($result);
  }
  else
  {
  echo "Deleted";
  }
    
?>
