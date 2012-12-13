<?php

//Class to hold and Perform  Activities
class PackageDataAccess{
	
	// Store the single instance of Database
	private static $m_pInstance; 
	//Constructor
	function __construct(){
		if (!self::$m_pInstance)
		{
			self::$m_pInstance = new Mongo("54.243.241.254") ;
		}
	}
	
	
	//singleton function
	public function getPackageDetail($limit = 100,$packageId = "1")
	{
	
		// select a database
		$db = self::$m_pInstance->RFID;
		// select a collection (analogous to a relational database's table)
		$collection = $db->Packages;
		
		if($limit != 100){
			$time = time()- 5;
			$time = $time * 1000;
			//get package detail which has timestamp greater than current time minus 1 hour
			$query = array("packageId" => $packageId, "timestamp" => array('$gt'=>$time)); //note the single quotes around '$gt'
					//excutes query and get a cursor
			$cursor = $collection->find($query);
		}else{
			
			$query = array("packageId" => $packageId);
			//excutes query and get a cursor
			$cursor = $collection->find($query);
			$cursor->skip($collection->count()-$limit)->limit($limit);
		}
		
		
		//get the data from the cursor
		while( $cursor->hasNext() ) {
			$data[] = $cursor->getNext();
		}
		
		//return the data array object
		return $data;
	} 	
}

?>

