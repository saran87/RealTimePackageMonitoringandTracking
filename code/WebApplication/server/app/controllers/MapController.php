<?php 

class MapController extends BaseController{

	public function latestCoords($truck_id, $package_id){
		
		$newArr=[];

		$latestTemp = Temperature::where('package_id', $package_id)->where('truck_id',$truck_id)->orderBy('timestamp', 'asc')->get()->last();

		$latestHum = Humidity::where('package_id', $package_id)->where('truck_id',$truck_id)->orderBy('timestamp', 'asc')->get()->last();

		$latestVib = Vibration::where('package_id', $package_id)->where('truck_id',$truck_id)->orderBy('timestamp', 'asc')->get()->last();

		$latestShck = Shock::where('package_id', $package_id)->where('truck_id',$truck_id)->orderBy('timestamp', 'asc')->get()->last();	

		if($latestTemp){
			if($latestTemp->loc[0]!=45 && $latestTemp->loc[0]!=46){

				if($latestTemp->loc[0]!=0 && $latestTemp->loc[0]!=0){

					$xArr["timestamp"]=$latestTemp->timestamp;
					$xArr["loc"]["lat"]=$latestTemp->loc[0];
					$xArr["loc"]["lng"]=$latestTemp->loc[1];
					$xArr["temperature"]["value"]=$latestTemp->value;
					$xArr["truck_id"]=$latestTemp->truck_id;
					$xArr["package_id"]=$latestTemp->package_id;
					$xArr["is_above_threshold"]=$latestTemp->is_above_threshold;

					$newArr[]=$xArr;

				}
			}
		}

		if($latestHum){
			if( $latestHum->value<=100 || $latestHum->value<0 ){			

				if($latestHum->loc[0]!=45 && $latestHum->loc[0]!=46){
					if($latestHum->loc[0]!=0 && $latestHum->loc[0]!=0){

						$xArr1["timestamp"]=$latestHum->timestamp;
						$xArr1["loc"]["lat"]=$latestHum->loc[0];
						$xArr1["loc"]["lng"]=$latestHum->loc[1];
						$xArr1["humidity"]["value"]=$latestHum->value;
						$xArr1["truck_id"]=$latestHum->truck_id;
						$xArr1["package_id"]=$latestHum->package_id;
						$xArr1["is_above_threshold"]=$latestHum->is_above_threshold;

						$newArr[]=$xArr1;
					}
				}
			}
		}

		if($latestVib){
		
			if($latestVib->loc[0]!=45 && $latestVib->loc[0]!=46){
				if($latestVib->loc[0]!=0 && $latestVib->loc[0]!=0){

					$xArr2["timestamp"]=$latestVib->timestamp;
					$xArr2["loc"]["lat"]=$latestVib->loc[0];
					$xArr2["loc"]["lng"]=$latestVib->loc[1];
					$xArr2["vibration"]["value"]="vibration";
					$xArr2["truck_id"]=$latestVib->truck_id;
					$xArr2["package_id"]=$latestVib->package_id;
					$xArr2["is_above_threshold"]=$latestVib->is_above_threshold;
					
					$newArr[]=$xArr2;

				}
			}
		}

		if($latestShck){

			if($latestShck->loc[0]!=45 && $latestShck->loc[0]!=46){
				if($latestShck->loc[0]!=0 && $latestShck->loc[0]!=0){

				$xArr3["timestamp"]=$latestShck->timestamp;
				$xArr3["loc"]["lat"]=$latestShck->loc[0];
				$xArr3["loc"]["lng"]=$latestShck->loc[1];
				$xArr3["shock"]["value"]="shock";
				$xArr3["truck_id"]=$latestShck->truck_id;
				$xArr3["package_id"]=$latestShck->package_id;
				$xArr3["is_above_threshold"]=$latestShck->is_above_threshold;
				
				$newArr[]=$xArr3;
				}			
			}
		}

		if($newArr){
			foreach ($newArr as $key => $node) {
	   			$timestamps[$key] = $node["timestamp"];
			}
			array_multisort($timestamps, SORT_DESC, $newArr);
			
		}

		return $newArr;
		
		 
	}	

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
