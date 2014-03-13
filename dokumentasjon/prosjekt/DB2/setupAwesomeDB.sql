CREATE TABLE IF NOT EXISTS employee (
	username VARCHAR(15) NOT NULL,
	name VARCHAR(50),
	email VARCHAR(50),
	password VARCHAR(50),
	PRIMARY KEY(username)
	);

CREATE TABLE IF NOT EXISTS meeting (
	meetid INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50),
	description VARCHAR(255),
	startTime TIMESTAMP,
	endTime TIMESTAMP,
	place VARCHAR(50),
	roomid INT REFERENCES room(roomid),
    isAppointment BOOLEAN,
	username VARCHAR(15) REFERENCES employee(username),
	PRIMARY KEY(meetid)

	);

CREATE TABLE IF NOT EXISTS room (
	roomid INT NOT NULL AUTO_INCREMENT,
	name VARCHAR(50),
    capacity INT,
	PRIMARY KEY(roomid)
	);

CREATE TABLE IF NOT EXISTS meeting_participants (
	meetid INT,
	username VARCHAR(15),
	status VARCHAR(10),
	PRIMARY KEY(meetid, username),
	FOREIGN KEY(meetid) REFERENCES meeting(meetid) ON DELETE CASCADE,
	FOREIGN KEY(username) REFERENCES employee(username) ON DELETE CASCADE
	);

CREATE TABLE IF NOT EXISTS message (
	messid INT NOT NULL AUTO_INCREMENT, 
	message VARCHAR(255),
	time TIMESTAMP,
	owner VARCHAR(15),
	isSeen BOOLEAN,
	PRIMARY KEY(messid)
	)

	
