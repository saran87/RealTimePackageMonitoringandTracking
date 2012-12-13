 // Create an object containing LatLng, population.
	var map;
	var polyLine;
	var image = 'view/img/package.png';
   function startTracking(){
   
		var site = new Site();
		
		site.getPackageDetail(handleFirstResponse);
   
   }
	function startTrackingPackage(){
	
		new Site().getLatestPackage(handleResponse);	
	}

  function	handleFirstResponse(response){
	
	if(response.data){
	
		for ( var index in response.data){
				var data = response.data[index];
				var temperature = 0;
				var vibration = 0;
				if(data.temperature){ 
					temperature = data.temperature.value+ " F";
				}
				if(data.vibration){
					vibration = data.vibration.value + " g";
				}
				
				// create a new javascript Date object based on the timestamp
				// multiplied by 1000 so that the argument is in milliseconds, not seconds
				var date = new Date(data.timestamp);
				// hours part from the timestamp
				var hours = date.getHours();
				// minutes part from the timestamp
				var minutes = date.getMinutes();
				// seconds part from the timestamp
				var seconds = date.getSeconds();

				// will display time in 10:30:23 format
				var formattedTime = hours + ':' + minutes + ':' + seconds;
				var d = new Date();
				d.setTime(data.timestamp);
				var message = "<div> <label class='label'> Package Id:</label>" + data.packageId +"</div><div> <label class='label'> Time: </label>" + d + "</div> <div> <label class='label'> Temperature: </label>" + temperature + "</div> <div> <label class='label'>Vibration :</label>" + vibration + "</div>";
				if(data.location){
					var lat = data.location.latitude;
					var lan = data.location.longitude + index;
					var latlng = new google.maps.LatLng(lat,lan);
					if(index == 0)
						mapPoints(latlng,"start",message)
					else
						addLatLng(latlng,message);
					//mapPoints(latlng,data.truckId,message);
				}
			}
			//get every sec
			setTimeout(startTrackingPackage,10000);
		}
	else{
		alert("No data recieved");
		//retry after 10 sec
		setTimeout(startTrackingPackage,100000);
	}
  }
  
  function	handleResponse(response){
	
	if(response.data){
	
		for ( var index in response.data){
				var data = response.data[index];
				var temperature = 0;
				var vibration = 0;
				if(data.temperature){ 
					temperature = data.temperature.value+ " F";
				}
				if(data.vibration){
					vibration = data.vibration.value + " g";
				}
				
				// create a new javascript Date object based on the timestamp
				// multiplied by 1000 so that the argument is in milliseconds, not seconds
				var date = new Date(data.timestamp);
				// hours part from the timestamp
				var hours = date.getHours();
				// minutes part from the timestamp
				var minutes = date.getMinutes();
				// seconds part from the timestamp
				var seconds = date.getSeconds();

				// will display time in 10:30:23 format
				var formattedTime = hours + ':' + minutes + ':' + seconds;
				var d = new Date();
				d.setTime(data.timestamp);
				var message = "<div> <label class='label'> Package Id:</label>" + data.packageId +"</div><div> <label class='label'> Time: </label>" + d + "</div> <div> <label class='label'> Temperature: </label>" + temperature + "</div> <div> <label class='label'>Vibration :</label>" + vibration + "</div>";
				if(data.location){
					var lat = data.location.latitude;
					var lan = data.location.longitude + index;
					var latlng = new google.maps.LatLng(lat,lan);
					addLatLng(latlng,message);
				}
			}
			//get every sec
			setTimeout(startTrackingPackage,10000);
		}
	else{
		alert("No data recieved");
		//retry after 10 sec
		setTimeout(startTrackingPackage,100000);
	}
  }
  /*
  function initializeMap() {
		if (navigator.geolocation) {
			navigator.geolocation.getCurrentPosition(CreateMap, error);
		} else {
			error('not supported');
		}
    }
	
	function CreateMap(position){
	
		 var latlng = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
		mapPoints(latlng,"your here","you are here");
	}*/
	
	function mapPoints(latlng,title,message){

		var myOptions = {
				zoom: 18,
				center: latlng,
				mapTypeControl: true,
				navigationControlOptions: {style: google.maps.NavigationControlStyle.SMALL},
				mapTypeId: google.maps.MapTypeId.SATELLITE,
				maxZoom:20
			  };
		   

		map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
		  
		var marker = new google.maps.Marker({
			 map:map,
			draggable:true,
			animation: google.maps.Animation.DROP,
			position: latlng,
			title:title,
			icon:image
		  });
			
		var infowindow = new google.maps.InfoWindow({
              content: message
            });
	
		   google.maps.event.addListener(marker, 'click', function() {
						infowindow.open(map,marker);
			});
			
		polyLine = new google.maps.Polyline({
				  path: [latlng],
				  strokeColor:"#FF0000",
				  strokeOpacity:1.0,
				  zIndex:2
		  });
	
		  polyLine.setMap(map);
	
	}
	
	function error(msg) {
	  var s = document.querySelector('#map_canvas');
	  s.innerHTML = typeof msg == 'string' ? msg : "failed";
	  s.className = 'fail';	  
	  // console.log(arguments);
	}
	
	
      /**
       * Add the latitude and longitude to polyline and a marker and info window
       * @param {google.maps.LatLng(} latitude , longitude
       */
      function addLatLng(latLng,message) {

        var path = polyLine.getPath();

        // Because path is an MVCArray, we can simply append a new coordinate
        // and it will automatically appear
        path.push(latLng);
		console.log(latLng);
        // Add a new marker at the new plotted point on the polyline.
        var marker = new google.maps.Marker({
          position:latLng,
          title: '#' + path.getLength(),
          map: map,
		  icon:image
        });
		//Add an info window to the position
		 var infowindow = new google.maps.InfoWindow({
              content: message
            });
		google.maps.event.addListener(marker, 'click', function() {
						infowindow.open(map,marker);
			});
		 
      }
	  
	 