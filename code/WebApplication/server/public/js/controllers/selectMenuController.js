angular.module('myModule')
	.controller('selectMenuCtrl',['selectService','$scope','$rootScope','$location','$routeParams','$timeout',function(selectService,$scope,$rootScope,$location,$routeParams,$timeout){

		$rootScope.typeOfService = 'dashboard';

		$scope.location=$location;

		$scope.selected = {};


		$scope.render = function(){
			
			if( ($rootScope.tid!=undefined || $rootScope.tid) && ($rootScope.pid!=undefined || $rootScope.pid) ){
					
				$scope.sideNavHrefs = {

					"dashboard" : "#/dashboard"+"/"+$rootScope.tid+"/"+$rootScope.pid,
					"map" : "#/map"+"/"+$rootScope.tid+"/"+$rootScope.pid,
					"temperature" : "#/temperature"+"/"+$rootScope.tid+"/"+$rootScope.pid,
					"humidity" : "#/humidity"+"/"+$rootScope.tid+"/"+$rootScope.pid,
					"vibration" : "#/vibration"+"/"+$rootScope.tid+"/"+$rootScope.pid,
					"shock" : "#/shock"+"/"+$rootScope.tid+"/"+$rootScope.pid
				};

			} else if($routeParams.truck_id && $routeParams.package_id) {
				console.log("in rps");
				$rootScope.tid = $routeParams.truck_id;
				$rootScope.pid = $routeParams.package_id;				

				$scope.sideNavHrefs = {

					"dashboard" : "#/dashboard"+"/"+$rootScope.tid+"/"+$rootScope.pid,
					"map" : "#/map"+"/"+$rootScope.tid+"/"+$rootScope.pid,
					"temperature" : "#/temperature"+"/"+$rootScope.tid+"/"+$rootScope.pid,
					"humidity" : "#/humidity"+"/"+$rootScope.tid+"/"+$rootScope.pid,
					"vibration" : "#/vibration"+"/"+$rootScope.tid+"/"+$rootScope.pid,
					"shock" : "#/shock"+"/"+$rootScope.tid+"/"+$rootScope.pid
				};


			} else {


					$scope.sideNavHrefs = {

						"dashboard" : "#/dashboard",
						"map" : "#/map",
						"temperature" : "#/temperature",
						"humidity" : "#/humidity",
						"vibration" : "#/vibration",
						"shock" : "#/shock"
					};
			}
		};		

		$scope.render();		

		$scope.setServiceType = function(service){

			$rootScope.typeOfService = service;
		}		
		

	  	$scope.urlFunc = function(){	  			  		

	  		//console.log($scope.currentUrl+$scope.selected.id.id+'/'+$scope.selected.package);
	  		$rootScope.tid=$scope.selected.id.id;
	  		$rootScope.pid=$scope.selected.package;	  		

	    	$location.path($rootScope.typeOfService+'/'+$scope.selected.id.id+'/'+$scope.selected.package);
	    	
	    	$scope.render();
	    	

	    }; //end function urlFunc

	    $scope.packageUrl = function(){

	    	if($scope.txtpackage_id!='undefined'){

	    		$rootScope.pid=$scope.txtpackage_id;
	    		$scope.currentUrl=$location.path();
	    		$location.path($scope.currentUrl+'/'+$scope.txtpackage_id);
	    	}  	
	    }; //end function packageUrl	



		/*
			Code below if for updating trucks list and packages list
		*/


		
		$scope.trucks=[]; //holds the object for trucks list and packages list

		$scope.truckList =[];

		var latestTimestamp='';

		selectService.getLatest()
			.then(function(data){
				latestTimestamp = data.timestamp;
		});		

		var packagesList = function(truck_id){					;

			var packages=[];

			selectService.getPackages(truck_id)
			.then(function(data){				

				angular.forEach(data, function(value, key){

					packages.push(value);
					
				});

			});			

			return packages;

		}		

		var truckPromise = selectService.getTrucks();

		truckPromise.then(function(data){

			//getting back a distinct array of trucks like [ ["1"],["2"],["3"] ] 
			//looping over array to form an array of trucks list			

			angular.forEach(data, function(value, key){

				angular.forEach(value, function(value, key){

					var packs=[]; //initialize empty array of packages

					$scope.truckList.push(value);

					var packsPromise = 	selectService.getPackages(value);
					
					packsPromise.then(function(packdata){

						angular.forEach(packdata, function(v,k){

							//push the packages for the mentioned truck_id
							packs.push(v); 
							
						}); //end forEach
						

					}); //end packsPromise.then

					var truckObj = {
						"id": value,
						"packages": packs
					};

					$scope.trucks.push(truckObj);
					
				}); //end inner forEach
				
			}); //end outer forEach

			updater();

		}); //end truckPromise.then

		var updater = function(action){			

			if(action==undefined){

          		var actionBy=0;

        	} else {

          		var actionBy=action;

        	}

			selectService.getLatest(actionBy)
			.then(function(data){				

				if(data.timestamp>latestTimestamp){

					//if a new truck is added
					if($scope.truckList.indexOf(data.truck_id) < 0){						

						var newTruckObj = {

							"id": data.truck_id,
							"packages": packagesList(data.truck_id)

						}

						/*console.log("new truck added");
						console.dir(newTruckObj);*/

						$scope.trucks.push(newTruckObj); //update truck object

						$scope.truckList.push(data.truck_id); //update trucklist

						var packmsg='';

						for(var j=0; j<packagesList.length;j++){

							packmsg+=' '+packagesList[j];

						}

						var mesg = "New truck added with Truck_id: "+data.truck_id + " Packages: " +packmsg + " SensorId: " + data.sensor_id ;

						var options = {
							header: "New Truck Update",
							life: 8000
						};

						
						$.jGrowl(mesg, options);

						latestTimestamp=data.timestamp; //update the latestTimestamp

					} else {

					// else a new package in an existing truck is added					

						var len = $scope.trucks.length;

						for(var i=0; i<len; i++){							

							if($scope.trucks[i].id == data.truck_id){

								$scope.trucks[i].packages = packagesList(data.truck_id);								

								$.jGrowl("Packages Updated in truck_id "+data.truck_id, {life: 8000});
							}
						}

						latestTimestamp=data.timestamp //update to latest timestamp
					} //inner else

				} else {
										
				}



			}); //end then

			
			$timeout(updater, 60000);


		} // end Updater	

		$scope.refresh = function(){

			updater(1);

		}

}]);
		
