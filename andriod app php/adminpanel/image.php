<?php
include 'connect.php';

$_POST['image'];
	$path = date("d-m-Y").'-'.time().'-'.rand(10000, 100000).'.jpg';
	
	if(file_put_contents($path,base64_decode($_POST['image']))){
	
		echo 'success';
		
	}else{
		echo 'failed';
    }
	
	<?php

	if(!empty($_POST['image'])) {

	if(file_put_contents(date("d-m-Y"). '-'.time().'-'.rand(10000, 100000).'.jpg', base64_decode($_POST['image']))){ 
		echo 'success';

	}else 
		echo 'Failed to upload image';
	
	}else 
		echo 'No image found';
	}

?>


<?php
include 'connect.php';

$image_cate = "Images/";
$image = $_POST['image'];
$imagestore = rand()."_".".jpeg";
$target = $image_cate."/".$imagestore ;
file_put_contents($target,base64_decode($image))


    $path = 'Images/'.date("d-m-Y").'-'.time().'-'.rand(10000, 100000).'.jpg';
	
	if(file_put_contents($path,base64_decode($image))){
	
		echo 'success';
		
	}else{
		echo 'failed';
	}

?>

$image = $_POST['image'];
$decode_image = base64_decode($image);
file_put_contents("Image.jpg",$decode_image);




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
