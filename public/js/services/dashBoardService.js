angular.module('myServices')
	.factory('dashBoardService', ['$http','$q', function($http, $q){

		var datapath = constants.ROOT;		
		var latestTimeStamp = '';

		var _getConfigurationsOf = function(truck_id, package_id){

			var errors ={
				"isError": false,
				"errorMsg": ""
			};

			var deferred = $q.defer();

			$http.get(datapath+'configs/'+truck_id+'/'+package_id)
				.success(function(data){

					if(data.length<0 || data.length==0 || !data.config){ //check for empty array

						errors.isError = true;
						errors.errorMsg = "Empty array returned";

						deferred.resolve([data,latestTimeStamp, errors]);

					} else {

						deferred.resolve([data,latestTimeStamp, errors]);

					}

				})
				.error(function(data,status){

					console.log("Error: " + status );

				});

				return deferred.promise;

		}

		return {
			getConfigurationsOf: _getConfigurationsOf
		};		

	}]);