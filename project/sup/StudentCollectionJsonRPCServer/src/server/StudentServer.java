package ser423.student.server;

import java.net.*;
import java.io.*;
import java.util.*;

/**
 * Copyright (c) 2020 Tim Lindquist,
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p/>
 * Purpose: This class is part of an example developed for the mobile
 * computing class at ASU Poly to demonstrate Java Json RPC server using http.
 *
 * see http://pooh.poly.asu.edu/Mobile
 * @author Tim Lindquist Tim.Lindquist@asu.edu
 *         Software Engineering, CIDSE, IAFSE, ASU Poly
 * @version February 2020
 *
 **/
public class StudentServer extends Thread {
   
   private Socket conn;
   private int id;
   StudentCollection mLib;
   CollectionSkeleton skeleton;

   public StudentServer (Socket sock, int id, StudentCollection mLib) {
      this.conn = sock;
      this.id = id;
      this.mLib = mLib;
      skeleton = new CollectionSkeleton(mLib);
   }

   public void run() {
      try {
         OutputStream outSock = conn.getOutputStream();
         InputStream inSock = conn.getInputStream();
         byte clientInput[] = new byte[4096]; // up to 1024 bytes in a message.
         int numr = inSock.read(clientInput,0,4096);
         if (numr != -1) {
            //System.out.println("read "+numr+" bytes. Available: "+
            //                   inSock.available());
            Thread.sleep(200);
            int ind = numr;
            while(inSock.available()>0){
               numr = inSock.read(clientInput,ind,4096-ind);
               ind = numr + ind;
               Thread.sleep(200);
            }
            String clientString = new String(clientInput,0,ind);
            //System.out.println("read from client: "+id+" the string: "
            //                   +clientString);
            if(clientString.indexOf("{")>=0){
               String request = clientString.substring(clientString.indexOf("{"));
               //System.out.println("request from client: "+request);
               String response = skeleton.callMethod(request);
               byte clientOut[] = response.getBytes();
               outSock.write(clientOut,0,clientOut.length);
              //System.out.println("response is: "+response);
            }else{
               System.out.println("No json object in clientString: "+
                                  clientString);
            }
         }
         inSock.close();
         outSock.close();
         conn.close();
      } catch (Exception e) {
         System.out.println("Can't get I/O for the connection.");
      }
   }

   public static void main (String args[]) {
      Socket sock;
      StudentCollection mLib;
      int id=0;
      int portNo = 8080;
      try {
         if (args.length < 1) {
            System.out.println("Usage: java -jar lib/server.jar portNum");
            System.exit(0);
         }
         portNo = Integer.parseInt(args[0]);
         mLib = new StudentCollectionImpl();
         if (portNo <= 1024) portNo=8080;
         ServerSocket serv = new ServerSocket(portNo);
         while (true) {
            System.out.println("Student server waiting for connects on port "
                               +portNo);
            sock = serv.accept();
            System.out.println("Student server connected to client: "+id);
            StudentServer myServerThread = new StudentServer(sock,id++,mLib);
            myServerThread.start();
         }
      } catch(Exception e) {e.printStackTrace();}
   }
}
