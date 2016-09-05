-- Seeds the database with example data --

-- First, initialize employers and employees

INSERT INTO Users (user_id, classifier, first_name, last_name, email, company, biography) VALUES
( 'ddoodm', 'Employer', 'Deinyon', 'Davies', 'deinyond@gmail.com', 'DDOODM Software Inc.', 'I made this place.' );

INSERT INTO Users (user_id, classifier, first_name, last_name, email, biography) VALUES
( 'mikej', 'Employee', 'Mike', 'Johnson', 'mikeyj@gmail.com', 'This is Mike; your humble employee.' );

-- Now, post jobs by employers

INSERT INTO JobDescriptions (description_id, title, details, listing_date, end_date, payment) VALUES
(
    '97340674-71a1-11e6-8b77-86f30ca893d3',
    'Make a ZDoom Clone in C#',
    'ZDoom is a popular source-port of the classic first-person-shooter, Doom. Could you please re-make ZDoom in C# for me?',
    {d '2016-02-14'},
    {d '2017-02-01'},
    1024
);
INSERT INTO Jobs (job_id, employer_id, description_id, state) VALUES
(
    '836661b4-71a1-11e6-8b77-86f30ca893d3',
    'ddoodm',
    '97340674-71a1-11e6-8b77-86f30ca893d3',
    'Proposed'
);