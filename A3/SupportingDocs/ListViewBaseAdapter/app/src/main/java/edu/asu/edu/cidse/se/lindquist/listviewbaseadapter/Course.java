package edu.asu.edu.cidse.se.lindquist.listviewbaseadapter;

import org.json.JSONObject;

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
public class Course {
    public String prefix;
    public String title;

    public Course(String prefix, String title){
        this.prefix = prefix;
        this.title = title;
    }

    public Course(JSONObject jobj){
        try{
            this.prefix = jobj.optString("prefix","unknown");
            this.title = jobj.optString("title","unknown");
        }catch(Exception ex){
            android.util.Log.d(this.getClass().getSimpleName(),"exception in json contstructor");
        }
    }
}
