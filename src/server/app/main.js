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

			res.charSet('utf-8');
			res.send(rows[0])
		});
	// else: get all employees
	} else {
		connection.query('SELECT * FROM employee', function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))

			res.charSet('utf-8');
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
			
			res.charSet('utf-8');
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
			
			res.charSet('utf-8');
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
			
			res.charSet('utf-8');
			res.send()
	});
})

//------------------------------------------------------------------------------------------------
// Meeting routes
//------------------------------------------------------------------------------------------------
// Routes: 			GET		/meeting 		params:meetid,startTime,endTime,username												
//					POST 	/meeting 		params:name,description,startTime,endTime,place,roomid,isAppointment,username											
// 					PUT		/meeting 		params:name,description,startTime,endTime,place,roomid,isAppointment,username																						
//					DELETE	/meeting  		params:meetid										
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
					connection.query("SELECT employee.username, employee.name, employee.email, meeting_participants.status FROM employee, meeting_participants WHERE employee.username=meeting_participants.username AND meeting_participants.meetid='" + req.params.meetid + "'", function(err, rows, fields) {
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

				res.charSet('utf-8');
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
		connection.query("SELECT DISTINCT(meeting.meetid), meeting.name, meeting.description, meeting.startTime, meeting.endTime, meeting.place, meeting.roomid, meeting.isAppointment, meeting.username FROM meeting, meeting_participants WHERE meeting.meetid = meeting_participants.meetid AND meeting_participants.username IN (" + users + ") OR meeting.username IN (" + users + ") AND startTime BETWEEN '" + req.params.startTime + "' AND '" + req.params.endTime + "'", function(err, rows, fields) {
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
				res.charSet('utf-8');
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
				res.charSet('utf-8');
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
				res.charSet('utf-8');
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
		
		var meetID = undefined; 
		
		// if we have participants
		if(typeof req.params.participants !== "undefined") {
			// get meeting id from newly inserted meeting
			connection.query("SELECT meetid FROM meeting WHERE meetid = (SELECT MAX(meetid) FROM meeting)", function(err, r, fields) {
				if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
				var participant = undefined;
				// for all participants
				for (var i = 0; i < req.params.participants.length; i++) { 
					participant = req.params.participants[i];
					
					if (participant.status === undefined) 
						participant.status = 'NULL';
					
					// create meeting participants which connects meetings and employees
					connection.query("INSERT INTO meeting_participants (meetid, username, status) values ('" + r[0].meetid + "','" + participant.username + "','" + participant.status + "')", function(err, rows, fields) {
						if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))

						
					});
					var time = moment(req.params.startTime);

					var minutes = time.minutes();

					if (minutes < 10) {
						minutes = '0' + minutes;
					}

					// create a message which is assigned to each participant.
					var invitedString = 'Du har blitt invitert til ' + req.params.name + ' den ' + time.date() + '.' + time.month() + '.' + time.year() + ' kl. ' + time.hours() + ':' + minutes;
					console.log(participant.username)
					connection.query("INSERT INTO message (message, time, meetid, owner, isSeen) VALUES('" + invitedString + "',NOW(),'" + r[0].meetid + "','" + participant.username + "','" + 0 + "')", function(err, rows, fields) {
						if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
					
					});	
				}
				res.charSet('utf-8');
				res.send(201, r[0].meetid)
			});
		}
	});
})

//------------------------------------------------------------------------------------------------
// PUT 		/meeting																
//------------------------------------------------------------------------------------------------

server.put('/meeting', function(req, res, next) {
	if (req.params.name === undefined || req.params.meetid === undefined) {
		return next(new restify.InvalidArgumentError('Required fields not supplied.'))
	}

	var placeOrRoom = undefined;

	// deal with the possibility that you can define either room or place.	
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

	// fetch the old meeting
	connection.query("SELECT * FROM meeting WHERE meetid='" + req.params.meetid + "'", function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))

			oldMeeting = rows[0];
	});

	var newStartTime = moment(req.params.startTime);
	var newEndTime = moment(req.params.endTime);

	var newStartTimeMinutes = newStartTime.minutes();
	var newEndTimeMinutes = newEndTime.minutes();

	// if minute value is smaller than 10, append a zero before number
	if (newStartTimeMinutes < 10) {
		newStartTimeMinutes = '0' + newStartTimeMinutes;
	}

	if (newEndTimeMinutes < 10) {
		newEndTimeMinutes = '0' + newEndTimeMinutes;
	}

	// create all possible messages
	var timeandPlaceChangedText = newMeeting.name + " har blitt endret. Ny tid er: " + newStartTime.hours() + ':' + newStartTimeMinutes + " til: " + newEndTime.hours() + ':' + newEndTimeMinutes + " og nytt sted er: " + placeOrRoom;
	var timeChangedText = newMeeting.name + " har blitt endret. Ny tid er: " + newStartTime.hours() + ':' + newStartTimeMinutes + " til: " + newEndTime.hours() + ':' + newEndTimeMinutes;
	var placeChangedText = newMeeting.name + "har blitt flyttet til" + placeOrRoom; 
	var outputMessage;

	// check what has been changed
	if (oldMeeting.startTime !== oldMeeting.startTime || oldMeeting.endTime !== oldMeeting.endTime) {
		timeChanged = true;
	} 
	if (oldMeeting.place != newMeeting.place || oldMeeting.roomid != newMeeting.roomid) {
		placeChanged = true;
	}

	// set message according to events.
	if (timeChanged && placeChanged) {
		outputMessage = timeandPlaceChangedText;
	} else if (timeChanged) {
		outputMessage = timeChangedText;
	} else if (placeChanged) {
		outputMessage = placeChangedText;
	}

	// Then update meeting with new values
	connection.query("UPDATE meeting SET name='" + req.params.name + "', description='" + req.params.description + "', startDate='" + req.params.startDate
					+ "', endDate='" + req.params.endDate +"', startTime='" + req.params.startTime +"', endTime='" + req.params.endTime +"', place='" 
					+ req.params.place + "', roomid='" + req.params.room.roomID + "', isAppointment='" + req.params.isAppointment + "', username='" + req.params.responsible.username + "' WHERE meetid='" + req.params.meetid + "'", function(err, rows, fields) {
			
		if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
		
		// if the meeting has participants
		if(typeof req.params.participants !== "undefined") {
			
			// update all participants to have a status to be INVITED
			connection.query("UPDATE meeting_participants SET status='INVITED' WHERE meetid=" + req.params.meetid, function(err, rows, fields) {
				if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))

				// for all meeting participants send a message that the meeting has changed.
				for (var i = 0; i < req.params.participants.length; i++) { 
					
					var participant = req.params.participants[i];

					connection.query("INSERT INTO message (message, time, meetid, owner, isSeen) VALUES('" + outputMessage + "',NOW(),'" + req.params.meetid + "' ,'" + participant.username + "','" + 0 + "')", function(err, rows, fields) {
						if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
					
					});	
				}
			});	
		}
		res.charSet('utf-8');
		res.send()
	});
})

//------------------------------------------------------------------------------------------------
// DELETE 		/meeting															
//------------------------------------------------------------------------------------------------

server.del('/meeting', function(req, res, next) {
	if (req.params.meetid === undefined ) {
		return next(new restify.InvalidArgumentError('Required fields not supplied.'))
	}

	var meetName = undefined;

	// select all meeting participants
	connection.query("SELECT * FROM meeting_participants WHERE meetid=" + req.params.meetid, function(err, participants, fields) {
		if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))

		// get the name of the meeting
		connection.query("SELECT name FROM meeting WHERE meetid=" + req.params.meetid, function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))

			meetName = rows[0].name;

			// for all meeting participants, send message that meeting is canceled.
			for (var i = 0; i < participants.length; i++) { 
				var participant = participants[i];
			
				var outputMessage = meetName + " har blitt avlyst";
				
				// Send cenceled message
				connection.query("INSERT INTO message (message, time, meetid, owner, isSeen) VALUES('" + outputMessage + "',NOW(),'" + req.params.meetid + "','" + participant.username + "','" + 0 + "')", function(err, rows, fields) {
					if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))

					res.charSet('utf-8');
					
				});	
			}

			// delete the meeting from the database
			connection.query("DELETE FROM meeting WHERE meetid=" + req.params.meetid, function(err, rows, fields) {
				if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))

				res.send()
			});
		});
	});
})

//------------------------------------------------------------------------------------------------
// MeetingParticipants routes
//------------------------------------------------------------------------------------------------
// Routes: 			GET		/meeting_participants 				params: meetid									
//					POST 	/meeting_participants 				params:username,password,name,email,status									
// 					PUT		/meeting_participants 				params:username,password,name,email,status								 												
//					DELETE	/meeting_participants  				params:username								
//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------
// GET 		/meeting_participants 																	
//------------------------------------------------------------------------------------------------

server.get('/meeting_participants', function(req, res, next) {
	
	var meeting_participants = undefined;

	// if meeting id is defined
	if(typeof req.params.meetid !== "undefined") {
		// get all meeting participants to that particular meeting
		connection.query("SELECT * FROM meeting_participants WHERE meetid='" + req.params.meetid + "'", function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))

			meeting_participants = rows;

			// for all meeting participants
			async.forEach(Object.keys(meeting_participants), function (item, callback){ 
				// merge employee data with meeting participant data, because of inheritance
			    connection.query("SELECT * FROM employee WHERE username='" + meeting_participants[item].username + "'", function(err, rows, fields) {
					if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
								
					rows[0].status = meeting_participants[item].status;
					rows[0].alarm = meeting_participants[item].alarm;
					meeting_participants[item] = rows[0];

					callback(null);
				});

			}, function(err) {
				res.charSet('utf-8');
			    res.send(meeting_participants)
			});
		});
	// get all meeting participants
	} else {
		// fetch all meeting participants in database
		connection.query("SELECT * FROM meeting_participants", function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))

			meeting_participants = rows;
			// for all meeting participants
			async.forEach(Object.keys(meeting_participants), function (item, callback){ 
				// merge employee data with meeting participant data, because of inheritance
			    connection.query("SELECT * FROM employee WHERE username='" + meeting_participants[item].username + "'", function(err, rows, fields) {
					if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
								
					rows[0].status = meeting_participants[item].status;
					rows[0].alarm = meeting_participants[item].alarm;
					meeting_participants[item] = rows[0];

					callback(null);
				});

			}, function(err) {
				res.charSet('utf-8');
			    res.send(meeting_participants)
			});
		});
	}
})

//------------------------------------------------------------------------------------------------
// POST 	/meeting_participants 																	
//------------------------------------------------------------------------------------------------

server.post('/meeting_participants', function(req, res, next) {

	if (req.params.meetid === undefined || req.params.username === undefined ) {
		return next(new restify.InvalidArgumentError('Required fields not supplied.'))
	}
	// create a meeting participant in database
	connection.query("insert into meeting_participants (meetid, username, status) values ('" + req.params.meetid + "," + req.params.username + "," + req.params.status + "')", function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
			
			res.charSet('utf-8');
			res.send(201, rows)
	});
})

//------------------------------------------------------------------------------------------------
// PUT 		/meeting_participants 																
//------------------------------------------------------------------------------------------------

server.put('/meeting_participants', function(req, res, next) {
	if (req.params.username === undefined) {
		return next(new restify.InvalidArgumentError('Required fields not supplied.'))
	}

	connection.query("UPDATE meeting_participants SET status='" + req.params.status + "' WHERE username='" + req.params.username + "' AND meetid='" + req.params.meetid + "'", function(err, rows, fields) {
		if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
		
	});
	
	var meetName = undefined;
	var outputMessage = undefined; 
	var userInqestion = req.params.username;
	var userAdmin = undefined; 	
	
	//get Useradmin of meeting
	connection.query("SELECT name, username FROM meeting WHERE meetid=" + req.params.meetid, function(err, rows, fields) {
		if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
		
		userAdmin = rows[0].username;
		meetName = rows[0].name;
		
		
		if (req.status == 'ATTENDING'){
			var outputMessage = userInQestion + " har bekreftet møteinkallingen til " + meetname; 
		}
	
		else if (req.status == 'DECLINED'){
			var outputMessage = userInQestion + " har avslått møteinkallingen til "+ meetname; 
		}
		
		if(req.status == 'ATTENDING' || req.status == 'DECLINED'){
			connection.query("INSERT INTO message (message, time, meetid, owner, isSeen) VALUES('" + outputMessage + "',NOW(),'" + req.params.meetid + "','" + userAdmin + "','" + 0 + "')", function(err, rows, fields) {
				if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))				
		
				res.charSet('utf-8');
				res.send()
			});
		}
	}
})

//------------------------------------------------------------------------------------------------
// DELETE 		/meeting_participants 															
//------------------------------------------------------------------------------------------------

server.del('/meeting_participants', function(req, res, next) {

	if (req.params.username === undefined) {
		return next(new restify.InvalidArgumentError('Required fields not supplied.'))
	}
	if(typeof req.params.meetid !== "undefined" && typeof req.params.username !== "undefined") {
		connection.query("DELETE FROM meeting_participants WHERE username='" + req.params.username + "' AND meetid='" + req.params.meetid +"'", function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
			
			res.charSet('utf-8');
			res.send()
		});
	} else {
		connection.query("DELETE FROM meeting_participants WHERE username='" + req.params.username + "'", function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
			
			res.charSet('utf-8');
			res.send()
		});
	}
})

//------------------------------------------------------------------------------------------------
// Room routes
//------------------------------------------------------------------------------------------------
// Routes: 			GET		/room 									params:roomID									
//					POST 	/room 									params:name,capacity				
// 					PUT		/room 									params:name,capacity															
//					DELETE	/room  									params:roomID			
//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------
// GET 		/room 																	
//------------------------------------------------------------------------------------------------

server.get('/room', function(req, res, next) {
	
	// if room id is specified
	if(typeof req.params.roomID !== "undefined") {
		// get room by id
		connection.query("SELECT * FROM room WHERE roomid='" + req.params.roomID + "'", function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))

			res.charSet('utf-8');
			res.send(rows[0])
		});
	// else: get all rooms
	} else {
		connection.query('SELECT * FROM room', function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))

			res.charSet('utf-8');
			res.send(rows)
		});
	}
})

//------------------------------------------------------------------------------------------------
// POST 	/room 																	
//------------------------------------------------------------------------------------------------

server.post('/room', function(req, res, next) {

	if (req.params.name === undefined || req.params.capacity === undefined ) {
		return next(new restify.InvalidArgumentError('Required fields not supplied.'))
	}

	// create a new room
	connection.query("INSERT INTO room (name, capacity) values ('" + req.params.name + "','" + req.params.capacity + "')", function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
			
			res.charSet('utf-8');
			res.send(201, rows)
	});
})

//------------------------------------------------------------------------------------------------
// PUT 		/room																
//------------------------------------------------------------------------------------------------

server.put('/room', function(req, res, next) {

	if (req.params.roomID === undefined) {
		return next(new restify.InvalidArgumentError('Required fields not supplied.'))
	}

	// update room by id
	connection.query("UPDATE room SET name='" + req.params.name + "', capacity='" + req.params.capacity + "' WHERE roomID='" + req.params.roomID + "'", function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
			
			res.charSet('utf-8');
			res.send()
	});
})

//------------------------------------------------------------------------------------------------
// DELETE 		/room 															
//------------------------------------------------------------------------------------------------

server.del('/room', function(req, res, next) {
	connection.query("DELETE FROM room WHERE roomID='" + req.params.roomID + "'", function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
			
			res.charSet('utf-8');
			res.send()
	});
})


//------------------------------------------------------------------------------------------------
// Message routes
//------------------------------------------------------------------------------------------------
// Routes: 			GET		/message 						params:messid,username,timeFrom									
//					POST 	/message 						params:messid,isSeen							
// 					PUT		/message 																								
//					DELETE	/message  												
//------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------
// GET 		/message 																	
//------------------------------------------------------------------------------------------------

server.get('/message', function(req, res, next) {
	
	// if message id is specified
	if(typeof req.params.messid !== "undefined") {
		// get message by id
		connection.query("SELECT * FROM message WHERE messid='" + req.params.messid + "'", function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))

			res.charSet('utf-8');
			res.send(rows[0])
		});
	// if a username and timeFrom is specified
	} else if(typeof req.params.username !== "undefined" && typeof req.params.timeFrom !== "undefined") {
		// get all messages which is created after a certain point in time
		connection.query("SELECT * FROM message WHERE owner='" + req.params.username + "' AND time > '" + rew.params.timeFrom + "'", function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))

			res.charSet('utf-8');
			res.send(rows)
		});
	// if only username is specified
	} else if(typeof req.params.username !== "undefined") {
		// get all messages which is sent to a specific user
		connection.query("SELECT * FROM message WHERE owner='" + req.params.username + "'", function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))

			res.charSet('utf-8');
			res.send(rows)
		});
	// else: get all messages
	} else {
		connection.query('SELECT * FROM message', function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))

			res.charSet('utf-8');
			res.send(rows)
		});
	}
})

//------------------------------------------------------------------------------------------------
// POST 		/message																
//------------------------------------------------------------------------------------------------

server.post('/message', function(req, res, next) {

	if (req.params.message === undefined || req.params.owner === undefined) {
		return next(new restify.InvalidArgumentError('Required fields not supplied.'))
	}

	// create a new message
	connection.query("INSERT INTO message (message, owner) values ('" + req.params.message + "','" + req.params.username + "')", function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
			
			res.charSet('utf-8');
			res.send(201, rows)
	});
})

//------------------------------------------------------------------------------------------------
// PUT 		/message																
//------------------------------------------------------------------------------------------------

server.put('/message', function(req, res, next) {

	if (req.params.messid === undefined || req.params.isSeen === undefined) {
		return next(new restify.InvalidArgumentError('Required fields not supplied.'))
	}

	// update a message
	connection.query("UPDATE message SET isSeen='" + req.params.isSeen + "' WHERE messid='" + req.params.messid + "'", function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
			
			res.charSet('utf-8');
			res.send()
	});
})

//------------------------------------------------------------------------------------------------
// DELETE 		/message															
//------------------------------------------------------------------------------------------------

server.del('/message', function(req, res, next) {

	if (req.params.messid === undefined) {
		return next(new restify.InvalidArgumentError('Required fields not supplied.'))
	}

	// delete a message from the database
	connection.query("DELETE FROM message WHERE messid='" + req.params.messid + "'", function(err, rows, fields) {
			if (err) return next(new restify.InvalidArgumentError(JSON.stringify(err.errors)))
			
			res.charSet('utf-8');
			res.send()
	});
})



// to pretty format the response JSON
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


// // Listen on port
// server.listen(9004, function() {
// 	console.log('%s listening at %s', server.name, server.url);
// });


