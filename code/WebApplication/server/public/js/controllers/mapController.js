angular.module('myModule')
	.controller('mapController', ['$scope','mapService','dashBoardService','$rootScope','$routeParams','$http','$timeout','$q',function($scope,mapService,dashBoardService,$rootScope,$routeParams,$http, $timeout,$q){

    var latestTimestamp='';

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

      var deferred = $q.defer();

      dashBoardService.getConfigurationsOf(truck,pack)
      .then(function(data){

        if(!data[2].isError){

          if(data[0].config.temperature.timeperiod!=0 && data[0].config.humidity.timeperiod!=0 &&data[0].config.vibrationx.timeperiod!=0){              

            $scope.refreshRate=Math.min.apply( Math, [data[0].config.temperature.timeperiod,data[0].config.humidity.timeperiod,data[0].config.vibrationx.timeperiod] );

          } else {

            $scope.refreshRate=60;

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

    var directionsDisplay;
    var directionsService = new google.maps.DirectionsService();
    var map;
    var geocoder;

    directionsDisplay = new google.maps.DirectionsRenderer();
     
    geocoder = new google.maps.Geocoder();

      /**
     * Add the latitude and longitude to polyline and a marker and info window
     * @param {google.maps.LatLng(} latitude , longitude
     */
    function addLatLng(latLng,message,type) {

      var iconType = 'default';

      if(type){
        iconType = type;
      }
       
        // Add a new marker at the new plotted point on the polyline.
        var marker = new google.maps.Marker({
          position:latLng,
          title: 'Click for location information',
          map: map,
          animation: google.maps.Animation.DROP,
          icon: "../img/"+iconType+"-pin.png"
        });

        //Add an info window to the position
        var infowindow = new google.maps.InfoWindow({});

        google.maps.event.addListener(marker, 'click', function() {
            infowindow.open(map,marker);
            
            getAddress(latLng,function(addr){

              message+="</h6><strong>Address</strong>: " + addr + "</h6>";

              infowindow.setContent(message);
            
            });
                        
        });
     
      }

      function getAddress(latlng,callback) {

        var addr = '';          

        geocoder.geocode({'latLng': latlng}, function(results, status) {
          //console.log(results[1].formatted_address);
          if (status == google.maps.GeocoderStatus.OK) {
            if (results[1]) {

              addr=results[1].formatted_address;                                

              callback(addr);
              
            } else {
              
              callback('No results found');
            }
          } else {
            
            callback('Geocoder failed due to: ' + status);
          }
        });          

      }

      function initialize(initLoc) {
        //directionsDisplay = new google.maps.DirectionsRenderer();
        var initLocation = initLoc;
        //geocoder = new google.maps.Geocoder();
        var mapOptions = {
          zoom: 8,
          center: initLoc
        }
        map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
        directionsDisplay.setMap(map);
      }

      function markerContent(inObj){
        var contentStr = '<strong>Truck ID</strong>: ' + inObj.truck_id + ' <strong>Package ID</strong>: ' + inObj.package_id;

        if(inObj.temperature){
          if(inObj.is_above_threshold){

            contentStr+="<h6 class='text-danger'>Temperature <small>(Above Threshold)</small></h6>";

          } else {

            contentStr+="<h6 class='text-info'>Temperature</h6>";

          }

          contentStr+=
            "<div>" + 
              "<strong>Temperature</strong> of <strong>" + inObj.temperature.value + " F</strong> at <strong>" + new Date(inObj.timestamp) + "</strong></div>";
        
        } else if (inObj.humidity){

          if(inObj.is_above_threshold){

            contentStr+="<h6 class='text-danger'>Humidity <small>(Above Threshold)</small></h6>";

          } else {

            contentStr+="<h6 class='text-info'>Humidity</h6>";

          }            

          contentStr+=
            "<div>" + 
              "<strong>Humidity</strong> of <strong>" + inObj.humidity.value + " %RH</strong> at <strong>" + new Date(inObj.timestamp) + "</strong></div>";

        } else if(inObj.vibration){

          if(inObj.is_above_threshold){

            contentStr+="<h6 class='text-danger'>Vibration <small>(Above Threshold)</small></h6>";

          } else {

            contentStr+="<h6 class='text-info'>Vibration</h6>";

          }

          contentStr+=
            "<div>" + 
              "<strong>Vibration</strong> occured at <strong>" + new Date(inObj.timestamp) + "</strong></div>";

        } else if(inObj.shock){            

          contentStr+="<h6 class='text-danger'>Shock</h6>";

          contentStr+=
            "<div>" + 
              "<strong>Shock</strong> experienced at <strong>" + new Date(inObj.timestamp) + "</strong></div>";

        } else {

          contentStr+="<strong>No data available</strong>";

        }

        return contentStr;

      }

      function getType(inObj){

        if(inObj.temperature){

          return 'temperature';

        }
        else if(inObj.humidity){

          return 'humidity';

        }
        else if(inObj.vibration){

          return 'vibration';

        } 
        else if(inObj.shock){

          return 'shock';

        }


      }

      initData().then(function(){

        mapService.getCordinatesOf(truck,pack)
        .then(function(data){ 

          if(!data[2].isError){            

            $scope.noData=false;
            
            latestTimestamp=data[1];

            $scope.ts=latestTimestamp;

            $scope.loaded=true;            

            var waypts=[];

            var len = data[0].length;

            if(len>2){

              var diff=Math.ceil(len/8);

              for(var i=diff; i<=len-diff;){

                var wayptObj = {
                  location: new google.maps.LatLng(data[0][i].loc.lat, data[0][i].loc.lng),
                  stopover: true
                };

                waypts.push(wayptObj);

                i=i+diff;
              }
            }

            initialize(new google.maps.LatLng(data[0][0].loc.lat, data[0][0].loc.lng));

            var start=new google.maps.LatLng(data[0][0].loc.lat, data[0][0].loc.lng);

            var end=new google.maps.LatLng(data[0][data[0].length-1].loc.lat,data[0][data[0].length-1].loc.lng);         
            
            var request = {
              origin: start,
              destination: end,
              waypoints: waypts,
              optimizeWaypoints: true,
              travelMode: google.maps.TravelMode.DRIVING
            };

            directionsService.route(request, function(response, status) {
                if (status == google.maps.DirectionsStatus.OK) {
                  directionsDisplay.setDirections(response);
                  var route = response.routes[0];                  
                }

            });


            for(var i=0; i<data[0].length; i++){

              var latLng=new google.maps.LatLng(data[0][i].loc.lat, data[0][i].loc.lng);

              var msg = markerContent(data[0][i]);

              var type=getType(data[0][i]);

              addLatLng(latLng, msg,type);

              if(i==data.length-1){

                mapUpdater();           
              }
            }            

          } else {

            $scope.noData = true;

            $scope.errorMsg = data[2].errorMsg;

            console.log("no data " + data[2].errorMsg);

          }

        });

      });
      


  var mapUpdater = function(action){

    if(action==undefined){

      var actionBy=0;
    } else {
      var actionBy=action;
    }

    if(truck!=undefined && pack!=undefined){

      mapService.getLatestCoordinatesof(truck, pack, latestTimestamp, actionBy)
      .then(function(data){

        if(!data[2].isError){

          latestTimestamp=data[1];
          $scope.ts=latestTimestamp;          

            for(var i=0; i<data[0].length; i++){

              var latLng=new google.maps.LatLng(data[0][i].loc.lat, data[0][i].loc.lng);

              var msg = markerContent(data[0][i]);

              var type=getType(data[0][i]);

              addLatLng(latLng, msg, type);

            }          
        
        } else {

          console.log("No map update");

        }

      });

    }

    var timer = $timeout(mapUpdater, $scope.refreshRate * 1000);

    $scope.$on('$locationChangeStart', function() {
        $timeout.cancel(timer);             
    });

  }

  $scope.refresh=function(){

    mapUpdater(1);
  }


}]);