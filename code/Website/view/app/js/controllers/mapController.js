angular.module('myModule')
	.controller('mapController', ['$scope','$http',function($scope,$http){
	  	
	  	
	  	$scope.myData=[];
	  	$http.get(dataPath).success(function(data){

	  		//console.log("len " + data.data.length);

		  		for (var i=0; i<=10; i++) {

		  			var d=data.data[i];

		  			console.log(d.location.latitude +", " + d.location.longitude + " " + d.packageId + " " + d.timestamp);
		  			
		  			$scope.myData[i] = d;
		  		}
		  		//console.log(d.packageId + " " + d.location.);
	  		});
	  }])