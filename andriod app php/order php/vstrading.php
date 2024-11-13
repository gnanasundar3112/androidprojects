<?php
	include "connect.php";
	include "main.php";
	include "functions.php";
	
	$GLOBALS['mfirm']='1';
	$mdate=date("Y-m-d");
	$GLOBALS['yyyy']=acyear($mdate);
	$GLOBALS['myearmark']=yearmark($GLOBALS['yyyy']);
	$GLOBALS['mfirmmark']=firmmark($GLOBALS['mfirm']);
?>