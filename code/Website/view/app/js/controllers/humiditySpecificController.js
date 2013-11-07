angular.module('myModule')
  .controller('humiditySpecificCtrl',['$scope','$routeParams','humidityService',function($scope,$routeParams,humidityService){  	

  		if($routeParams.truckid && $routeParams.packageid){
	    	$scope.headMessage = "Humidity for Package ID "+$routeParams.packageid+" in truck "+$routeParams.truckid;
	    }
	    else if($routeParams.packageid){

	    	$scope.headMessage = "Humidity for Package ID "+$routeParams.packageid;

	    }

	$scope.data =[];	  
	$scope.humidityData=[];

    humidityService.getH();        
    $scope.humidityData=humidityService.humidityData;
    $scope.data=[humidityService.hgData];

  }]);