<?php 

class HumidityController extends BaseController{



	public function humidityOf(){

		$humidityAll=Humidity::all();

		return $humidityAll;

	}

	public function humidityOfPackageInTruck($truckid,$packageid){		

		$humidityOfPackageInTruckArr = Humidity::where('packageid', $packageid)->where('truckid',$truckid)->get();

		return $humidityOfPackageInTruckArr;		
	}

	public function humidityOfPackage($packageid){

		$humidityOfPackageArr = Humidity::where('packageid', $packageid)->get();

		return $humidityOfPackageArr;

	}

	public function latestEntry(){

		$last = Humidity::all()->last();
		
		return $last;

	}

	public function HumidityAfterTimestamp($truckid, $packageid, $timestamp){

		$ts = (float)$timestamp ;

		$humidityAfterTimestampArr = Humidity::where('truckid',$truckid)->where('packageid', $packageid)->where('timestamp','>',$ts)->get();

		return $humidityAfterTimestampArr;
	}

}//end class

 ?>