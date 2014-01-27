angular.module('myServices')
	.factory('vibrationService',['$http','$q', function($http,$q){		

		var datapath=constants.ROOT;

		var _vibrationData=[];		

		var latestTimeStamp='';
				

		var getHighVal = function (inArr){

			return Math.max.apply(Math, inArr);
		}

		function maxVibrationGValue(value){

             var valueArr = value.split(" ");

             var total = 0;

             var max = Number.MIN_VALUE;
             for(var index in valueArr){

                         if(Math.abs(valueArr[index]) > Math.abs(max)){

                                 max = valueArr[index];
                         }
             }

             return (max);

     	}


		var _getVibrationData = function(truck_id,package_id){

			var errors = {

				"isError": false,
				"errorMsg": ""
			}

			var deferred = $q.defer();
			
			$http.get(datapath+'vibration'+'/'+truck_id+'/'+package_id)
				.success(function(data){

					//No data returned for truck_id and package_id
					//Handle it and send the errors object
					if(data.length<0 || data.length==0){

						errors.isError = true;
						errors.errorMsg = "No Vibration data available for package " + package_id + " in truck " + truck_id;

						deferred.resolve([_vibrationData, latestTimeStamp, errors]);

					} else {										

						var max = data.length;
						var d;
						_vibrationData=[];						

						latestTimeStamp=data[max-1].timestamp;


						for(var i=0; i<max; i++){

							if(data[i].value.x && data[i].value.y && data[i].value.z){

						    	var	highestVals=data[i];						    	

						        highestVals.value.x = maxVibrationGValue(highestVals.value.x);
						        highestVals.value.y = maxVibrationGValue(highestVals.value.y);
						        highestVals.value.z = maxVibrationGValue(highestVals.value.z);

						        _vibrationData.push(highestVals);

					    	}
				      	}

				      	deferred.resolve([_vibrationData, latestTimeStamp, errors]);
			     	}
			      	

				})
				.error(function(data, status){

					//500 error - Bad url request
					//handle the error and send the errors object in resolve
					
					errors.isError = true;

					errors.errorMsg = "Could not complete request. Status: " + status;

					deferred.resolve([ [], '', errors]);

				});	

				return deferred.promise;					

		}

		var _getLatestVibrationData = function(truck_id,package_id,timestamp,actionBy){

			var path=datapath+'vibrationEntry/'+truck_id+'/'+package_id+'/'+timestamp;

			if(actionBy==0){
				var action='bg';
				path=path+'?action='+action
				console.log(path);
			} else {
				var action='usr';
				path=path+'?action='+action
				console.log(path);
			}

			var _newvibrationData=[];			

			var errors ={
				"isError": false,
				"errorMsg": ""
			};

			var deferred = $q.defer();

			$http.get(path)
			.success(function(data){

				if(data.length>0 || data.length!=0){

					var len = data.length;

					latestTimeStamp = data[len-1].timestamp;

					var d;

					for(var i=0; i<len; i++){

						if(data[i].value.x && data[i].value.y && data[i].value.z){

							var newhighestVals=data[i];

							newhighestVals.value.x = maxVibrationGValue(highestVals.value.x);
						    newhighestVals.value.y = maxVibrationGValue(highestVals.value.y);
						    newhighestVals.value.z = maxVibrationGValue(highestVals.value.z);

						    _newvibrationData.push(newhighestVals);
						}

					}

					deferred.resolve([_newvibrationData, latestTimeStamp, errors]);


				} else {					

					errors.isError = true;
					errors.errorMsg = "Empty update i.e. no new vibration data available";
					
					console.log("No new Vibration data in service");

					deferred.resolve([_newvibrationData,latestTimeStamp, errors]);
				}

			})
			.error(function(data,status){

				//500 bad URL request
				errors.isError = true;
				
				errors.errorMsg = "Invalid request. Status " + status;				

				deferred.resolve([ [],'', errors]);	

			});

			return deferred.promise;

		}


		var _getVibrationGraphData = function(id){

			var deferred = $q.defer();

			$http.get(datapath+'vibrationgraph/'+id)
				.success(function(data){

					var graphDataObj = {
						"id": data._id,
			        	"timestamp": data.timestamp,
						"x" : [],
						"y" : [],
						"z" : []
				    }

				    graphDataObj.x=data.value.x;
				    graphDataObj.y=data.value.y;
				    graphDataObj.z=data.value.z;				    

				    deferred.resolve(graphDataObj);

				})
				.error(function(status, data){

					console.log("Error: No graph data");

				});

				return deferred.promise;

		}

		var _getPSDData = function(id){

			var deferred = $q.defer();

			$http.get(datapath+'psd/'+id)
				.success(function(data){

				//console.dir(data);

					deferred.resolve(data);		

				})
				.error(function(data){

					console.log("ERROR: NO PSD DATA");

				});

			return deferred.promise;		

		}

		return{
					
			getVibrationGraphData: _getVibrationGraphData,						
			getVibrationData: _getVibrationData,			
			getPSDData: _getPSDData,
			getLatestVibrationData: _getLatestVibrationData	

		};

	}]);