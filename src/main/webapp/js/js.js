var map;
var trips = {};
var tripids = [];
var username = 23452;

function initialize() {
	var mapOptions = {
		center : new google.maps.LatLng(-33.8688, 151.2195),
		zoom : 13
	};
	map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);

	// Try get location
	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(function(position) {
			var pos = new google.maps.LatLng(position.coords.latitude,
					position.coords.longitude);

			var infowindow = new google.maps.InfoWindow({
				map : map,
				position : pos,
				content : 'Im now here.'
			});

			map.setCenter(pos);
		}, function() {
			handleNoGeolocation(true);
		});
	} else {
		// Browser doesn't support Geolocation
		handleNoGeolocation(false);
	}

	var input = /** @type {HTMLInputElement} */
	(document.getElementById('pac-input'));

	var types = document.getElementById('type-selector');
	map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);
	map.controls[google.maps.ControlPosition.TOP_LEFT].push(types);

	var autocomplete = new google.maps.places.Autocomplete(input);
	autocomplete.bindTo('bounds', map);

	var infowindow = new google.maps.InfoWindow();
	var marker = new google.maps.Marker({
		map : map,
		anchorPoint : new google.maps.Point(0, -29)
	});

	google.maps.event
			.addListener(
					autocomplete,
					'place_changed',
					function() {
						infowindow.close();
						marker.setVisible(false);
						var place = autocomplete.getPlace();
						if (!place.geometry) {
							return;
						}

						// If the place has a geometry, then present it on a
						// map.
						if (place.geometry.viewport) {
							map.fitBounds(place.geometry.viewport);
						} else {
							map.setCenter(place.geometry.location);
							map.setZoom(17); // Why 17? Because it looks
							// good.
						}
						marker.setIcon(/** @type {google.maps.Icon} */
						({
							url : place.icon,
							size : new google.maps.Size(71, 71),
							origin : new google.maps.Point(0, 0),
							anchor : new google.maps.Point(17, 34),
							scaledSize : new google.maps.Size(35, 35)
						}));
						marker.setPosition(place.geometry.location);
						marker.setVisible(true);

						var address = '';
						if (place.address_components) {
							address = [
									(place.address_components[0]
											&& place.address_components[0].short_name || ''),
									(place.address_components[1]
											&& place.address_components[1].short_name || ''),
									(place.address_components[2]
											&& place.address_components[2].short_name || '') ]
									.join(' ');
						}

						button = '<div><button type="button" class="btn btn-success" onclick="addLocation('
								+ place.geometry.location.lat()
								+ ','
								+ place.geometry.location.lng()
								+ ')" >Add to My Trip</button></div>';
						infowindow.setContent('<div><strong>' + place.name
								+ '</strong><br>' + address + button);
						infowindow.open(map, marker);
					});

	// Sets a listener on a radio button to change the filter type on Places
	// Autocomplete.
	function setupClickListener(id, types) {
		var radioButton = document.getElementById(id);
		google.maps.event.addDomListener(radioButton, 'click', function() {
			autocomplete.setTypes(types);
		});
	}

	setupClickListener('changetype-all', []);
	setupClickListener('changetype-address', [ 'address' ]);
	setupClickListener('changetype-establishment', [ 'establishment' ]);
	setupClickListener('changetype-geocode', [ 'geocode' ]);

}

function draw2(id) {

	var places = trips[id];

	var flightPlanCoordinates = [];
	for (var i = 0; i < places.length; i++) {
		p = places[i];
		flightPlanCoordinates.push(new google.maps.LatLng(p["lat"], p["lng"]));
	}

	var flightPath = new google.maps.Polyline({
		path : flightPlanCoordinates,
		geodesic : true,
		strokeColor : '#FF0000',
		strokeOpacity : 1.0,
		strokeWeight : 2
	});

	flightPath.setMap(map);
}
function draw(lat, lng, photo) {

	var url = photo;
	var contentString = '<img style="max-width: 40%; max-height:40%;" src="'
			+ url + '" class="img-circle">'
	var infowindow = new google.maps.InfoWindow({
		content : contentString
	});

	var myLatlng = new google.maps.LatLng(lat, lng);
	var marker = new google.maps.Marker({
		position : myLatlng,
		map : map,
		title : 'My Travel Place!'
	});
	google.maps.event.addListener(marker, 'click', function() {
		infowindow.open(map, marker);
	});
	marker.setMap(map);
}
function addLocation(lat, lng) {
	$("#latitude").val(lat);
	$("#longitude").val(lng);
	addPlace();
}

function addTrip(tripid, trip, lat, lng, start, end, photo) {
	if (trip == undefined)
		trip = [];
	trip.push({
		"id" : tripid,
		"lat" : lat,
		"lng" : lng,
		"start" : start,
		"end" : end,
		"photo" : photo
	});
	var data = {
		"username" : username,
		"id" : $("#travelid").val(),
		"trip" : trip
	};
	$.ajax({
		type : "POST",
		url : "/context/trip/add",
		data : JSON.stringify(data),
	}).done(function(msg) {
		console.log("Data Saved: " + msg);
		loadTrips();
	});
}

function getTrip(id) {
	var json = {
		"username" : username,
		"id" : id
	};
	$.ajax({
		type : "GET",
		url : "/context/trip/get/" + JSON.stringify(json),
	}).done(function(msg) {
		console.log("Data Saved: " + msg);
		trips[id] = msg;
	});
}
function getAddTrip(id) {
	var json = {
		"username" : username,
		"id" : id
	};
	$.ajax({
		type : "GET",
		url : "/context/trip/get/" + JSON.stringify(json),
	}).done(function(msg) {
		console.log("Data Saved: " + msg);
		trips[id] = $.parseJSON(msg);
		addPlaces(id);
	});
}

function addPlace() {
	var pid = parseInt(Math.random() * 10000000);
	if (isNaN($("#travelid").val())) {
		alert("Travel Id Can only be Numbers");
		return;
	}
	addTrip(pid, trips[$("#travelid").val()], $("#latitude").val(), $(
			"#longitude").val(), $("#startdate").val(), $("#enddate").val(), $(
			"#photourl").val());
}

function loadTrips() {
	$
			.ajax({
				type : "GET",
				url : "/context/trip/getall/" + username
			})
			.done(
					function(msg) {
						ids = $.parseJSON(msg)
						tripids = ids
						for (var i = 0; i < ids.length; i++) {
							$("#accordion").remove();
							var contain = '<div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true"> </div>';
							$("#left").append(contain);
							getAddTrip(ids[i]);
						}
					});
}

function addPlaces(id) {
	var places = trips[id];
	var content = '  <div class="panel panel-default"> \
		    <div class="panel-heading" role="tab" id="headingThree">  \
		      <h4 class="panel-title" onclick="draw2('
			+ id
			+ ')"> \
		        <a class="collapsed" data-toggle="collapse" data-parent="#accordion" href="#tripid'
			+ id
			+ '" aria-expanded="false" aria-controls="collapseThree"> \
		          Travel Id : '
			+ id
			+ '\
		        </a> \
			<a href="/share.html?place='
			+ encodeURI(JSON.stringify(places))
			+ '">Share My Travel</a>\
		      </h4> \
		    </div> \
		    <div id="tripid'
			+ id
			+ '" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingThree"> \
		      <div class="panel-body"> ';
	for (var i = 0; i < places.length; i++) {
		p = places[i];

		var photo = String(p["photo"]);
		if (photo == undefined)
			photo = ""
		var l = photo.indexOf("serve");
		if (l >= 0)
			photo = photo.substring(l);

		var pp = '<div class="bg-success" onclick="draw(' + p["lat"] + ','
				+ p["lng"] + ',\'' + photo + '\')" ><p> Place ' + (i + 1)
				+ ' </p><p> Start Date: ' + p["start"] + 'End Date: '
				+ p["end"]
				+ '</p><img style="max-width: 80%; max-height:80%;" src="'
				+ p["photo"] + '" class="img-circle"></div>';
		content += pp;
	}
	content += ' \
					      </div> \
					    </div> \
					  </div>';
	$("#accordion").append(content);
}