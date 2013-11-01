angular.module('myModule')
  .controller('temperatureController',['$scope','temperatureService',function($scope,temperatureService){
    
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

   
    //console.log($scope.data);

    //$scope.stuff=[[[1372702120000,72.275], [1372702074000,72.1625],[1377994074000,69.4625],[1372702085000,69.575],[1377994074000,69.125]]];

    //console.log($scope.stuff);

   }]);