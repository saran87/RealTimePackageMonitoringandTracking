<?php 

class HumidityController extends BaseController{



	public function humidityOf(){

		$humidityAll=Humidity::all();

		return $humidityAll;

	}

	public function humidityOfPackageInTruck($truck_id,$package_id){		

		$humidityOfPackageInTruckArr = Humidity::where('package_id', $package_id)->where('truck_id',$truck_id)->orderBy('timestamp', 'asc')->get();

		return $humidityOfPackageInTruckArr;		
	}

	public function humidityOfPackage($package_id){

		$humidityOfPackageArr = Humidity::where('package_id', $package_id)->get();

		return $humidityOfPackageArr;

	}

	public function latestEntry(){

		$last = Humidity::all()->last();
		
		return $last;

	}

	public function HumidityAfterTimestamp($truck_id, $package_id, $timestamp){

		$ts = (float)$timestamp ;

		$humidityAfterTimestampArr = Humidity::where('truck_id',$truck_id)->where('package_id', $package_id)->where('timestamp','>',$ts)->orderBy('timestamp', 'asc')->get();

		return $humidityAfterTimestampArr;
	}

}//end class

 ?>