<?php 

class HumidityController extends BaseController{



	public function humidityOf(){

		$humidityAll=Humidity::all();

		return $humidityAll;

	}

	public function humidityOfPackageInTruck($truck_id,$package_id){		

		$humidityOfPackageInTruckArr = Humidity::where('package_id', $package_id)->where('truck_id',$truck_id)->orderBy('timestamp', 'asc')->get();

		$humidityArr=[];		

		foreach ($humidityOfPackageInTruckArr as $key => $value) {

			$xArr['value']=$value->value;
			$xArr['timestamp']=$value->timestamp;
			$xArr['is_above_threshold']=$value->is_above_threshold;
			
			$humidityArr[]=$xArr;
		}		

		return $humidityArr;		
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

		$humidityArr=[];		

		foreach ($humidityAfterTimestampArr as $key => $value) {

			$xArr['value']=$value->value;
			$xArr['timestamp']=$value->timestamp;
			$xArr['is_above_threshold']=$value->is_above_threshold;
			
			$humidityArr[]=$xArr;
		}		

		return $humidityArr;
	}

}//end class

 ?>