'use strict';


// Declare app level module which depends on filters, and services
angular.module('myApp', ['ui.bootstrap','myModule','myServices','myDirectives']). 	
  	config(['$routeProvider', function($routeProvider) {
  	$routeProvider.when('/dashboard', {templateUrl: 'partials/dashboard.html', controller: 'dashboardCtrl'});
    $routeProvider.when('/map', {templateUrl: 'partials/partial1.html', controller: 'mapController'});
    $routeProvider.when('/temperature', {templateUrl: 'partials/temperature.html'}); 
    $routeProvider.when('/humidity', {templateUrl: 'partials/humidity.html', controller: 'humidityController'});
    $routeProvider.when('/vibration', {templateUrl: 'partials/vibration.html', controller: 'vibrationController'});
    $routeProvider.when('/temperature/:truckid/:packageid', {templateUrl: 'partials/temperatureSpecific.html'});
    $routeProvider.when('/humidity/:truckid/:packageid', {templateUrl: 'partials/humidity.html'});
    $routeProvider.otherwise({redirectTo: '/map'});
  
  }]);