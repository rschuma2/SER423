DROP TABLE studenttakes;
DROP TABLE student;
DROP TABLE course;

CREATE TABLE student (
  name TEXT,
  major TEXT,
  email TEXT,
  studentnum NUMBER,
  studentid INTEGER PRIMARY KEY);

CREATE TABLE course (
  coursename TEXT,
  courseid INTEGER PRIMARY KEY);

CREATE TABLE studenttakes (
  studentid INTEGER,
  courseid INTEGER,
  FOREIGN KEY(studentid) REFERENCES student(studentid) ON DELETE CASCADE,
  FOREIGN KEY(courseid) REFERENCES course(courseid) ON DELETE CASCADE );

INSERT INTO student VALUES
   ('Tim Turner','Software','tim.turner@asu.edu',101,1);
INSERT INTO student VALUES
   ('Harry Holms','Software','harry.holms@asu.edu',102,2);
INSERT INTO student VALUES
   ('Sam Smith','Engineering','sammy@asu.edu',103,3);
INSERT INTO course VALUES
   ('Ser421 Web',1);
INSERT INTO course VALUES
   ('Ser502 Langs',2);
INSERT INTO course VALUES
   ('Cse445 Dist Apps',3);
INSERT INTO course VALUES
   ('Ser423 Mobile',4);
INSERT INTO studenttakes VALUES
   (1,1);
INSERT INTO studenttakes VALUES
   (1,2);
INSERT INTO studenttakes VALUES
   (2,3);
INSERT INTO studenttakes VALUES
   (2,1);
INSERT INTO studenttakes VALUES
   (3,4);
INSERT INTO studenttakes VALUES
   (3,1);
