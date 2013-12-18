<?php
/**
 * @author Michele Andreoli <michi.andreoli@gmail.com>
 * @author Saravana Kumar <saran87@gmail.com> - Hannning window implementation
 * @name Filter.class.php
 * @version 0.3 updated 08-18-2013
 * @license http://opensource.org/licenses/gpl-license-php GNU Public License
 */

class Filter {
	/**
	 * Low-pass filter
	 * @param array<double> $func
	 * @param int $val cut value
	 * @return array<double> return the filtered signal
	 */
	public function filterLP($func, $val) {
		
		for ($i = 0; $i < count($func); $i++) {
			if ($i > $val && $i < (count($func) - $val))
				$func[$i] = 0;
		}
		
		return $func;
	}
	
	/**
	 * High-pass filter
	 * @param array<double> $func
	 * @param int $val cut value
	 * @return array<double> return the filtered signal
	 */
	public function filterHP($func, $val) {
		
		for ($i = 0; $i < count($func); $i++) {
			if ($i <= $val || $i >= (count($func) - $val))
				$func[$i] = 0;
		}
		
		return $func;
	}

	/**
	 * hannWindow filter
	 * @param array<double> $func
	 * @param int $length window length
	 * @return array<double> return the filtered signal
	 */
	public function hannWindow($func) {
		
		$length = count($func);

		$N =  $length - 1;

		for ($i = 0; $i < $length; $i++) {
				
			$window = (0.5) * (1 - cos( (2 * M_PI ) * ( $i / $N)  ));
			$func[$i] = $func[$i] * $window;
		}
		
		return $func;
	}

	/**
	 * @Hann Function  
	 *		Generates a L - Point periodic Hann window
	 *      Periodic Hann window is useful for DFT/FFT, such as spectral analysis
	 * 
	 * The coefficients of a Hann window are computed from the following equation
	 *  W(n) = 0.5 [ 1 - cos( (2*pi) * (n/N) ) ] , 0 <= n <= N
	 *  
	 * The window length is L = N + 1
	 *  
	 * @param    int     $Length
	 * @param    string  $sflag   By default 'periodic' or 'symmetric'
	 * @return   Array   returns a L point Hann window 
	 */
	function hann($length, $sflag = "periodic")
	{
	   $window = array();

	   $N = $length - 1;

	   for ($i=0; $i < $N; $i++) { 

	   		$window[] = (0.5) * (1 - cos( (2 * M_PI ) * ( $i / $N)  ));
	   
	   }

	   
	    return $window;
	}
}

?>