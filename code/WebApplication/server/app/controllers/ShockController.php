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

		return $shockOfPackageInTruckArr;		
	}

	public function shockGraphData($id){

		$shockGraphDataArr=Shock::find($id);

		return $shockGraphDataArr;

	}

	
	public function ShockAfterTimestamp($truck_id, $package_id, $timestamp){

		$ts = (float)$timestamp ;

		$shockAfterTimestampArr = Shock::where('truck_id',$truck_id)->where('package_id', $package_id)->where('timestamp','>',$ts)->orderBy('timestamp', 'asc')->get();

		return $shockAfterTimestampArr;

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

	public function GetRMSValue(){

		$sqrSum = 0;

		foreach ($this->dataArray as $axis => $axisValues) {

			$value = $axisValues[$this->maxGArray['index']];

			$sqrSum += ($value * $value);  
			
		}

		return sqrt($sqrSum);
	}

	public function ProcessGValue(){

		$maxg = 0;
		$index = 0;
		$maxAxis = "";
	
		foreach ($this->dataArray as $axis => $axisValues) {
			
			$axisMaxg = 0;
			$axisIndex = 0;

			foreach ($axisValues as $count => $value) {
			
				if( $count <= 71){
					continue;
				}

				if (abs($value) > abs($axisMaxg)){
					$axisMaxg = $value;
					$axisIndex = $count;
					$maxAxis = $axis;
				}
			}
			
			if (abs($axisMaxg) > abs($maxg)) {
				
				$maxg = $axisMaxg;
				$index = $axisIndex;
				$maxAxis = $axis;
			}
		}
	
		$this->maxGArray =  array('index' => $index, 'gvalue' => $maxg, 'axis' => $maxAxis );
		$data = array('index' => $index, 'gvalue' => $maxg, 'axis'=>$maxAxis );
		
		return $data;
	}

	



	public function latestEntry(){

		$last = Shock::all()->last();
		
		return $last;
	}

}

 ?>