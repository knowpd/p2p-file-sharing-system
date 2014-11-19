CREATE TABLE USERS
(
	userName			varchar(20),
	userId				varchar(20) NOT NULL,
	passWord			varchar(20) NOT NULL,
	reminderQuestion	integer NOT NULL,
	reminderAnswer		varchar(20)
);
ALTER TABLE USERS
   ADD CONSTRAINT USERS_PK Primary Key (userId);


CREATE TABLE ACTIVEPEERS
(
	PvtIP               varchar(20) NOT NULL,
	ipAddress			varchar(20) NOT NULL,
	userId				varchar(20) NOT NULL,
	userType			varchar(10) NOT NULL
);
ALTER TABLE ACTIVEPEERS
   ADD CONSTRAINT ACTIVEPEERS_PK Primary Key (ipAddress);


CREATE TABLE FILELOCATION
(
	fileName 			varchar(50),
	fileSignature		varchar(1024) NOT NULL,
	ipAddress			varchar(20) NOT NULL,
	fileAbstract		varchar(128) NOT NULL,
	PvtIPAdd			varchar(20) NOT NULL
);



