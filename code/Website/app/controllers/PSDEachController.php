<?php 

class PSDEachController extends BaseController {

	private $dataArray;

	private $rmsArray=array();

	private $rrmsArray;

	private $maxGArray;

	private $orientation;

	private $mainArray;	



	
	/*function getPSDArray($id){

			$dataArray = Vibration::find($id);

			$mainArray['x']['value']=$dataArray['value']['x'];
			$mainArray['y']['value']=$dataArray['value']['y'];
			$mainArray['z']['value']=$dataArray['value']['z'];					

			$this->intitalize($mainArray);

			$this->ProcessDataArray();			

			$arr=$this->GetPSD();

			return $arr;
	}*/

	public function getPSDEach($id){

		$dataArr = Vibration::find($id);

		$mainArr['x']['value']=$dataArr['value']['x'];
		$mainArr['y']['value']=$dataArr['value']['y'];
		$mainArr['z']['value']=$dataArr['value']['z'];

		$this->intitalize($mainArr);		

		$psdArr['x']=$this->GetPSD($this->mainArray['x']);
		$psdArr['y']=$this->GetPSD($this->mainArray['y']);
		$psdArr['z']=$this->GetPSD($this->mainArray['z']);

		print_r($psdArr);



	}


	public function intitalize($dataArray){

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


		public function GetPSD($inArr){

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