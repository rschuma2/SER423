package edu.asu.edu.cidse.se.lindquist.listviewbaseadapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

/**
 * Copyright © 2020 Tim Lindquist,
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
 * Purpose: This is a single view app which provides the ability to manipulate a list of students
 * and the courses they take. Three model classes: Course, Student, and StudentCollection
 * provide the model. The model is (re)initialized each time the app is started to include all
 * of the students in the resources/raw/students.json file. The app provides the ability to
 * manipulate the collection by providing the ability to add more students (Action bar plus button),
 * to remove a student (by selecting row from the student of students to be removed and clicking
 * the Action Bar garbage can icon). The listview at the bottom of the view displays the current
 * set of courses in which the selected student is enrolled. The add and remove buttons provide
 * for adding courses and removing courses from a student's schedule. The Add button add the Spinner
 * selected course if its not already on the student's schedule. The remove button removes the
 * course in the selected row in the course list.
 *
 * @author Tim Lindquist Tim.Lindquist@asu.edu
 *         Software Engineering, CIDSE, IAFSE, Arizona State University Polytechnic
 * @version February, 2020
 */
public class Student {
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

    public String[] getCourses(){
        ArrayList<String> ret = new ArrayList<String>();
        takes = this.sortTakes(takes);
        for (int i=0; i<takes.size(); i++){
            ret.add(takes.get(i).prefix+" "+takes.get(i).title);
        }
        return ret.toArray(new String[]{});
    }

    // sort ascending the vector of courses by their prefix
    private Vector<Course> sortTakes(Vector<Course> takes){
        Vector<Course> ret = takes;
        for(int i=0; i<ret.size(); i++){
            for(int j=i+1; j<ret.size(); j++){
                if(ret.get(i).prefix.compareTo(ret.get(j).prefix) > 0){
                    Course tmp = ret.remove(j);
                    ret.add(i,tmp);
                }
            }
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
