<?php
include 'connect.php';

$user_id = $_POST['user_id'];

$sql = "DELETE FROM `user_register` WHERE `user_id`='$user_id'";
 
  if(mysqli_query($con,$sql)){
    $result['user_register']="delete";
     echo json_encode($result);
  }
  else
  {
  echo "Deleted";
  }
    
?>
