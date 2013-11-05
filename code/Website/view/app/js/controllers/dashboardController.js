angular.module("myModule")
	.controller('dashboardCtrl',['$scope', function($scope){
		
		$scope.thresholds = {
			"temperature" : "70",
			"humdity" : "48",
			"vibration" : "10",
			"shock" : "20"
		};


	}]);