
angular.module('myServices')
	.factory('temperatureService',['$http', function($http){

		var dataPath="data/tempdata.json";
		var _temperatureData = [];
		var _gData=[];		
		var max = 0;
		var _firstTemperatureTimeStamp;
		var _latestTemperatureTimeStamp;

		var _getTemperatureData = function(){		
			
			$http.get(dataPath)
				.success(function(data){
					max = data.length;
					var d;

					_firstTemperatureTimeStamp=data[0].timestamp;
					

					for(var i=0; i<max; i++){
						
				        d=data[i];
				        
				        if(d.temperature){
				          
				          _temperatureData.push(d);
				          _gData.push([d.timestamp,d.temperature.value]);
				         
				        }
			      	}
			      	    	

				});	

		}

		var _getLatestTemperatureEntry = function(){

			$http.get(dataPath)
				.success(function(data){										

					_latestTemperatureTimeStamp=data[0].timestamp;

				});	

				return _latestTemperatureTimeStamp;

		}

		return{

			tdata: _temperatureData,
			gdata: _gData,
			firstTemperatureTimeStamp: _firstTemperatureTimeStamp,
			getLatestTemperatureEntry: _getLatestTemperatureEntry,
			getT: _getTemperatureData,
			getMax: max
		};

	}]);