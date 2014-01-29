<?php 

class TemperatureController extends BaseController{



	public function temperatureOf(){

		$temperatureAll=Temperature::all();

		return $temperatureAll;
	}

	public function temperatureOfPackageInTruck($truck_id,$package_id){		

		$temperatureOfPackageInTruckArr = Temperature::where('package_id', $package_id)->where('truck_id',$truck_id)->orderBy('timestamp', 'asc')->get();

		$temperatureArr=[];		

		foreach ($temperatureOfPackageInTruckArr as $key => $value) {

			$xArr['value']=$value->value;
			$xArr['timestamp']=$value->timestamp;
			$xArr['is_above_threshold']=$value->is_above_threshold;
			if($value->loc[0]!=null || $value->loc[1]!=null){
				if($value->loc[0]!=0 && $value->loc[1]!=0){

					$xArr['loc'][0]=$value->loc[0];
					$xArr['loc'][1]=$value->loc[1];
				}
			}
			
			$temperatureArr[]=$xArr;
		}	

		return $temperatureArr;		
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

		$temperatureArr=[];		

		foreach ($temperatureAfterTimestampArr as $key => $value) {

			$xArr['value']=$value->value;
			$xArr['timestamp']=$value->timestamp;
			$xArr['is_above_threshold']=$value->is_above_threshold;
			if($value->loc[0]!=null || $value->loc[1]!=null){
				if($value->loc[0]!=0 && $value->loc[1]!=0){

					$xArr['loc'][0]=$value->loc[0];
					$xArr['loc'][1]=$value->loc[1];
				}
			}
				
			
			$temperatureArr[]=$xArr;
		}	

		return $temperatureArr;

	}

}//end class

 ?>