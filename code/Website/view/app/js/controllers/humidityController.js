var dataPath='../../../RealTimePackageMonitoringandTracking/rfid/index.php?s=package&type=json&';

angular.module('myModule')
  .controller('humidityController',['$scope','humidityService',function($scope,humidityService){

    $scope.humidityData=humidityService.humidityData;
  	
    humidityService.getHumidityData();

  }]);