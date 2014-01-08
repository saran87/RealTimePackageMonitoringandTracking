angular.module('myModule')
	.controller('shockController',['$scope','$rootScope','$routeParams','shockService','$http', function($scope,$rootScope,$routeParams,shockService,$http){

		var latestTimestamp; //holds the latestTimestamp for data received from a package
    	var truck=$rootScope.tid; //truck_id selected in the Dropdown menu
    	var pack=$rootScope.pid; //package_id selected in the Dropdown me

	    shockService.getShockData(truck,pack)
	    .then(function(data){

	    	if(!data[2].isError){

	    		latestTimestamp=data[1];
	    		$scope.shockData=data[0];

	    		$scope.discreteGraph();

	    	} else {

	    		console.log("Error: " + data[2].errorMsg);

	    	}

	    });

	    $scope.discreteGraph = function(indexOf,id){	    	

	    	var XArr=[];
	    	var YArr=[];
	    	var ZArr=[];


	    	for(var i=0; i<$scope.shockData.length; i++){

	    		XArr.push([ $scope.shockData[i].timestamp, $scope.shockData[i].value.x]);
	    		YArr.push([ $scope.shockData[i].timestamp, $scope.shockData[i].value.y]);
	    		ZArr.push([ $scope.shockData[i].timestamp, $scope.shockData[i].value.z]);
	    	}


	    	$scope.discdata = [
	    		{
	    			"key": "Shock X",
	    			"values": XArr

	    		},

	    		{
	    			"key": "Shock Y",
	    			"values": YArr

	    		},

	    		{
	    			"key": "Shock Z",
	    			"values": ZArr

	    		}

	    	];

	    }

	    $scope.xAxisTickFormatFunction = function(){
          return function(d){
              return d3.time.format('%H:%M:%S')(new Date(d));
            }
    	}


		$scope.shockGraph = function(indexOf,id){

	      var xVals=[], 
	          yVals = [],
	          zVals=[];

	      //call getShockGraphData(id) method to get the data for the id passed in
	      var graphvals = shockService.getShockGraphData(id); 

	      graphvals.then(function(data){

	        var ts = data.timestamp;

	        var currX = data.x.split(" ");
	        var currY = data.y.split(" ");
	        var currZ = data.z.split(" ");	 

	        for(var i=0; i<512; i++){	           

	          xVals.push([Math.ceil((800/512)*i+1),parseFloat(currX[i])]);
	          yVals.push([Math.ceil((800/512)*i+1),parseFloat(currY[i])]);
	          zVals.push([Math.ceil((800/512)*i+1),parseFloat(currZ[i])]);

	        }	        

	        $scope.sgData = [

	          {
	            "key" : "Shock x",
	            "values" : xVals

	          },

	          {
	            "key" : "Shock y",
	            "values" : yVals

	          },

	          {
	            "key" : "Shock z",
	            "values" : zVals

	          }

	        ];

	      }); //end then

	      $("html, body").animate({ scrollTop: 0 }, 200);

	    } //end shockGraph

	    var colors = ['#0000FF', '#C79F25', '#FF0000'];

	    $scope.colorDefault = function() {
	      return function(d, i) {
	          return colors[i];
	        };
	    }

	}]);