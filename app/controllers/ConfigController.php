<?php 

class ConfigController extends BaseController{

/*
	Functions that return complete configuration details. 
	truck_id and Packageid needs to be provided in some cases.
*/

	public function configurations(){

		$allConfigsArr = Configurations::all();

		return $allConfigsArr;
	}

	//for specific package and truck - knowing packageid and truck_id
	public function configsPackageInTruck($truck_id,$package_id){

		$packagesArr = Configurations::where('package_id', $packageid)->where('truck_id',$truck_id)->get();

		return $packagesArr;
	}

	public function packagesWithPackageId($packageid){

		$allPackagesArr = Configurations::where('package_id', $packageid)->get();

		return $allPackagesArr;
	}


/* 
	Functions that return lists i.e. not complete configuration details
*/


	public function listAllTrucks(){		
		
		//$allTrucksArr = Configurations::orderBy('timestamp', 'asc')->distinct()->get(array('truck_id'));

		$allTrucksArr = Configurations::orderBy('timestamp', 'asc')->groupBy('truck_id')->distinct()->get(array('truck_id', 'timestamp', 'package_id'));		
		return $allTrucksArr;

	}

	/*
		[{"_id":"52a9f81e68f40f52d8d60664","packageid":"5"},{"_id":"52a9f84068f40f52d8d60665","packageid":"1"},{"_id":"52a9fbea68f40f52d8d60666","packageid":"9"}]
	*/

	public function listAllTrucksAfterTimestamp($timestamp){

		$ts=(float)$timestamp;

		$allTrucksAfterArr = Configurations::where('timestamp','>',$ts)->orderBy('timestamp', 'asc')->groupBy('truck_id')->distinct()->get(array('truck_id', 'timestamp', 'package_id'));
		
		return $allTrucksAfterArr;

	}

	public function listPackagesInTruckWithtruck_id($truck_id){

		$packagesInTruckArr = Configurations::where('truck_id', $truck_id)->get();

		return $packagesInTruckArr;
	}

	public function latestEntry(){

		$last = Configurations::all()->last();
		
		return $last;

	}

	public function configurationsOf($truck_id,$package_id){

		$configurationsOfObj = Configurations::where('package_id', $package_id)->where('truck_id',$truck_id)->get()->last();

		return $configurationsOfObj;
	}	
}

 ?>