package edu.asu.cidse.se.lindquis.studentjson;
/*
 * Copyright 2016 Tim Lindquist,
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
 * Purpose: Example classes conversion to/from json
 * This example shows the use of Android's
 * org.json package in creating json string of a Java object.
 *
 * Ser423 Mobile Applications
 * see http://pooh.poly.asu.edu/Mobile
 * @author Tim Lindquist Tim.Lindquist@asu.edu
 *         Software Engineering, CIDSE, IAFSE, ASU Poly
 * @version January 2016
 */

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Vector;

/**
 * Created by lindquis on 1/14/16.
 */
public class Student {
    public String name;
    public int studentid;
    public Vector<String> takes;
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
            android.util.Log.w(this.getClass().getSimpleName(),
                    "error converting to/from json");
        }
    }
    public String toJsonString(){
        String ret = "";
        try{
            JSONObject jo = new JSONObject();
            jo.put("name",name);
            jo.put("studentid", studentid);
            JSONArray ja = new JSONArray(takes);
            jo.put("takes",ja);
            ret = jo.toString();
        }catch (Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),
                    "error converting to/from json");
        }
        return ret;
    }
}
