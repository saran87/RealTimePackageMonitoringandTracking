
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

					//console.log("Debug");
					//console.dir(data);

					if(data.length<0 || data.length==0){ //check for empty array

						//console.log("Error in truck package");

						errors.isError = true;
						errors.errorMsg = "Empty array returned";

						deferred.resolve([_humidityData, _humidityGraphData, latestTimeStamp, errors]);

					} else {

						var len = data.length;
						//console.log("debug length " + len);
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

					console.log("Error no data fetched " + status);

				});

				return deferred.promise;
		} //end function _getHumidityDataOf()

		var _getLatestHumidityData = function(truck_id, package_id, timestamp){

			var _newhumidityData=[];
			var _newhumidityGraphData=[];

			var errors ={
				"isError": false,
				"errorMsg": ""
			};

			var deferred = $q.defer();

			$http.get(datapath+'humidityEntry/'+truck_id+'/'+package_id+'/'+timestamp)
			.success(function(data){

				if(data.length>0 || data.length!=0){

					var len = data.length;

					latestTimeStamp = data[len-1].timestamp;

					var d;

					for(var i=0; i<len; i++){

						d=data[i];

						_newhumidityData.push(d);
						_newhumidityGraphData.push([d.timestamp,d.value]);

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