angular.module('myModule')
	.controller('mapController', ['$scope','mapService','dashBoardService','$rootScope','$routeParams','$http','$timeout',function($scope,mapService,dashBoardService,$rootScope,$routeParams,$http, $timeout){

    var latestTimestamp='';

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

        dashBoardService.getConfigurationsOf(truck,pack)
        .then(function(data){

          if(!data[2].isError){

            if(data[0].config.temperature.timeperiod!=0 && data[0].config.humidity.timeperiod!=0 &&data[0].config.vibrationx.timeperiod!=0){

              console.log("in ifff");
              console.log();

              $scope.refreshRate=Math.min.apply( Math, [data[0].config.temperature.timeperiod,data[0].config.humidity.timeperiod,data[0].config.vibrationx.timeperiod] );

            } else {

              $scope.refreshRate=60;

            }

              if(data[0].is_realtime){

                $rootScope.rt=true;           

              } else {

                $rootScope.rt=false;

              }
          }

        });

        var directionsDisplay;
        var directionsService = new google.maps.DirectionsService();
        var map;  

        /**
       * Add the latitude and longitude to polyline and a marker and info window
       * @param {google.maps.LatLng(} latitude , longitude
       */
      function addLatLng(latLng,message) {
         
          // Add a new marker at the new plotted point on the polyline.
          var marker = new google.maps.Marker({
            position:latLng,
            title: '#',
            map: map        
          });
      
          //Add an info window to the position
          var infowindow = new google.maps.InfoWindow({
            content: message
          });

          google.maps.event.addListener(marker, 'click', function() {
              infowindow.open(map,marker);
          });
       
        }

        $scope.getAddress=function(lat,lng) {          

          var addr = '';

          console.log(lat +" " + lng);

          /*$http.get("http://maps.googleapis.com/maps/api/geocode/json?latlng="+lat+","+lng+"&sensor=false")
            .success(function(data){

              console.dir(data);

              addr=data.formatted_address;

            })
            .error(function(data){

              addr="Could not trace location";

            });

            return addr;*/

        }

        function initialize(initLoc) {
          directionsDisplay = new google.maps.DirectionsRenderer();
          var initLocation = initLoc;
          var mapOptions = {
            zoom: 5,
            center: initLoc
          }
          map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
          directionsDisplay.setMap(map);
        }

        function markerContent(inObj){
          var contentStr = 'Truck ID: ' + inObj.truck_id + ' Package ID: ' + inObj.package_id;

          if(inObj.temperature){
            if(inObj.is_above_threshold){

              contentStr+="<h6 class='text-danger'>Temperature</h6>";

            } else {

              contentStr+="<h6 class='text-info'>Temperature</h6>";

            }

            contentStr+=
              "<div>" + 
                "Temperature of " + inObj.temperature.value + " F at " + new Date(inObj.timestamp) + "<div>Address: " + inObj.loc.lat + "," + inObj.loc.lng  + " <button ng-click='getAddress("+inObj.loc.lat+","+inObj.loc.lng+")' class='btn btn-sm'>Addr</button>" + "</div>" + 
              "</div>";            
          
          } else if (inObj.humidity){

            if(inObj.is_above_threshold){

              contentStr+="<h6 class='text-danger'>Humidity</h6>";

            } else {

              contentStr+="<h6 class='text-info'>Humidity</h6>";

            }

            contentStr+=
              "<div>" + 
                "Temperature of " + inObj.humidity.value + " %RH at " + new Date(inObj.timestamp) + "<div>Address: " + inObj.inObj.loc.lat + "," + inObj.loc.lng  + " <button ng-click='getAddress("+inObj.loc.lat+","+inObj.loc.lng+")' class='btn btn-sm'>Addr</button>" + "</div>" + 
              "</div>";

          } else if(inObj.vibration){

            if(inObj.is_above_threshold){

              contentStr+="<h6 class='text-danger'>Vibration</h6>";

            } else {

              contentStr+="<h6 class='text-info'>Vibration</h6>";

            }

            contentStr+=
              "<div>" + 
                "Vibration occured at" + new Date(inObj.timestamp) + " at " + inObj.loc.lat + " " + inObj.loc.lng + 
              "</div>";

          } else if(inObj.shock){

            if(inObj.is_above_threshold){

              contentStr+="<h6 class='text-danger'>Shock</h6>";

            } else {

              contentStr+="<h6 class='text-info'>Shock</h6>";

            }

            contentStr+=
              "<div>" + 
                "Shock experienced at " + new Date(inObj.timestamp) + " at " + inObj.loc.lat + " " + inObj.loc.lng + 
              "</div>";

          } else {

            contentStr+="No data available";

          }

          return contentStr;

        }       

        mapService.getCordinatesOf(truck,pack)
        .then(function(data){ 

          if(!data[2].isError){

            latestTimestamp=data[1];
            
            var waypts=[
                /*{
                    location: new google.maps.LatLng(data[0][10].loc.lng, data[0][10].loc.lat),
                    stopover: true
                },
                {
                    location: new google.maps.LatLng(data[0][120].loc.lng, data[0][120].loc.lat),
                    stopover: true
                }*/

            ];            


            if(truck=="NO_ID" && pack=="NO_ID_realtime"){

              initialize(new google.maps.LatLng(data[0][0].loc.lng, data[0][0].loc.lat));

              var start=new google.maps.LatLng(data[0][0].loc.lng, data[0][0].loc.lat);

              var end=new google.maps.LatLng(data[0][data[0].length-1].loc.lng,data[0][data[0].length-1].loc.lat);

              var infoWindowArray=[];        
              
              var markerArray=[];              
              
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


              for(var i=1; i<data[0].length-2; i++){

                var latLng=new google.maps.LatLng(data[0][i].loc.lng, data[0][i].loc.lat);

                var msg = markerContent(data[0][i]);

                addLatLng(latLng, msg);

              }

            } else {

              initialize(new google.maps.LatLng(data[0][0].loc.lat, data[0][0].loc.lng));

              var start=new google.maps.LatLng(data[0][0].loc.lat, data[0][0].loc.lng);

              var end=new google.maps.LatLng(data[0][data[0].length-1].loc.lat,data[0][data[0].length-1].loc.lng);

              var infoWindowArray=[];        
              
              var markerArray=[];              
              
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


              for(var i=1; i<data[0].length-2; i++){

                var latLng=new google.maps.LatLng(data[0][i].loc.lat, data[0][i].loc.lng);

                var msg = markerContent(data[0][i]);

                addLatLng(latLng, msg);

              }

            }//end else

          } else {

            console.log("no data " + data[2].errorMsg);
          }



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

          if(truck=="NO_ID" && pack=="NO_ID_realtime"){

            for(var i=0; i<data[0].length; i++){

              var latLng=new google.maps.LatLng(data[0][i].loc.lng, data[0][i].loc.lat);

              var msg = markerContent(data[0][i]);

              addLatLng(latLng, msg);

            }

          } else {

            for(var i=0; i<data[0].length; i++){

              var latLng=new google.maps.LatLng(data[0][i].loc.lat, data[0][i].loc.lng);

              var msg = markerContent(data[0][i]);

              addLatLng(latLng, msg);

            }

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

  $scope.refresh=function (){

    mapUpdater(1);

  }


}]);