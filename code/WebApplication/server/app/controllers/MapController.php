<?php 

class MapController extends BaseController{	
	

	public function coords($truck_id,$package_id){

		$newArr=[];

		$temp = Temperature::where('package_id', $package_id)->where('truck_id',$truck_id)->orderBy('timestamp', 'asc')->get();	

		foreach ($temp as $key => $jsons) {

				if($jsons->loc[0]!=45 && $jsons->loc[0]!=46){

					if($jsons->loc[0]!=0 && $jsons->loc[0]!=0){
				
					$xArr["timestamp"]=$jsons->timestamp;
					$xArr["loc"]["lat"]=$jsons->loc[0];
					$xArr["loc"]["lng"]=$jsons->loc[1];
					$xArr["temperature"]["value"]=$jsons->value;
					$xArr["truck_id"]=$jsons->truck_id;
					$xArr["package_id"]=$jsons->package_id;
					$xArr["is_above_threshold"]=$jsons->is_above_threshold;


					$newArr[]=$xArr;
					}
				}
		}
		

		$hum = Humidity::where('package_id', $package_id)->where('truck_id',$truck_id)->orderBy('timestamp', 'asc')->get();		

		foreach ($hum as $key => $jsons) {			

			if( $jsons->value<=100 || $jsons->value<0 ){			

				if($jsons->loc[0]!=45 && $jsons->loc[0]!=46){
					if($jsons->loc[0]!=0 && $jsons->loc[0]!=0){
					
					$xArr1["timestamp"]=$jsons->timestamp;
					$xArr1["loc"]["lat"]=$jsons->loc[0];
					$xArr1["loc"]["lng"]=$jsons->loc[1];
					$xArr1["humidity"]["value"]=$jsons->value;
					$xArr1["truck_id"]=$jsons->truck_id;
					$xArr1["package_id"]=$jsons->package_id;
					$xArr1["is_above_threshold"]=$jsons->is_above_threshold;

					$newArr[]=$xArr1;
					}
				}		
								
			}
			
		}



		$vib = Vibration::where('package_id', $package_id)->where('truck_id',$truck_id)->orderBy('timestamp', 'asc')->get();

		foreach ($vib as $key => $jsons) {			
			
			if($jsons->loc[0]!=45 && $jsons->loc[0]!=46){
				if($jsons->loc[0]!=0 && $jsons->loc[0]!=0){

				$xArr2["timestamp"]=$jsons->timestamp;
				$xArr2["loc"]["lat"]=$jsons->loc[0];
				$xArr2["loc"]["lng"]=$jsons->loc[1];
				$xArr2["vibration"]["value"]="vibration";
				$xArr2["truck_id"]=$jsons->truck_id;
				$xArr2["package_id"]=$jsons->package_id;
				$xArr2["is_above_threshold"]=$jsons->is_above_threshold;
				
				$newArr[]=$xArr2;
				}

			}			
		
		}
		

		$shck = Shock::where('package_id', $package_id)->where('truck_id',$truck_id)->orderBy('timestamp', 'asc')->get();

		foreach ($shck as $key => $jsons) {			

			if($jsons->loc[0]!=45 && $jsons->loc[0]!=46){
				if($jsons->loc[0]!=0 && $jsons->loc[0]!=0){

				$xArr3["timestamp"]=$jsons->timestamp;
				$xArr3["loc"]["lat"]=$jsons->loc[0];
				$xArr3["loc"]["lng"]=$jsons->loc[1];
				$xArr3["shock"]["value"]="shock";
				$xArr3["truck_id"]=$jsons->truck_id;
				$xArr3["package_id"]=$jsons->package_id;
				$xArr3["is_above_threshold"]=$jsons->is_above_threshold;
				
				$newArr[]=$xArr3;
				}			
			}
		}

		

		if($newArr){
			foreach ($newArr as $key => $node) {
	   			$timestamps[$key] = $node["timestamp"];
			}
			array_multisort($timestamps, SORT_ASC, $newArr);			
		}

		return $newArr;	

	}

	public function coordsAfterTimestamp($truck_id, $package_id, $timestamp){

		$newArr=[];

		$ts = (float)$timestamp;

		$temp = Temperature::where('package_id', $package_id)->where('truck_id',$truck_id)->where('timestamp','>',$ts)->orderBy('timestamp', 'asc')->get();	

		foreach ($temp as $key => $jsons) {

				if($jsons->loc[0]!=45 && $jsons->loc[0]!=46){

					if($jsons->loc[0]!=0 && $jsons->loc[0]!=0){
				
					$xArr["timestamp"]=$jsons->timestamp;
					$xArr["loc"]["lat"]=$jsons->loc[0];
					$xArr["loc"]["lng"]=$jsons->loc[1];
					$xArr["temperature"]["value"]=$jsons->value;
					$xArr["truck_id"]=$jsons->truck_id;
					$xArr["package_id"]=$jsons->package_id;
					$xArr["is_above_threshold"]=$jsons->is_above_threshold;


					$newArr[]=$xArr;
					}
				}
		}
		

		$hum = Humidity::where('package_id', $package_id)->where('truck_id',$truck_id)->where('timestamp','>',$ts)->orderBy('timestamp', 'asc')->get();		

		foreach ($hum as $key => $jsons) {			

			if( $jsons->value<=100 || $jsons->value<0 ){			

				if($jsons->loc[0]!=45 && $jsons->loc[0]!=46){
					if($jsons->loc[0]!=0 && $jsons->loc[0]!=0){
					
					$xArr1["timestamp"]=$jsons->timestamp;
					$xArr1["loc"]["lat"]=$jsons->loc[0];
					$xArr1["loc"]["lng"]=$jsons->loc[1];
					$xArr1["humidity"]["value"]=$jsons->value;
					$xArr1["truck_id"]=$jsons->truck_id;
					$xArr1["package_id"]=$jsons->package_id;
					$xArr1["is_above_threshold"]=$jsons->is_above_threshold;

					$newArr[]=$xArr1;
					}
				}		
								
			}
			
		}



		$vib = Vibration::where('package_id', $package_id)->where('truck_id',$truck_id)->where('timestamp','>',$ts)->orderBy('timestamp', 'asc')->get();

		foreach ($vib as $key => $jsons) {			
			
			if($jsons->loc[0]!=45 && $jsons->loc[0]!=46){
				if($jsons->loc[0]!=0 && $jsons->loc[0]!=0){

				$xArr2["timestamp"]=$jsons->timestamp;
				$xArr2["loc"]["lat"]=$jsons->loc[0];
				$xArr2["loc"]["lng"]=$jsons->loc[1];
				$xArr2["vibration"]["value"]="vibration";
				$xArr2["truck_id"]=$jsons->truck_id;
				$xArr2["package_id"]=$jsons->package_id;
				$xArr2["is_above_threshold"]=$jsons->is_above_threshold;
				
				$newArr[]=$xArr2;
				}

			}			
		
		}
		

		$shck = Shock::where('package_id', $package_id)->where('truck_id',$truck_id)->where('timestamp','>',$ts)->orderBy('timestamp', 'asc')->get();

		foreach ($shck as $key => $jsons) {			

			if($jsons->loc[0]!=45 && $jsons->loc[0]!=46){
				if($jsons->loc[0]!=0 && $jsons->loc[0]!=0){

				$xArr3["timestamp"]=$jsons->timestamp;
				$xArr3["loc"]["lat"]=$jsons->loc[0];
				$xArr3["loc"]["lng"]=$jsons->loc[1];
				$xArr3["shock"]["value"]="shock";
				$xArr3["truck_id"]=$jsons->truck_id;
				$xArr3["package_id"]=$jsons->package_id;
				$xArr3["is_above_threshold"]=$jsons->is_above_threshold;
				
				$newArr[]=$xArr3;
				}			
			}
		}

		

		if($newArr){
			foreach ($newArr as $key => $node) {
	   			$timestamps[$key] = $node["timestamp"];
			}
			array_multisort($timestamps, SORT_ASC, $newArr);
			
		}

		return $newArr;
	}
	

	
}

 ?>
