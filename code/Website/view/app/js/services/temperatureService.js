//var dataPath='../../../RealTimePackageMonitoringandTracking/rfid/index.php?s=package&type=json&';

angular.module('myServices')
	.factory('temperatureService',['$http', function($http){

		var _temperatureData = [];
		var _gData=[];

		var _getTemperatureData = function(){
		
			var dataPath = "data/data.json";	
			$http.get(dataPath)
				.success(function(data){					
					var d;

					for(var i=0; i<=100; i++){
				        d=data.data[i];
				        
				        if(d.temperature){
				          
				          _temperatureData.push(d);
				          _gData.push([d.timestamp,d.temperature.value]);
				         
				        }
			      	}

				});	

		}	

		return{

			tdata: _temperatureData,
			gdata: _gData,	
			getT: _getTemperatureData
		};

	}]);