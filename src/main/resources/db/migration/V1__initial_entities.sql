create table TEAM_ENTITY(
ID SERIAL PRIMARY KEY,
NAME VARCHAR(255)
);

create table MATCH_ENTITY(
ID SERIAL PRIMARY KEY,
CREATION_TIMESTAMP TIMESTAMP NOT NULL,
START_TIMESTAMP TIMESTAMP NOT NULL,
END_TIMESTAMP TIMESTAMP,
MATCH_STATUS VARCHAR(50),
TEAM1_ID SERIAL REFERENCES TEAM_ENTITY(ID) ,
TEAM2_ID SERIAL REFERENCES TEAM_ENTITY(ID)
);

create table PLAYER_ENTITY(
ID SERIAL PRIMARY KEY,
NAME VARCHAR(255)
);

create table PLAYER_EVENT_ENTITY(
ID SERIAL PRIMARY KEY,
 PLAYER_ID SERIAL references PLAYER_ENTITY(ID),
 MATCH_ID SERIAL references MATCH_ENTITY(ID),
 EVENT_MINUTE INT,
 EVENT_TYPE VARCHAR(50)
);

create table TEAM_ENTITY_PLAYERS(
TEAM_ENTITY_ID SERIAL references TEAM_ENTITY(ID),
PLAYERS_ID SERIAL references PLAYER_ENTITY(ID)
);

create table MATCH_ENTITY_PLAYER_EVENTS(
MATCH_ENTITY_ID SERIAL references MATCH_ENTITY(ID),
PLAYER_EVENTS_ID SERIAL references PLAYER_EVENT_ENTITY(ID)
);