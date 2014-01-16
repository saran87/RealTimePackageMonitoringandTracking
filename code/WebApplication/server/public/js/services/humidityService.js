
angular.module('myServices')
	.factory('humidityService',['$http','$q',function($http,$q){		

		var datapath = constants.ROOT;

		var _humidityData = [];
		var _humidityGraphData = [];	

		var latestTimeStamp = '';			
		
		var _getHumidityData = function(){			

			/*var deferred = $q.defer();

			$http.get(dataPath)
				.success(function(data){
					max=data.length;
					var d;

					for(var i=0; i<max; i++){
						d=data[i];

												
						_humidityData.push(d);
						
						_humidityGraphData.push([d.timestamp,d.value]);

						deferred.resolve([_humidityData, _humidityGraphData]);
						

					}
				})
				.error(function(data){

					console.log("Error: No humidityData");
				});

				return deferred.promise;*/
		}


		var _getHumidityDataOf = function(truck_id, package_id){			

			var errors ={
				"isError": false,
				"errorMsg": ""
			};

			var deferred = $q.defer();

			$http.get(datapath+'humidity/'+truck_id+'/'+package_id)
				.success(function(data){					

					//No data returned for truck_id and package_id
					//Handle it and send the errors object
					if(data.length<0 || data.length==0){ //check for empty array						

						errors.isError = true;
						errors.errorMsg = "No Humidity data available for package " + package_id + " in truck " + truck_id;

						deferred.resolve([_humidityData, _humidityGraphData, latestTimeStamp, errors]);

					} else {

						var len = data.length;
						
						var d;

						_humidityData = [];
						_humidityGraphData=[];
						
						latestTimeStamp=data[len-1].timestamp;

						for(var i=0; i<len; i++){

							//console.log("debug data[i]");
			      			//console.dir(data[i]);
							
						    d=data[i];

						    //console.log("debug d");
			      			//console.dir(d);

			      			if(d.value>=0 && d.value<=100){

						        _humidityData.push(d);
						        _humidityGraphData.push([d.timestamp,d.value]);
					    	}
					        
			      		}//end for

			      		/*console.log("debug _humidityData");
			      		console.dir(_humidityData);
			      		console.log("debug _humidityGraphData");
			      		console.dir(_humidityGraphData);*/

			      		deferred.resolve([_humidityData, _humidityGraphData, latestTimeStamp, errors]);
					
					} // end else

				})
				.error(function(data, status){

					//500 error - Bad url request
					//handle the error and send the errors object in resolve
					
					errors.isError = true;

					errors.errorMsg = "Could not complete request. Status: " + status;

					deferred.resolve([ [], [], '', errors]);

				});

				return deferred.promise;
		} //end function _getHumidityDataOf()

		var _getLatestHumidityData = function(truck_id, package_id, timestamp,actionBy){

			var path = datapath+'humidityEntry/'+truck_id+'/'+package_id+'/'+timestamp;

			if(actionBy==0){
				var action='bg';
				path=path+'?action='+action
				console.log(path);
			} else {
				var action='usr';
				path=path+'?action='+action
				console.log(path);
			}

			var _newhumidityData=[];
			var _newhumidityGraphData=[];

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

						d=data[i];

						if(d.value>=0 && d.value<=100){
							_newhumidityData.push(d);
							_newhumidityGraphData.push([d.timestamp,d.value]);
						}

					}

				deferred.resolve([_newhumidityData, _newhumidityGraphData, latestTimeStamp, errors]);



				} else {

					errors.isError = true;
					errors.errorMsg = "Empty update";
					console.log("No new humidity data");

					deferred.resolve([_newhumidityData, _newhumidityGraphData, latestTimeStamp, errors]);
				}

			})
			.error(function(status,data){

				console.log("No latest data");

			});

			return deferred.promise;

		}

		return {

			humidityData: _humidityData,
			humidityGraphData: _humidityGraphData,
			getH: _getHumidityData,
			getHumidityDataOf: _getHumidityDataOf,
			getLatestHumidityData: _getLatestHumidityData
			
		};


	}]);