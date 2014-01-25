angular.module('myServices')
	.factory('selectService',['$http','$timeout','$q', function($http,$timeout,$q){

		var datapath = constants.ROOT;		
		var _latestTimestamp='';

		var _getTrucks = function(){

			var deferred = $q.defer();

			$http.get(datapath+'alltrucks')
				.success(function(data){	

					_latestTimestamp=data[data.length-1].timestamp;				

					deferred.resolve([data, _latestTimestamp]);

				})
				.error(function(data){

					console.log("ERROR:Nothing here");

				});

				return deferred.promise;
		}

		var _getPackages = function(truck_id){

			var deferred = $q.defer();			

			$http.get(datapath+'packages/'+truck_id)
				.success(function(data){

					var packs=[];					

					angular.forEach(data, function(value, key){						

						packs.push(value.package_id);						
						
					});					
					
					deferred.resolve(packs);
					

				})
				.error(function(data){

					console.log("No data here");

				});

				return deferred.promise;
		}
				

		var _getLatest = function(timestamp, actionBy){

			var path = datapath+'alltrucksAfter/'+timestamp;

			if(actionBy==0){
				var action='bg';
				path=path+'?action='+action
				console.log(path);
			} else {
				var action='usr';
				path=path+'?action='+action
				console.log(path);
			}

			var errors = {

				"isError": false,
				"errorMsg": ""
			}

			var deferred = $q.defer();	

			$http.get(path)
				.success(function(data){

					if(data.length>0){

						_latestTimestamp=data[data.length-1].timestamp;

						deferred.resolve([data, _latestTimestamp]);

					}

				})
				.error(function(data){

					console.log("No data");

				});

				return deferred.promise;
			}

		return {

			getTrucks: _getTrucks,
			getPackages: _getPackages,
			getLatest: _getLatest		
		};

}]);