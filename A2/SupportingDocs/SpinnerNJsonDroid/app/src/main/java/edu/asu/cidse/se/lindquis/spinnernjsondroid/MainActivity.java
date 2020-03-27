package edu.asu.cidse.se.lindquis.spinnernjsondroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/*
 * Copyright 2018 Tim Lindquist,
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
 * and the use of a Spinner with an Array Adapter. It also shows
 * how to access a data file (define an input stream reader) that
 * is added to the resources raw folder res/raw/student.json
 *
 * Ser423 Mobile Applications
 * see http://pooh.poly.asu.edu/Mobile
 * @author Tim Lindquist Tim.Lindquist@asu.edu
 *         Software Engineering, CIDSE, IAFSE, ASU Poly
 * @version November 25, 2018
 */

public class MainActivity extends Activity implements  AdapterView.OnItemSelectedListener{

    private Student aStudent;
    private String selectedCourse;
    private EditText nameET, studIDET, newcourseET;
    private Spinner courseSpinner;
    private ArrayList<String> courses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nameET = (EditText)findViewById(R.id.nameET);
        studIDET = (EditText)findViewById(R.id.studIDET);
        newcourseET = (EditText)findViewById(R.id.newcourseET);
        courseSpinner = (Spinner)findViewById(R.id.courseSpinner);

        InputStream is = this.getApplicationContext().getResources().openRawResource(R.raw.student);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        try {
            // note that the json is in a single line of input so no need to read line-by-line
            JSONObject studentJson = new JSONObject(new JSONTokener(br.readLine()));
            aStudent = new Student(studentJson);
            nameET.setText(aStudent.name);
            studIDET.setText(Integer.toString(aStudent.studentid));
            courses = new ArrayList<>(aStudent.takes);
            ArrayAdapter<String> anAA = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, courses);
            courseSpinner.setAdapter(anAA);
            courseSpinner.setOnItemSelectedListener(this);
            selectedCourse = courses.size()>0 ? courses.get(0) : getString(R.string.unknown);
        }catch (Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),"exception reading student.json");
        }

    }

    // AdapterView.OnItemSelectedListener method. Called when spinner selection Changes
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedCourse = courseSpinner.getSelectedItem().toString();
        android.util.Log.d(this.getClass().getSimpleName(),"Spinner item "+
                courseSpinner.getSelectedItem().toString() + " selected.");
    }

    // AdapterView.OnItemSelectedListener method. Called when spinner selection Changes
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        android.util.Log.d(this.getClass().getSimpleName(),"In onNothingSelected: No item selected");

    }

    public void addClicked (View v) {
        android.util.Log.d(this.getClass().getSimpleName(),"Add clicked newcourseET is: "+newcourseET.getText().toString());
        if(newcourseET.getText().toString().trim().length() > 0) {
            String newEntry = newcourseET.getText().toString().trim();
            if (! courses.contains(newEntry)){
                courses.add(newEntry);
                ((ArrayAdapter)courseSpinner.getAdapter()).notifyDataSetChanged();
                int i = courses.indexOf(newEntry);
                courseSpinner.setSelection(i);
            }
        }
    }
}
