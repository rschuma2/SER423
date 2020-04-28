package ser423.student.server;
/*
 * Copyright 2020 Tim Lindquist,
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
 *
 * Purpose: Example Java JsonRPC server for collection of students.
 *
 * Ser423 Mobile Applications
 * see http://pooh.poly.asu.edu/Mobile
 * @author Tim Lindquist Tim.Lindquist@asu.edu
 *         Software Engineering, CIDSE, IAFSE, ASU Poly
 * @version February, 2020
 */

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Vector;
import java.util.Arrays;

public class Student {
   public String name;
   public int studentid;
   public Vector<String> takes;

   Student(String name, int studentid, String[] courses){
      this.name = name;
      this.studentid = studentid;
      this.takes = new Vector<String>();
      this.takes.addAll(Arrays.asList(courses));
   }

   Student(String jsonStr){
      try{
         JSONObject jo = new JSONObject(jsonStr);
         name = jo.getString("name");
         studentid = jo.getInt("studentid");
         takes = new Vector<String>();
         JSONArray ja = jo.getJSONArray("takes");
         for (int i=0; i< ja.length(); i++){
            takes.add(ja.getString(i));
         }
      }catch (Exception ex){
         System.out.println(this.getClass().getSimpleName()+
                            ": error converting from json string");
      }
   }

   public Student(JSONObject jsonObj){
      try{
         System.out.println("constructor from json received: "+
                            jsonObj.toString());
         name = jsonObj.getString("name");
         studentid = jsonObj.getInt("studentid");
         takes = new Vector<String>();
         JSONArray ja = jsonObj.getJSONArray("takes");
         for (int i=0; i< ja.length(); i++){
            takes.add(ja.getString(i));
         }
      }catch(Exception ex){
         System.out.println(this.getClass().getSimpleName()+
                            ": error converting from json string");
      }
   }

   public JSONObject toJson(){
      JSONObject jo = new JSONObject();
      try{
         jo.put("name",name);
         jo.put("studentid",studentid);
         jo.put("takes",takes);
      }catch (Exception ex){
         System.out.println(this.getClass().getSimpleName()+
                            ": error converting to json");
      }
      return jo;
   }

   public String toJsonString(){
      String ret = "";
      try{
         ret = this.toJson().toString();
      }catch (Exception ex){
         System.out.println(this.getClass().getSimpleName()+
                            ": error converting to json string");
      }
      return ret;
   }
}
