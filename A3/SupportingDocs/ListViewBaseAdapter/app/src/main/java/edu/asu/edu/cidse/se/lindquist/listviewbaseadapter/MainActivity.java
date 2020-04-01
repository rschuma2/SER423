package edu.asu.edu.cidse.se.lindquist.listviewbaseadapter;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
public class MainActivity extends AppCompatActivity implements ListView.OnItemClickListener,
        DialogInterface.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final int SETTINGS_RESULT = 2;
    private EditText nameBox, idBox; // used in the alert dialog for adding a student
    private TextView nameTV, idTV, catTV;
    private ListView studentLV, courseLV;   // the list view for displaying student name and id
    private StudentCollection students;  // a collection of students (serializable)
    private String selectedStud, selectedCourse;
    private String[] availTitles;
    private String[] availPrefixes;
    private Spinner courseSpinner;
    private boolean removingStudDialog, addingStudDialog;
    ListAdapter studentAdapt, courseAdapt;
    private boolean notifyOnAdd, notifyOnRemove;
    private String studentCat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        studentLV = (ListView)findViewById(R.id.student_list_view);
        courseLV = (ListView)findViewById(R.id.course_list_view);
        nameTV = (TextView)findViewById(R.id.stud_nameTV);
        idTV = (TextView)findViewById(R.id.stud_numberTV);
        catTV = (TextView)findViewById(R.id.discipline);
        selectedStud = "Un-Known";
        removingStudDialog = false;
        addingStudDialog = false;
        studentCat = "Software Engineering";

        // get the preference settings
        /*
        notifyOnAdd = false;
        notifyOnRemove = false;
        catTV.setText(studentCat);
        */
        this.logUserSettings();

        // set the initial model
        students = new StudentCollection(this);
        students.resetFromJsonFile(this);
        this.setAdapters();

        studentLV.setOnItemClickListener(this);
        courseLV.setOnItemClickListener(this);
        setTitle("Students In");

        // setup the spinner of all available courses
        availTitles = this.getResources().getStringArray(R.array.course_titles);
        availPrefixes = this.getResources().getStringArray(R.array.course_prefixes);
        courseSpinner = (Spinner)findViewById(R.id.courseSpinner);
        ArrayAdapter<String> anAA = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, availPrefixes);
        courseSpinner.setAdapter(anAA);
        courseSpinner.setOnItemSelectedListener(this);
        selectedCourse = availPrefixes.length>0 ? availPrefixes[0] : getString(R.string.unknown);


    }

    // create the menu items for this activity, placed in the action bar.
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        android.util.Log.d(this.getClass().getSimpleName(), "called onCreateOptionsMenu()");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void setAdapters(){
        android.util.Log.d(this.getClass().getSimpleName(),"in Set Adapters selected student: "+this.selectedStud);
        String[] studNames = students.getNames();
        studentAdapt = new ListAdapter(this,studNames);
        studentLV.setAdapter(studentAdapt);
        if(studNames.length > 0 ) {
            if(this.selectedStud.equals("Un-Known")) {
                this.selectedStud = studNames[0];
            }
            String[] crsNames = students.get(this.selectedStud).getCourses();
            courseAdapt = new ListAdapter(this, crsNames);
            if (crsNames.length > 0) {
                //this.selectedCourse = crsNames[0];
            }
            courseLV.setAdapter(courseAdapt);
        }
        android.util.Log.d(this.getClass().getSimpleName(),"setAdapters selected student is now: "+this.selectedStud);
        nameTV.setText(this.selectedStud);
        idTV.setText("ID: "+students.get(this.selectedStud).studentid);
    }

    // Handle selections of action bar menu items. These are defined a menu.xml resource that is
    // inflated by the onCreateOptionsMenu method. Note that the home action bar (back arrow) is
    // placed by default in any action bar for an activity that is defined with a parent activity
    // in manifest.xml. In this example, StudentDisplayActivity's action bar has an arrow which
    // appears in the left of the action bar. Selecting that arrow is handled by the case:
    // onOptionsItemSelected method for that activity in:    case android.R.id.home:
    // The built-in icons that you can place in an action bar (and elsewhere) are
    // defined in the android.R.drawable static final class. See:
    //   https://developer.android.com/reference/android/R.drawable.html
    // these are referenced in the menu.xml, such as:
    //         android:icon="@android:drawable/ic_menu_delete"
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        android.util.Log.d(this.getClass().getSimpleName(), "called onOptionsItemSelected()");
        switch (item.getItemId()) {
            case R.id.action_add:
                android.util.Log.d(this.getClass().getSimpleName(),"onOptionsItemSelected -> add");
                this.newStudentAlert();
                return true;
            case R.id.action_remove:
                android.util.Log.d(this.getClass().getSimpleName(),"onOptionsItemSelected -> remove");
                this.removeStudentAlert();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //listview.onitemclicklistener method
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        LinearLayout ll = (LinearLayout)view;
        TextView tv = (TextView)ll.getChildAt(0);
        android.util.Log.d(this.getClass().getSimpleName(),"onItemClick(AdapterView) text is: "+tv.getText());
        String selectedTxt = (String)tv.getText();
        Student aStud = students.get(selectedStud);
        String[] studs = students.getNames();
        if(Arrays.asList(studs).contains(selectedTxt)) {
            // user selected a student
            if (position >= 0 && position < studs.length) {
                android.util.Log.d(this.getClass().getSimpleName(), "in method onItemClick. selected: " + studs[position]);
                this.selectedStud = selectedTxt;
                String[] crsNames = students.get(this.selectedStud).getCourses();
                courseAdapt = new ListAdapter(this, crsNames);
                if (crsNames.length > 0) {
                    this.selectedCourse = crsNames[0];
                }
                courseLV.setAdapter(courseAdapt);
                nameTV.setText(this.selectedStud);
                idTV.setText("ID: "+students.get(this.selectedStud).studentid);
            }
        }else{
            // user selected a course taken by the selected student.
            if (position >= 0 && position < aStud.takes.size()) {
                String prefix = aStud.takes.get(position).prefix;
                String title = aStud.takes.get(position).title;
                android.util.Log.d(this.getClass().getSimpleName(), "in method onItemClick. selected: " +
                        prefix + " " + title);
                this.selectedCourse = prefix+" "+title;
            }
        }
    }

    // AdapterView.OnItemSelectedListener method. Called when spinner selection Changes
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        android.util.Log.d(this.getClass().getSimpleName(),"onItemSelected(AdapterView)");
        this.selectedCourse = courseSpinner.getSelectedItem().toString();
        android.util.Log.d(this.getClass().getSimpleName(),"Spinner item "+
                courseSpinner.getSelectedItem().toString() + " selected.");
    }

    // AdapterView.OnItemSelectedListener method. Called when spinner selection Changes
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        android.util.Log.d(this.getClass().getSimpleName(),"In onNothingSelected: No item selected");

    }


    // add the course selected from all courses spinner from the student's takes, if not already present
    public void addClicked(View v) {
        android.util.Log.d(this.getClass().getSimpleName(),"in addClicked");
        // the course is not unknown and then the course isn't already in takes
        this.selectedCourse = this.courseSpinner.getSelectedItem().toString();
        if(!this.selectedCourse.equalsIgnoreCase("unknown") &&
                this.findPrefix(students.get(this.selectedStud).takes,this.selectedCourse)<0){
            Course aCourse = new Course(this.selectedCourse,
                    availTitles[Arrays.asList(availPrefixes).indexOf(this.selectedCourse)]);
            (students.get(this.selectedStud).takes).add(aCourse);
        }
        this.setAdapters();
    }

    // remove the course selected from all courses spinner from the student's takes, if present
    public void removeClicked(View v) {
        int isWhere = this.findPrefix(students.get(this.selectedStud).takes,this.selectedCourse);
        android.util.Log.d(this.getClass().getSimpleName(),"in removeClicked selected course "+this.selectedCourse+" isWhere is: "+isWhere);
        // the course is not unknown and then the course is in takes
        if(!this.selectedCourse.equalsIgnoreCase("unknown") && isWhere > -1){
            students.get(this.selectedStud).takes.remove(isWhere);
        }
        this.selectedCourse = "unknown";
        this.setAdapters();
    }

    // returns -1 if the course prefix is not found in takes. Otherwise, the index into the vector
    // where it is found.
    private int findPrefix(Vector<Course> takes, String courseName) {
        String[] preNTitle = courseName.split(" ");
        int ret = -1;
        for(int i=0; i<takes.size(); i++){
            Course aCrs = takes.get(i);
            if (aCrs.prefix.equalsIgnoreCase(preNTitle[0])){
                ret = i;
            }
        }
        return ret;
    }

    // show an alert view for the user to confirm removing the selected student
    private void removeStudentAlert() {
        android.util.Log.d(this.getClass().getSimpleName(),"in removeStudentAlert");
        addingStudDialog = false;
        removingStudDialog = true;
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Remove Student "+this.selectedStud+"?");
        dialog.setNegativeButton("Cancel", this);
        dialog.setPositiveButton("Remove", this);
        dialog.show();
    }

    // show an altert soliciting information for adding a new student
    private void newStudentAlert() {
        android.util.Log.d(this.getClass().getSimpleName(),"in newStudentAlert");
        addingStudDialog = true;
        removingStudDialog = false;
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Student Name and id");
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        this.nameBox = new EditText(this);
        nameBox.setHint("Name");
        layout.addView(nameBox);

        this.idBox = new EditText(this);
        idBox.setHint("Id Number");
        idBox.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(idBox);
        dialog.setView(layout);
        dialog.setNegativeButton("Cancel", this);
        dialog.setPositiveButton("Add", this);
        dialog.show();
    }

    // DialogInterface.onClickListener method. Gets called when negative or positive button is clicked
    // in the Alert Dialog created by the newStudentAlert method.
    @Override
    public void onClick(DialogInterface dialog, int whichButton) {
        android.util.Log.d(this.getClass().getSimpleName(),"onClick positive button? "+
                (whichButton==DialogInterface.BUTTON_POSITIVE));
        if(this.removingStudDialog && !this.addingStudDialog) {
            if (whichButton == DialogInterface.BUTTON_POSITIVE) {
                students.remove(this.selectedStud);
                if(this.notifyOnRemove){
                    Toast.makeText(this, "You have removed student "+this.selectedStud,
                            Toast.LENGTH_SHORT).show();
                }
                this.selectedStud = "Un-Known";
                this.setAdapters();
            }
        }else if(!this.removingStudDialog && this.addingStudDialog){
            if (whichButton == DialogInterface.BUTTON_POSITIVE) {
                String name = nameBox.getText().toString();
                String[] firstNLast = name.split(" ");
                if(firstNLast.length==0 || firstNLast[0].equals("")){
                    firstNLast = new String[]{"noFirst","noLast"};
                }else if(firstNLast.length==1){
                    firstNLast = new String[]{firstNLast[0],"noLast"};
                }
                int num = idBox.getText().toString().equals("") ? 0 : Integer.parseInt(idBox.getText().toString());
                students.add(new Student(firstNLast[0]+" "+firstNLast[1], num, new Course[]{}));
                this.selectedStud = firstNLast[0]+" "+firstNLast[1];
                if(this.notifyOnAdd){
                    Toast.makeText(this, "You have added student "+this.selectedStud,
                            Toast.LENGTH_SHORT).show();
                }
                this.setAdapters();
            }
        }
    }

    // called when the settings activity completes / back button
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case SETTINGS_RESULT:
                logUserSettings();
                break;
            default:
                break;
        }
    }

    private void logUserSettings() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean notifyOnAdd = sharedPrefs.getBoolean("pref_notify_add",false);
        android.util.Log.w(this.getClass().getSimpleName(),"Notify on Add is: "+notifyOnAdd);
        this.notifyOnAdd = notifyOnAdd;

        boolean notifyOnRemove = sharedPrefs.getBoolean("pref_notify_remove",false);
        android.util.Log.w(this.getClass().getSimpleName(),"Notify on remove is: "+notifyOnRemove);
        this.notifyOnRemove = notifyOnRemove;

        String title = sharedPrefs.getString("pref_title",getString(R.string.default_title));
        android.util.Log.w(this.getClass().getSimpleName(),"Students title is: "+title);
        this.studentCat = title;
        catTV.setText(this.studentCat);
    }


}
