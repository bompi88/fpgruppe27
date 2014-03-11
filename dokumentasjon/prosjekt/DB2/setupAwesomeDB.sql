CREATE TABLE User (
	username VARCHAR(15) NOT NULL,
	name VARCHAR(50),
	email VARCHAR(50),
	password VARCHAR(50),
	PRIMARY KEY(username)
	);

CREATE TABLE Meeting (
	meetid INT NOT NULL AUTO_INCREMENT,
    name = VARCHAR(50),
	description VARCHAR(255),
	startDate DATE,
    endDate DATE,
	startTime TIME,
	endTime TIME,
	place VARCHAR(50),
	roomid INT REFERENCES Room(roomid),
    isAppointment BOOLEAN,
	username VARCHAR(15) REFERENCES User(username)
	PRIMARY KEY(meetid)

	);

CREATE TABLE Room (
	roomid INT NOT NULL, AUTO_INCREMENT,
	name VARCHAR(50),
    capacity INT,
	PRIMARY KEY(roomid)
	);

CREATE TABLE MeetingParticipants (
	meetid INT,
	username VARCHAR(15),
	status VARCHAR(10),
	PRIMARY KEY(meetid, username),
	FOREIGN KEY(meetid) REFERENCES Meeting(meetid) ON DELETE CASCADE,
	FOREIGN KEY(username) REFERENCES User(username) ON DELETE CASCADE
	);

CREATE TABLE Message (
	messid 	INT 	NOT NULL, 
	message 	VARCHAR(255);
	date 	DATE;
	timestamp TIME;
	owner 	 VARCHAR(15);
	isSeen	BOOLEAN 
	)

	
