angular.module('myModule')
	.controller('selectMenuCtrl',['$scope','$location',function($scope.$location){

		$scope.location=$location;

		$scope.trucks = [
			{
				"truckid": 1,
				"name": "xyz",
				"packages": [1,2,3]
			},
			{
				"truckid": 2,
				"name": "abc",
				"packages": [1,2,3]
			},
			{
				"truckid": 2,
				"name": "mno",
				"packages": [1,2,3]
			}
		];
		

		$scope.truckid=0;
		$scope.packageid=0;

		$scope.assignTruckid = function(truckid){

			$scope.truckid = truckid;
			return $scope.truckid;

		};

		$scope.assignPackageid = function(packageid){
			$scope.packageid = packageid;
			return $scope.packageid;
		};

		$scope.$watch('location.search()', function() {

			$scope.truckid = $location.search().truckid;
			$scope.packageid = $location.search().packageid;
			
		},true);

		$scope.change = function(x,y){

			$location.search('truckid',x);


		}

	}]);