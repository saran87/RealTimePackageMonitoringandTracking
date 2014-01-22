angular.module('myModule')
  .controller('humiditySpecificCtrl',['$scope','$rootScope','$routeParams','$location','$timeout','humidityService', 'dashBoardService','$q',function($scope,$rootScope,$routeParams,$location,$timeout,humidityService,dashBoardService,$q){

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
  		Calling the getHumidityDataOf() method in humidityService to retrive
  		data for the specified Truck and Package. Data received will be an array
  		of length 4. 
  		Last array element is an error object.
  	*/

    $scope.humidityData=[];

    function initData(){

      var deferred = $q.defer();

      dashBoardService.getConfigurationsOf(truck,pack)
      .then(function(data){

        if(!data[2].isError){

          if(data[0].config.humidity.timeperiod!=0){
            $scope.refreshRate = data[0].config.humidity.timeperiod;
          } else {
            $scope.refreshRate = 60;
          }
          
          if(data[0].config.humidity.maxthreshold==0){
              $scope.maxThreshold = 50; 

            } else {

              $scope.maxThreshold = data[0].config.humidity.maxthreshold;

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

    var thArr=[];

    $scope.itemfilter = {};
    $scope.itemfilter.is_above_threshold = "all";

    initData().then(function(){

      humidityService.getHumidityDataOf(truck,pack)
      .then(function(data){

        var tArr=[];        

        //check for error in the error object
        if(!data[3].isError){

          $scope.noData = false;
          latestTimestamp=data[2]; //assign the latest timestamp i.e. for first time

          $scope.ts=latestTimestamp;

          $scope.loaded=true;
          
          for(var i=0;i<data[0].length;i++){

            $scope.humidityData.push(data[0][i]);

            tArr.push(data[1][i]);

            thArr.push([data[1][i][0], $scope.maxThreshold]);

          }
          
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

          humidityUpdater(); //call the updater function - Polling function

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

      var humidityUpdater = function(action){

        if(!$scope.noData && $rootScope.rt){

          if(action==undefined){

            var actionBy=0;

          } else {

            var actionBy=action;

          }

      		if(truck!=undefined || pack!=undefined){

      			humidityService.getLatestHumidityData(truck,pack,latestTimestamp,actionBy)
      			.then(function(data){

      				if(!data[3].isError){

      					console.log("latestTimestamp " + data[2]);

      					for(var i=0; i<data[0].length; i++){

      						$scope.humidityData.push(data[0][i]);

      					}

      					console.log("new humidity update: " + $scope.humidityData);

      					//console.log("data[1] check");

      					//console.dir(data[1]);

      					for(var j=0; j<data[1].length; j++){

      						//console.log("into holder: " + data[1][j]);
      						holder.push(data[1][j]);
                  thArr.push([data[1][j][0], $scope.maxThreshold]);

      					}

      					$scope.data=[

  					    	{
  					    		"key": "Humidity Graph",	    		
  					    		"values": holder
  					    	},

                  {
                    "key": "Threshold",
                    "values": thArr
                  }
  				    
  				    	];

      					
      					console.dir("new humiditygraph update: " + $scope.data);

      					latestTimestamp=data[2];
                $scope.ts=latestTimestamp;

      				} else {

      					console.log("no new humidity data");

      				}

      			});    			

      	}

      	var timer = $timeout(humidityUpdater, $scope.refreshRate*1000);

    		$scope.$on('$locationChangeStart', function() {
         		$timeout.cancel(timer);         		
     		});

      }

    } //end updater

      $scope.refresh = function(){

        if($rootScope.rt){

          humidityUpdater(1);
        }

      }

  }])
  .filter('hiddenFilter', function(){
    return function(humidityData, show_or_hide, attribute){
      var shownItems = [];
          if (show_or_hide === 'all'){

            shownItems=humidityData;

            return shownItems;

          } 
          angular.forEach(humidityData, function (item) {
              if (show_or_hide === 'shown') {
                  if (item[attribute] === true) shownItems.push(item);
              }
          });
          return shownItems;
    }
  });