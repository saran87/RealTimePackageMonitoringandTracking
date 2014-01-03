<?php 

class PSDController extends BaseController {

	private $dataArray;

	private $rmsArray=array();

	private $rrmsArray;

	private $maxGArray;

	private $orientation;

	private $mainArray;	



	
	function getPSDArray($id){

			$dataArray = Vibration::find($id);

			$mainArray['x']['value']=$dataArray['value']['x'];
			$mainArray['y']['value']=$dataArray['value']['y'];
			$mainArray['z']['value']=$dataArray['value']['z'];					

			$this->intitalize($mainArray);

			$this->ProcessDataArray();			

			$arr['rms']=$this->GetPSD();

			$arr['x']=$this->GetPSDEach($this->mainArray['x']);
			$arr['y']=$this->GetPSDEach($this->mainArray['y']);
			$arr['z']=$this->GetPSDEach($this->mainArray['z']);

			return $arr;
	}

	private function intitalize($dataArray){

			//include all of the files with .inc extension in section folder
			/*foreach(glob("fft/*.php") as $filename){
				require_once($filename);
			}*/
				require_once('fft/FFT.class.php');

				require_once('fft/Filter.php');

				require_once('fft/Complex.class.php');

			$fft = new FFT(333);

			foreach ($dataArray as $key => $value) {				

				$this->mainArray[$key] = explode(" ",$value["value"]);
			}
		}
		

		public function GetRMSValue(){

			return $this->rmsArray;
		}


		public function GetPSD(){

			$count = count($this->rmsArray);
			
			$fft = new FFT($count);
			$filter = new Filter();

			//Filter the signal
			$this->rmsArray = $filter->hannWindow($this->rmsArray);
			
			// Calculate the FFT 
			$f = $fft->getAbsFFT($fft->fft($this->rmsArray));
			
			return $f;
		}

		public function GetPSDEach($inArr){

			$count = count($inArr);
			
			$fft = new FFT($count);
			$filter = new Filter();

			//Filter the signal
			$inArr = $filter->hannWindow($inArr);
			
			// Calculate the FFT 
			$f = $fft->getAbsFFT($fft->fft($inArr));
			
			return $f;
		}

		/*
		*  Process Data Array  - Function
		*   	Data array in below format 
						[x] = [0,1,2,3 ...]
						[y] = [0,1,2,3 ...]
						[z] = [0,1,2,3 ...]
				is converted to 
						[rmsValue] = [ sqr(x^2 + y^2 + z^2), ... , ....]
		*
		*/

		public function ProcessDataArray(){

			$maxg = 0;
			$index = 0;
			$maxAxis = "";

			// for each axis go through the values and calculate the square root of each instance			
			
			foreach ($this->mainArray as $axis => $axisValues) {
				

				foreach ($axisValues as $count => $value) {					
					
					if(array_key_exists($count,$this->rmsArray)){

						$this->rmsArray[$count] =  ($value * $value) + $this->rmsArray[$count];
					}
					else{

						$this->rmsArray[$count] =  ($value * $value);
					}
				}
			}
			
			foreach ($this->rmsArray as $key => $value) {
					
				$this->rmsArray[$key] = sqrt($value);
	 		}
		
		}

}

 ?>