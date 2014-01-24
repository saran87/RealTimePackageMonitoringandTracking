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

					//No data returned for truck_id and package_id
					//Handle it and send the errors object
					if(data.length<0 || data.length==0){ //check for empty array

						//console.log("Error in truck package");

						errors.isError = true;
						errors.errorMsg = "No Temperature data available for package " + package_id + " in truck " + truck_id;

						deferred.resolve([_temperatureData, _temperatureGraphData, latestTimeStamp, errors]);

					} else {

						var len = data.length;
						//console.log("debug length " + len);
						var d;

						_temperatureData = [];
						_temperatureGraphData=[];
						
						latestTimeStamp=data[len-1].timestamp;

						for(var i=0; i<len; i++){							
							
						    d=data[i];
						    
			      			if(d.value>=0){
						        _temperatureData.push(d);
						        _temperatureGraphData.push([d.timestamp,parseFloat(d.value).toFixed(2)]);		         
					    	}
					        
			      		}//end for

			      		//_temperatureData=_temperatureData.reverse();
			      		
			      		deferred.resolve([_temperatureData, _temperatureGraphData, latestTimeStamp, errors]);
					
					} // end else

				})
				.error(function(data, status){

					errors.isError = true;

					errors.errorMsg = "Could not complete request. Status: " + status;

					deferred.resolve([ [], [], '', errors]);

				});

				return deferred.promise;
		}

		var _getLatestTemperatureData = function(truck_id, package_id, timestamp,actionBy){

			var path=datapath+'temperatureEntry/'+truck_id+'/'+package_id+'/'+timestamp;

			if(actionBy==0){
				var action='bg';
				path=path+'?action='+action
				console.log(path);
			} else {
				var action='usr';
				path=path+'?action='+action
				console.log(path);
			}

			var _newtemperatureData=[];
			var _newtemperatureGraphData=[];

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

						if(d.value>=0){
							_newtemperatureData.push(d);
							_newtemperatureGraphData.push([d.timestamp,d.value]);
						}

					}

					//_newtemperatureData=_newtemperatureData.reverse();

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