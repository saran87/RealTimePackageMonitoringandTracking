<?php
	
	/**
	 * The common.inc is where common functions used by the whole site resides.
	 *
	 */
	 
	 // ------------------------------------------------------------------------

	/**
	 * Converts Object to an array
	 *
	 * @param	object	object that needs to converted to an array
	 * @return	array	converted object as an array
	 */
	 
	function toArray($object){
		foreach($object as $key=>$value){
			$array[] = $value;
		}
		return $array;
	}
	/**
	 * getBrowser
	 *
	 * @param	
	 * @return	returns the client browser name
	 */
	function getBrowser(){
		$agent = $_SERVER["HTTP_USER_AGENT"];
		if(preg_match('/chrome/i',$agent,$clientDetail)){
			return "chrome";
		}
		else if(preg_match('/msie/i',$agent,$clientDetail)){
			return "Internet Explorer";
		}
		else if(preg_match('/firefox/i',$agent,$clientDetail)){
			return "Mozilla FireFox";
		}
		else if(preg_match('/opera/i',$agent,$clientDetail)){
			return "opera";
		}
		else if(preg_match('/safari/i',$agent,$clientDetail)){
			return "Safari";
		}
		return "";
	}

	function getOS(){
		$agent = $_SERVER["HTTP_USER_AGENT"];
		if(preg_match('/windows/i',$agent,$clientDetail)){
			return "windows";
		}
		else if(preg_match('/mac/i',$agent,$clientDetail)){
			return "MAC OS";
		}
		else if(preg_match('/linux/i',$agent,$clientDetail)){
			return "Linux";
		}
	}

	function getQueryString($type){
	
		//get the action from the GET request
		$section = isset($_GET[$type]) ? $_GET[$type] : '';
		
		//if action is not set then get it from post
		if( $section == '' ){
			$section = isset($_POST[$type]) ? $_POST[$type] : '';
		}
		
		return $section;
	
	}
	

	function santizeInput($value){
		global $errorMessage;
		if(trim($value) ==""){
			$errorMessage[] = "You have entered invalid input";
			return false;
		}
		return true;
	}

	function santizePassword($input,$password){
		
		global $errorMessage;
		if(trim($input) != $password){
			$errorMessage[] = "You have entered invalid password";
			return false;
		}
		return true;
	}
	function crossSiteScriptingImg($value) {
		$reg = "/((\%3C)|<)((\%69)|i|(\%49))((\%6D)|m|(\%4D))((\%67)|g|(\%47))[^\n]+((\%3E)|>)/i";
		return preg_match($reg,$value);
	}
	function crossSiteScripting($value) {
		$reg = "/((\%3C)|<)((\%2F)|\/)*[a-z0-9\%]+((\%3E)|>)/i";
		return preg_match($reg,$value);
	}
	
	/*
	* Redirect page
	* Redirects the page based on the option provided
	*/
	function redirectPage($option){
		
		if( $option == PAGE_NOT_FOUND){
			header('Location:' . PAGE_404); 
			exit();
		}
		else if ( $option == LOGIN){
		
			header('Location:' . LOGIN_PAGE); 
			exit();
		}
	
	}
		
	/**
		Validate an email address.
		Provide email address (raw input)
		Returns true if the email address has the email 
		address format and the domain exists.
		Referred from : http://www.linuxjournal.com/article/9585?page=0,3
	*/
		function validateEmail($email)
		{
		   $isValid = true;
		   $atIndex = strrpos($email, "@");
		   if (is_bool($atIndex) && !$atIndex)
		   {
			  $isValid = false;
		   }
		   else
		   {
			  $domain = substr($email, $atIndex+1);
			  $local = substr($email, 0, $atIndex);
			  $localLen = strlen($local);
			  $domainLen = strlen($domain);
			  if ($localLen < 1 || $localLen > 64)
			  {
				 // local part length exceeded
				 $isValid = false;
			  }
			  else if ($domainLen < 1 || $domainLen > 255)
			  {
				 // domain part length exceeded
				 $isValid = false;
			  }
			  else if ($local[0] == '.' || $local[$localLen-1] == '.')
			  {
				 // local part starts or ends with '.'
				 $isValid = false;
			  }
			  else if (preg_match('/\\.\\./', $local))
			  {
				 // local part has two consecutive dots
				 $isValid = false;
			  }
			  else if (!preg_match('/^[A-Za-z0-9\\-\\.]+$/', $domain))
			  {
				 // character not valid in domain part
				 $isValid = false;
			  }
			  else if (preg_match('/\\.\\./', $domain))
			  {
				 // domain part has two consecutive dots
				 $isValid = false;
			  }
			  else if(!preg_match('/^(\\\\.|[A-Za-z0-9!#%&`_=\\/$\'*+?^{}|~.-])+$/',
						 str_replace("\\\\","",$local)))
			  {
				 // character not valid in local part unless 
				 // local part is quoted
				 if (!preg_match('/^"(\\\\"|[^"])+"$/',
					 str_replace("\\\\","",$local)))
				 {
					$isValid = false;
				 }
			  }
			  if ($isValid && !(checkdnsrr($domain,"MX") || checkdnsrr($domain,"A")))
			  {
				 // domain not found in DNS
				 $isValid = false;
			  }
		   }
		   return $isValid;
		}
		/**
		 * ouputJson
		 * 			output json to response
		 * @param $data array of data with key/value to be ouput as json
		 * @return void
		 */

	function ouputJson ($data){
		
		header("Expires: Mon, 26 Jul 2017 05:00:00 GMT");
		header("Last-Modified: " . gmdate("D, d M Y H:i:s") . " GMT");
		header("Cache-Control: no-store, no-cache, must-revalidate");
		header("Cache-Control: post-check=0, pre-check=0", false);
		header("Pragma: no-cache");
		//MUST change the content-type
		header("Content-Type:application/json");
		// This will become the response value for the XMLHttpRequest object
		echo json_encode($data);
	}
	
?>