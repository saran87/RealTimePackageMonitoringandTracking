angular.module('myModule')
  .controller('temperatureController',['$scope','$routeParams','temperatureService',function($scope,$routeParams,temperatureService){

  	$scope.headMessage = "Temperature Details for All";
    
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
