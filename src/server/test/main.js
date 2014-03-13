//lets load newrelic - if we have it
if (process.env.license) {
  require('newrelic');  
}

var restify = require('restify'), 
	userSave = require('save')('user'),
	mysql = require('mysql');

var app_name = 'calendeer-api';

// create a new server, both http and https
var server = restify.createServer({ name: app_name });

var connection = mysql.createConnection('mysql://bjorbrat_user:fpgruppe27@ec2-176-34-253-124.eu-west-1.compute.amazonaws.com/bjorbrat_fpgruppe27');

connection.connect();

// sets default headers and maps GET,PUT,POST
server.use(restify.fullResponse())
	.use(restify.bodyParser());
	server.use(restify.queryParser());
server.pre(restify.pre.userAgentConnection());

//------------------------------------------------------------------------------------------------
// Employee routes
//------------------------------------------------------------------------------------------------
// Routes: 			GET		/employee				params: username 													
//					POST 	/employee 													
// 					PUT		/employee/:id 												
//					GET 	/employee/:id 												
//					DELETE	/employee/:id  												
//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------
// GET 		/employee 																	
//------------------------------------------------------------------------------------------------

server.get('/employee', function(req, res, next) {
	
	if(typeof req.params.username !== "undefined") {
		connection.query("SELECT * FROM employee WHERE username='" + req.params.username + "'", function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
			res.send(rows[0])
		});
	} else {
		connection.query('SELECT * FROM employee', function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
			res.send(rows)
		});
	}
})

//------------------------------------------------------------------------------------------------
// POST 	/employee 																	
//------------------------------------------------------------------------------------------------

server.post('/employee', function(req, res, next) {
	if (req.params.username === undefined || req.params.password === undefined ) {
		return next(new restify.InvalidArgumentError('Required fields not supplied.'))
	}

	connection.query("insert into employee (username, password) values ('" + req.params.username + "','" + req.params.password + "')", function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
			
			res.send(201, rows)
	});
})

//------------------------------------------------------------------------------------------------
// PUT 		/employee/:id 																
//------------------------------------------------------------------------------------------------

server.put('/employee', function(req, res, next) {
	if (req.params.username === undefined) {
		return next(new restify.InvalidArgumentError('Required fields not supplied.'))
	}

	connection.query("UPDATE employee SET password='" + req.params.password + "', name='" + req.params.name + "', email='" + req.params.email + "' WHERE username='" + req.params.username + "'", function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
			
			res.send()
	});
})

//------------------------------------------------------------------------------------------------
// DELETE 		/employee/:id 															
//------------------------------------------------------------------------------------------------

server.del('/employee', function(req, res, next) {
	connection.query("DELETE FROM employee WHERE username='" + req.params.username + "'", function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
			
			res.send()
	});
})

//------------------------------------------------------------------------------------------------
// Meeting routes
//------------------------------------------------------------------------------------------------
// Routes: 			GET		/meeting 													
//					POST 	/meeting 													
// 					PUT		/meeting/:id 												
//					GET 	/meeting/:id 												
//					DELETE	/meeting/:id  												
//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------
// GET 		/meeting 																	
//------------------------------------------------------------------------------------------------

server.get('/meeting', function(req, res, next) {
	
	if(typeof req.params.meetid !== "undefined") {
		connection.query("SELECT * FROM meeting WHERE meetid='" + req.params.meetid + "'", function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
			res.send(rows[0])
		});
	} else if(typeof req.params.startdate !== "undefined" && typeof req.params.enddate !== "undefined") {
		connection.query("SELECT * FROM meeting WHERE meetid='" + req.params.username + "'", function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
			res.send(rows)
		});
	} else if(typeof req.params.username !== "undefined") {
		connection.query("SELECT * FROM meeting, employee WHERE meeting.username=employee.username AND employee.username='" + req.params.username + "'", function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
			res.send(rows[0])
		});
	} else {
		connection.query('SELECT * FROM meeting', function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
			res.send(rows)
		});
	}
})

//------------------------------------------------------------------------------------------------
// POST 	/meeting 																	
//------------------------------------------------------------------------------------------------

server.post('/meeting', function(req, res, next) {
	if (req.params.name === undefined ) {
		return next(new restify.InvalidArgumentError('Required fields not supplied.'))
	}
	
	if (req.params.room === 0)
		req.params.room = 'NULL';

	connection.query("INSERT INTO meeting (name, description, startDate, endDate, startTime, endTime, place, roomid, isAppointment, username) values ('" 
						+ req.params.name + "','" + req.params.description + "','" + req.params.startDate + "','" + req.params.endDate 
						+ "','" + req.params.startTime + "','" + req.params.endTime + "','" + req.params.place + "','" + req.params.room.roomID + "','" 
						+ req.params.isAppointment + "','" + req.params.responsible.username + "')", function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
			
			
			if(typeof req.params.participants !== "undefined") {
				connection.query("SELECT meetid FROM meeting WHERE meetid = (SELECT MAX(meetid) FROM meeting)", function(err, r, fields) {
					if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
			
			
					for (var i = 0; i < req.params.participants.length; i++) { 
						var participant = req.params.participants[i];
						
						if (participant.status === undefined) 
							participant.status = 'NULL';
						
    					connection.query("INSERT INTO meeting_participants (meetid, username, status) values ('" + r[0].meetid + "','" + participant.username + "','" + participant.status + "')", function(err, rows, fields) {
							if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
						});
					}
			
				});
			}
			
			res.send(201, rows)
	});
})

//------------------------------------------------------------------------------------------------
// PUT 		/meeting/:id 																
//------------------------------------------------------------------------------------------------

server.put('/meeting', function(req, res, next) {
	if (req.params.username === undefined) {
		return next(new restify.InvalidArgumentError('Required fields not supplied.'))
	}

	connection.query("UPDATE meeting SET password='" + req.params.password + "', name='" + req.params.name + "', email='" + req.params.email + "' WHERE username='" + req.params.username + "'", function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
			
			res.send()
	});
})

//------------------------------------------------------------------------------------------------
// DELETE 		/meeting/:id 															
//------------------------------------------------------------------------------------------------

server.del('/meeting', function(req, res, next) {
	connection.query("DELETE FROM meeting WHERE username='" + req.params.username + "'", function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
			
			res.send()
	});
})

//------------------------------------------------------------------------------------------------
// MeetingParticipants routes
//------------------------------------------------------------------------------------------------
// Routes: 			GET		/meeting_participants 													
//					POST 	/meeting_participants 													
// 					PUT		/meeting_participants/:id 												
//					GET 	/meeting_participants/:id 												
//					DELETE	/meeting_participants/:id  												
//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------
// GET 		/meeting_participants 																	
//------------------------------------------------------------------------------------------------

server.get('/meeting_participants', function(req, res, next) {
	
	if(typeof req.params.meetid !== "undefined") {
		connection.query("SELECT * FROM meeting_participants WHERE meetid='" + req.params.meetid + "'", function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
			res.send(rows[0])
		});
	} else {
		connection.query('SELECT * FROM meeting_participants', function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
			res.send(rows)
		});
	}
})

//------------------------------------------------------------------------------------------------
// POST 	/meeting_participants 																	
//------------------------------------------------------------------------------------------------

server.post('/meeting_participants', function(req, res, next) {
	if (req.params.username === undefined || req.params.password === undefined ) {
		return next(new restify.InvalidArgumentError('Required fields not supplied.'))
	}

	connection.query("insert into employee (username, password) values ('" + req.params.username + "','" + req.params.password + "')", function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
			
			res.send(201, rows)
	});
})

//------------------------------------------------------------------------------------------------
// PUT 		/meeting_participants/:id 																
//------------------------------------------------------------------------------------------------

server.put('/meeting_participants', function(req, res, next) {
	if (req.params.username === undefined) {
		return next(new restify.InvalidArgumentError('Required fields not supplied.'))
	}

	connection.query("UPDATE employee SET password='" + req.params.password + "', name='" + req.params.name + "', email='" + req.params.email + "' WHERE username='" + req.params.username + "'", function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
			
			res.send()
	});
})

//------------------------------------------------------------------------------------------------
// DELETE 		/meeting_participants/:id 															
//------------------------------------------------------------------------------------------------

server.del('/meeting_participants', function(req, res, next) {
	connection.query("DELETE FROM employee WHERE username='" + req.params.username + "'", function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
			
			res.send()
	});
})









// Listen on port
server.listen(process.env.PORT, function() {
	console.log('%s listening at %s', server.name, server.url);
});