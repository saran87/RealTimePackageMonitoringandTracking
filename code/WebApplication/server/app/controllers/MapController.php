<?php 

class MapController extends BaseController{	

	

	public function coords($truck_id,$package_id){		

		$temp = Temperature::where('package_id', $package_id)->where('truck_id',$truck_id)->orderBy('timestamp', 'asc')->get();	

		foreach ($temp as $key => $jsons) {			

			
				$xArr["timestamp"]=$jsons->timestamp;
				$xArr["loc"]["lat"]=$jsons->loc[0];
				$xArr["loc"]["lng"]=$jsons->loc[1];
				$xArr["temperature"]["value"]=$jsons->value;
				$xArr["truck_id"]=$jsons->truck_id;
				$xArr["package_id"]=$jsons->package_id;

				$newArr[]=$xArr;
						
		}
		

		/*$hum = Humidity::where('package_id', $package_id)->where('truck_id',$truck_id)->orderBy('timestamp', 'asc')->get();

		

		foreach ($hum as $key => $jsons) {			

			if( !$jsons->value<0 || !$jsons->value>100 ){
				$xArr1["loc"]["lat"]=$jsons->loc[0];
				$xArr1["loc"]["lng"]=$jsons->loc[1];
				$xArr1["humidity"]["value"]=$jsons->value;
				$xArr1["truck_id"]=$jsons->truck_id;
				$xArr1["package_id"]=$jsons->package_id;

				$newArr[]=$xArr1;						
			}
			
		}

		$vib = Vibration::where('package_id', $package_id)->where('truck_id',$truck_id)->orderBy('timestamp', 'asc')->get();

		foreach ($vib as $key => $jsons) {

			$xArr2["loc"]["lat"]=$jsons->loc[0];
			$xArr2["loc"]["lng"]=$jsons->loc[1];
			$xArr2["vibration"]["value"]="vibration";
			$xArr2["truck_id"]=$jsons->truck_id;
			$xArr2["package_id"]=$jsons->package_id;
			
			$newArr[]=$xArr2;

			# code...
		}

		$shck = Shock::where('package_id', $package_id)->where('truck_id',$truck_id)->orderBy('timestamp', 'asc')->get();

		foreach ($vib as $key => $jsons) {

			if($jsons->value){

				$xArr3["loc"]["lat"]=$jsons->loc[0];
				$xArr3["loc"]["lng"]=$jsons->loc[1];
				$xArr3["shock"]["value"]="shock";
				$xArr3["truck_id"]=$jsons->truck_id;
				$xArr3["package_id"]=$jsons->package_id;
				
				$newArr[]=$xArr3;
			}

			# code...
		}*/

		//array_multisort($newArr, SORT_ASC);

		return $newArr;

	}
	

	
}

 ?>
