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
// Routes: 			GET		/employee 													
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
			res.send(rows)
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
// PUT 		/user/:id 																
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



// Listen on port
server.listen(process.env.PORT, function() {
	console.log('%s listening at %s', server.name, server.url);
});

