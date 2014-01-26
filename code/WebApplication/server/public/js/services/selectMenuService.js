angular.module('myServices')
	.factory('selectService',['$http','$timeout','$q', function($http,$timeout,$q){

		var datapath = constants.ROOT;		
		var _latestTimestamp='';

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

		var _getTrucks = function(){

			var deferred = $q.defer();

			$http.get(datapath+'alltrucks')
				.success(function(data){

					if(data.length>0){

						_latestTimestamp=data[data.length-1].timestamp;		
								
						deferred.resolve([data, _latestTimestamp]);
					} else {

						deferred.resolve([ [], '']);
					}					

				})
				.error(function(data, status){

					deferred.resolve([ [], '']);
					console.log("ERROR: " + status);

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

			var deferred = $q.defer();	

			$http.get(path)
				.success(function(data){

					if(data.length>0){

						_latestTimestamp=data[data.length-1].timestamp;

						console.log(_latestTimestamp);

						deferred.resolve([data, _latestTimestamp]);

					} else {

						deferred.resolve([ [], '']);
					}

				})
				.error(function(data, status){

					console.log("ERROR: " + status);
					deferred.resolve([ [], '']);

				});

				return deferred.promise;
			}

		return {

			getTrucks: _getTrucks,
			getPackages: _getPackages,
			getLatest: _getLatest		
		};

}]);