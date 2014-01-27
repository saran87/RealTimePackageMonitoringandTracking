<?php 

class ShockController extends BaseController{

	private $dataArray;

	private $dropHeight;

	private $maxGArray;

	private $orientation;

	public function shockData(){
		$shockall = Shock::all();

		return $shockall;
	}

	public function shockOfPackageInTruck($truck_id,$package_id){		

		$shockOfPackageInTruckArr = Shock::where('package_id', $package_id)->where('truck_id',$truck_id)->orderBy('timestamp', 'asc')->get();

		$shockArr=[];		

		foreach ($shockOfPackageInTruckArr as $key => $value) {

			$xArr['_id']=$value->_id;
			$xArr['value']=$value->value;
			$xArr['timestamp']=$value->timestamp;
			$xArr['is_above_threshold']=$value->is_above_threshold;

			$shockArr[]=$xArr;
		}		

		return $shockArr;		
	}

	public function shockGraphData($id){

		$shockGraphDataArr=Shock::find($id);

		return $shockGraphDataArr;

	}

	
	public function ShockAfterTimestamp($truck_id, $package_id, $timestamp){

		$ts = (float)$timestamp ;

		$shockAfterTimestampArr = Shock::where('truck_id',$truck_id)->where('package_id', $package_id)->where('timestamp','>',$ts)->orderBy('timestamp', 'asc')->get();

		$shockArr=[];		

		foreach ($shockAfterTimestampArr as $key => $value) {

			$xArr['_id']=$value->_id;
			$xArr['value']=$value->value;
			$xArr['timestamp']=$value->timestamp;
			$xArr['is_above_threshold']=$value->is_above_threshold;

			$shockArr[]=$xArr;
		}		

		return $shockArr;

	}
	

	private function intitalize($dataArray){

		foreach ($dataArray as $key => $value) {
			$this->dataArray[$key] = explode(" ",$value["value"]);
		}
	}

	public function GetDropHeight(){


		$maxGArray = $this->ProcessGValue();
		$maxIndex = $maxGArray['index'] - 71;
		
		/*
			t=0.0625
			t is 1/1600

		*/
		$time = (($maxIndex * 0.0625) + 70)/1000;
		$height = 4.9 * ($time * $time) ;

		$maxGArray['height'] = $height;
		
		return $maxGArray;
	}

	public function GetOrientation(){
		
		$axis = $this->maxGArray['axis'];

		$isNegative = ($this->maxGArray['gvalue'] >= 0) ? false:true;

		if($axis == "shockx"){

			if($isNegative){
				return 6;
			}else{
				return 5;
			}

		}elseif ($axis == "shocky") {
			
			if($isNegative){
				return 4;
			}else{
				return 2;
			}

		}elseif ($axis == "shockz") {
			
			if($isNegative){
				return 1;
			}else{
				return 3;
			}

		}

	}



	public function latestEntry(){

		$last = Shock::all()->last();
		
		return $last;
	}

}

 ?>