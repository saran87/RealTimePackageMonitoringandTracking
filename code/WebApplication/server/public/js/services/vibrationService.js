angular.module('myServices')
	.factory('vibrationService',['$http','$q', function($http,$q){		

		var datapath=constants.ROOT;

		var _vibrationData=[];		

		var latestTimeStamp='';
				

		var getHighVal = function (inArr){

			return Math.max.apply(Math, inArr);
		}


		var _getVibrationData = function(truck_id,package_id){

			var errors = {

				"isError": false,
				"errorMsg": ""
			}

			var deferred = $q.defer();
			
			$http.get(datapath+'vibration'+'/'+truck_id+'/'+package_id)
				.success(function(data){

					if(data.length<0 || data.length==0){

						errors.isError = true;
						errors.errorMsg = "Empty array returned";

						deferred.resolve([_vibrationData, latestTimeStamp, errors]);

					} else {										

						var max = data.length;
						var d;
						_vibrationData=[];						

						latestTimeStamp=data[max-1].timestamp;


						for(var i=0; i<max; i++){

							if(data[i].value.x && data[i].value.y && data[i].value.z){

						    	var	highestVals=data[i];						    	

						        highestVals.value.x = getHighVal(highestVals.value.x.split(" "));
						        highestVals.value.y = getHighVal(highestVals.value.y.split(" "));
						        highestVals.value.z = getHighVal(highestVals.value.z.split(" "));

						        _vibrationData.push(highestVals);

					    	}
				      	}

				      	deferred.resolve([_vibrationData, latestTimeStamp, errors]);
			     	}
			      	

				})
				.error(function(data, status){

					console.log(data+" "+status);

				});	

				return deferred.promise;					

		}

		var _getLatestVibrationData = function(truck_id,package_id,timestamp){

			var _newvibrationData=[];			

			var errors ={
				"isError": false,
				"errorMsg": ""
			};

			var deferred = $q.defer();

			$http.get(datapath+'vibrationEntry/'+truck_id+'/'+package_id+'/'+timestamp)
			.success(function(data){

				if(data.length>0 || data.length!=0){

					var len = data.length;

					latestTimeStamp = data[len-1].timestamp;

					var d;

					for(var i=0; i<len; i++){

						if(data[i].value.x && data[i].value.y && data[i].value.z){

							var newhighestVals=data[i];

							newhighestVals.value.x = getHighVal(newhighestVals.value.x.split(" "));
						    newhighestVals.value.y = getHighVal(newhighestVals.value.y.split(" "));
						    newhighestVals.value.z = getHighVal(newhighestVals.value.z.split(" "));

						    _newvibrationData.push(newhighestVals);
						}

					}

					deferred.resolve([_newvibrationData, latestTimeStamp, errors]);


				} else {

					errors.isError = true;
					errors.errorMsg = "Empty update";
					console.log("No new vibration data in service");

					deferred.resolve([_newvibrationData,latestTimeStamp, errors]);
				}

			})
			.error(function(data,status){

				console.log("no latest data from vibration " + status );

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