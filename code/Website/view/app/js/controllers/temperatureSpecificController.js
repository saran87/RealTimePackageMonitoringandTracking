angular.module('myModule')
	.controller('temperatureSpecificCtrl',['$scope','$location','$routeParams','temperatureService',function($scope,$location,$routeParams,temperatureService){

		if($routeParams.truckid && $routeParams.packageid){
	    	$scope.headMessage = "Temperature for Package ID "+$routeParams.packageid+" in truck "+$routeParams.truckid;
	    }
	    else if($routeParams.packageid){

	    	$scope.headMessage = "Temperature for Package ID "+$routeParams.packageid;

	    }

		$scope.data =[];
	  	$scope.temperatureData=[];
	  	$scope.limit=10;
	  	$scope.templimit=10;
	  	$scope.plus = 0;
	    temperatureService.getT($scope.plus,$scope.limit);
	    $scope.temperatureData=temperatureService.tdata;
	    $scope.data=[temperatureService.gdata];

	    $scope.showMore = function(){

	    	$scope.plus += $scope.limit;
	    	$scope.templimit += $scope.limit;
	    	temperatureService.getT($scope.plus,$scope.templimit);
	    	$scope.temperatureData=temperatureService.tdata;   
	    }	    

	}]);