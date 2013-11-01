
angular.module('myServices')
	.factory('humidityService',['$http',function($http){

		var _humidityData = [];
		
		var _getHumidityData = function(){
<<<<<<< HEAD
			
=======
			var dataPath = "data/data.json";	
>>>>>>> fb25a946287a920e13da76e420460eaf4e06e9c4
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