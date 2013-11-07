angular.module('myModule',[])
	.controller('mainCtrl',['$scope','$rootScope','$location','$routeParams',function($scope,$rootScope,$location,$routeParams){
		
		//setting the parameter varibales

		if($routeParams.truckid && $routeParams.packageid){
	    	$rootScope.truckidparam = $routeParams.truckid;
	    	$rootScope.packageidparam = $routeParams.packageid;
	    }
	    else if($routeParams.packageid){

	    	$rootScope.packageidparam=$routeParams.packageid;

	    }

	    $rootScope.$on( "$routeChangeStart", function(event, next, current) {
	      if ( $rootScope.truckidparam == null && $rootScope.$routeParams.packageid == null) {
	        
	        if ( next.templateUrl == "partials/temperatureSpecific.html" ) {
	          
	        } else {
	          
	          $location.path( "/temperature" );
	        }
	      }         
	    });




	}]);