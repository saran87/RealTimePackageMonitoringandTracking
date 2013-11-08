angular.module('myModule')
	.controller('temperatureSpecificCtrl',['$scope','$location','$routeParams','temperatureService','$timeout',function($scope,$location,$routeParams,temperatureService,$timeout){

		if($routeParams.truckid && $routeParams.packageid){
	    	$scope.headMessage = "Temperature for Package ID "+$routeParams.packageid+" in truck "+$routeParams.truckid;
	    }
	    else if($routeParams.packageid){

	    	$scope.headMessage = "Temperature for Package ID "+$routeParams.packageid;

	    }

		$scope.data =[];
	  	$scope.temperatureData=[];	  	
	  	temperatureService.getT();
	  	$scope.temperatureData=temperatureService.tdata;
	  	$scope.presentTemperature=temperatureService.firstTemperatureTimeStamp;
		$scope.latestTemperature=temperatureService.getLatestTemperatureEntry();
	    
	    $scope.data=[temperatureService.gdata];	    

	    //$scope.timeInMs = 0;
  
	    var updateLatest = function() {

	    	if($scope.latestTemperature > $scope.presentTemperature){
	    		
	    		//get the latest temeprature value and update
	    	}

	        $scope.timeInMs+= 500;
	        $timeout(updateLatest, 5000);
	    }
	    
	    $timeout(updateLatest, 5000);

	}]);