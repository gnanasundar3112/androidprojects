<?php
include 'connect.php';

$cate_id = $_POST['cate_id'];

$sql = "DELETE FROM `catemaster` WHERE `cate_id`='$cate_id'";
 
  if(mysqli_query($con,$sql)){
    $result['catemaster']="delete";
     echo json_encode($result);
  }
  else
  {
  echo "Deleted";
  }
    
?>
