
angular.module('myServices')
	.factory('temperatureService',['$http', function($http){

		var _temperatureData = [];
		var _gData=[];
		//var plus = 0;
		var max = 0;

		var _getTemperatureData = function(plus,limit){
		
			var dataPath="data/tempdata.json";
			$http.get(dataPath)
				.success(function(data){
					max = data.length;
					var d;
					if((plus>=max)==false){

						for(var i=plus; i<=limit; i++){

					        d=data[i];
					        
					        if(d.temperature){
					          
					          _temperatureData.push(d);
					          _gData.push([d.timestamp,d.temperature.value]);
					         
					        }
				      	}
			      	} else {
			      		console.log("No more values");
			      	}     	

				});	

		}	

		return{

			tdata: _temperatureData,
			gdata: _gData,	
			getT: _getTemperatureData
		};

	}]);