<?php 

class TemperatureController extends BaseController{



	public function temperatureOf(){

		$temperatureAll=Temperature::all();

		return $temperatureAll;

	}

	public function temperatureOfPackageInTruck($truck_id,$package_id){		

		$temperatureOfPackageInTruckArr = Temperature::where('package_id', $package_id)->where('truck_id',$truck_id)->orderBy('timestamp', 'asc')->get();

		return $temperatureOfPackageInTruckArr;		
	}

	public function temperatureOfPackage($package_id){

		$temperatureOfPackageArr = Temperature::where('package_id', $package_id)->get();

		return $temperatureOfPackageArr;

	}

	public function latestEntry(){

		$last = Temperature::all()->last();
		
		return $last;
	}

	public function TemperatureAfterTimestamp($truck_id, $package_id, $timestamp){

		$ts = (float)$timestamp;


		$temperatureAfterTimestampArr = Temperature::where('truck_id',$truck_id)->where('package_id', $package_id)->where('timestamp','>',$ts)->orderBy('timestamp', 'asc')->get();

		return $temperatureAfterTimestampArr;

	}

}//end class

 ?>