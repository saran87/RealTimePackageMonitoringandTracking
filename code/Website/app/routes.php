<?php

/*
|--------------------------------------------------------------------------
| Application Routes
|--------------------------------------------------------------------------
|
| Here is where you can register all of the routes for an application.
| It's a breeze. Simply tell Laravel the URIs it should respond to
| and give it the Closure to execute when that URI is requested.
|
*/

Route::get('/configs', 'ServiceController@configurations');

Route::get('/temperature','ServiceController@temperature');

	/*return Response::json(array('packageId' => '1', 'truckId' => '1', 'timestamp' => 1372702120000, 'temperature' => array('sensorId' => '1','value' => '72.275'), 'location' => array('latitude'=>43.084136089005405, 'longitude' => -77.67932448361998)));*/

Route::get('temperature/{truckid}/{packageid}', 'ServiceController@temperatureShow')->where('truckid', '\d+')->where('packageid','\d+');

Route::get('/humidity','ServiceController@humidity');

Route::get('/vibration', 'ServiceController@vibration');