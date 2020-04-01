package edu.asu.cidse.se.lindquis.simpleadapterlist;

/**
 * Copyright © 2018 Tim Lindquist,
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p/>
 * Purpose: An app to demonstrate a ListView with a SimpleAdapter
 * The simple adapter is not so simple, but it does allow a list with multiple row components
 * to be constructed from a pre-defined adapter. Both activities of this example create and
 * manipulate a listView using a simple adapter
 *
 * @author Tim Lindquist Tim.Lindquist@asu.edu
 *         Software Engineering, CIDSE, IAFSE, Arizona State University Polytechnic
 * @version November 20, 2018
 */
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Vector;
import java.util.Arrays;

public class Student implements Serializable {

    private static final boolean debugOn = false;

    public String name;
    public int studentid;
    public Vector<Course> takes;

    public Student(String name, int studentid, Course[] courses){
        this.name = name;
        this.studentid = studentid;
        this.takes = new Vector<Course>();
        this.takes.addAll(Arrays.asList(courses));
    }

    public Student(String jsonStr){
        try{
            JSONObject jo = new JSONObject(jsonStr);
            name = jo.getString("name");
            studentid = jo.getInt("studentid");
            takes = new Vector<Course>();
            JSONArray ja = jo.optJSONArray("takes");
            for (int i=0; i< ja.length(); i++){
                takes.add(new Course(ja.getJSONObject(i)));
            }
        }catch (Exception ex){
            android.util.Log.d(this.getClass().getSimpleName(), "error getting Student from json string");
        }
    }

    public Student(JSONObject jsonObj){
        try{
            debug("constructor from json received: " + jsonObj.toString());
            name = jsonObj.optString("name","unknown");
            studentid = jsonObj.optInt("studentid",0);
            takes = new Vector<Course>();
            JSONArray ja = jsonObj.getJSONArray("takes");
            for (int i=0; i< ja.length(); i++){
                takes.add(new Course(ja.getJSONObject(i)));
            }
        }catch(Exception ex){
            android.util.Log.d(this.getClass().getSimpleName(), "error getting Student from json string");
        }
    }

    public JSONObject toJson(){
        JSONObject jo = new JSONObject();
        try{
            jo.put("name",name);
            jo.put("studentid",studentid);
            jo.put("takes",takes);
        }catch (Exception ex){
            android.util.Log.d(this.getClass().getSimpleName(), "error getting Student from json string");
        }
        return jo;
    }

    public String toJsonString(){
        String ret = "";
        try{
            ret = this.toJson().toString();
        }catch (Exception ex){
            android.util.Log.d(this.getClass().getSimpleName(), "error getting Student from json string");
        }
        return ret;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Student ").append(name).append(" has id ");
        sb.append(studentid).append(" and takes courses ");
        for (int i=0; i<takes.size(); i++){
            sb.append(takes.get(i).toString()).append(" ");
        }
        return sb.toString();
    }

    private void debug(String message) {
        if (debugOn)
            android.util.Log.d(this.getClass().getSimpleName(), message);
    }

}
