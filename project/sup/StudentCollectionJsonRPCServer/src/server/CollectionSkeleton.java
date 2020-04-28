package ser423.student.server;

import java.net.*;
import java.io.*;
import java.util.*;
import org.json.JSONObject;
import org.json.JSONArray;

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
 * computing class at ASU Poly. The application provides a jsonrpc student service.
 *
 * see http://pooh.poly.asu.edu/Mobile
 * @author Tim Lindquist Tim.Lindquist@asu.edu
 *         Software Engineering, CIDSE, IAFSE, ASU Poly
 * @version February 2020
 *
 **/
public class CollectionSkeleton extends Object {

   StudentCollection mLib;

   public CollectionSkeleton (StudentCollection mLib){
      this.mLib = mLib;
   }

   public String callMethod(String request){
      JSONObject result = new JSONObject();
      try{
         JSONObject theCall = new JSONObject(request);
         String method = theCall.getString("method");
         int id = theCall.getInt("id");
         JSONArray params = null;
         if(!theCall.isNull("params")){
            params = theCall.getJSONArray("params");
         }
         result.put("id",id);
         result.put("jsonrpc","2.0");
         if(method.equals("resetFromJsonFile")){
            mLib.resetFromJsonFile();
            result.put("result",true);
         }else if(method.equals("saveToJsonFile")){
            boolean saved = mLib.saveToJsonFile();
            result.put("result",saved);
         }else if(method.equals("remove")){
            String sName = params.getString(0);
            boolean removed = mLib.remove(sName);
            result.put("result",removed);
         }else if(method.equals("add")){
            JSONObject studJson = params.getJSONObject(0);
            Student stud = new Student(studJson);
            boolean added = mLib.add(stud);
            result.put("result",added);
         }else if(method.equals("get")){
            String sName = params.getString(0);
            Student stud = mLib.get(sName);
            result.put("result",stud.toJson());
         }else if(method.equals("getNames")){
            String[] names = mLib.getNames();
            JSONArray resArr = new JSONArray();
            for (int i=0; i<names.length; i++){
               resArr.put(names[i]);
            }
            result.put("result",resArr);
         }
      }catch(Exception ex){
         System.out.println("exception in callMethod: "+ex.getMessage());
      }
      System.out.println("returning: "+result.toString());
      return "HTTP/1.0 200 Data follows\nServer:localhost:8080\nContent-Type:text/plain\nContent-Length:"+(result.toString()).length()+"\n\n"+result.toString();
   }
}

