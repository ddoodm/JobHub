CREATE SCHEMA ADMIN;

------ Jobs ------
CREATE TABLE JobDescriptions (
  description_id  NUMERIC         NOT NULL PRIMARY KEY,
  title           VARCHAR(255)    NOT NULL,
  details         LONG VARCHAR,
  listing_date    DATE            NOT NULL,
  end_date        DATE,
  payment_id      NUMERIC         NOT NULL,
  commentbox_id   NUMERIC         NOT NULL,
  attachmentset_id NUMERIC        NOT NULL,

  FOREIGN KEY (payment_id) REFERENCES Payments(payment_id),
  FOREIGN KEY (commentbox_id) REFERENCES CommentBoxes(commentbox_id),
  FOREIGN KEY (attachmentset_id) REFERENCES AttachmentSets(attachmentset_id)
);

CREATE TABLE Jobs (
  job_id          NUMERIC   NOT NULL PRIMARY KEY,
  employer_id     NUMERIC   NOT NULL,
  employee_id     NUMERIC,
  description_id  NUMERIC   NOT NULL,
  state           SMALLINT  NOT NULL,

  FOREIGN KEY (employee_id) REFERENCES Employees(user_id),
  FOREIGN KEY (employer_id) REFERENCES Employers(user_id),
  FOREIGN KEY (description_id) REFERENCES JobDescriptions(description_id)
);

CREATE TABLE JobPayloads (
  payload_id      NUMERIC         NOT NULL PRIMARY KEY,
  job_id          NUMERIC         NOT NULL,
  version         VARCHAR(32)     NOT NULL,
  details         LONG VARCHAR    NOT NULL,
  submission_date DATE            NOT NULL,
  commentbox_id   NUMERIC         NOT NULL,
  attachmentset_id NUMERIC        NOT NULL,

  FOREIGN KEY (job_id) REFERENCES Jobs(job_id),
  FOREIGN KEY (commentbox_id) REFERENCES CommentBoxes(commentbox_id),
  FOREIGN KEY (attachmentset_id) REFERENCES AttachmentSets(attachmentset_id)
);

CREATE TABLE AttachmentSets(
  attachmentset_id  NUMERIC     NOT NULL PRIMARY KEY
);

CREATE TABLE Attachments(
  attachment_id   NUMERIC         NOT NULL PRIMARY KEY,
  title           VARCHAR(255)    NOT NULL,
  details         LONG VARCHAR,
  file_path       VARCHAR(1024)   NOT NULL,
  commentbox_id   NUMERIC         NOT NULL,
  attachmentset_id NUMERIC        NOT NULL,

  FOREIGN KEY (commentbox_id) REFERENCES CommentBoxes(commentbox_id),
  FOREIGN KEY (attachmentset_id) REFERENCES AttachmentSets(attachmentset_id)
);

------ Communication ------
CREATE TABLE CommentBoxes(
  commentbox_id   NUMERIC         NOT NULL PRIMARY KEY
);

CREATE TABLE Comment(
  comment_id      NUMERIC         NOT NULL PRIMARY KEY,
  commentbox_id   NUMERIC         NOT NULL,
  user_id         NUMERIC         NOT NULL,
  message         LONG VARCHAR    NOT NULL,

  FOREIGN KEY (commentbox_id) REFERENCES CommentBoxes(commentbox_id),
  FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

------ Users ------
CREATE TABLE Users(
  user_id         NUMERIC         NOT NULL PRIMARY KEY,
  handle          VARCHAR(64)     NOT NULL,
  first_name      VARCHAR(255),
  last_name       VARCHAR(255),
  email           VARCHAR(512),
  phone           VARCHAR(64),
  biography       LONG VARCHAR

  -- Has many conversations
);

CREATE TABLE Employers(
  user_id         NUMERIC         NOT NULL PRIMARY KEY,
  FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

CREATE TABLE Employees(
  user_id         NUMERIC         NOT NULL PRIMARY KEY,
  FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

CREATE TABLE UserExperience(
  experience_id   NUMERIC     NOT NULL,
  user_id         NUMERIC     NOT NULL,
  PRIMARY KEY (experience_id, user_id),
  FOREIGN KEY (experience_id) REFERENCES UnitsOfExperience(experience_id),
  FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

CREATE TABLE UnitsOfExperience(
  experience_id   NUMERIC       NOT NULL PRIMARY KEY,
  name            VARCHAR(255)  NOT NULL
);

------ Other ------
CREATE TABLE Payments(
  payment_id      NUMERIC       NOT NULL PRIMARY KEY,
  amount_due      DECIMAL       NOT NULL,
  amount_paid     DECIMAL

  -- TODO: This could be updated with alternative payment types
);