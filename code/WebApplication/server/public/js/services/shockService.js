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

					if(data.length<0 || data.length==0){

						errors.isError = true;
						
						errors.errorMsg = "Empty array returned";

						deferred.resolve([_shockData, latestTimestamp, errors]);

					} else {

						var max = data.length;
						var d;
						_shockData=[];
						
						latestTimestamp=data[max-1].timestamp;

						for(var i=0; i<max; i++){

							if(data[i].value.x && data[i].value.y && data[i].value.z){

						    	var highestVals=data[i];

						    	var tmpx=getHighVal(highestVals.value.x.split(" "));
						    	var tmpy=getHighVal(highestVals.value.y.split(" "));
						    	var tmpz=getHighVal(highestVals.value.z.split(" "));

						    	var maxVal=Math.max(tmpx[0],tmpy[0],tmpz[0]);
						    	var maxIndex=Math.max(tmpx[1],tmpy[1],tmpz[1]);
						    	var t = ((maxIndex * 1.25) + 70 * (1.25))/1000;
						    	var height = 4.9 * Math.pow(t,2);

						    	//console.log(tmpx[0], tmpy[0],tmpz[0]);

						    	//console.log(maxVal + "---" + maxIndex + "----" + t + "----" + height);

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

						        _shockData.push(highestVals);
					        }				        
				      	}

				      	console.dir(_shockData);

				      	deferred.resolve([_shockData, latestTimestamp, errors]);
					}			
			      	

				})
				.error(function(data, status){

					console.log(data+" "+status);

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

		return{

			shockData: _shockData,
			getShockData: _getShockData,
			getShockGraphData: _getShockGraphData

		};
		

	}]);