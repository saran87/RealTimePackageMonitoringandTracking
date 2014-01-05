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

		$allTrucksArr = Configurations::distinct()->get(array('truck_id'));
		return $allTrucksArr;
	}

	/*
		[{"_id":"52a9f81e68f40f52d8d60664","packageid":"5"},{"_id":"52a9f84068f40f52d8d60665","packageid":"1"},{"_id":"52a9fbea68f40f52d8d60666","packageid":"9"}]
	*/

	public function listPackagesInTruckWithtruck_id($truck_id){

		$packagesInTruckArr = Configurations::where('truck_id', $truck_id)->select('package_id')->get();

		return $packagesInTruckArr;
	}

	public function latestEntry(){

		$last = Configurations::all()->last();
		
		return $last;

	}
}

 ?>