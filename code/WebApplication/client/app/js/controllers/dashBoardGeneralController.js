angular.module('myModule')
	.controller('dashBoardGeneralCtrl', ['$scope',"$http", function($scope,$http){

		$scope.msg = "This is General Dashboard";

	}]);