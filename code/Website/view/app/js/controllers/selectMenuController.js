angular.module('myModule')
	.controller('selectMenuCtrl',['$scope','$location',function($scope,$location){

		$scope.location=$location;
		$scope.truckid=0;
		$scope.packageid=0;			

		$scope.selected = {};

	    $scope.trucks = [
	        {
	            "id" : "1",
	            "name" : "TruckOne",
	            "packages" : ["1","2","3"]
	        }

	       ,{
	            "id" : "2",
	            "name" : "TruckTwo",
	            "packages" : ["1","2","3"]
	        }
	       ,
	        {

	        	"id" : "3",
	            "name" : "Volvo",
	            "packages" : ["1","2","3"]

	        }
	    ];

	  	$scope.urlFunc = function(){
	    	$location.path('temperature/'+$scope.selected.id.id+'/'+$scope.selected.package);
	    };		

	}]);