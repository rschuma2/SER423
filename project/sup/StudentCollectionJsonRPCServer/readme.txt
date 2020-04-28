Author: Tim Lindquist (Tim.Lindquist@asu.edu), ASU Polytechnic, CIDSE, SE
Version: February 2020

See http://pooh.poly.asu.edu/Mobile

Purpose: demonstrate Json-RPC
This program is executable on both Mac OS X and Windows.

The project includes a student collection service in the form of a JsonRPC
(JavaScript Object Notation Remote Procedure Call) server. Android and iOS
clients of the service are distributed in class as separate examples.
Communication between the service is done using JSON-RPC over http. The
server makes methods available to clients (such as, getNames, get, add, and remove).
Clients call one of the server's methods using the JsonRPC protocol (see link  below).
The purpose of the example is to demonstrate JSON and JSON-RPC, and accessing networks
via native apps. Reference the following sources for background on these technologies:

JSON (JavaScript Object Notation):
 http://en.wikipedia.org/wiki/JSON
 The JSON web site: http://json.org/

JSON-RPC (JSON Remote Procedure Call):
 http://www.jsonrpc.org
 http://en.wikipedia.org/wiki/JSON-RPC

This example depends on the following frameworks:
1. Ant
   see: http://ant.apache.org/
2. Json for the jdk as implemented by Doug Crockford.
   See: https://github.com/stleary/JSON-java

The student collection service is deployed as a stand alone Java app.

To build and run the example, you will need to have Ant installed on
your system.

If you don't already have Ant, see:
http://ant.apache.org/

This example assumes that you will use Ant from the command line to build
the client and service. The Ant build file includes targets to compile the
service.

ant build.java.server

run the server from the command line with the statement:

java -cp classes:lib/json.jar ser423.student.server.StudentServer 8080

this command needs to be modified slightly to be execute on Windows.

Or, run the created executable jar file with:
java -jar lib/studentserver.jar 8080

You can change the port to which the server listens for client connections
with the command line argument.

end
