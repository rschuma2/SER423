package edu.asu.cisde.se.schumacher.a3_1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Vector;

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
public class PlaceDisplayActivity extends AppCompatActivity implements ListView.OnItemClickListener,
        AdapterView.OnItemSelectedListener,
        DialogInterface.OnClickListener {

    private PlaceLibrary students;
    private TextView stud_numberTV;
    private ListView courseLV;
    private String selectedStud, selectedCourse;

    private String[] availTitles;
    private String[] availPrefixes;
    private String[] colLabels;
    private int[] colIds;
    private Spinner courseSpinner;
    private List<HashMap<String,String>> fillMaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_display);
        courseLV = (ListView)findViewById(R.id.course_list_view);
        stud_numberTV = (TextView)findViewById(R.id.stud_numberTV);
        availTitles = this.getResources().getStringArray(R.array.course_titles);
        availPrefixes = this.getResources().getStringArray(R.array.course_prefixes);
        courseSpinner = (Spinner)findViewById(R.id.courseSpinner);
        ArrayAdapter<String> anAA = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, availPrefixes);
        courseSpinner.setAdapter(anAA);
        courseSpinner.setOnItemSelectedListener(this);
        selectedCourse = availPrefixes.length>0 ? availPrefixes[0] : getString(R.string.unknown);

        Intent intent = getIntent();
        students = intent.getSerializableExtra("students")!=null ? (PlaceLibrary)intent.getSerializableExtra("students") :
                new PlaceLibrary(this);
        selectedStud = intent.getStringExtra("selected")!=null ? intent.getStringExtra("selected") : "unknown";
        Student aStud = students.get(selectedStud);
        stud_numberTV.setText("ID # "+Integer.toString(aStud.studentid));

        this.prepareAdapter();
        SimpleAdapter sa = new SimpleAdapter(this, fillMaps, R.layout.course_list_item, colLabels, colIds);
        courseLV.setAdapter(sa);
        courseLV.setOnItemClickListener(this);

        // set up a back button to return to the view main activity / view
        try {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }catch(Exception ex){
            android.util.Log.d(this.getClass().getSimpleName(),"exception action bar: "+ex.getLocalizedMessage());
        }
        setTitle(aStud.name+"'s Courses");
    }

    // create the menu items for this activity, placed in the action bar.
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        android.util.Log.d(this.getClass().getSimpleName(), "called onCreateOptionsMenu()");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.student_display_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /*
     * Implement onOptionsItemSelected(MenuItem item){} to handle clicks of buttons that are
     * in the action bar.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        android.util.Log.d(this.getClass().getSimpleName(), "called onOptionsItemSelected() id: "+item.getItemId()
                +" title "+item.getTitle());
        switch (item.getItemId()) {
            // the user selected the up/home button (left arrow at left of action bar)
            case android.R.id.home:
                android.util.Log.d(this.getClass().getSimpleName(),"onOptionsItemSelected -> home");
                Intent i = new Intent();
                i.putExtra("students", students);
                this.setResult(RESULT_OK,i);
                finish();
                return true;
            // the user selected the action (garbage can) to remove the student
            case R.id.action_remove:
                android.util.Log.d(this.getClass().getSimpleName(),"onOptionsItemSelected -> remove");
                this.removeStudentAlert();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void prepareAdapter(){
        colLabels = this.getResources().getStringArray(R.array.col_header_course);
        colIds = new int[] {R.id.course_prefix, R.id.course_title};
        Student aStud = students.get(selectedStud);

        // the model
        // first row is header strings for the columns
        fillMaps = new ArrayList<HashMap<String,String>>();
        HashMap<String,String> titles = new HashMap<>();
        titles.put("Prefix","Prefix");
        titles.put("Title","Title");
        fillMaps.add(titles);
        // now add the data for the remaining rows
        Vector<Course> takes = this.sortTakes(aStud.takes);
        for (int i = 0; i < takes.size(); i++) {
            Course aCourse = takes.get(i);
            HashMap<String,String> map = new HashMap<>();
            Log.d(this.getClass().getSimpleName(),"mapping: "+aCourse.prefix+" "+aCourse.title);
            map.put("Prefix", aCourse.prefix);
            map.put("Title", aCourse.title);
            fillMaps.add(map);
        }
    }

    //listview.onitemclicklistener method
    // log the selection when its not the header row.
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        Student aStud = students.get(selectedStud);
        if (position > 0 && position < aStud.takes.size()+1) {
            String prefix = aStud.takes.get(position - 1).prefix;
            String title = aStud.takes.get(position - 1).title;
            android.util.Log.d(this.getClass().getSimpleName(), "in method onItemClick. selected: " +
                    prefix + " " + title);
        }
    }

    // AdapterView.OnItemSelectedListener method. Called when spinner selection Changes
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.selectedCourse = courseSpinner.getSelectedItem().toString();
        android.util.Log.d(this.getClass().getSimpleName(),"Spinner item "+
                courseSpinner.getSelectedItem().toString() + " selected.");
    }

    // AdapterView.OnItemSelectedListener method. Called when spinner selection Changes
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        android.util.Log.d(this.getClass().getSimpleName(),"In onNothingSelected: No item selected");

    }

    // returns -1 if the course prefix is not found in takes. Otherwise, the index into the vector
    // where it is found.
    private int findPrefix(Vector<Course> takes, String prefix) {
        int ret = -1;
        for(int i=0; i<takes.size(); i++){
            Course aCrs = takes.get(i);
            if (aCrs.prefix.equalsIgnoreCase(prefix)){
                ret = i;
            }
        }
        return ret;
    }

    // add the course selected from all courses spinner from the student's takes, if not already present
    public void addClicked (View v) {
        // the course is not unknown and then the course isn't already in takes
        if(!this.selectedCourse.equalsIgnoreCase("unknown") &&
                this.findPrefix(students.get(this.selectedStud).takes,this.selectedCourse)<0){
            Course aCourse = new Course(this.selectedCourse,
                    availTitles[Arrays.asList(availPrefixes).indexOf(this.selectedCourse)]);
            (students.get(this.selectedStud).takes).add(aCourse);
        }
        this.prepareAdapter();
        SimpleAdapter sa = new SimpleAdapter(this, fillMaps, R.layout.course_list_item, colLabels, colIds);
        courseLV.setAdapter(sa);

    }

    // remove the course selected from all courses spinner from the student's takes, if present
    public void removeClicked (View v) {
        int isWhere = this.findPrefix(students.get(this.selectedStud).takes,this.selectedCourse);
        // the course is not unknown and then the course is in takes
        if(!this.selectedCourse.equalsIgnoreCase("unknown") && isWhere > -1){
            students.get(this.selectedStud).takes.remove(isWhere);
        }
        this.prepareAdapter();
        SimpleAdapter sa = new SimpleAdapter(this, fillMaps, R.layout.course_list_item, colLabels, colIds);
        courseLV.setAdapter(sa);
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

    // show an alert view for the user to confirm removing the selected student
    private void removeStudentAlert() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Remove Student "+this.selectedStud+"?");
        dialog.setNegativeButton("Cancel", this);
        dialog.setPositiveButton("Remove", this);
        dialog.show();
    }

    // DialogInterface.onClickListener method. Gets called when negative or positive button is clicked
    // in the Alert Dialog created by the newStudentAlert method.
    @Override
    public void onClick(DialogInterface dialog, int whichButton) {
        android.util.Log.d(this.getClass().getSimpleName(),"onClick positive button? "+
                (whichButton==DialogInterface.BUTTON_POSITIVE));
        if(whichButton == DialogInterface.BUTTON_POSITIVE) {
            // ok, so remove the student and return the modified model to main activity
            students.remove(this.selectedStud);
            Intent i = new Intent();
            i.putExtra("students", students);
            this.setResult(RESULT_OK,i);
            finish();
        }
    }

}
