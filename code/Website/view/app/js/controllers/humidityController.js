angular.module('myModule')
  .controller('humidityController',['$scope','humidityService',function($scope,humidityService){  	

    $scope.headMessage="Humidity data for All";
    
    $scope.humidityData=humidityService.humidityData;
  	
    humidityService.getH();    

    console.log($scope.humidityData);

  }]);