<!DOCTYPE html>
<html>
<head>
<title>My Places</title>
<meta name="viewport" content="initial-scale=1.0, user-scalable=no">
<meta charset="utf-8">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap-theme.min.css">
<link rel="stylesheet" href="css/index.css">
<script src="https://code.jquery.com/jquery-2.1.1.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js"></script>
<script
	src="https://maps.googleapis.com/maps/api/js?v=3.exp&libraries=places"></script>
<script src="js/js.js"></script>

<script>google.maps.event.addDomListener(window, 'load', initialize);
	loadTrips();</script>

</head>
<body>
	<div class="row" style="height: 100%">
		<div class="col-xs-6 col-md-4" style="height: 100%" id="left">
			<div>
				<form class="form-horizontal" role="form">
					<div class="form-group">
						<label class="col-sm-2 control-label">Longitude</label>
						<div class="col-sm-10">
							<input type="Longitude" class="form-control" id="longitude">
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">Latitude</label>
						<div class="col-sm-10">
							<input type="Latitude" class="form-control" id="latitude">
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">Start Date</label>
						<div class="col-sm-10">
							<input type="Latitude" class="form-control" id="startdate">
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">End Date</label>
						<div class="col-sm-10">
							<input type="Latitude" class="form-control" id="enddate">
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">Travel Id</label>
						<div class="col-sm-10">
							<input type="Latitude" class="form-control" id="travelid">
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-2 col-sm-10">
							<button onclick="addTrips()" class="btn btn-default">Add
								to My travel</button>
						</div>
					</div>
				</form>
			</div>

			<input id="pac-input" class="controls" type="text"
				placeholder="Enter a location">
			<div id="type-selector" class="controls">
				<input type="radio" name="type" id="changetype-all"
					checked="checked"> <label for="changetype-all">All</label>
				<input type="radio" name="type" id="changetype-establishment">
				<label for="changetype-establishment">Establishments</label> <input
					type="radio" name="type" id="changetype-address"> <label
					for="changetype-address">Addresses</label> <input type="radio"
					name="type" id="changetype-geocode"> <label
					for="changetype-geocode">Geocodes</label>
			</div>
		</div>
		<div class=“col-xs-12 col-sm-6 col-md-8“ style="height: 100%">
			<div id="map-canvas"></div>
		</div>
	</div>
</body>
</html>