-- some test users
INSERT INTO beers.users(username, password, first_name, last_name, email, is_admin)
VALUES ('pesho','pass1','Petar','Petrov','pesho@abv.bg',true);

INSERT INTO beers.users(username, password, first_name, last_name, email, is_admin)
VALUES ('gosho','pass2','Georgi','Georgiev','gosho@abv.bg',false);

INSERT INTO beers.users(username, password, first_name, last_name, email, is_admin)
VALUES ('nadya','pass3','Nadejda','Georgieva','nadya@abv.bg',false);

-- some test styles
INSERT INTO beers.styles(name)
VALUES ('Special Ale');

INSERT INTO beers.styles(name)
VALUES ('English Porter');

INSERT INTO beers.styles(name)
VALUES ('Indian Pale Ale');

-- some test beers
INSERT INTO beers.beers(name, abv)
VALUES('Glarus English Ale',4.6);

INSERT INTO beers.beers(name, abv)
VALUES('Rhombus Porter',5.0);

INSERT INTO beers.beers(name, abv)
VALUES('Opasen Char',6.6);
