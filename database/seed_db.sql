-- Seeds the database with example data --

-- First, initialize employers and employees

INSERT INTO Users (user_id, first_name, last_name, email, phone, company, biography) VALUES
( 'ddoodm', 'Deinyon', 'Davies', 'deinyond@gmail.com', '0408000820', 'DDOODM Software Inc.', 'I made this place.' );
INSERT INTO Employers (user_id) VALUES ('ddoodm')

INSERT INTO Users (user_id, first_name, last_name, email, phone, biography) VALUES
( 'mikej', 'Mike', 'Johnson', 'mikeyj@gmail.com', '1234253634', 'This is Mike; your humble employee.' );
INSERT INTO Employees (user_id) VALUES ('mikej')