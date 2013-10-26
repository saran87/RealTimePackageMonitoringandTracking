'use strict';


// Declare app level module which depends on filters, and services
angular.module('myApp', ['ui.bootstrap','myModule','myServices','myDirectives']). 	
  	config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/map', {templateUrl: 'partials/partial1.html', controller: 'mapController'});
    $routeProvider.when('/temperature', {templateUrl: 'partials/temperature.html', controller: 'temperatureController'}); 
    $routeProvider.when('/humidity', {templateUrl: 'partials/humidity.html', controller: 'humidityController'});
    $routeProvider.when('/vibration', {templateUrl: 'partials/vibration.html', controller: 'vibrationController'});
    $routeProvider.otherwise({redirectTo: '/map'});
  
  }]);