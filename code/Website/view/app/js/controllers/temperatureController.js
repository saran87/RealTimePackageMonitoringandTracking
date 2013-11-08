angular.module('myModule')
  .controller('temperatureController',['$scope','$routeParams','temperatureService',function($scope,$routeParams,temperatureService){

  	$scope.headMessage = "Temperature Details for All";
    
  	$scope.data =[];
  	$scope.temperatureData=[];
  	
    temperatureService.getT();
    $scope.temperatureData=temperatureService.tdata;
    $scope.data=[temperatureService.gdata];    

   }]);
