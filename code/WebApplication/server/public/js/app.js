'use strict';

// Declare app level module which depends on filters, and services

angular.module('myApp', ['ui.bootstrap','myModule','myServices','myDirectives','nvd3ChartDirectives','google-maps'])
    .config(['$routeProvider', function($routeProvider) {

    /*
        Routes and Partials related to /
    */

    $routeProvider.when('/',
    {
        templateUrl: 'partials/dashboardGeneral.html',
    });

    /*
        Routes and Partials related to Dashboard
    */

    $routeProvider.when('/dashboard', 
    {
        templateUrl: 'partials/dashboardGeneral.html'
        
    });

    $routeProvider.when('/dashboard/:truck_id/:package_id', 
    {
        templateUrl: 'partials/dashboard.html'
        
    });

    /*
        Routes and Partials related to Map
    */

    $routeProvider.when('/map',
    {
        templateUrl: 'partials/map.html',
        controller: 'mapController'
    });

    $routeProvider.when('/map/:truck_id/:package_id',
    {
        templateUrl: 'partials/map.html',
        controller: 'mapController'
    });


    /*
        Routes and Partials related to Temperature
    */

    $routeProvider.when('/temperature',
    {
        templateUrl: 'partials/temperature.html'
    });

    $routeProvider.when('/temperature/:truck_id/:package_id',
    {
        templateUrl: 'partials/temperatureSpecific.html'
    });

    $routeProvider.when('/temperature/:package_id',
    {
        templateUrl: 'partials/temperatureSpecific.html'
    });


    /*
        Routes and Partials related to Humidity
    */

    $routeProvider.when('/humidity', 
    {
        templateUrl: 'partials/humidity.html'
    });

    $routeProvider.when('/humidity/:package_id',
    {
        templateUrl: 'partials/humiditySpecific.html'
    });

    $routeProvider.when('/humidity/:truck_id/:package_id',
    {
        templateUrl: 'partials/humiditySpecific.html'
        
    });


    /*
        Routes and Partials related to Vibration
    */

    $routeProvider.when('/vibration',
    {
        templateUrl: 'partials/vibration.html'
    });

    $routeProvider.when('/vibration/:package_id',
    {
        templateUrl: 'partials/vibrationSpecific.html'
    });

    $routeProvider.when('/vibration/:truck_id/:package_id',
    {
        templateUrl: 'partials/vibrationSpecific.html'
    });


    /*
        Routes and Partials related to Shock
    */

    $routeProvider.when('/shock', 
    {
        templateUrl: 'partials/shock.html'
    });

    $routeProvider.when('/shock/:package_id',
    {
        templateUrl: 'partials/shockSpecific.html'
    });

    $routeProvider.when('/shock/:truck_id/:package_id',
    {
        templateUrl: 'partials/shockSpecific.html'
    });


    /*
        If all else fails go back and show the dashboard
    */
    
    $routeProvider.otherwise(
    {
        redirectTo: '/'
    });
  
  }]);
    