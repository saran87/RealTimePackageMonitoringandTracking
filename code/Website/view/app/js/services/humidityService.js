
angular.module('myServices')
	.factory('humidityService',['$http',function($http){

		var _humidityData = [];
		var _hData = [];		
		
		var _getHumidityData = function(){			

			var dataPath = "data/humiditydata.json";	

			$http.get(dataPath)
				.success(function(data){
					max=data.length;
					var d;

					for(var i=0; i<max; i++){
						d=data[i];

						if(d.humidity){
							
							_humidityData.push(d);
							
							_hData.push([d.timestamp,d.humidity.value]);
						}

					}				


				});

		}

		return {

			humidityData: _humidityData,
			getH: _getHumidityData
			
		};


	}]);