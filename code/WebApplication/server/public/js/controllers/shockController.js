angular.module('myModule')
	.controller('shockController',['$scope','$rootScope','$routeParams','shockService','dashBoardService','$http','$timeout','$q', function($scope,$rootScope,$routeParams,shockService,dashBoardService,$http,$timeout,$q){

		var latestTimestamp; //holds the latestTimestamp for data received from a package

		$scope.noData=false;

		$scope.openGraph = false;

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

		function initData(){

    		var deferred = $q.defer();

	    	dashBoardService.getConfigurationsOf(truck,pack)
	    	.then(function(data){

		      if(!data[2].isError){	      		      	

		        if(data[0].config.shockx.timeperiod!=0){

	  				$scope.refreshRate = data[0].config.shockx.timeperiod;

	  			} else {
	  				
	  				$scope.refreshRate = 60;
	  			}


		        if(data[0].config.shockx.maxthreshold!=0){
		        	
		        	$scope.maxThreshold = data[0].config.shockx.maxthreshold;

		        } else {

		        	$scope.maxThreshold = 2;	

		        }
		        

		        if(data[0].is_realtime){

					$rootScope.rt=true;	        	

		        } else {

		        	$rootScope.rt=false;

		        }

		        deferred.resolve();		        
		        
		      } 

	    	});

	    	return deferred.promise;
    	}

    	initData().then(function(){

    		shockService.getShockData(truck,pack)
		    .then(function(data){

		    	if(!data[2].isError){

		    		$scope.noData=false;

		    		latestTimestamp=data[1];

		    		$scope.ts=latestTimestamp;

		    		$scope.loaded=true;
		    		
		    		$scope.shockData=data[0];

		    		$scope.discreteGraph();

		    		shockUpdater();

		    	} else {	    		

		    		$scope.noData = true;

		    		$scope.errorMsg = data[2].errorMsg;

		    		console.log("Error: " + data[2].errorMsg);

		    	}

		    });

    	});		    
		

	    var shockUpdater = function(action){	    	

	    	if(!$scope.noData && $rootScope.rt){

		    	if(action==undefined){

		    		var actionBy=0;
		    	} else {
		    		var actionBy=action;
		    	}

		    	if(truck!=undefined || pack!=undefined){

		    		shockService.getLatestShockData(truck,pack,latestTimestamp,actionBy)
		    		.then(function(data){

		    			if(!data[2].isError){

		    				for(var i=0; i<data[0].length; i++){

	                			$scope.shockData.push(data[0][i]);
	            			}

	            			latestTimestamp=data[1];

	            			$scope.ts=latestTimestamp;

	            			$scope.discreteGraph();

		    			} else {

		    				console.log("no new shock data");

		    			}

		    		});

		    	}

		    	var timer = $timeout(shockUpdater, $scope.refreshRate*1000);

		      	$scope.$on('$locationChangeStart', function() {
		        	$timeout.cancel(timer);             
		      	});

		    }

	    }

	    $scope.refresh = function(){

	    	if($rootScope.rt){
	    		
	    		shockUpdater(1);
	    	}
	    }

	    $scope.discreteGraph = function(indexOf,id){	    	

	    	var XArr=[];
	    	var YArr=[];
	    	var ZArr=[];


	    	for(var i=0; i<$scope.shockData.length; i++){

	    		XArr.push([ $scope.shockData[i].timestamp, parseFloat($scope.shockData[i].value.x)]);
	    		YArr.push([ $scope.shockData[i].timestamp, parseFloat($scope.shockData[i].value.y)]);
	    		ZArr.push([ $scope.shockData[i].timestamp, parseFloat($scope.shockData[i].value.z)]);
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
              return d3.time.format('%H:%M')(new Date(d));
            }
    	}


		$scope.shockGraph = function(indexOf,id){

			$scope.openGraph = true;

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

	        var len= currX.length;	 

	        for(var i=1; i<=len; i++){

		        if(i>0){
	            	ts = ts + (0.125*1000); //incrementing timestamp values
	          	} 	           

	          xVals.push([ts,parseFloat(currX[i])]);
	          yVals.push([ts,parseFloat(currY[i])]);
	          zVals.push([ts,parseFloat(currZ[i])]);

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

	    $scope.setSelected = function(indexSelected){

	    	$scope.indexSelected = indexSelected;
	    }

	    $scope.xAxisTickFormatFunction = function(){
          return function(d){
              return d3.time.format('%H:%M')(new Date(d));
            }
	    }
	   

	    $scope.yAxisTickFormatFunction = function(){
	          return function(d){
	              return d3.format('.02f')(d);
	            }
	    }	    
	    
	    var colors = ['#1f77b4', '#ff7f0e', '#2ca02c'];

	    $scope.colorDefault = function() {
	      return function(d, i) {
	          return colors[i];
	        };
	    }

	    $scope.closeGraph = function(){
	    	$scope.openGraph = false;
	    	$scope.indexSelected = null;
	    }

	}]);