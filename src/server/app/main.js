var restify = require('restify'),
	mysql = require('mysql'),
	async = require('async'),
	moment = require('moment');

var app_name = 'calendeer-api';

// create a new http server
var server = restify.createServer({ 
	name: app_name,
	formatters: {
    	'application/json': myCustomFormatJSON
  	}
});

// create a connection  to the database.
var connection = mysql.createConnection('mysql://bjorbrat_user:fpgruppe27@ec2-176-34-253-124.eu-west-1.compute.amazonaws.com/bjorbrat_fpgruppe27');

connection.connect();

// sets default headers and maps GET,PUT,POST
// sets parsers etc.
server.use(restify.fullResponse())
server.use(restify.bodyParser());
server.use(restify.queryParser());
server.pre(restify.pre.userAgentConnection());


//------------------------------------------------------------------------------------------------
// Employee routes
//------------------------------------------------------------------------------------------------
// Routes: 			GET		/employee			params: username 													
//					POST 	/employee 			params: username,password,name,email										
// 					PUT		/employee 			params: username,password,name,email																		
//					DELETE	/employee  			params: username									
//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------
// GET 		/employee 																	
//------------------------------------------------------------------------------------------------

server.get('/employee', function(req, res, next) {
	
	// if username param is defined, get employee with username
	if(typeof req.params.username !== "undefined") {
		connection.query("SELECT * FROM employee WHERE username='" + req.params.username + "'", function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
			res.send(rows[0])
		});
	// else: get all employees
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

	// check required fields
	if (req.params.username === undefined || req.params.password === undefined || req.params.name === undefined || req.params.email === undefined) {
		return next(new restify.InvalidArgumentError('Required fields not supplied.'))
	}

	// if all fields is defined, insert the employee into the database
	connection.query("INSERT INTO employee (username, password, name, email) values ('" + req.params.username + "','" + req.params.password + "','" +  req.params.name + "','" + req.params.email + "')", function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
			
			res.send(201, rows)
	});
})

//------------------------------------------------------------------------------------------------
// PUT 		/employee																
//------------------------------------------------------------------------------------------------

server.put('/employee', function(req, res, next) {

	// check required fields
	if (req.params.username === undefined || req.params.password === undefined || req.params.name === undefined || req.params.email === undefined) {
		return next(new restify.InvalidArgumentError('Required fields not supplied.'))
	}

	// update an employee which has username X
	connection.query("UPDATE employee SET password='" + req.params.password + "', name='" + req.params.name + "', email='" + req.params.email + "' WHERE username='" + req.params.username + "'", function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
			
			res.send()
	});
})

//------------------------------------------------------------------------------------------------
// DELETE 		/employee 															
//------------------------------------------------------------------------------------------------

server.del('/employee', function(req, res, next) {

	// must have a username
	if (req.params.username === undefined) {
		return next(new restify.InvalidArgumentError('Required fields not supplied.'))
	}

	// Fire a delete query for employee with username X
	connection.query("DELETE FROM employee WHERE username='" + req.params.username + "'", function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
			
			res.send()
	});
})

//------------------------------------------------------------------------------------------------
// Meeting routes
//------------------------------------------------------------------------------------------------
// Routes: 			GET		/meeting 		params:meetid,startTime,endTime,username												
//					POST 	/meeting 													
// 					PUT		/meeting 																							
//					DELETE	/meeting  												
//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------
// GET 		/meeting 																	
//------------------------------------------------------------------------------------------------

server.get('/meeting', function(req, res, next) {
	
	var meetings = undefined;
	
	// if meeting id is given, then get that particular meeting	
	if(typeof req.params.meetid !== "undefined") {

		// select our meeting 
		connection.query("SELECT * FROM meeting WHERE meetid='" + req.params.meetid + "'", function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))

			meetings = rows[0];

			// gather information about fields which needs information from other tables.
			// Do this in an asynchronic fashion.
			async.series({
				// get info about our meeting participants
				participants: function(callback) {
					connection.query("SELECT * FROM meeting_participants WHERE meetid='" + req.params.meetid + "'", function(err, rows, fields) {
						if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))

						callback(null, rows);
					})
				},
				// get info about the meeting responsible
			    responsible: function(callback){
			    	connection.query("SELECT * FROM employee WHERE username='" + meetings.username + "'", function(err, rows, fields) {
						if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
						
						callback(null, rows[0]);
					})
			        
			    },
			    // get info about the room which the meeting taking place.
			    room: function(callback){
					connection.query("SELECT * FROM room WHERE roomid='" + meetings.roomid + "'", function(err, rows, fields) {
						if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
						
						callback(null, rows[0]);
					})
			    }
			},
			// join all this information together
			function(err, results) {

				meetings.responsible = results.responsible;
				meetings.room = results.room;
				meetings.participants = results.participants;

				res.send(meetings)
			});	
		});
	// if we want all events in a given start and end time. Fetch from one or many calendars, which is identified with an eployees username.
	} else if(typeof req.params.startTime !== "undefined" && typeof req.params.endTime !== "undefined" && typeof req.params.username !== "undefined") {

		var users = '';

		// include all username given at http request.
		// gives: 'username1','username2', ... , 'username3' etc.
		if(req.params.username[1].length > 1) {
			users += "'" + req.params.username[0] + "'";

			for (var i = 1; i < req.params.username.length; i++) {
				users += ", '" + req.params.username[i] + "'";
			}
		} else {
			users = "\'" + req.params.username + "\'";
		}

		//get all meetings for all defined users in the given timeinterval.
		connection.query("SELECT * FROM meeting WHERE username IN (" + users + ") AND startTime BETWEEN '" + req.params.startTime + "' AND '" + req.params.endTime + "'", function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
			
			meetings = rows;

			// for each meeting, get all related information to that meeting
			async.forEach(Object.keys(meetings), function (item, callback){ 
				// get all meeting participants for each meeting
			    connection.query("SELECT * FROM meeting_participants WHERE meetid='" + meetings[item].meetid + "'", function(err, rows, fields) {
					if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
								
					meetings[item].participants = rows;
					
					// get the responsible for each meeting
					connection.query("SELECT * FROM employee WHERE username='" + meetings[item].username + "'", function(err, rows, fields) {
						if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
						
						meetings[item].responsible = rows[0];

						// get the room for each meeting
						connection.query("SELECT * FROM room WHERE roomid='" + meetings[item].roomid + "'", function(err, rows, fields) {
							if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
							
							meetings[item].room = rows[0];
							callback(null);
						})	
					})
				});
			// respond when finished
			}, function(err) {
			    res.send(meetings)
			}); 
		});
	// If only username is given, then return all meetings where this username is listed as responsible
	} else if(typeof req.params.username !== "undefined") {
		// get all meetings where the employee with username X is listed as responsible
		connection.query("SELECT * FROM meeting WHERE username='" + req.params.username + "'", function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
			
			meetings = rows;

			// for each meeting, get and merge information about meeting participants, 
			async.forEach(Object.keys(meetings), function (item, callback){ 
			    connection.query("SELECT * FROM meeting_participants WHERE meetid='" + meetings[item].meetid + "'", function(err, rows, fields) {
					if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
								
					meetings[item].participants = rows;
					
					// get the responsible of meeting
					connection.query("SELECT * FROM employee WHERE username='" + meetings[item].username + "'", function(err, rows, fields) {
							if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
							
							meetings[item].responsible = rows[0];

							// get room
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

			// for each meeting, get and merge information about meeting participants, 
			async.forEach(Object.keys(meetings), function (item, callback){ 
			    connection.query("SELECT * FROM meeting_participants WHERE meetid='" + meetings[item].meetid + "'", function(err, rows, fields) {
					if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
								
					meetings[item].participants = rows;
					
					// get the responsible of meeting
					connection.query("SELECT * FROM employee WHERE username='" + meetings[item].username + "'", function(err, rows, fields) {
							if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
							
							meetings[item].responsible = rows[0];

							// get room
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
	
	if (req.params.room === undefined)
		req.params.room = 'NULL';

	// insert a meeting with corresponding values.
	connection.query("INSERT INTO meeting (name, description, startTime, endTime, place, roomid, isAppointment, username) values ('" 
						+ req.params.name + "','" + req.params.description + "','" + req.params.startTime + "','" + req.params.endTime 
						+ "','" + req.params.place + "','" + req.params.room.roomID + "','" 
						+ req.params.isAppointment + "','" + req.params.responsible.username + "')", function(err, rows, fields) {
		if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
		
		// if we have participants
		if(typeof req.params.participants !== "undefined") {
			// get meeting id from newly inserted meeting
			connection.query("SELECT meetid FROM meeting WHERE meetid = (SELECT MAX(meetid) FROM meeting)", function(err, r, fields) {
				if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
		
				// for all participants
				for (var i = 0; i < req.params.participants.length; i++) { 
					var participant = req.params.participants[i];
					
					if (participant.status === undefined) 
						participant.status = 'NULL';
					
					// create meeting participants which connects meetings and employees
					connection.query("INSERT INTO meeting_participants (meetid, username, status) values ('" + r[0].meetid + "','" + participant.username + "','" + participant.status + "')", function(err, rows, fields) {
						if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))

						var time = moment(req.params.startTime);

						// create a message which is assigned to each participant.
						var invitedString = 'Du har blitt invitert til ' + req.params.name + ' den ' + time.date() + '.' + time.month() + '.' + time.year() + ' kl. ' + time.hours() + ':' + time.minutes();
						connection.query("INSERT INTO message (message, time, owner, isSeen) VALUES('" + invitedString + "',NOW(),'" + participant.username + "','" + 0 + "')", function(err, rows, fields) {
							if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
						
						});	
					});
				}
				res.send(201, r[0].meetid)
			});
		}
	});
})

//------------------------------------------------------------------------------------------------
// PUT 		/meeting/:id 																
//------------------------------------------------------------------------------------------------

server.put('/meeting', function(req, res, next) {
	if (req.params.name === undefined ) {
		return next(new restify.InvalidArgumentError('Required fields not supplied.'))
	}

	var placeOrRoom = undefined;
	
	if (req.params.room === 0) {
		req.params.room = 'NULL';
		placeOrRoom = req.param.place;
	} else {
		placeOrRoom = req.param.room.name;
	}

	var oldMeeting = undefined;
	var newMeeting = req.params;
	var timeChanged = false;
	var placeChanged = false;

	connection.query("SELECT * FROM meeting WHERE meetid='" + req.params.meetid + "'", function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))

			oldMeeting = rows[0];
	});

	var newStartTime = moment(req.params.startTime);
	var newEndTime = moment(req.params.endTime);

	var timeandPlaceChangedText = newMeeting.name + " har blitt endret. Ny tid er: " + newStartTime.hours() + ':' + newStartTime.minutes() + " til: " + newEndTime.hours() + ':' + newEndTime.minutes() + " og nytt sted er: " + placeOrRoom;
	var timeChangedText = newMeeting.name + " har blitt endret. Ny tid er: " + newStartTime.hours() + ':' + newStartTime.minutes() + " til: " + newEndTime.hours() + ':' + newEndTime.minutes();
	var placeChangedText = newMeeting.name + "har blitt flyttet til" + placeOrRoom; 
	var outputMessage;

	if (oldMeeting.startTime !== oldMeeting.startTime || oldMeeting.endTime !== oldMeeting.endTime) {
		timeChanged = true;
	} 
	if (oldMeeting.place != newMeeting.place || oldMeeting.roomid != newMeeting.roomid) {
		placeChanged = true;
	}

	if (timeChanged && placeChanged) {
		outputMessage = timeandPlaceChangedText;
	} else if (timeChanged) {
		outputMessage = timeChangedText;
	} else if (placeChanged) {
		outputMessage = placeChangedText;
	}

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
						
							connection.query("INSERT INTO message (message, time, owner, isSeen) VALUES('" + outputMessage + "',NOW(),'" + participant.username + "','" + 0 + "')", function(err, rows, fields) {
								if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
							
							});	
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

	var meetName = undefined;
	console.log("var her")

	console.log(req.params.meetid)

	connection.query("SELECT * FROM meeting_participants WHERE meetid=" + req.params.meetid, function(err, participants, fields) {
		if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))

		console.log(participants)

	connection.query("SELECT name FROM meeting WHERE meetid=" + req.params.meetid, function(err, rows, fields) {
		if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))

		meetName = rows[0].name;

		for (var i = 0; i < participants.length; i++) { 
			var participant = participants[i];
		
			var outputMessage = meetName + " har blitt avlyst";
			
			connection.query("INSERT INTO message (message, time, owner, isSeen) VALUES('" + outputMessage + "',NOW(),'" + participant.username + "','" + 0 + "')", function(err, rows, fields) {
				if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
				console.log("send to:" + participant.username)
				res.send()
			});	
		}

				connection.query("DELETE FROM meeting WHERE meetid=" + req.params.meetid, function(err, rows, fields) {
		if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
		});
	});


		
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


//------------------------------------------------------------------------------------------------
// Message routes
//------------------------------------------------------------------------------------------------
// Routes: 			GET		/message 									params: messageid									
//					POST 	/message 													
// 					PUT		/message/:id 												
//					GET 	/message/:id 												
//					DELETE	/message/:id  												
//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------
// GET 		/message 																	
//------------------------------------------------------------------------------------------------

server.get('/message', function(req, res, next) {
	
	if(typeof req.params.messid !== "undefined") {
		connection.query("SELECT * FROM message WHERE messid='" + req.params.messid + "'", function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
			res.send(rows[0])
		});
	} else if(typeof req.params.username !== "undefined") {

		//connection.query("SELECT * FROM message WHERE username='" + req.params.username + "' AND time > '" + rew.params.timeFrom + "'", function(err, rows, fields) {
		connection.query("SELECT * FROM message WHERE owner='" + req.params.username + "'", function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
			res.send(rows)
		});
	} else {
		connection.query('SELECT * FROM message', function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
			res.send(rows)
		});
	}
})

//------------------------------------------------------------------------------------------------
// PUT 		/message																
//------------------------------------------------------------------------------------------------

server.put('/message', function(req, res, next) {
	if (req.params.messid === undefined || req.params.isSeen === undefined) {
		return next(new restify.InvalidArgumentError('Required fields not supplied.'))
	}

	connection.query("UPDATE message SET isSeen='" + req.params.isSeen + "' WHERE messid='" + req.params.messid + "'", function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
			
			res.send()
	});
})

//------------------------------------------------------------------------------------------------
// DELETE 		/meeting_participants/:id 															
//------------------------------------------------------------------------------------------------

server.del('/message', function(req, res, next) {
	connection.query("DELETE FROM message WHERE messid='" + req.params.messid + "'", function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
			
			res.send()
	});
})

server.post('/message', function(req, res, next) {
	if (req.params.message === undefined) {
		return next(new restify.InvalidArgumentError('Required fields not supplied.'))
	}

	connection.query("INSERT INTO message (message, owner) values ('" + req.params.message + "','" + req.params.username + "')", function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
			
			res.send(201, rows)
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



// // Listen on port
// server.listen(process.env.PORT, function() {
// 	console.log('%s listening at %s', server.name, server.url);
// });


// Listen on port
server.listen(9004, function() {
	console.log('%s listening at %s', server.name, server.url);
});
