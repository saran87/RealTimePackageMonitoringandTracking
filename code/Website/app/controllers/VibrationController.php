<?php 

class VibrationController extends BaseController{

	public function vibration(){

		$vibrationAll = Vibration::all();

		return $vibrationAll;
	}

	public function vibrationgraphdata($id){

		$vibgraphdata=Vibration::find($id);

		return $vibgraphdata;

	}	

	public function latestEntry(){

		$last = Humidity::all()->last();
		
		return $last;
	}

}

 ?>