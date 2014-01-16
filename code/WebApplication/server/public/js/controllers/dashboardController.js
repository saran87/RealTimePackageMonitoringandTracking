angular.module("myModule")
	.controller('dashboardCtrl',['dashBoardService','$scope','$rootScope','$routeParams', function(dashBoardService, $scope,$rootScope,$routeParams){
		
		var latestTimestamp; //holds the latestTimestamp for data received from a package
		
		if( ($rootScope.tid!=undefined || $rootScope.tid) && ($rootScope.pid!=undefined || $rootScope.pid) ){

			var truck=$rootScope.tid; //truck_id selected in the Dropdown menu
			var pack=$rootScope.pid; //package_id selected in the Dropdown menu
		} 
		else if($routeParams.truck_id && $routeParams.package_id){

			$rootScope.tid=$routeParams.truck_id;
			$rootScope.pid=$routeParams.package_id;

			var truck=$routeParams.truck_id; //truck_id selected in the Dropdown menu
			var pack=$routeParams.package_id; //package_id selected in the Dropdown menu
		} 
		else {
			console.log("Undefined truck and package");
		}
		

		dashBoardService.getConfigurationsOf(truck,pack)
		.then(function(data){

			if(!data[2].isError){

				$scope.configObj = data[0];							//"is_realtime"


			} else {
				console.log("Error: no data returned");
			}

		});

	}]);