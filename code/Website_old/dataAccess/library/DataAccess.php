<?php

/*
    This is the base class for the data access class
	gets the reference of database singleton isntance and stores in a variable
	$database is the singleton object to query the database
*/
require_once("Database.class.php");


class DataAccess{


	protected $database = null;

	//constructer to get the database singleton object and store it locally
	function __construct(){
		
		$this->database = Database::getInstance();
	}
}

?>