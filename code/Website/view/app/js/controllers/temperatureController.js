var dataPath='../../../RealTimePackageMonitoringandTracking/rfid/index.php?s=package&type=json&';

angular.module('myModule')
  .controller('temperatureController',['$scope','temperatureService',function($scope,temperatureService){
    
  	$scope.data =[];
    temperatureService.getT();
    $scope.temperatureData=temperatureService.tdata;

    $scope.data=[temperatureService.gdata];
    //console.log($scope.data);

    //$scope.stuff=[[[1372702120000,72.275], [1372702074000,72.1625],[1377994074000,69.4625],[1372702085000,69.575],[1377994074000,69.125]]];

    //console.log($scope.stuff);

   }]);