
angular.module('myServices')
	.factory('temperatureService',['$http','$q', function($http,$q){

		var datapath = constants.ROOT;		

		var _temperatureData = [];
		var _temperatureGraphData=[];

		var latestTimeStamp = '';
			

		var _getTemperatureData = function(){

			/*var deferred = $q.defer();
			
			$http.get(dataPath)
				.success(function(data){

					var max = data.length;
					var d;

					_firstTemperatureTimeStamp=data[0].timestamp;
					

					for(var i=0; i<max; i++){
						
				      d=data[i];
			          
			          _temperatureData.push(d);
			          _temperatureGraphData.push([d.timestamp,d.value]);		         
				        
			      	}

			      	deferred.resolve([_temperatureData, _temperatureGraphData]);

				})
				.error(function(data, status){

					console.log(data+" "+status);

				});	

				return deferred.promise;*/

		}		

		var _getTemperatureDataOf = function(truck_id, package_id){
			
			var errors ={
				"isError": false,
				"errorMsg": ""
			};

			var deferred = $q.defer();

			$http.get(datapath+'temperature/'+truck_id+'/'+package_id)
				.success(function(data){

					//console.log("Debug");
					//console.dir(data);

					if(data.length<0 || data.length==0){ //check for empty array

						//console.log("Error in truck package");

						errors.isError = true;
						errors.errorMsg = "Empty array returned";

						deferred.resolve([_temperatureData, _temperatureGraphData, latestTimeStamp, errors]);

					} else {

						var len = data.length;
						//console.log("debug length " + len);
						var d;

						_temperatureData = [];
						_temperatureGraphData=[];
						
						latestTimeStamp=data[len-1].timestamp;

						for(var i=0; i<len; i++){

							//console.log("debug data[i]");
			      			//console.dir(data[i]);
							
						    d=data[i];

						    //console.log("debug d");
			      			//console.dir(d);

			      			if(d.value>=0){
						        _temperatureData.push(d);
						        _temperatureGraphData.push([d.timestamp,d.value]);		         
					    	}
					        
			      		}//end for

			      		/*console.log("debug _temperatureData");
			      		console.dir(_temperatureData);*/
			      		console.log("debug _temperatureGraphData");
			      		console.dir(_temperatureGraphData);

			      		deferred.resolve([_temperatureData, _temperatureGraphData, latestTimeStamp, errors]);
					
					} // end else

				})
				.error(function(data, status){

					console.log("Error no data fetched " + Status);

				});

				return deferred.promise;
		}

		var _getLatestTemperatureData = function(truck_id, package_id, timestamp){

			var _newtemperatureData=[];
			var _newtemperatureGraphData=[];

			var errors ={
				"isError": false,
				"errorMsg": ""
			};

			var deferred = $q.defer();

			$http.get(datapath+'temperatureEntry/'+truck_id+'/'+package_id+'/'+timestamp)
			.success(function(data){

				if(data.length>0 || data.length!=0){

					var len = data.length;

					latestTimeStamp = data[len-1].timestamp;

					var d;

					for(var i=0; i<len; i++){

						d=data[i];

						_newtemperatureData.push(d);
						_newtemperatureGraphData.push([d.timestamp,d.value]);

					}

				deferred.resolve([_newtemperatureData, _newtemperatureGraphData, latestTimeStamp, errors]);



				} else {

					errors.isError = true;
					errors.errorMsg = "Empty update";
					console.log("No new temperature data");

					deferred.resolve([_newtemperatureData, _newtemperatureGraphData, latestTimeStamp, errors]);
				}

			})
			.error(function(status,data){

				console.log("No latest data");

			});

			return deferred.promise;

		}

		return{

			tdata: _temperatureData,
			temperatureGraphData: _temperatureGraphData,			
			getT: _getTemperatureData,
			getTemperatureDataOf: _getTemperatureDataOf,
			getLatestTemperatureData: _getLatestTemperatureData
			
		};

	}]);