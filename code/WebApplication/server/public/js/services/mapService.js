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
						errors.errorMsg = "No location data(GPS) available for package " + package_id + " in truck " + truck_id;

						deferred.resolve([data, latestTimeStamp, errors]);

					} else {

						var len = data.length;
						
						latestTimeStamp=data[len-1].timestamp;						

			      		deferred.resolve([data, latestTimeStamp, errors]);
					
					} // end else

				})
				.error(function(data, status){

					errors.isError = true;

					errors.errorMsg = "Could not complete request. Status: " + status;

					deferred.resolve([data, '', errors]);

				});

				return deferred.promise;
		}

		var _getLatestCoordinatesof = function(truck_id, package_id, timestamp, actionBy){

			var path=datapath+'mapsEntry/'+truck_id+'/'+package_id+'/'+timestamp;

			if(actionBy==0){
				var action='bg';
				path=path+'?action='+action
				console.log(path);
			} else {
				var action='usr';
				path=path+'?action='+action
				console.log(path);
			}			

			var errors ={
				"isError": false,
				"errorMsg": ""
			};

			var deferred = $q.defer();

			$http.get(path)
				.success(function(data){

					if(data.length>0 || data.length!=0){

						var len = data.length;

						latestTimeStamp=data[len-1].timestamp;						

			      		deferred.resolve([data, latestTimeStamp, errors]);

					} else {

						errors.isError = true;
						errors.errorMsg = "Empty update";					
					}

				})
				.error(function(data,status){

					errors.isError = true;
					errors.errorMsg = "Empty update";

				});

				return deferred.promise;
		}

		var _getAddress = function(lat,lng){

			var deferred=$q.defer();

			var geocoder;

			geocoder = new google.maps.Geocoder();

			var latlng = new google.maps.LatLng(lat, lng);          

	          geocoder.geocode({'latLng': latlng}, function(results, status) {
	            
	            if (status == google.maps.GeocoderStatus.OK) {
	              if (results[1]) {
	                results[1].formatted_address;
	                deferred.resolve(results[1].formatted_address);
	              } else {
	                
	                deferred.resolve("Error");
	              }
	            } else {
	              deferred.resolve("Error in maps");
	            }
	          });

	          return deferred.promise

		}


		return{

			getCordinatesOf: _getCordinatesOf,
			getLatestCoordinatesof: _getLatestCoordinatesof,
			getAddress: _getAddress	
			
		};

	}]);