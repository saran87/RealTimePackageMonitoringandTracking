<?php

	/**
	 * The BaseController.inc contains base class for all controllers .
	 *
	 *
	 */
	 
	 // ------------------------------------------------------------------------

	/**
	 * BaseController Class
	 *
	 * This class contains common methods that are used bby all controllers
	 *
	 * @author	Saravana Kumar
	 */
	class BaseController{
	
	
	/**
	* Holds variables to be handed to the view in array format.
	*
	* @var array
	*/
	public $viewVars = array();
	
	
	/**
	* Saves a variable for use inside a view templates.
	*
	* @param mixed $key A string or an array of data.
	* @param mixed $value Value in case $key is a string (which then works as the variable name).
	* Unused if $key is an associative array, otherwise serves as the values to $keys's variable.
	* @return void
	*/
	public function setViewData($key, $value = null) {
			if (is_array($key)) {
				if (is_array($value)) {
					$data = array_combine($key, $value);
				} else {
					$data = $key;
				}
			} else {
				$data = array($key => $value);
			}
			$this->viewVars = $data + $this->viewVars;
	}
	
	/**
	 * RenderView
	 * It includes the appropriate view needed for a section
	 *
	 * @param view template file path 
	 * @return	bool
	 */
	public function renderView($view = null) {
		
		$isDone = true;
			
		$count = 0;
		
		if( $view != null){
		
			extract($this->viewVars, EXTR_OVERWRITE);
			
			//load the appropriate view
			//include all of the files with .inc extension in view folder
			foreach(glob($view) as $filename){
				$count++;
				include $filename;
			}	
			
			if($count != 1){
				$isDone = false;
			}
			return $isDone;			
		}
	}
	
	/**
	 * Authenticate user
	 * Validates the user token using authorizer
	 *
	 * @param void
	 * @return	bool if user is authenticated or not
	 */
	public function authenticate() {
		
		$isAuth = false;
			
		$authorizer = new Authorizer();
		
		return $authorizer->VerifyToken();
	
	}
	
}
