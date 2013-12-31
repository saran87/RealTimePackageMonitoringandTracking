'use strict';
var dataPath='../../RealTimePackageMonitoringandTracking/rfid/index.php?s=package&type=json&';
/* Controllers */

angular.module('myApp.controllers', []).
  controller('MyCtrl1', [function() {

  }])
  .controller('MyCtrl2', [function() {

  }])
  .controller('dataListCtrl', ['$scope','$http',function($scope,$http){
  	
  	
  	$scope.myData=[];
  	$http.get(dataPath).success(function(data){

  		//console.log("len " + data.data.length);

	  		for (var i=0; i<=10; i++) {

	  			var d=data.data[i];

	  			console.log(d.location.latitude +", " + d.location.longitude + " " + d.package_id + " " + d.timestamp);
	  			
	  			$scope.myData[i] = d;
	  		}
	  		//console.log(d.package_id + " " + d.location.);
  		});
  }])
  .controller('temperatureCtrl',['$scope','$http',function($scope, $http){

  	$scope.temperatureData=[];
  	$http.get(dataPath).success(function(data){

  		for(var i=0; i<=100; i++){
  			var d=data.data[i];
  			
  			if(d.temperature){
  				console.log("at " + i + "Val: " + d.temperature.value);
  				$scope.temperatureData.push(d);
  			}
  		}


  	});

  }])
  .controller('humidityCtrl',['$scope','$http', function($scope,$http){
  	$scope.humidityData=[];
  	$http.get(dataPath).success(function(data){

  		for(var i=0; i<=100; i++){
  			var d=data.data[i];
  			
  			if(d.humidity){
  				console.log("at " + i + "Val: " + d.humidity.value);
  				$scope.humidityData.push(d);
  			}
  		}

  	});

  }])
  .controller('vibrationCtrl',['$scope','$http', function($scope, $http){
  	$scope.toDisplay = "Here comes vibration from controller";
  }])