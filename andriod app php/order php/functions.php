<?php
    require_once('connect.php');
    
    function acyear($mdate)

    {
        if(date('m',strtotime($mdate))<'04'){
			$yyyy1 = date("Y",strtotime($mdate))-1;
            $yyyy2 =substr(date("Y",strtotime($mdate)),2);
            $yyyy = $yyyy1.$yyyy2;
            }
		ELSE {
        
			$yyyy1 = date('Y',strtotime($mdate));
            $yyyy2=substr(date("Y",strtotime($mdate))+1,2,2);
            
            $yyyy = $yyyy1.$yyyy2;
            }
	
        return $yyyy;
    }

	function maxdays($mdate)
	
    {
		$month=date('m',strtotime($mdate));
		$myear=date("Y",strtotime($mdate));
		if ($month == "02")
		{
			if ($year % 4 == 0) return 29;
			else return 28;
		}
		else if ($month == "01" || $month == "03" || $month == "05" || $month == "07" || $month == "08" || $month == "10" || $month == "12") return 31;
		else return 30;
	}	

    function yearmark($myyyy){
        include "connect.php";
        $mstr = $con->prepare("SELECT year_mark FROM acyear where year_id='$myyyy'");
        $mstr -> execute();
        $mstr -> bind_result($year_mark);
        $myearmark = '';
        while($mstr -> fetch()){
            $myearmark = $year_mark;
        }
        return $myearmark;
    }

    function firmmark($mxfirm){
        include "connect.php";
        $mstr = $con->prepare("SELECT firm_mark FROM firmmaster where firm_id='$mxfirm'");
        $mstr -> execute();
        $mstr -> bind_result($firm_mark);
        $mfirmmark = '';
        while($mstr -> fetch()){
            $mfirmmark = $firm_mark;
        }
        return $mfirmmark;
    }
    
	
	function datecheck($mtype,$mpara1)
    {
    	$GLOBALS['yyyy']='202223';
    	if ($mtype==1)
        	{
	        	$mdate=substr($mpara1,0,4).'-'.substr($mpara1,4,2).'-01';
        	}  else {
            	$mdate=$mpara1;
            }
		$mday=date('d',strtotime($mdate));
		$mmonth=date('m',strtotime($mdate));
		$myear=date("Y",strtotime($mdate));
        $mout=var_dump(checkdate($mmonth,$mday,$myear));    
        if ($mout==bool(true))
        {
        	echo ($GLOBALS['yyyy']==acyear($mdate));
        	if ($GLOBALS['yyyy']==acyear($mdate)) {
            } else {
            	return 'Entered Date is not in valid Financial year...'; 
            }
            
        } else {
           	return "Please Enter Valid Date Format";
      	}
	}	
	
    firmmark('1');

?>
