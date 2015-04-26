module.exports = {
	create: function (meas){
	this.lat = meas.latitude;
	this.longitude = meas.longitude;
	this.date = "";
	this.waterQuality = 0.0;
	this.measurements = meas;

	this.calculateQuality = function () {
		var result = 0.0;
		/*
		 * if(!this.measurements.containsKey("ph")) { result += 8 * 0.11; }
		 */
	// console.dir(result);
		var keys = Object.keys(this.measurements);
		if (keys.indexOf("Dis- solved oxygen, mg/L,") < 0) {
			result += 5 * 0.17;
		}
		if (keys.indexOf("Temper- ature, water, deg C,") < 0) {
			result += 10 * 0.11;
		}
		for (var keyIdx in keys) {
			var measurement = keys[keyIdx];

			/*
			 * if(measurement.getKey().equals("ph")) {
			 * phValue = var.parsevar(measurement.getValue()); if(phValue
			 * <= 2 && phValue >= 12) { result += 0; } result +=
			 * phQuality(phValue) * 0.11; }
			 */

			if (measurement === "Dis- solved oxygen, mg/L," && !!this.measurements[measurement]) {
				var str = this.measurements[measurement];
				result += this.oxygenQuality(parseFloat(str.replace("P", ""))) * 0.17;
			}

			if (measurement === "Temper- ature, water, deg C," && !!this.measurements[measurement]) {
				var str = this.measurements[measurement];
				result += this.tempQuality(parseFloat(str.replace("P", ""))) * 0.11;
			}
		}
		if (! result) {
		console.log("result:" + result);			
		}
		return result;
	};

	this.phQuality = function (x) {
		var result;
		if (x <= 0 || x >= 12)
			result = 0;
		else if (x > 0 && x <= 4) {
			result = x / 4.0;
		} else if (x > 4 && x <= 7.5) {
			result = x + 0.7;
		} else {
			result = x - 0.7;
		}

		return result;
	};

	this.oxygenQuality = function (x) {
		var index;
		if (x < 10) {
			index = x;
		} else {
			index = 10 - (x - 10);
		}
		console.dir(index);
		return index;
	};

	this.tempQuality = function (x) {
		var index;
		if (x < 0) {
			index = (-0.3) * x;
		} else {
			index = 0.3 * x;
		}
		return index;
	};

	this.getLat = function () {
		return this.lat;
	};

	this.setLat = function (lat) {
		this.lat = lat;
	};

	this.getLongitude = function () {
		return longitude;
	};

	this.setLongitude = function (longitude) {
		this.longitude = longitude;
	};

	this.getDate = function () {
		return date;
	};

	this.setDate = function (setDate) {
		this.date = date;
	};

	this.getQuality = function () {
		return calculateQuality();
	};

	this.setQuality = function (quality) {
		this.waterQuality = quality;
	};
	
	this.getMeasurements = function () {
		return this.measurements;
	};

	this.setMeasurements = function (measurements) {
		this.measurements = measurements;
	};

	this.toString = function () {
		var str = "";
		var keys = Object.keys(this.measurements);
		for (var keyIdx in keys) {
			var key = keys[keyIdx];
			str += "," + key + ":" + this.measurements[key];
		}

		return "{lat:" + lat + ",long:" + longitude + str + "}";
	};

	this.getUrl = function() {
		var serverUrl = "http://192.168.1.2:1337/measurement/create?latitude="
						+ this.lat + "&longitude=" + this.longitude
						+ "&quality=" + this.waterQuality;
		var keys = Object.keys(this.measurements);
		for (var keyIdx in keys) {
			var key = keys[keyIdx];
			serverUrl += "&" + key + "="
					+ this.measurements[key];
		}
		return serverUrl;
	}

	return this;
}
};