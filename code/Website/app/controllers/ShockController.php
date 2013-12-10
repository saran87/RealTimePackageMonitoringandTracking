<?php 

class ShockController extends BaseController{

	function shockData(){
		$shockall = Shock::all();

		return $shockall;
	}

}

 ?>