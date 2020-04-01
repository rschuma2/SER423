package edu.asu.edu.cidse.se.lindquist.listviewbaseadapter;

import android.app.Activity;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.Iterator;

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
public class StudentCollection {
    public Hashtable<String, Student> students;
    private static final boolean debugOn = true;

    public StudentCollection(Activity parent) {
        debug("creating a new student collection");
        students = new Hashtable<String, Student>();
        try {
            this.resetFromJsonFile(parent);
        } catch (Exception ex) {
            android.util.Log.d(this.getClass().getSimpleName(), "error resetting from students json file" + ex.getMessage());
        }
    }

    private void debug(String message) {
        if (debugOn)
            android.util.Log.d(this.getClass().getSimpleName(), "debug: " + message);
    }

    public boolean resetFromJsonFile(Activity parent) {
        boolean ret = true;
        try {
            students.clear();
            InputStream is = parent.getApplicationContext().getResources().openRawResource(R.raw.students);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            // note that the json is in a multiple lines of input so need to read line-by-line
            StringBuffer sb = new StringBuffer();
            while (br.ready()) {
                sb.append(br.readLine());
            }
            String placesJsonStr = sb.toString();
            JSONObject studentsJson = new JSONObject(new JSONTokener(placesJsonStr));
            Iterator<String> it = studentsJson.keys();
            while (it.hasNext()) {
                String sName = it.next();
                JSONObject aStud = studentsJson.optJSONObject(sName);
                debug("importing student named " + sName + " json is: " + aStud.toString());
                if (aStud != null) {
                    Student stu = new Student(aStud.toString());
                    students.put(sName, stu);
                }
            }
        } catch (Exception ex) {
            android.util.Log.d(this.getClass().getSimpleName(), "Exception reading json file: " + ex.getMessage());
            ret = false;
        }
        return ret;
    }

    public boolean add(Student aStud) {
        boolean ret = true;
        debug("adding student named: " + ((aStud == null) ? "unknown" : aStud.name));
        try {
            students.put(aStud.name, aStud);
        } catch (Exception ex) {
            ret = false;
        }
        return ret;
    }

    public boolean remove(String aName) {
        debug("removing student named: " + aName);
        return ((students.remove(aName) == null) ? false : true);
    }

    public String[] getNames() {
        String[] ret = {};
        debug("getting " + students.size() + " student names.");
        if (students.size() > 0) {
            ret = (String[]) (students.keySet()).toArray(new String[0]);
        }
        return ret;
    }

    public String getById(int id) {
        String ret = "unknown";
        String[] keys = (String[]) (students.keySet()).toArray(new String[0]);
        for (int i = 0; i < keys.length; i++) {
            Student aStud = students.get(keys[i]);
            if (aStud.studentid == id) {
                ret = aStud.name;
                break;
            }
        }
        return ret;
    }

    public Student get(String aName) {
        Student ret = new Student("unknown", 0, new Course[]{new Course("empty", "empty")});
        Student aStud = students.get(aName);
        if (aStud != null) {
            ret = aStud;
        }
        return ret;
    }
}
