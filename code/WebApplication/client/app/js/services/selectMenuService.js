angular.module('myServices')
	.factory('selectService',['$http','$timeout','$q', function($http,$timeout,$q){

		var datapath = constants.ROOT;		
		var _latestTimestamp='';

		var _getTrucks = function(){

			var deferred = $q.defer();

			$http.get(datapath+'alltrucks')
				.success(function(data){

					deferred.resolve(data);

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

		/*var _pol = function(){

			$http.get('http://localhost/master/RealTimePackageMonitoringandTracking/code/api/public/index.php/alltrucks')
				.success(function(data){

					console.log("timeout");

					console.dir(data);

				})
				.error(function(data){

					console.log("ERROR:Nothing here");

				});

			$timeout(_pol, 5000);

		}

		_pol();*/

		var _getLatest = function(){

			var errors = {

				"isError": false,
				"errorMsg": ""
			}

			var deferred = $q.defer();	

			$http.get(datapath+'latestEntry')
				.success(function(data){

					deferred.resolve(data);

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