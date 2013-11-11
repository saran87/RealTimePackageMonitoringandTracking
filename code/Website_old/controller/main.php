<?php
	
	/**
	 * The main.inc is the entry point of all functions and classes .
	 *
	 * The routines here dispatch control to the controller, which then directs 
	 * the controll to appropriate sections
	 *
	 * The user access to the page is verified and authenticated here.
	 */
	 
	function Intialize(){

	try{
			//Start the session
			session_start();
			//Include required class files for further processing
			IncludeRequiredClasses();
			//Intialize the controller object and start rendering the page
			$controller = new Controller();
			$controller->RenderPage();
		}
		catch(Exception  $ex){
			echo "Error occured";
		}
	}
	
	function IncludeRequiredClasses(){
		require_once('common/ErrorHandler.php');
		//include main controller file
		require_once('common/controller.php');
		//include common utility file
		require_once('common/common.php');
		require_once('common/Authorizer.php');
		require_once('common/BaseController.php');
		
		
	}

?>
