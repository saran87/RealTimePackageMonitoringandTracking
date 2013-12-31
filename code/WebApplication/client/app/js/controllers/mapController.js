angular.module('myModule')
	.controller('mapController', ['$scope','mapService','$rootScope','$http',function($scope,mapService,$rootScope,$http){

		var truck=$rootScope.tid; //truck_id selected in the Dropdown menu
    	var pack=$rootScope.pid; //package_id selected in the Dropdown me  

        angular.extend($scope, {
        center: {
            latitude: 0, // initial map center latitude
            longitude: 0, // initial map center longitude
        },
        markers: [], // an array of markers,
        zoom: 8, // the zoom level
    });  			

        /*angular.extend($scope, {
            
            paths: {
                p1: {
                    color: '#008000',
                    weight: 8,
                    latlngs: [
                        { lat: 43.103069493, lng: -77.631136500914 },
                        { lat: 43.10306949366, lng: -77.631136500914 },
                        { lat: 43.085411630549, lng: -77.680327426728 },
                        { lat: 43.085213365225, lng: -77.680295907749 },
                        { lat: 43.085213365225, lng: -77.680295907749 },
                        { lat: 43.085213365225, lng: 77.680295907749 },
                        { lat: 43.07683425, lng: -77.64773069 }
            
                    ],
                }
            },
            markers: {
                first: {
                    lat: 43.103069493, 
                    lng: -77.631136500914,
                    message: "This",
                    focus: true,
                    draggable: false
                }
            },
            defaults: {
                scrollWheelZoom: false
            }
            });*/


	  	
	  }])