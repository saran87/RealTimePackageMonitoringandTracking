angular.module('myModule')
  .controller('vibrationController',['$scope','$rootScope','$routeParams','vibrationService','dashBoardService','$http','$timeout', '$q',function($scope,$rootScope,$routeParams,vibrationService,dashBoardService,$http,$timeout, $q){

    var latestTimestamp; //holds the latestTimestamp for data received from a package

    $scope.noData=false;
    
    if( ($rootScope.tid!=undefined || $rootScope.tid) && ($rootScope.pid!=undefined || $rootScope.pid) ){

      var truck=$rootScope.tid; //truck_id selected in the Dropdown menu
      var pack=$rootScope.pid; //package_id selected in the Dropdown menu
    } 
    else if($routeParams.truck_id && $routeParams.package_id){

      $rootScope.tid=$routeParams.truck_id;
      $rootScope.pid=$routeParams.package_id;

      var truck=$routeParams.truck_id; //truck_id selected in the Dropdown menu
      var pack=$routeParams.package_id; //package_id selected in the Dropdown menu
    } 
    else {
      console.log("Undefined truck and package");
    }    

    function initData(){

      var deferred=$q.defer();

      dashBoardService.getConfigurationsOf(truck,pack)
      .then(function(data){

        if(!data[2].isError){

          if(data[0].config.vibrationx.timeperiod!=0){
            $scope.refreshRate = data[0].config.vibrationx.timeperiod;
          } else {
            $scope.refreshRate = 60;
          }        

          if(data[0].config.vibrationx.maxthreshold==0){
              $scope.maxThreshold = 0.06; 

            } else {

              $scope.maxThreshold = data[0].config.vibrationx.maxthreshold;

            }          

            if(data[0].is_realtime){

            $rootScope.rt=true;           

            } else {

            $rootScope.rt=false;

            }

            deferred.resolve();
        }        

      });

      return deferred.promise;
    }

    $scope.itemfilter = {};
    $scope.itemfilter.is_above_threshold = "all";

    initData().then(function(){

      vibrationService.getVibrationData(truck,pack)
      .then(function(data){

        if(!data[2].isError){

          latestTimestamp=data[1];

          $scope.ts=latestTimestamp;

          $scope.loaded=true;
          
          $scope.vibrationData=data[0];

          vibrationUpdater();
          
        } else {

          $scope.noData = true;

          $scope.errorMsg = data[2].errorMsg;

          console.log("Error: " + data[2].errorMsg);

        }      

      });   //end then

    });

    
    
    $scope.setSelected = function (indexSelected) {
      
      $scope.indexSelected = indexSelected;
    };

    var vibrationUpdater = function(action){

      if(!$scope.noData && $rootScope.rt){

        if(action==undefined){

          var actionBy=0;
        } else {
          var actionBy=action;
        }

        if(truck!=undefined || pack!=undefined){

          vibrationService.getLatestVibrationData(truck,pack,latestTimestamp,actionBy)
          .then(function(data){

            if(!data[2].isError){

              for(var i=0; i<data[0].length; i++){

                  $scope.vibrationData.push(data[0][i]);

              }

              latestTimestamp=data[1];

              $scope.ts=latestTimestamp;

            } else {

              console.log("no new vibration data");

            }
   
          });
        }

      }

      var timer = $timeout(vibrationUpdater, $scope.refreshRate*1000);

      $scope.$on('$locationChangeStart', function() {
          $timeout.cancel(timer);             
      });      

    } //end vibrationUpdater

    $scope.refresh = function(){

      if($rootScope.rt){

        vibrationUpdater(1);
      }

    }
      
    $scope.downloadCSV = function(){

      vibrationService.getVibrationData(truck,pack)
      .then(function(data){

        if(!data[2].isError){

          var downloadArr=[["timestamp","vibrationx","vibrationy","vibrationz"]];

          for(var i=0; i<data[0].length; i++){

            downloadArr.push([data[0][i].timestamp, data[0][i].value.x, data[0][i].value.y, data[0][i].value.z ]);

          }          

          var finalVal = '';

          for (var i = 0; i < downloadArr.length; i++) {
            var value = downloadArr[i];

            for (var j = 0; j < value.length; j++) {
                var innerValue = value[j];
                var result = (innerValue.toString()).replace(/"/g, '""');
                if (result.search(/("|,|\n)/g) >= 0)
                    result = '"' + result + '"';
                if (j > 0)
                    finalVal += ',';
                finalVal += result;
            }

            finalVal += '\n';
        }

        console.log(finalVal);
          
          var blob = new Blob([finalVal],{
            type: "text/csv;charset=utf-8;",
          });

          //navigator.msSaveBlob(blob, "filename.csv")  
          
        } else {

          console.log("Error: " + data[2].errorMsg);

        }      

      }); 

    }
    

    $scope.psdGraph = function(indexOf,id){

      $scope.psdData=[];

      $scope.isCl=false; 

      $scope.isVib=false;
      $scope.isPsd=true;     

      var psdVals=[];
      var psdValsX=[];
      var psdValsY=[];
      var psdValsZ=[];   

      vibrationService.getPSDData(id)
      .then(function(data){         
      
        for(var i=3; i<513;i++){

            psdVals.push([
              (320/512)*i,
              Math.abs((data.rms[i]^2)/1600)
            ]);

            psdValsX.push([
              (320/512)*i,
              Math.abs((data.x[i]^2)/1600)
            ]);

            psdValsY.push([
              (320/512)*i,
              Math.abs((data.y[i]^2)/1600)
            ]);

            if(i>2){            
              psdValsZ.push([
                (320/512)*i,
                Math.abs((data.z[i]^2)/1600)
              ]); 
            }
            
        } 
        
        $scope.psdData = [
          {
            "key": "RMS Data",
            "values": psdVals
          },
          {
            "key": "PSD X",
            "values": psdValsX
          },
          {
            "key": "PSD Y",
            "values": psdValsY
          },
          {
            "key": "PSD Z",
            "values": psdValsZ
          }
        ];             

      }); //end then

      $("html, body").animate({ scrollTop: 0 }, 200);
    }

    var psdColors = ['#1f77b4', '#ff7f0e', '#2ca02c','#d62728'];

    $scope.psdColorsFunc = function() {
      return function(d, i) {
          return psdColors[i];
        };
    }    

    $scope.vibrationGraph = function(indexOf,id){

      $scope.isCl=false;

      $scope.isPsd=false; 
      $scope.isVib=true;

      var xVals=[], 
          yVals = [],
          zVals=[];

      //call getVibrationGraphData(id) method to get the data for the id passed in
      var graphvals = vibrationService.getVibrationGraphData(id); 

      graphvals.then(function(data){

        var ts = data.timestamp;
        var currX = data.x.split(" ");
        var currY = data.y.split(" ");
        var currZ = data.z.split(" ");

        for(var i=0; i<currX.length; i++){

          if(i>0){
            ts = ts + (0.125*1000); //incrementing timestamp values
          } 

          xVals.push([
            ts,
            parseFloat(parseFloat(currX[i]).toFixed(2))
            ]);
          yVals.push([
            ts,
            parseFloat(parseFloat(currY[i]).toFixed(2))
            ]);
          zVals.push([
            ts,
            parseFloat(parseFloat(currZ[i]).toFixed(2))
            ]);         

        }

        $scope.exampleData = [

          {
            "key" : "Vibration x",
            "values" : xVals

          },

          {
            "key" : "Vibration y",
            "values" : yVals

          },

          {
            "key" : "Vibration z",
            "values" : zVals

          }

        ];          

      }); //end then

      $("html, body").animate({ scrollTop: 0 }, 200);

    } //end vibrationGraph    
    

    $scope.xAxisTickFormatFunction = function(){
          return function(d){
              return d3.time.format('%H:%M')(new Date(d));
            }
    }

    $scope.xAxisTickFormatFunction2 = function(){
          return function(d){
              return d3.time.format('%H:%M')(new Date(d));
            }
    }

    $scope.yAxisTickFormatFunction = function(){
          return function(d){
              return d3.format('.02f')(d);
            }
    }

    /*$scope.toolTipData = function(){
      return function(key, x, y, e, graph) {
          return  '<h3> '+ key + '</h3>' +
                '<p>' +  y + ' at ' + d3.time.format('%x')(new Date(d)) + '</p>'
      }
    }*/
  
    
    var colors = ['#1f77b4', '#ff7f0e', '#2ca02c'];

    $scope.colorDefault = function() {
      return function(d, i) {
          return colors[i];
        };
    }


   }])
    .filter('hiddenFilter', function(){
      return function(vibrationData, show_or_hide, attribute){
        var shownItems = [];
            if (show_or_hide === 'all'){

              shownItems=vibrationData;

              return shownItems;

            } 
            angular.forEach(vibrationData, function (item) {
                if (show_or_hide === 'shown') {
                    if (item[attribute] === true) shownItems.push(item);
                }
            });
            return shownItems;
      }
    });
