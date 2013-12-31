angular.module('myModule',[])
	.controller('mainCtrl',['$scope','$rootScope','$location','$routeParams','$route',function($scope,$rootScope,$location,$routeParams, $route){		

		$scope.istruck_idSet = false;
		$scope.ispackage_idSet = false;

		$rootScope.$on( "$routeChangeStart", function(event, current, previous) {

			/*console.log("debug rcstart");

			console.log(event);
			console.log(current);
			console.log(previous);*/

	    	
	    });


	    $rootScope.$on( "$routeChangeSuccess", function(event, current, previous) {
	    	
	    	if($routeParams.truck_id!==undefined || $routeParams.package_id!==undefined){

	    		console.log("truck_id=" + $routeParams.truck_id + " package_id="+$routeParams.package_id );

	    	}
	    	
	    });

	}]);