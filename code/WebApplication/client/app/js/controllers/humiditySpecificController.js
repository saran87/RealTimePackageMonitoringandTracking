angular.module('myModule')
  .controller('humiditySpecificCtrl',['$scope','$rootScope','$routeParams','$location','$timeout','humidityService', 'dashBoardService',function($scope,$rootScope,$routeParams,$location,$timeout,humidityService,dashBoardService){

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
	

	  $scope.location = $location;  //location variable

  	var holder = []; //temporary array to recieve updates and update the graph

  	/*
  		Calling the getHumidityDataOf() method in humidityService to retrive
  		data for the specified Truck and Package. Data received will be an array
  		of length 4. 
  		Last array element is an error object.
  	*/

    $scope.humidityData=[];

    dashBoardService.getConfigurationsOf(truck,pack)
    .then(function(data){

      if(!data[2].isError){

        //$scope.maxThreshold = data[0].config.humidity.maxthreshold;
        if(data[0].config.humidity.maxthreshold==0){
            $scope.maxThreshold = 72; 

          } else {

            $scope.maxThreshold = data[0].config.humidity.maxthreshold;

          }
          

          if(data[0].config.is_realtime){

            $rootScope.rt=true;           

          } else {

            $rootScope.rt=false;

          }



      }

    });

  	humidityService.getHumidityDataOf(truck,pack)
	  	.then(function(data){

        var tArr=[];
        var thArr=[];

	  		//check for error in the error object
	  		if(!data[3].isError){

	  			latestTimestamp=data[2]; //assign the latest timestamp i.e. for first time

          //for(var i=data[0].length-167;i<data[0].length;i++){
          for(var i=0;i<data[0].length;i++){

            $scope.humidityData.push(data[0][i]);

            tArr.push(data[1][i]);

            thArr.push([data[1][i][0], $scope.maxThreshold]);

          }

          console.dir(tArr);

		  		//$scope.humidityData=data[0]; //assign the humidity data to be displayed in table

		  		//data to be shown in the graph formatted in the second element of data array
		  		$scope.data=[

			    	{
			    		"key": "Humidity Graph",	    		
			    		"values": tArr
			    	}, 

            {
              "key": "Threshold",
              "values": thArr
            }
		    
		    	];

		    	holder=data[1]; //assign the same graph data into the temporary holder array

		    //	humidityUpdater(); //call the updater function - Polling function

	    	} else {
	    		//if the response is bad - Display the error message	    		

	    		console.log("Error: " + data[3].errorMsg);

	    	}

	  	}); //end then

	  	/*
	  		This function formats the Ticks on x-axis of the graph
	  	*/

	  	$scope.xAxisTickFormatFunction2 = function(){
          return function(d){
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

        var humidityUpdater = function(){

    		if(truck!=undefined || pack!=undefined){

    			humidityService.getLatestHumidityData(truck,pack,latestTimestamp)
    			.then(function(data){

    				if(!data[3].isError){

    					console.log("latestTimestamp " + data[2]);

    					for(var i=0; i<data[0].length; i++){

    						$scope.humidityData.push(data[0][i]);

    					}

    					console.log("new humidity update: " + $scope.humidityData);

    					console.log("data[1] check");

    					console.dir(data[1]);

    					for(var j=0; j<data[1].length; j++){

    						console.log("into holder: " + data[1][j]);
    						holder.push(data[1][j]);

    					}

    					$scope.data=[

					    	{
					    		"key": "Humidity Graph",	    		
					    		"values": holder
					    	}
				    
				    	];

    					
    					console.dir("new humiditygraph update: " + $scope.data);

    					latestTimestamp=data[2];

    				} else {

    					console.log("no new humidity data");

    				}

    			});    			

    		}

    		var timer = $timeout(humidityUpdater, 10000);

    		$scope.$on('$locationChangeStart', function() {
         		$timeout.cancel(timer);         		
     		});

    	}
	
	

	

  }]);