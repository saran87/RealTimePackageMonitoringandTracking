<?php

	/**
	 * The controller.inc is where all the request are handled and dispatched to appropriate methods .
	 *
	 * The routines here dispatch control to the controller, which then directs 
	 * the controll to appropriate sections
	 *
	 */
	 
	 // ------------------------------------------------------------------------

	/**
	 * Controller Class
	 *
	 * This class contains functions that handles and process the request
	 *
	 * @author	Saravana Kumar
	 */
	class Controller{

		private $section; 
		private $action;
		private $type;
		
		/**
		 * Intialize
		 * Intializes the section and action params from query string
		 *
		 * @return void
		 */
		 function __construct() {
			$this->section = getQueryString("s");
			$this->action = getQueryString("action");
			if( $this->action == ""){
				//to render index page if none is specified
				$this->action = "index";
			}
			$this->type = getQueryString("type");
		 }
		
		
		/**
		 * RenderPage
		 * Based upon section requested it branches
		 *
		 * @return	void
		 */
		function RenderPage(){
		
			if($this->section == ''){		
				$this->RenderFrontPage();
			}
			else{
				
				$error = false;
				
				//include all of the files with .inc extension in section folder
				foreach(glob(CONTROLLER_PATH.$this->section."/*.php") as $filename){
					require_once($filename);
				}
				
				//Check if the controller class exists
				if (class_exists($this->section)){
					//create object for particular section
					$obj = new $this->section();
					
					if(method_exists($obj,$this->action)){
						
						$method = array($obj, $this->action);
						
						//make the call to the method in the controller object
						$result=@call_user_func($method,$_SERVER['REMOTE_ADDR'],$_COOKIE['token']);
						if(method_exists($obj,'renderView')){
							
							if($this->type != "json"){
								
								$method = array($obj, 'renderView');
								$viewPage = VIEW_PATH . $this->section . "/" . $this->action . ".php";
							
								//start rendering view
								$isDone = @call_user_func($method,$viewPage);
								if(!$isDone){
									$error = true;
								}
							}
						}
						else{
							$error = false;
						}
					}	
					else{
						$error = true;
					}
				}
				else{
					$error = true;
				}
				
				if($error)
					ErrorHandler::HandleError(PAGE_NOT_FOUND);				
			}
		}
		// -------------------------------------------------------------
		/**
		 * RenderFrontPage
		 * It prepares the content/objects needed for home page
		 *
		 * @return	void
		 */
		private function RenderFrontPage(){
					
			$isIncluded  = false;
			
			//include all of the files with .inc extension in section folder
			foreach(glob(CONTROLLER_PATH."home"."/*.php") as $filename){
				require_once($filename);
			}
			$home = new Home();
			$home->index();
			
			//include all of the files in front page folder
			foreach(glob(VIEW_PATH . FRONT_PAGE) as $filename){
				$isIncluded = true;
				$home->renderView($filename);
			}
			
			//if the file is not included redirect to NOT FOUND page
			if(!$isIncluded){
				ErrorHandler::HandleError(PAGE_NOT_FOUND);	
			}
			
		}
		
		/**
		 * CreateVisitorDetail
		 * Get the visitor detail and logs it into a file 
		 *
		 * 
		 * @return	visitor object
		 */
		function CreateVisitorDetail(){
			
			$name = $_SERVER["REMOTE_ADDR"];
			$ipAddress = $_SERVER["REMOTE_ADDR"];
			$browser = getBrowser();// In common.inc  to get browser details
			$os = getOS();// In common.inc  to get operating system detail
			$timeStamp =  date("F j, Y, g:i a");
			$lastPage = isset($_SERVER["HTTP_REFERER"]) ? $_SERVER["HTTP_REFERER"] : "" ;
			$visitor = new User($name,$ipAddress,$browser,$os,$timeStamp,$lastPage);
			$userDataAccess = new UserDataAccess();
			$userDataAccess->SaveData($visitor);
			return $visitor;
		}
	}
?>