<?php

	/**
	 * The package.php is where all the request for package and account creating 
	 * are handled and dispatched to appropriate methods .
	 *
	 * The routines here dispatch control to the controller, which then directs 
	 * the controll to appropriate sections
	 *
	 */
	 
	 // ------------------------------------------------------------------------

	/**
	 * Package Class
	 *
	 * This class contains functions that handles and process the request 
	 * related to Package
	 *
	 * @author	Saravana Kumar
	 */
	 
	class package extends BaseController{
	
		private $title = "Game Arcade";
		
		function __construct(){
			require_once(ROOT_PATH . "/dataAccess/packageDataAccess.php");
		
		}
	
		
		public function index(){
		
			$data = array();
			$this->setViewData('title',$this->title);
			
			$dataAccess = new PackageDataAccess();
			$data[data] = $dataAccess->getPackageDetail();
			
			ouputJson($data);
		}
		
		public function latest(){
		
			$data = array();
			$this->setViewData('title',$this->title);
	
			$dataAccess = new PackageDataAccess();
			$data[data] = $dataAccess->getPackageDetail(10);
			
			ouputJson($data);
		}
		
	}
	
?>