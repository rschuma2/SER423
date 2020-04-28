Author: Tim Lindquist
Version: March 2017
Project: Ser423 Mobile Computing Database Example

This project provides the background for creating a course database
using sqlite3. Both iOS and Android support applications that manipulate
persistent data using a sqlite database. This activity produces a
database that will be used in both Android and iOS sample database apps.

Following through this readme.txt file and setting up the database requires
a command line version of sqlite3. On Mac OSX, you can install sqlite3 using
MacPorts with the following command:
 sudo port install sqlite3

Versions are available for Windows, linux and Mac OSX at:
   http://www.sqlite.org/download.html

SQLite is an in process (light-weight) relational database manager.

Instructions for using sqlite3
***************************************************
I. Creating and Populating the Database Using sqlite3 from the terminal

   1. Create a new database coursedb
      sqlite3 coursedb.db

   2. To populate the database from the file initcoursedb.sql
       sqlite>.read initcoursedb.sql
      The first time you execute this it will give errors as sqlite3
      tries to drop tables that don't exist yet.

   3. To Create a backup of the database int the file backupcoursedb.db
       sqlite>.backup main afile.db
  
   4. To recover the database from the backup created above
       sqlite>.restore main afile.db

   5. To query or modify the database, use any of the queries below from
      the sqlite> prompt. Be sure to end the sql statement with a semi-colon

   6. To exit sqlite interpreter
      sqlite>.quit

II Some Queries. Execute each from the sqlite prompt. How are they executed
   by a mobile (iOS or Android) app?
a. Who are the students and their id numbers?
    select name,studentid from student;

b. Add a new entry into the student table whose id is one larger than all
   other ids in the table.
    insert into student (name,studentid) values ('John Jones',(SELECT MAX(studentid) FROM student)+1);
   compare the above insert to the following:
    insert into student values ('John Jones','Software','jj@asu.edu',104,(SELECT MAX(studentid) FROM student)+1);

c. update John's student entry to indicate his major
    update student set major='Psychology' where name='John Jones';

d. How many entries are there in the student table?
     SELECT COUNT(studentid) FROM student;

e. What courses is student Tim Turner taking?
    SELECT course.coursename,student.studentid,course.courseid FROM student,course,studenttakes WHERE student.studentid=studenttakes.studentid and course.courseid=studenttakes.courseid and student.name='Tim Turner';

f. Insert a new course:
    insert into course values ('Cse445 Distributed Software',(SELECT MAX(courseid) FROM course)+1);

g. Update a field in a table's row. Change Harry's major to Mathematics.
    update student set major='Mathematics' where name='Harry Holmstrom';

h. What happens when you execute the following sql statement?
    insert into student values('Fred Fischer', 'basket weaving', 'ff@asu.edu', 105, null);
   What is Fred's id?

i. What happens when you execute either of the following sql statements?
    insert into studenttakes values (6,8);
    delete from course where coursename='Ser421 Web/Mobile';
   Re-initialize the database and execute these statements again after doing
   each of the following:
    sqlite>pragma foreign_keys=ON;
    sqlite>pragma foreign_keys=OFF;
   Note that in the initcoursedb.sql file that the studenttakes table is
   dropped before either the course of student tables. Defining foreign key
   constraints would prohibit dropping either while studenttakes records
   refer to them.
   With foreign keys on, how should you delete the course Ser421?
   delete from studenttakes where courseid=(select courseid from course where coursename='Ser421 Web/Mobile');
   delete from course where coursename='Ser421 Web/Mobile';

   alternate between the following designations in the creation of the table studenttakes:
CREATE TABLE studenttakes (
  studentid INTEGER,
  courseid INTEGER,
  FOREIGN KEY(studentid) REFERENCES student(studentid) ON DELETE CASCADE,
  FOREIGN KEY(courseid) REFERENCES course(courseid) ON DELETE CASCADE );

CREATE TABLE studenttakes (
  studentid INTEGER,
  courseid INTEGER,
  FOREIGN KEY(studentid) REFERENCES student(studentid),
  FOREIGN KEY(courseid) REFERENCES course(courseid));

   what differs when the table is defined one way versus the other? Answer: cascading deletes would
   cause the corresponding rows in studentstakes to be deleted when a student or course is deleted.
   
j. How do I check to see whether a table already exists in a sqlite3
   database? Here's a query to check whether the course table exists.
   If it does exist then the results set includes a single entry.
    select name from sqlite_master where type='table' and name='course';
