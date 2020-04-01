package edu.asu.cidse.se.lindquis.listsearching;

/**
 * Created by lindquis on 11/21/17.
 */

import android.app.Activity;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Iterator;

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
 * Purpose: This app adds to the SimpleAdapter example (same course topic). The Simple adapter
 * shows how list views can use a SimpleAdapter as its data source. The
 * application is students registered for courses. Here we expand that example
 * to demonstrate functionality of searching. The following steps have been followed:
 *
 * 1. Create an xml resources folder and create the searchable.xml in that folder.
 * 2. When the search is performed and handled in the same event (as here) add the
 *    following to manifest.xml for the searchable activity (MainActivity):
 *    a. android:launchMode="singleTop"
 *    b. the action intent: <action android:name="android.intent.action.SEARCH" />
 *    c. a metadata element in the searchable activity's manifest indicating the
 *       name of the searchable.xml file:
 *         <meta-data android:name="android.app.searchable" android:resource="@xml/searchable"/>
 * 3. Add a menu item (see res/menu/activitiy_main.xml for the action bar search menu item
 * 4. Handle the search menu item selection in the Activity's method
 *    public boolean onOptionsItemSelected(MenuItem item)
 *    with a case for action_search. Here, call onSearchRequested to get the search bar to appear
 * 5. Although you can do character by character searching as in iOS, its a bother. The default
 *    in Android is: when the user completes entering the search text and returns/closes the box,
 *    Android notifies the search result activity (same activity here) with a search intent.
 *    We defined the activity to be singleTop to get that intent to be delivered to the onNewIntent
 *    method, rather than creating another MainActivity and delivering it to its onCreate method.
 *    The search string is an extra added to the intent.
 * 6. Android does not support built-in scoped search. Nor does it have at the time of creating
 *    this example, a segmented button. Although there are some availble through open-souce projects.
 *    Instead, in this example to get the same effect as with iOS, we use radio buttons representing
 *    the scope. It may be better to have the scope addressed as part of a dialog before calling
 *    onSearchRequested, although that would also be cumbersome. Got a better solution? Let me know.
 *
 * @author Tim Lindquist Tim.Lindquist@asu.edu
 *         Software Engineering, CIDSE, IAFSE, Arizona State University Polytechnic
 * @version November, 2018
 */
public class StudentCollection extends Object implements Serializable {

    public Hashtable<String, Student> students;
    private static final boolean debugOn = false;

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

