<?php
	/**
	 * The index.php page is the entry to all page requests .
	 *
	 * The routines here dispatch control to the appropriate controller, which then
	 * prints the appropriate page.
	 *
	 */
	require('settings.inc'); 
	require('controller/main.php'); 
	//Handle the request to the controller
	//Initalize site by calling this method in main.inc
	Intialize();

?>