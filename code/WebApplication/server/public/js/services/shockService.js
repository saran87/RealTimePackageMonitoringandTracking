angular.module('myServices')
	.factory('shockService',['$http','$q', function($http,$q){

		var datapath=constants.ROOT;

		var _shockData=[];		
		var _shockGraphData=[];

		var latestTimestamp='';

		var getHighVal = function (inArr){

			var highv=(Math.max.apply(Math, inArr)).toString();			
			
			return [parseFloat(Math.max.apply(Math, inArr)),inArr.indexOf(highv)];

		}

		var _getShockData = function(truck_id,package_id){

			var errors = {
				"isError": false,
				"errorMsg": ""
			}

			var deferred=$q.defer();
			
			$http.get(datapath+'shock'+'/'+truck_id+'/'+package_id)
				.success(function(data){

					//No data returned for truck_id and package_id
					//Handle it and send the errors object
					if(data.length<0 || data.length==0){

						errors.isError = true;
						
						errors.errorMsg = "No shock data available for package " + package_id + " in truck " + truck_id;

						deferred.resolve([_shockData, latestTimestamp, errors]);

					} else {

						var max = data.length;
						var d;
						_shockData=[];

						//console.dir(data);
						latestTimestamp=data[max-1].timestamp;

						for(var i=0; i<max; i++){

							if(data[i].value.x && data[i].value.y && data[i].value.z){

						    	var highestVals=data[i];

						    	var tmpx=getHighVal(highestVals.value.x.split(" "));
						    	var tmpy=getHighVal(highestVals.value.y.split(" "));
						    	var tmpz=getHighVal(highestVals.value.z.split(" "));

						    	var maxVal=Math.max(tmpx[0],tmpy[0],tmpz[0]);
						    	var maxIndex=Math.max(tmpx[1],tmpy[1],tmpz[1]);
						    	
						    	//var t = ((maxIndex * 0.000625) + 70 * (0.000625))/1000;

						    	var t = (maxIndex * 0.000625) + (70 * 0.000625);

						    	var height = 4.9 * Math.pow(t,2);

						        highestVals.value.x = tmpx[0];
						        highestVals.value.xindex = tmpx[1];
						        
						        highestVals.value.y = tmpy[0];
						        highestVals.value.yindex = tmpy[1];
						        
						        highestVals.value.z = tmpz[0];
						        highestVals.value.zindex = tmpz[1];

						        highestVals.value.maxvalue = maxVal;
						        highestVals.value.maxindex = maxIndex;
						        highestVals.value.t = t;
						        highestVals.value.dropheight = height.toFixed(2);

						        if(data[i].is_instantaneous){

						        	highestVals.is_instantaneous=true;
						        } else {

						        	highestVals.is_instantaneous=false;
						        }

						        _shockData.push(highestVals);
					        }				        
				      	}				      	

				      	deferred.resolve([_shockData, latestTimestamp, errors]);
					}			
			      	

				})
				.error(function(data, status){

					//500 error - Bad url request
					//handle the error and send the errors object in resolve
					
					errors.isError = true;

					errors.errorMsg = "Could not complete request. Status: " + status;

					deferred.resolve([ [], '', errors]);
				});

				return deferred.promise;
		}

		var _getShockGraphData = function(id){

			var deferred = $q.defer();

			$http.get(datapath+'shockgraph/'+id)
				.success(function(data){					

					var graphDataObj = {
						"id": data._id,
			        	"timestamp": data.timestamp,
						"x" : [],
						"y" : [],
						"z" : []
				    }

				    graphDataObj.x=data.value.x;
				    graphDataObj.y=data.value.y;
				    graphDataObj.z=data.value.z;

				    deferred.resolve(graphDataObj);

				})
				.error(function(status, data){

					console.log("Error: No graph data");

				});

				return deferred.promise;

		}


		var _getLatestShockData = function(truck_id,package_id,timestamp,actionBy){

			var path=datapath+'shockEntry/'+truck_id+'/'+package_id+'/'+timestamp;

			if(actionBy==0){
				var action='bg';
				path=path+'?action='+action
				console.log(path);
			} else {
				var action='usr';
				path=path+'?action='+action
				console.log(path);
			}

			var _newShockData=[];			

			var errors ={
				"isError": false,
				"errorMsg": ""
			};

			var deferred = $q.defer();

			$http.get(path)
			.success(function(data){

				if(data.length>0 || data.length!=0){

					var len = data.length;

					latestTimestamp = data[len-1].timestamp;

					var d;

					for(var i=0; i<len; i++){

						if(data[i].value.x && data[i].value.y && data[i].value.z){

							var newhighestVals=data[i];

							var tmpx=getHighVal(newhighestVals.value.x.split(" "));
					    	var tmpy=getHighVal(newhighestVals.value.y.split(" "));
					    	var tmpz=getHighVal(newhighestVals.value.z.split(" "));

					    	var maxVal=Math.max(tmpx[0],tmpy[0],tmpz[0]);
					    	var maxIndex=Math.max(tmpx[1],tmpy[1],tmpz[1]);

					    	var t = (maxIndex * 0.000625) + (70 * 0.000625);
					    	var height = 4.9 * Math.pow(t,2);

					    	newhighestVals.value.x = tmpx[0];
					        newhighestVals.value.xindex = tmpx[1];
					        
					        newhighestVals.value.y = tmpy[0];
					        newhighestVals.value.yindex = tmpy[1];
					        
					        newhighestVals.value.z = tmpz[0];
					        newhighestVals.value.zindex = tmpz[1];

					        newhighestVals.value.maxvalue = maxVal;
					        newhighestVals.value.maxindex = maxIndex;
					        newhighestVals.value.t = t;
					        newhighestVals.value.dropheight = height.toFixed(2);

					        if(data[i].is_instantaneous){

					        	newhighestVals.is_instantaneous=true;
					        } else {

					        	newhighestVals.is_instantaneous=false;
					        }

							_newShockData.push(newhighestVals);

						    
						}

					}

					deferred.resolve([_newShockData, latestTimestamp, errors]);


				} else {

					errors.isError = true;
					errors.errorMsg = "Empty update i.e. no new data";
					
					console.log("No new shock data in service");

					deferred.resolve([_newShockData,latestTimestamp, errors]);
				}

			})
			.error(function(data,status){

				//500 bad URL request
				errors.isError = true;
				
				errors.errorMsg = "Invalid request. Status " + status;				

				deferred.resolve([ [],'', errors]);			

			});

			return deferred.promise;

		}


		return{

			shockData: _shockData,
			getShockData: _getShockData,
			getShockGraphData: _getShockGraphData,
			getLatestShockData: _getLatestShockData

		};
		

}]);