angular.module('myModule')
	.controller('mapController', ['$scope','mapService','$rootScope','$routeParams','$http',function($scope,mapService,$rootScope,$routeParams,$http){

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

        var directionsDisplay;
        var directionsService = new google.maps.DirectionsService();
        var map;

        var waypts=[];

        function initialize() {
          directionsDisplay = new google.maps.DirectionsRenderer();
          var initLocation = new google.maps.LatLng(43.08498749, -77.63056059);
          var mapOptions = {
            zoom: 12,
            center: initLocation
          }
          map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
          directionsDisplay.setMap(map);
        }

        initialize();

        mapService.getCordinatesOf(truck,pack)
        .then(function(data){
            
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
            
            var start=new google.maps.LatLng(data[0][0].loc.lng, data[0][0].loc.lat);

            var end=new google.maps.LatLng(data[0][data[0].length-1].loc.lng,data[0][data[0].length-1].loc.lat);        
            

            for(var i=1; i<data[0].length-2; i++){                

                waypts.push({
                    location: new google.maps.LatLng(data[0][i].loc.lng, data[0][i].loc.lat),
                    stopover:true});
            }
            
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
           
        });

        

}])