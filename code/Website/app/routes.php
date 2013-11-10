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

Route::get('/', function()
{
	return View::make('hello');
});

Route::get('/temperature',function(){

	return Response::json(array('packageId' => '1', 'truckId' => '1', 'timestamp' => 1372702120000, 'temperature' => array('sensorId' => '1','value' => '72.275'), 'location' => array('latitude'=>43.084136089005405, 'longitude' => -77.67932448361998)));
});