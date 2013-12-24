<?php 

class MapController extends BaseController{	

	

	public function coords($truck_id,$package_id){		

		$temp = Temperature::where('package_id', $package_id)->where('truck_id',$truck_id)->get();		
		
		
		$temparr= json_decode($temp);

		//return $temparr;



		foreach ($temparr as $ob) {

			/*$location['timestamp'][]=$ob->timestamp;

			end($location['timestamp']);
			$last_id=key($location['timestamp']);

			if($ob->loc){
				
				$location['timestamp'][$last_id][]=[$ob->loc[0],$ob->loc[1]];				
			}*/

			if($ob->loc){

				$location[]=$ob->loc;

			}
		}

		return $location;
		

	}
	

	
}

 ?>