<?php 

class ServiceController extends BaseController{

	public function configurations(){

		$configs=Configurations::all();

		return $configs;

	}

	public function temperature(){

		$temperatureAll=Temperature::all();

		return $temperatureAll;

	}

	public function temperatureShow($truck_id,$package_id){		

		$temp = Temperature::where('package_id', $package_id)->where('truck_id',$truck_id)->get();

		return $temp;

	}

	public function humidity(){

		$humidityAll = Humidity::all();

		return $humidityAll;

	}

	

	
}

 ?>