<?php
require_once("library/DataAccess.php");

//Class to hold and Perform  Activities
class Users extends DataAccess{
	
	
	function __construct(){
		parent::__construct();
	}
	
	public function createNewUser($name, $email, $password){
		
		//array to hold the data retrieved
		$data = array();
		
		//query to insert user details into the users table
		$query = "INSERT INTO `users` (`id_users`,`name`, `email`, `password`,`cur_game_id`, `online_status`) VALUES (?, ?, ?, ?, ?, ?)";
		
		$password = sha1($password);
		//build the vaariables array which holds the data to bind to the prepare statement.
		$vars = array(NULL,$name,$email,$password,NULL,"online");
		
		//specify the types of data to be binded 
		$types = array("i","s","s","s","i","s");
	
		//excute the query 
		$err = $this->database->doQuery($query,$vars,$types);
		
		//check if any error occurred 
		if(empty($err)){
			
			$insertID = $this->database->getInsertId();
			
			$query = "SELECT `id_users`,`name`, `email`, `cur_game_id`, `online_status` FROM users WHERE `id_users` =" . $insertID ; 
			
			//excute the query 
			$err = $this->database->doQuery($query);
			
			//check if any error occurred 
			if(empty($err)){
				$data = $this->database->fetch_array();
			}else{
				
				ErrorHandler::HandleError(DB_ERROR,$err);
			}		
			
		}else{
			ErrorHandler::HandleError(DB_ERROR,$err);
			if(preg_match("/^Duplicate entry/i",$err)){
				$data['error'] = "You already have an account";
			}else{
				$data['error'] = "Server error ocuured";
			}
		}
		
		return $data;
	}
	
	/*
	* Get user details 
	*
	*/
	public function getUserDetails($email, $password){
	
	
		//array to hold the data retrieved
		$data = array();
		
		//query to insert user details into the users table
		$query = "SELECT `id_users`,`name`, `email`,`cur_game_id`, `online_status` FROM users WHERE `email` = ? and `password` = ? " ; 
		
		$password = sha1($password);
		//build the vaariables array which holds the data to bind to the prepare statement.
		$vars = array($email,$password);
		
		//specify the types of data to be binded 
		$types = array("s","s");
	
		//excute the query 
		$err = $this->database->doQuery($query,$vars,$types);
		
		//check if any error occurred 
		if(empty($err)){

			$data = $this->database->fetch_array();
			
		}else{
				
			ErrorHandler::HandleError(DB_ERROR,$err);
			$data['error'] = "Server error ocurred";
		}		
	
		return $data;
	}
	
	
	/*
	* Get users online list 
	*
	*/
	public function getUserList($status = "offline"){
	
	
		//array to hold the data retrieved
		$data = array();
		
		//query to insert user details into the users table
		$query = "SELECT `id_users`,`name`, `email`,`cur_game_id`, `online_status` FROM users WHERE `online_status` != ? and `id_users` != ?" ; 
		
		
		//build the vaariables array which holds the data to bind to the prepare statement.
		$vars = array($status,$_SESSION['id']);
		
		//specify the types of data to be binded 
		$types = array("s","i");
	
		//excute the query 
		$err = $this->database->doQuery($query,$vars,$types);
		
		//check if any error occurred 
		if(empty($err)){

			$data = $this->database->fetch_all_array();
			
		}else{
				
				ErrorHandler::HandleError(DB_ERROR,$err);
				$data['error'] = "Server error ocurred";
		}		
	
		return $data;
	}
	
	/*
	* Get User name for given userId 
	*
	*/
	
	public function getUserName($userId){
	
	
		if(!isset($userId)){
			$userId = $_SESSION['id'];
		}
		//array to hold the data retrieved
		$data = array();
		
		//query to insert user details into the users table
		$query = "SELECT `name` FROM users WHERE  `id_users` = ?" ; 
		
		
		//build the vaariables array which holds the data to bind to the prepare statement.
		$vars = array($userId);
		
		//specify the types of data to be binded 
		$types = array("i");
	
		//excute the query 
		$err = $this->database->doQuery($query,$vars,$types);
		
		//check if any error occurred 
		if(empty($err)){

			$data = $this->database->fetch_array();
			
		}else{
				
				ErrorHandler::HandleError(DB_ERROR,$err);
				$data['error'] = "Not able to get userName";
		}		
	
		return $data;
	}
}


?>

