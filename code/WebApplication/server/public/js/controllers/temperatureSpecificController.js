angular.module('myModule')
	.controller('temperatureSpecificCtrl',['$rootScope','$scope','$location','$routeParams','temperatureService','dashBoardService','$timeout','$q',function($rootScope,$scope,$location,$routeParams,temperatureService, dashBoardService, $timeout, $q){

		
		var latestTimestamp; //holds the latestTimestamp for data received from a package
		$scope.noData=false;
		
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

		$scope.location = $location;  //location variable

	  	var holder = []; //temporary array to recieve updates and update the graph

	  	/*
	  		Calling the getTemperatureDataOf() method in temperatureService to retrive
	  		data for the specified Truck and Package. Data received will be an array
	  		of length 4. 
	  		Last array element is an error object.
	  	*/

	  	$scope.temperatureData=[];

	  	function initData(){

	  		var deferred = $q.defer();
	
		  	dashBoardService.getConfigurationsOf(truck,pack)
			  	.then(function(data){		  		

			  		if(!data[2].isError){

			  			//$scope.maxThreshold = data[0].config.temperature.maxthreshold;
			  			if(data[0].config.temperature.timeperiod!=0){
			  				$scope.refreshRate = data[0].config.temperature.timeperiod;
			  			} else {
			  				$scope.refreshRate = 60;
			  			}

			  			if(data[0].config.temperature.maxthreshold==0){
				        	$scope.maxThreshold = 66;	

				        } else {

				        	$scope.maxThreshold = data[0].config.temperature.maxthreshold;
				        	
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

		$scope.itemfilter = {};
    	$scope.itemfilter.is_above_threshold = "all";

		var thArr = [];

		initData().then(function(){

			temperatureService.getTemperatureDataOf(truck,pack)
	  		.then(function(data){  		

	  		//check for error in the error object
		  		if(!data[3].isError){

		  			$scope.noData = false;
		  			var tArr = [];	  			
		  			for(var i=0; i<data[0].length;i++){

		  				$scope.temperatureData.push(data[0][i]);

		  				tArr.push(data[1][i]);
		  				thArr.push([data[1][i][0], $scope.maxThreshold]);

		  			}	  			

		  			latestTimestamp=data[2]; //assign the latest timestamp i.e. for first time
		  			$scope.ts=latestTimestamp;

		  			$scope.loaded=true;

			  		 //assign the temperature data to be displayed in table

			  		//data to be shown in the graph formatted in the second element of data array
			  		$scope.data=[

				    	{
				    		"key": "Temperature Graph",	    		
				    		"values": tArr
				    	},

				    	{
				    		"key": "Threshold",
				    		"values": thArr
				    	}
			    
			    	];

			    	holder=data[1]; //assign the same graph data into the temporary holder array

			    	temperatureUpdater(); //call the updater function - Polling function
			    	

		    	} else {
		    		//if the response is bad - Display the error message	    		

		    		$scope.noData = true;

	          		$scope.errorMsg = data[3].errorMsg;

	          		console.log("Error: " + data[3].errorMsg);

		    	}

	  		}); //end then

		});
	  	

	  	/*
	  		This function formats the Ticks on x-axis of the graph
	  	*/

	  	$scope.xAxisTickFormatFunction2 = function(){
	        return function(d){
    	        //return d3.time.format('%x-%H:%M')(new Date(d));
    	        return d3.time.format('%H:%M')(new Date(d));
            }
        }

        $scope.xAxisTickFormatFunction = function(){
	        return function(d){
    	        return d3.time.format('%H:%M')(new Date(d));
            }
        }

        var colors = ['#1f77b4', '#DE5454'];

	    $scope.colorDefault = function() {
	      return function(d, i) {
	          return colors[i];
	        };
	    }

	    $scope.yAxisTickFormatFunction = function(){
	        return function(d){
    	        return d3.format('.02f')(d);
            }
        }


        /*
        	This is a polling function that calls itself every 10 seconds
        	to check for latest updates in the temperature data and thereby
        	updates the table and the graph with the latest entries.
        */
    	

    var temperatureUpdater = function(action){

    		if(!$scope.noData && $rootScope.rt){

	    		if(action==undefined){

		    		var actionBy=0;
		    	} else {
		    		var actionBy=action;
		    	}


	    		if(truck!=undefined || pack!=undefined){

	    			temperatureService.getLatestTemperatureData(truck,pack,latestTimestamp,actionBy)
	    			.then(function(data){

	    				if(!data[3].isError){

	    					console.log("latestTimestamp " + data[2]);

	    					for(var i=0; i<data[0].length; i++){

	    						$scope.temperatureData.push(data[0][i]);    						

	    					}

	    					console.log("new temperature update: " + $scope.temperatureData);

	    					//console.log("data[1] check");

	    					//console.dir(data[1]);

	    					for(var j=0; j<data[1].length; j++){
	    						
	    						holder.push(data[1][j]);
	    						thArr.push([data[1][j][0], $scope.maxThreshold]);
	    					}

	    					$scope.data=[

						    	{
						    		"key": "Temperature Graph",	    		
						    		"values": holder
						    	},

						    	{
						    		"key": "Threshold",
						    		"values": thArr
						    	}
					    
					    	];
	    					
	    					//console.dir("new temperaturegraph update: " + $scope.data);

	    					latestTimestamp=data[2];
	    					$scope.ts=latestTimestamp;

	    				} else {

	    					console.log("no new temperature data");

	    				}

	    			});
    		}  			

    	

    		var timer = $timeout(temperatureUpdater, $scope.refreshRate*1000);

    		$scope.$on('$locationChangeStart', function() {
         		$timeout.cancel(timer);         		
     		});
     	}

    } //end updater

	$scope.refresh = function(){

		if($rootScope.rt){

			temperatureUpdater(1);

		}
	}    	   	
    	

	}])
	.filter('hiddenFilter', function(){
		return function(temperatureData, show_or_hide, attribute){
			var shownItems = [];
	        if (show_or_hide === 'all'){

	        	shownItems=temperatureData;

	        	return shownItems;

	        } 
	        angular.forEach(temperatureData, function (item) {
	            if (show_or_hide === 'shown') {
	                if (item[attribute] === true) shownItems.push(item);
	            }
	        });
	        return shownItems;
		}
	});