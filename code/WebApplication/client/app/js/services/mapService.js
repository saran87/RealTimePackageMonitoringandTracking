
angular.module('myServices')
	.factory('mapService',['$http','$q', function($http,$q){



		var datapath = constants.ROOT;		

		var _mapData = [];
		

		var latestTimeStamp = '';

		var _getCordinatesOf = function(truck_id, package_id){
			console.log(truck_id + " " + package_id)
			var errors ={
				"isError": false,
				"errorMsg": ""
			};

			var deferred = $q.defer();

			$http.get(datapath+'maps/'+truck_id+'/'+package_id)
				.success(function(data){					

					if(data.length<0 || data.length==0){ //check for empty array

						//console.log("Error in truck package");

						errors.isError = true;
						errors.errorMsg = "Empty array returned";

						deferred.resolve([data, errors]);

					} else {

						var len = data.length;
						//console.log("debug length " + len);
						var locationObj={};

						/*_mapData = [];						

						for(var i=0; i<len; i++){

							locationObj.lat=data[i][1];
							locationObj.lng=data[i][0];

							_mapData.push(locationObj);

			      		}//end for*/
			      		

			      		deferred.resolve([data, errors]);
					
					} // end else

				})
				.error(function(data, status){

					console.log("Error no data fetched " + status);

				});

				return deferred.promise;
		}		


		return{

			getCordinatesOf: _getCordinatesOf,
		
			
		};

	}]);