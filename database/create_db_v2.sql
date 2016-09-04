CREATE SCHEMA AIP;

CREATE TABLE Attachments(
  attachment_id   VARCHAR(64)     NOT NULL PRIMARY KEY,
  title           VARCHAR(255)    NOT NULL,
  file_path       VARCHAR(1024)   NOT NULL
);

------ Users ------
CREATE TABLE Users(
  user_id         VARCHAR(64)         NOT NULL PRIMARY KEY,
  first_name      VARCHAR(255),
  last_name       VARCHAR(255),
  email           VARCHAR(512),
  company         VARCHAR(255),
  biography       LONG VARCHAR

  -- Has many conversations
);

CREATE TABLE Employers(
  user_id         VARCHAR(64)         NOT NULL PRIMARY KEY,
  FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

CREATE TABLE Employees(
  user_id         VARCHAR(64)         NOT NULL PRIMARY KEY,
  FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

------ Jobs ------
CREATE TABLE JobDescriptions (
  description_id  VARCHAR(64)     NOT NULL PRIMARY KEY,
  title           VARCHAR(255)    NOT NULL,
  details         LONG VARCHAR,
  listing_date    DATE            NOT NULL,
  end_date        DATE,
  payment         DOUBLE          NOT NULL,
  attachment_id   VARCHAR(64),

  FOREIGN KEY (attachment_id) REFERENCES Attachments(attachment_id)
);

CREATE TABLE Jobs (
  job_id          VARCHAR(64)   NOT NULL PRIMARY KEY,
  employer_id     VARCHAR(64)   NOT NULL,
  employee_id     VARCHAR(64),
  description_id  VARCHAR(64)   NOT NULL,
  state           SMALLINT  NOT NULL,

  FOREIGN KEY (employee_id) REFERENCES Employees(user_id),
  FOREIGN KEY (employer_id) REFERENCES Employers(user_id),
  FOREIGN KEY (description_id) REFERENCES JobDescriptions(description_id)
);

CREATE TABLE JobPayloads (
  payload_id      VARCHAR(64)         NOT NULL PRIMARY KEY,
  job_id          VARCHAR(64)         NOT NULL,
  version         VARCHAR(32)     NOT NULL,
  details         LONG VARCHAR    NOT NULL,
  submission_date DATE            NOT NULL,
  attachment_id   VARCHAR(64)         NOT NULL,

  FOREIGN KEY (job_id) REFERENCES Jobs(job_id),
  FOREIGN KEY (attachment_id) REFERENCES Attachments(attachment_id)
);

