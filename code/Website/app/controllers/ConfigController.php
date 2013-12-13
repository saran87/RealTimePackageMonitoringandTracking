<?php 

class ConfigController extends BaseController{

/*
	Functions that return complete configuration details. 
	Truckid and Packageid needs to be provided in some cases.
*/

	public function configurations(){

		$allConfigsArr = Configurations::all();

		return $allConfigsArr;
	}

	//for specific package and truck - knowing packageid and truckid
	public function configsPackageInTruck($truckid,$packageid){

		$packagesArr = Configurations::where('packageid', $packageid)->where('truckid',$truckid)->get();

		return $packagesArr;
	}

	public function packagesWithPackageId($packageid){

		$allPackagesArr = Configurations::where('packageid', $packageid)->get();

		return $allPackagesArr;
	}


/* 
	Functions that return lists i.e. not complete configuration details
*/


	public function listAllTrucks(){

		$allTrucksArr = Configurations::distinct()->get(array('truckid'));
		return $allTrucksArr;
	}

	/*
		[{"_id":"52a9f81e68f40f52d8d60664","packageid":"5"},{"_id":"52a9f84068f40f52d8d60665","packageid":"1"},{"_id":"52a9fbea68f40f52d8d60666","packageid":"9"}]
	*/

	public function listPackagesInTruckWithTruckId($truckid){

		$packagesInTruckArr = Configurations::where('truckid', $truckid)->select('packageid')->get();

		return $packagesInTruckArr;
	}

	public function latestEntry(){

		$last = Configurations::all()->last();
		
		return $last;

	}	

}

 ?>