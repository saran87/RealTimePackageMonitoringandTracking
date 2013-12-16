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


/*Routes to get configurations Data*/

Route::get('/configs', 'ConfigController@configurations');

Route::get('/alltrucks', 'ConfigController@listAllTrucks');

//returns all packages having in specified truckid
Route::get('/packages/{truckid}', 'ConfigController@listPackagesInTruckWithTruckId'); 

Route::get('/latestEntry', 'ConfigController@latestEntry');


Route::get('truck/{truckid}/package/{packageid}', 'ConfigController@configsPackageInTruck');

//returns all documents having packageid
Route::get('/package/{packageid}', 'ConfigController@packagesWithPackageId'); 


/*Routes to get Temperature Data*/

Route::get('/temperature','TemperatureController@temperatureOf');

Route::get('/temperature/{truckid}/{packageid}', 'TemperatureController@temperatureOfPackageInTruck');

Route::get('/temperature/{packageid}', 'TemperatureController@temperatureOfPackage');

Route::get('/temperatureLatestEntry', 'TemperatureController@latestEntry');

Route::get('temperatureEntry/{truckid}/{packageid}/{timestamp}', 'TemperatureController@TemperatureAfterTimestamp');

/*Routes to get Humidity Data*/

Route::get('/humidity','HumidityController@humidityOf');

Route::get('/humidity/{truckid}/{packageid}', 'HumidityController@humidityOfPackageInTruck');

Route::get('/humidity/{packageid}', 'HumidityController@humidityOfPackage');

Route::get('/humidityLatestEntry', 'HumidityController@latestEntry');

Route::get('/humidityEntry/{truckid}/{packageid}/{timestamp}', 'HumidityController@HumidityAfterTimestamp');

/*Routes to get Vibration Data*/

Route::get('/vibration', 'VibrationController@vibration');

Route::get('/vibration/{truckid}/{packageid}', 'VibrationController@vibrationOfPackageInTruck');

Route::get('/vibrationgraph/{id}', 'VibrationController@vibrationgraphdata');

Route::get('/psd/{id}', 'PSDController@getPSDArray');

Route::get('/vibrationLatestEntry', 'VibrationController@latestEntry');

Route::get('/vibrationEntry/{truckid}/{packageid}/{timestamp}', 'VibrationController@VibrationAfterTimestamp');




/*Routes to get Shock Data*/

Route::get('/shock', 'ShockController@shockData');

Route::get('/shock/{truckid}/{packageid}', 'ShockController@shockData');

Route::get('/shockgraph/{id}', 'ShockController@shockGraphData');

Route::get('/shockLatestEntry', 'ShockController@latestEntry');


/*Routes to get Map Data*/



/*Routes to get Dashboards Data*/