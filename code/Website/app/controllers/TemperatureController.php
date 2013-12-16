<?php 

class TemperatureController extends BaseController{



	public function temperatureOf(){

		$temperatureAll=Temperature::all();

		return $temperatureAll;

	}

	public function temperatureOfPackageInTruck($truckid,$packageid){		

		$temperatureOfPackageInTruckArr = Temperature::where('packageid', $packageid)->where('truckid',$truckid)->get();

		return $temperatureOfPackageInTruckArr;		
	}

	public function temperatureOfPackage($packageid){

		$temperatureOfPackageArr = Temperature::where('packageid', $packageid)->get();

		return $temperatureOfPackageArr;

	}

	public function latestEntry(){

		$last = Temperature::all()->last();
		
		return $last;
	}

	public function TemperatureAfterTimestamp($truckid, $packageid, $timestamp){

		$ts = (float)$timestamp ;


		$temperatureAfterTimestampArr = Temperature::where('truckid',$truckid)->where('packageid', $packageid)->where('timestamp','>',$ts)->get();

		return $temperatureAfterTimestampArr;

	}

}//end class

 ?>