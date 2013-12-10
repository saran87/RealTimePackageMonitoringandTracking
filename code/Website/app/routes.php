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

Route::get('/configs', 'ServiceController@configurations');



/*Routes to get Temperature Data*/

Route::get('/temperature','ServiceController@temperature');

Route::get('temperature/{truckid}/{packageid}', 'ServiceController@temperatureShow')->where('truckid', '\d+')->where('packageid','\d+');



/*Routes to get Humidity Data*/

Route::get('/humidity','ServiceController@humidity');



/*Routes to get Vibration Data*/

Route::get('/vibration', 'ServiceController@vibration');

Route::get('/vibration/{truckid}/{packageid}', 'ServiceController@vibration')->where('truckid', '\d+')->where('packageid', '\d+');

Route::get('/vibrationgraph/{id}', 'ServiceController@vibrationgraphdata');

Route::get('/psd/{id}', 'PSDController@doStuff');



/*Routes to get Shock Data*/

Route::get('/shock', 'ShockController@shockData');

Route::get('/shock/{truckid}/{packageid}', 'ShockController@shockData');



/*Routes to get Map Data*/



/*Routes to get Dashboards Data*/