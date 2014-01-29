<?php 

class VibrationController extends BaseController{

	public function vibration(){

		$vibrationAll = Vibration::all();

		return $vibrationAll;
	}

	public function vibrationOfPackageInTruck($truck_id,$package_id){		

		$vibrationOfPackageInTruckArr = Vibration::where('package_id', $package_id)->where('truck_id',$truck_id)->orderBy('timestamp', 'asc')->get();

		$vibrationArr=[];		

		foreach ($vibrationOfPackageInTruckArr as $key => $value) {

			$xArr['_id']=$value->_id;
			$xArr['value']=$value->value;
			$xArr['timestamp']=$value->timestamp;
			$xArr['is_above_threshold']=$value->is_above_threshold;
			if($value->loc[0]!=null || $value->loc[1]!=null){
				if($value->loc[0]!=0 && $value->loc[1]!=0){

					$xArr['loc'][0]=$value->loc[0];
					$xArr['loc'][1]=$value->loc[1];
				}
			}
				

			$vibrationArr[]=$xArr;
		}		

		return $vibrationArr;

				
	}

	public function vibrationgraphdata($id){

		$vibgraphdata=Vibration::find($id);

		return $vibgraphdata;

	}	

	public function latestEntry(){

		$last = Humidity::all()->last();
		
		return $last;
	}

	public function VibrationAfterTimestamp($truck_id, $package_id, $timestamp){

		$ts = (float)$timestamp ;

		$vibrationAfterTimestampArr = Vibration::where('truck_id',$truck_id)->where('package_id', $package_id)->where('timestamp','>',$ts)->orderBy('timestamp', 'asc')->get();

		$vibrationArr=[];		

		foreach ($vibrationAfterTimestampArr as $key => $value) {

			$xArr['_id']=$value->_id;
			$xArr['value']=$value->value;
			$xArr['timestamp']=$value->timestamp;
			$xArr['is_above_threshold']=$value->is_above_threshold;
			if($value->loc[0]!=null || $value->loc[1]!=null){
				if($value->loc[0]!=0 && $value->loc[1]!=0){

					$xArr['loc'][0]=$value->loc[0];
					$xArr['loc'][1]=$value->loc[1];
				}
			}
			

			$vibrationArr[]=$xArr;
		}		

		return $vibrationArr;

	}

}

 ?>