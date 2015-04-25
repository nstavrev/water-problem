/**
 * MeasurementController
 *
 * @description :: Server-side logic for managing measurements
 * @help        :: See http://links.sailsjs.org/docs/controllers
 */

module.exports = {
	create : function(req,res){
		//Removes all measurements
		var params = req.params.all();

		for(var index in params){
			console.log(index);
			if(!params[index] || index == "Date / Time") continue;
			params[index] = params[index].replace(/\s/g, '');

		}

		Measurement.create(params, function(err, measurement){
			console.log("Measurement is saved successfully");
			res.send(measurement);
		});
		
		/*
		Measurement.destroy({}).exec(function(err, measurement){
			if(err){
				res.json({err : err});
				return;
			}
			console.log("Measurement created successfully");
			res.send(measurement);
		});
		*/
		
	},
	findAll : function(req,res){
		Measurement.find({}, function(err, all) {
			res.json(all);
		});
	},
	find : function(req,res){
		
		delete req.params.all()['id'];
		console.log(req.params.all());
		Measurement.find(req.params.all(), function(err, all) {
			res.json(all);
		});
	}
};

