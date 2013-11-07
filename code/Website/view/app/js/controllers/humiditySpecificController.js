angular.module('myModule')
  .controller('humiditySpecificCtrl',['$scope','$routeParams','humidityService',function($scope,$routeParams,humidityService){  	

  		if($routeParams.truckid && $routeParams.packageid){
	    	$scope.headMessage = "Humidity for Package ID "+$routeParams.packageid+" in truck "+$routeParams.truckid;
	    }
	    else if($routeParams.packageid){

	    	$scope.headMessage = "Humidity for Package ID "+$routeParams.packageid;

	    }
    
    $scope.humidityData=humidityService.humidityData;
  	
    humidityService.getH();        

  }]);