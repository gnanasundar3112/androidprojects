<?php

include 'connect.php';
	
$mproduct = $_GET['prod_id'];
$kdt=date("Y-m-d");

$sql = "select p.prod_id,c.eff_date,c.size_id,d.size_name,d.factor from productmaster as p left join 
	 (select a.prod_id,a.eff_date,a.size_id from productsize as a join (select prod_id,MAX(eff_date) 
	 as eff_date from productsize where eff_date<='$kdt' and prod_id='$mproduct' group by prod_id) as 
	 b ON(a.prod_id=b.prod_id and a.eff_date=b.eff_date)) as c ON(p.prod_id=c.prod_id) left join sizemaster as d 
	 on(c.size_id=d.size_id) where c.eff_date<='$kdt' and c.prod_id='$mproduct'";

if(!$con->query($sql)){
	echo "Error in connecting to Database.";
}else{
	$result = $con->query($sql);
    if($result->num_rows > 0){
        $return_arr['productsize'] = array();
	       while($row = $result->fetch_array()){
	       array_push($return_arr['productsize'],array(
	          'size_id'=>$row['size_id'],
	          'size_name'=>$row['size_name'],
			  'factor' =>$row['factor']));
		    }
	    echo json_encode($return_arr);
	    }
   }
?>