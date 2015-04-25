/**
 * ServiceController
 *
 * @description :: Server-side logic for managing services
 * @help        :: See http://links.sailsjs.org/docs/controllers
 */



 


module.exports = {

	initData: function (req, res) {
		return res.json({initialize:'Completed'});
	},


// http://192.168.1.2:1337/measurement/create?latitude=42.519121&longitude=27.464326&temperature=18&airRelativeHumidity=23&atmosphericPressure=989.34&soilHumidity=789.85&luminance=33838&waterQuality=97&date=23/4/2015

	  // {
	  //   "latitude": 43.0032,
	  //   "longitude": 23.23232,
	  //   "date": "25/04/2015"
	  ///  "measurement" : {
	          // "ph": 8,
	          // "temp": 45
	  //   } 
	  
	  //   "airRelativeHumidity": 23,
	  //   "atmosphericPressure": 989.34,
	  //   "soilHumidity": 789.85,
	  //   "luminance": 33838,
	  //   "waterQuality": 97,
	  //   "id": 1,
	  //   "createdAt": "2015-04-05T15:17:44.928Z",
	  //   "updatedAt": "2015-04-05T15:17:44.928Z"
	  // },

	create: function (req, res) {
     console.log(req.params.all());
     Location.create(req.params.all(), function(err, location){
     	if(err) {
     		res.json({err : err})
     	}
     	res.json(location)
     });

     Location.find({}, function(err,locations){
     	console.log(locations)
     });
     
	},


	getLatestMeasurements: function (req, res) {
	    res.setHeader("Access-Control-Allow-Origin", "*");
	    console.log('get all measurements');

			// var measurements = [
			// 	// {
			// 	// 	point: {
			// 			// lat, long
			// 	// 	},
			// 	// 	measurements: [ // for each date
			// 	// 		{ -- represents date at the same index
			// 				// lat, long, temp , date, etc, waterQ
			// 	// 		}
			// 	// 	]
			// 	// }
			// ];
		var result = [];
		for (var i = 0; i < measurements.length; i++) {
			var allMeasurements = measurements[i].measurements;
			allMeasurements.forEach(function (measurement) {
				result.push( [
					measurement.date,
					measurement.waterQuality,
					measurement.latitude,
					measurement.longitude,
					measurement.date,
					getPinString(measurement),
				] );
			});
		}



	    // var result = [
	    //   // requested date                         real date of measurement
	    //       ["11/4/2015" , 10, 42.5047926,  27.4626361, "11/4/2015", "1nqkva danna 29"],  
	    //       ["18/4/2015" , 20, 42.5047926,  27.4626361, "18/4/2015", "1nqkva danna 29"],
	    //       ["19/4/2015" , 50, 42.5047926,  27.4626361, "11/4/2015", "1nqkva danna 29"],
	    //       ["20/4/2015" , 60, 42.5047926,  27.4626361, "11/4/2015", "1nqkva danna 29"],
	    //       ["21/4/2015" , 80, 42.5047926,  27.4626361, "11/4/2015", "1nqkva danna 29"],
	    //       ["25/4/2015" , 100, 42.5047926,  27.4626361, "25/4/2015", "1nqkva danna 29"],
	    // ];

	    return res.json(result);
	},

	// http://localhost:1338/measurement/getalldates
  	getAllDates: function (req, res) {
    	res.setHeader("Access-Control-Allow-Origin", "*");
    	console.log('get all dates');


    	// [["25/4/2015", "25/5/2015"] 
    	return res.json(dates);
  	},

  	index: function (req, res) {
  		return res.view();
  	},
	
};

