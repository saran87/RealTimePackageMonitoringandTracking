var dataPath='../../../RealTimePackageMonitoringandTracking/rfid/index.php?s=package&type=json&';

angular.module('myServices')
	.factory('humidityService',['$http',function($http){

		var _humidityData = [];
		
		var _getHumidityData = function(){
			var dataPath = "data/data.json";	
			$http.get(dataPath)
				.success(function(data){

					var d;
					for(var i=0; i<=100; i++){
						d=data.data[i];

						if(d.humidity){
							_humidityData.push(d);
						}

					}

				});

		}

		return {

			humidityData: _humidityData,
			getHumidityData: _getHumidityData
		};


	}]);