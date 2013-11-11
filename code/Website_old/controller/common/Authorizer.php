<?php

/*
* Authorizer - Class with authorize the user getting into the application
*
* If the user is not authorized it redirects the user to login page
* Authorization happens by decoding the token and verifying the information from client.
*
*/


class Authorizer{

	
	
	private $cipherConstant = 80456;
	private $tokenSeperator = "0xaea";
	private $lengthSepartor = "-";
	private $timeOut = 3600 ; //one hour
	private $tokenName = "token";
	/**
	* GenerateToken - This method generates a token using an encryption technique
	*
	*
	*/
	public function GenerateToken($userId){
		
		$ipAddress = $_SERVER["SERVER_ADDR"];
		
		$timeStamp =  time();
		
		//clean the ipadress by removing - or :
		$ipAddress = preg_replace('/[:|-|.]*/i','',$ipAddress);
	
		//Get the cipher array of userId, ipAddress,, timestamp
		$cipherArr = $this->Encrypt($userId,$ipAddress,$timeStamp);
		
		//form a string with a seprator string between each cipher
		$cipherText = implode($this->tokenSeperator,$cipherArr); 
	
		//Get the len of the cipher text
		$cipherLen = strlen($cipherText);
		
		//Get the sha value of the cipher text to use it as check sum
		$checkSum = SHA1($cipherText);
		
		//create the final token by appending length of check sum and cipherLen and
		$token =  substr($checkSum,0,19) . $cipherLen . $this->lengthSepartor . substr($cipherText,0, $cipherLen/2) . substr($checkSum,19,39) . substr($cipherText,$cipherLen/2);
		
		
		return $token ;

	}
	/**
	* VerifyToken - verify the user token in cookie
	* if it is valid return true otherwise return false;
	*
	* return Boolean
	*/
	public function VerifyToken(){
	
		$isVerified = false;
		
		if(isset($_COOKIE[$this->tokenName])){
			$token = $_COOKIE[$this->tokenName];
			//parse the token into cipher arrays
			$cipherArr = $this->ParseToken($token);
			
			if($cipherArr != false){
				//decrypt the cipher array to data array
				$data = $this->Decrypt($cipherArr['userId'],$cipherArr['ipAddress'],$cipherArr['timeStamp']);

			
				if( intval($_SESSION['id']) === intval($data['userId'])){
				
					$ipAddress = $_SERVER["SERVER_ADDR"];
					//clean ipaddress
					$ipAddress = preg_replace('/[:|-|.]*/i','',$ipAddress);
					//echo $data['ipAddress'] . '---' . $ipAddress ;
					if( $data['ipAddress'] == $ipAddress){
					
						$timeStamp =  time();
						
						if( ($data['timeStamp'] + $this->timeOut) > $timeStamp){
							$isVerified = true;
						}
					}
				}
			}
		}
		return $isVerified;
	}
	/**
	* send Token to Client - set the user token to cookie
	*
	*/
	public function SendTokenToClient($token){
		
		setcookie($this->tokenName, $token, time() + $this->timeOut);
	}
	
	private function Encrypt($userId,$ipAddress,$timeStamp){
		
		$cipherArr = array();
	
		$cipherArr['userId']     = base_convert($this->EncryptWithEquation($userId),10,16);
		$cipherArr['ipAddress']  = base_convert($this->EncryptWithEquation($ipAddress,1),10,16);
		$cipherArr['timeStamp']  = base_convert($this->EncryptWithEquation($timeStamp,1),10,18);		
		
		return $cipherArr;
	}
	
	private function Decrypt($userId,$ipAddress,$timeStamp){
		
		$data = array();
		
		$data['userId']    = $this->DecryptWithEquation(base_convert($userId,16,10));
		$data['ipAddress'] = $this->DecryptWithEquation(base_convert($ipAddress,16,10),1);
		$data['timeStamp'] = $this->DecryptWithEquation(base_convert($timeStamp,18,10),1);;
		
		
		return $data;
	}

	private function EncryptWithEquation($data,$polynomial = 3){
	
		$cipher = 0;
	
		$cipher = pow($data,$polynomial);
		
		$cipher  += $this->cipherConstant;
		
		return $cipher;
	}
	
	private function DecryptWithEquation($data,$polynomial = 3){
	
		$data = $data - $this->cipherConstant;
		
		$data = pow($data,1/$polynomial);
		
		return round($data);
	}
	
	private function ParseToken($token){
		
		$checkSum = substr($token,0,19);
		
		$token =  substr($token,19);
		
		$cipherLen = strtok($token,$this->lengthSepartor);
		
		$token = strtok($this->lengthSepartor);
		
		$cipher = substr($token,0, $cipherLen/2);
		
		$token =  substr($token,$cipherLen/2);
		
		$checkSum .=  substr($token,0,21);
		$cipher .=  substr($token,21);
		
		//form a string with a seprator string between each cipher
		list($userId,$ipAddress,$timeStamp) = explode($this->tokenSeperator,$cipher); 
		
		$cipherArr['userId'] = $userId;
		$cipherArr['ipAddress'] = $ipAddress;
		$cipherArr['timeStamp'] = $timeStamp;
		
		//form a string with a seprator string between each cipher
		$cipherText = implode($this->tokenSeperator,$cipherArr); 
		
		if(sha1($cipherText) != $checkSum){
			return false;
		}
		
		return $cipherArr;
	}
}

?>