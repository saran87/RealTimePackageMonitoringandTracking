<?php 

class VibrationController extends BaseController{

	public function vibration(){

		$vibrationAll = Vibration::all();

		return $vibrationAll;
	}

	public function vibrationOfPackageInTruck($truckid,$packageid){		

		$vibrationOfPackageInTruckArr = Vibration::where('packageid', $packageid)->where('truckid',$truckid)->get();

		return $vibrationOfPackageInTruckArr;		
	}

	public function vibrationgraphdata($id){

		$vibgraphdata=Vibration::find($id);

		return $vibgraphdata;

	}	

	public function latestEntry(){

		$last = Humidity::all()->last();
		
		return $last;
	}

	public function VibrationAfterTimestamp($truckid, $packageid, $timestamp){



		$ts = (float)$timestamp ;

		$vibrationAfterTimestampArr = Vibration::where('truckid',$truckid)->where('packageid', $packageid)->where('timestamp','>',$ts)->get();

		return $vibrationAfterTimestampArr;

	}

}

 ?>