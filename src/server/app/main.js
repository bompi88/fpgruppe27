//lets load newrelic - if we have it
if (process.env.license) {
  require('newrelic');  
}

var restify = require('restify'), 
	userSave = require('save')('user'),
	mysql = require('mysql'),
	async = require('async');

var app_name = 'calendeer-api';

// create a new server, both http and https
var server = restify.createServer({ 
	name: app_name,
	formatters: {
    	'application/json': myCustomFormatJSON
  	}
});

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
	
	var meetings = undefined;

	if(typeof req.params.meetid !== "undefined") {

		connection.query("SELECT * FROM meeting WHERE meetid='" + req.params.meetid + "'", function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))

			meetings = rows[0];

			async.series({
				participants: function(callback) {
					connection.query("SELECT * FROM meeting_participants WHERE meetid='" + req.params.meetid + "'", function(err, rows, fields) {
						if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))

						callback(null, rows);
					})
				},
			    responsible: function(callback){
			    	connection.query("SELECT * FROM employee WHERE username='" + meetings.username + "'", function(err, rows, fields) {
						if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
						
						callback(null, rows[0]);
					})
			        
			    },
			    room: function(callback){
					connection.query("SELECT * FROM room WHERE roomid='" + meetings.roomid + "'", function(err, rows, fields) {
						if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
						
						callback(null, rows[0]);
					})
			    }
			},
			function(err, results) {

				if (meetings.isAppointment === 0) {
					meetings.isAppointment = false;
				} else {
					meetings.isAppointment = true;
				}

				meetings.responsible = results.responsible;
				meetings.room = results.room;
				meetings.participants = results.participants;

				res.send(meetings)
			});	
		});
	} else if(typeof req.params.startTime !== "undefined" && typeof req.params.endTime !== "undefined") {
		connection.query("SELECT * FROM meeting WHERE startTime BETWEEN '" + req.params.startTime + "' AND '" + req.params.endTime + "'", function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
			
			meetings = rows;

			async.forEach(Object.keys(meetings), function (item, callback){ 
			    connection.query("SELECT * FROM meeting_participants WHERE meetid='" + meetings[item].meetid + "'", function(err, rows, fields) {
					if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
								
					meetings[item].participants = rows;
					

					connection.query("SELECT * FROM employee WHERE username='" + meetings[item].username + "'", function(err, rows, fields) {
							if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
							
							meetings[item].responsible = rows[0];

							connection.query("SELECT * FROM room WHERE roomid='" + meetings[item].roomid + "'", function(err, rows, fields) {
								if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
								
								meetings[item].room = rows[0];
								callback(null);
						})	
					})
				});

			}, function(err) {
			    res.send(meetings)
			}); 
		});
	} else if(typeof req.params.username !== "undefined") {
		connection.query("SELECT * FROM meeting WHERE username='" + req.params.username + "'", function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
			
			meetings = rows;

			async.forEach(Object.keys(meetings), function (item, callback){ 
			    connection.query("SELECT * FROM meeting_participants WHERE meetid='" + meetings[item].meetid + "'", function(err, rows, fields) {
					if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
								
					meetings[item].participants = rows;
					

					connection.query("SELECT * FROM employee WHERE username='" + meetings[item].username + "'", function(err, rows, fields) {
							if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
							
							meetings[item].responsible = rows[0];

							connection.query("SELECT * FROM room WHERE roomid='" + meetings[item].roomid + "'", function(err, rows, fields) {
								if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
								
								meetings[item].room = rows[0];
								callback(null);
						})	
					})
				});

			}, function(err) {
			    res.send(meetings)
			}); 
		});
	} else {
		connection.query('SELECT * FROM meeting', function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
			
			meetings = rows;

			async.forEach(Object.keys(meetings), function (item, callback){ 
			    connection.query("SELECT * FROM meeting_participants WHERE meetid='" + meetings[item].meetid + "'", function(err, rows, fields) {
					if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
								
					meetings[item].participants = rows;
					

					connection.query("SELECT * FROM employee WHERE username='" + meetings[item].username + "'", function(err, rows, fields) {
							if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
							
							meetings[item].responsible = rows[0];

							connection.query("SELECT * FROM room WHERE roomid='" + meetings[item].roomid + "'", function(err, rows, fields) {
								if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
								
								meetings[item].room = rows[0];
								callback(null);
						})	
					})
				});

			}, function(err) {
			    res.send(meetings)
			}); 
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

	connection.query("INSERT INTO meeting (name, description, startTime, endTime, place, roomid, isAppointment, username) values ('" 
						+ req.params.name + "','" + req.params.description + "','" + req.params.startTime + "','" + req.params.endTime 
						+ "','" + req.params.place + "','" + req.params.room.roomID + "','" 
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
	if (req.params.name === undefined ) {
		return next(new restify.InvalidArgumentError('Required fields not supplied.'))
	}
	
	if (req.params.room === 0)
		req.params.room = 'NULL';

	connection.query("UPDATE meeting SET name='" + req.params.name + "', description='" + req.params.description + "', startDate='" + req.params.startDate
					+ "', endDate='" + req.params.endDate +"', startTime='" + req.params.startTime +"', endTime='" + req.params.endTime +"', place='" 
					+ req.params.place + "', roomid='" + req.params.room.roomID + "', isAppointment='" + req.params.isAppointment + "', username='" + req.params.responsible.username + "' WHERE meetid='" + req.params.meetID + "'", function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
			
			if(typeof req.params.participants !== "undefined") {
				connection.query("SELECT meetid FROM meeting WHERE meetid = (SELECT MAX(meetid) FROM meeting)", function(err, r, fields) {
					if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
			
					connection.query("DELETE FROM meeting WHERE meetid=" + r[0].meetID, function(err, rows, fields) {
						if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
					});
			
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


			res.send()
	});
})

//------------------------------------------------------------------------------------------------
// DELETE 		/meeting/:id 															
//------------------------------------------------------------------------------------------------

server.del('/meeting', function(req, res, next) {
	if (req.params.meetid === undefined ) {
		return next(new restify.InvalidArgumentError('Required fields not supplied.'))
	}

	connection.query("DELETE FROM meeting WHERE meetid=" + req.params.meetid, function(err, rows, fields) {
		if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))

		res.send()
	});
})

//------------------------------------------------------------------------------------------------
// MeetingParticipants routes
//------------------------------------------------------------------------------------------------
// Routes: 			GET		/meeting_participants 				params: meetid									
//					POST 	/meeting_participants 													
// 					PUT		/meeting_participants/:id 												
//					GET 	/meeting_participants/:id 												
//					DELETE	/meeting_participants/:id  												
//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------
// GET 		/meeting_participants 																	
//------------------------------------------------------------------------------------------------

server.get('/meeting_participants', function(req, res, next) {
	
	var meeting_participants = undefined;

	if(typeof req.params.meetid !== "undefined") {
		connection.query("SELECT * FROM meeting_participants WHERE meetid='" + req.params.meetid + "'", function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))

			meeting_participants = rows;

			async.forEach(Object.keys(meeting_participants), function (item, callback){ 
			    connection.query("SELECT * FROM employee WHERE username='" + meeting_participants[item].username + "'", function(err, rows, fields) {
					if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
								
					rows[0].status = meeting_participants[item].status;
					rows[0].alarm = meeting_participants[item].alarm;
					meeting_participants[item] = rows[0];

					callback(null);
				});

			}, function(err) {
			    res.send(meeting_participants)
			});
		});
	} else {
		connection.query("SELECT * FROM meeting_participants", function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))

			meeting_participants = rows;

			async.forEach(Object.keys(meeting_participants), function (item, callback){ 
			    connection.query("SELECT * FROM employee WHERE username='" + meeting_participants[item].username + "'", function(err, rows, fields) {
					if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
								
					rows[0].status = meeting_participants[item].status;
					rows[0].alarm = meeting_participants[item].alarm;
					meeting_participants[item] = rows[0];

					callback(null);
				});

			}, function(err) {
			    res.send(meeting_participants)
			});
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

//------------------------------------------------------------------------------------------------
// Room routes
//------------------------------------------------------------------------------------------------
// Routes: 			GET		/room 									params: roomid									
//					POST 	/room 													
// 					PUT		/room/:id 												
//					GET 	/room/:id 												
//					DELETE	/room/:id  												
//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------
// GET 		/room 																	
//------------------------------------------------------------------------------------------------

server.get('/room', function(req, res, next) {
	
	if(typeof req.params.roomID !== "undefined") {
		connection.query("SELECT * FROM room WHERE roomid='" + req.params.roomID + "'", function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
			res.send(rows[0])
		});
	} else {
		connection.query('SELECT * FROM room', function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
			res.send(rows)
		});
	}
})

//------------------------------------------------------------------------------------------------
// POST 	/meeting_participants 																	
//------------------------------------------------------------------------------------------------

server.post('/room', function(req, res, next) {
	if (req.params.name === undefined || req.params.capacity === undefined ) {
		return next(new restify.InvalidArgumentError('Required fields not supplied.'))
	}

	connection.query("INSERT INTO room (name, capacity) values ('" + req.params.name + "','" + req.params.capacity + "')", function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
			
			res.send(201, rows)
	});
})

//------------------------------------------------------------------------------------------------
// PUT 		/meeting_participants/:id 																
//------------------------------------------------------------------------------------------------

server.put('/room', function(req, res, next) {
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

server.del('/room', function(req, res, next) {
	connection.query("DELETE FROM employee WHERE username='" + req.params.username + "'", function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
			
			res.send()
	});
})



function myCustomFormatJSON(req, res, body) {
  if (!body) {
    if (res.getHeader('Content-Length') === undefined &&
        res.contentLength === undefined) {
      res.setHeader('Content-Length', 0);
    }
    return null;
  }

  if (body instanceof Error) {
    // snoop for RestError or HttpError, but don't rely on instanceof
    if ((body.restCode || body.httpCode) && body.body) {
      body = body.body;
    } else {
      body = {
        message: body.message
      };
    }
  }

  if (Buffer.isBuffer(body))
    body = body.toString('base64');

  var data = JSON.stringify(body, null, 2);

  if (res.getHeader('Content-Length') === undefined &&
      res.contentLength === undefined) {
    res.setHeader('Content-Length', Buffer.byteLength(data));
  }

  return data;
}



// Listen on port
server.listen(process.env.PORT, function() {
	console.log('%s listening at %s', server.name, server.url);
});