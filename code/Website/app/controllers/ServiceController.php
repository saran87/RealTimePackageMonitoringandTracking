<?php 

class ServiceController extends BaseController{

	public function index(){

		$service=Service::all();

		return $service;

	}

	public function temperature(){


	}

	public function humidity(){
		
	}
}

 ?>