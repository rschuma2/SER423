package edu.asu.cidse.se.lindquis.studentcoursesqliteapp;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Copyright (c) 2019 Tim Lindquist,
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
 * Purpose: an Android App to demonstrate using a Sqlite3 database.
 * In this example, I've extended the SqliteOpenHelper class to provide functionality
 * which facilitates having an initial database in the raw directory of the app bundle.
 * Since the bundle can't be altered by the app, the app detects whether its already been
 * copied to the app's database path (remember that every app is a separate android user,
 * and thus has its own separate internal storage area -- including database and files).
 * If the file already exists in the database folder then its not copied, but opened from
 * there. If it does not already exist, then it is copied from the bundle to initialize it.
 * Be careful in using this approach that you don't write over good app data with the initial
 * database. Also, the setting of the android:allowBackup property in AndroidManifest.xml helps
 * determine whether the internal data is saved when the app is deleted and re-installed.
 *
 * The basic approach with respect to connections within multiple threads is:
 * The SqliteOpenHelper object holds one database connection.
 * One helper instance, one db connection. Even if you share the helper object and
 * use it from multiple threads, there is still just one connection.
 * The SqliteDatabase object uses java mechanisms to serialize access. So, no matter how many threads
 * share the connection, calls to the database file are serialized.
 * One helper, one db connection, all of whose accesses to the Sqlite database are serialized.
 * If you try to write to a database from separate threads with separate connections simultaneously,
 * one may fail. It will not wait till as it should. Multiple accesses are not serialized.
 * Worse, it may simply not write your change, and if you don’t call an appropriate version of
 * insert or update on the SQLiteDatabase, you won’t get an exception. You’ll just get a message in
 * LogCat. So, if you want to access a db from multiple threads, then they should share one helper object.
 *
 * To help debugging a sqlite3 database app's from a command line execute:
 *   adb devices
 *   if only 1 device then:
 *   adb shell run-as edu.asu.cidse.se.lindquis.studentcoursesqliteapp
 *   otherwise
 *   adb -s emulator-5554 shell run-as edu.asu.cidse.se.lindquis.studentcoursesqliteapp
 *   this starts a shell in the home directory of the app on the emulator running as the app's user.
 *   Now change to the files folder with (or databases):
 *   cd files
 *   to get a full listing of all files
 *   ls -l
 *   sqlite3 coursedb.db
 *   this will start sqlite3 and wait for sqlite3 commands. See the readme.txt from StudentSqlite3
 *   You can now enter sqlite queries and changes to help determine whether your app is doing what
 *   you expect. To exit sqlite3:
 *   .quit
 *   to exit adb's shell
 *   exit
 *   For help enter: adb help
 *
 * @author Tim Lindquist Tim.Lindquist@asu.edu
 *         Software Engineering, CIDSE, IAFSE, Arizona State University Polytechnic
 * @version March 24, 2019
 */
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        DialogInterface.OnClickListener, TextView.OnEditorActionListener, SearchView.OnQueryTextListener  {

    private Button addButt, removeButt;
    private EditText nameET, majorET, emailET, numET;
    private Spinner studentSpinner, coursesSpinner;
    private String selectedStudent, searchString;
    private String[] studs;
    private ArrayAdapter<String> studAdapter, courseAdapter;
    private Menu menu;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addButt = (Button)findViewById(R.id.addButt);
        removeButt = (Button)findViewById(R.id.removeButt);
        nameET = (EditText)findViewById(R.id.nameET);
        majorET = (EditText)findViewById(R.id.majorET);
        emailET = (EditText)findViewById(R.id.emailET);
        numET = (EditText)findViewById(R.id.numET);
        studentSpinner = (Spinner)findViewById(R.id.studentNamesSpinner);
        coursesSpinner = (Spinner)findViewById(R.id.courseSpinner);

        this.selectedStudent = this.setupStudentSpinner();
        loadFields();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Log.d(this.getClass().getSimpleName(), "in onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;

        // setup the search in action bar
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (android.widget.SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);
        return true;
    }

    // OnQueryListener methods for search menu item
    public boolean onQueryTextChange(String query){
        //android.util.Log.d(this.getClass().getSimpleName(), "in onQueryTextChange: " + query);
        return false;
    }

    public boolean onQueryTextSubmit(String query){
        android.util.Log.d(this.getClass().getSimpleName(), "in onQueryTextSubmit: " + query);
        this.searchString = query;
        //MenuItemCompat.collapseActionView((MenuItem)menu.findItem(R.id.action_search));
        searchView.clearFocus();
        android.util.Log.d(this.getClass().getSimpleName(), "Search string is " + this.searchString);
        return false;
    }

    private String setupStudentSpinner(){
        String ret = "unknown";
        try{
            CourseDB db = new CourseDB((Context)this);
            SQLiteDatabase crsDB = db.openDB();
            Cursor cur = crsDB.rawQuery("select name from student;", new String[]{});
            ArrayList<String> al = new ArrayList<String>();
            while(cur.moveToNext()){
                try{
                    al.add(cur.getString(0));
                }catch(Exception ex){
                    android.util.Log.w(this.getClass().getSimpleName(),"exception stepping thru cursor"+ex.getMessage());
                }
            }
            studs = (String[]) al.toArray(new String[al.size()]);
            ret = (studs.length>0 ? studs[0] : "unknown");
            studAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, studs);
            studentSpinner.setAdapter(studAdapter);
            studentSpinner.setOnItemSelectedListener(this);
        }catch(Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),"unable to setup student spinner");
        }
        return ret;
    }

    private void loadFields(){
        try{
            CourseDB db = new CourseDB((Context)this);
            SQLiteDatabase crsDB = db.openDB();
            Cursor cur = crsDB.rawQuery("select major,email,studentnum from student where name=? ;",
                    new String[]{selectedStudent});
            String major = "unknown";
            String email = "any@any.com";
            String num = "000";
            while (cur.moveToNext()){
                major = cur.getString(0);
                email = cur.getString(1);
                num = Integer.toString(cur.getInt(2));
            }
            nameET.setText(selectedStudent);
            majorET.setText(major);
            emailET.setText(email);
            numET.setText(num);
            cur.close();
            cur = crsDB.rawQuery("select course.coursename FROM student,course,studenttakes WHERE student.studentid=studenttakes.studentid and course.courseid=studenttakes.courseid and student.name=?",
                    new String[]{selectedStudent});
            ArrayList<String> al = new ArrayList<>();
            while(cur.moveToNext()){
                al.add(cur.getString(0));
            }
            String[] courses = (String[]) al.toArray(new String[al.size()]);
            if (courses.length == 0){
                courses = new String[]{"unknown"};
            }
            courseAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, courses);
            coursesSpinner.setAdapter(courseAdapter);
            coursesSpinner.setOnItemSelectedListener(this);
            cur.close();
            crsDB.close();
            db.close();
        } catch (Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),"Exception getting student info: "+
                    ex.getMessage());
        }
    }

    public void addClicked(View v){
        android.util.Log.d(this.getClass().getSimpleName(), "add Clicked. Adding: " + this.nameET.getText().toString());
        try{
            CourseDB db = new CourseDB((Context)this);
            SQLiteDatabase crsDB = db.openDB();
            //String insert = "insert into student values('"+this.nameET.getText().toString()+"','"+
            //                 this.majorET.getText().toString()+"','"+this.emailET.getText().toString()+"',"+
            //                 this.numET.getText().toString()+",null);";
            //crsDB.execSQL(insert);
            ContentValues hm = new ContentValues();
            hm.put("name", this.nameET.getText().toString());
            hm.put("major", this.majorET.getText().toString());
            hm.put("email", this.emailET.getText().toString());
            int studNum = Integer.parseInt(this.numET.getText().toString());
            hm.put("studentnum", studNum);
            crsDB.insert("student",null, hm);
            crsDB.close();
            db.close();
            String addedName = this.nameET.getText().toString();
            setupStudentSpinner();
            this.selectedStudent = addedName;
            this.studentSpinner.setSelection(Arrays.asList(studs).indexOf(this.selectedStudent));
            //this.loadFields();
        } catch (Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),"Exception adding student information: "+
                    ex.getMessage());
        }
    }

    public void removeClicked(View v){
        android.util.Log.d(this.getClass().getSimpleName(), "remove Clicked");
        //String delete = "delete from student where student.name='"+this.selectedStudent+"';";
        String delete = "delete from student where student.name=?;";
        //delete from studenttakes where courseid=(select courseid from course where coursename='Ser421 Web/Mobile');
        //delete from course where coursename='Ser421 Web/Mobile';
        //delete from student where student.name='Tim Turner';
        try {
            CourseDB db = new CourseDB((Context) this);
            SQLiteDatabase crsDB = db.openDB();
            //crsDB.execSQL(delete);
            crsDB.execSQL(delete, new String[]{this.selectedStudent});
            crsDB.close();
            db.close();
        }catch(Exception e){
            android.util.Log.w(this.getClass().getSimpleName()," error trying to delete student");
        }
        this.selectedStudent = this.setupStudentSpinner();
        this.loadFields();
    }

    public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
        // note that inputType and keyboard actions imeOptions must be defined to manage the keyboard
        // these can be defined in the xml as an attribute of the EditText.
        // returning false from this method
        android.util.Log.d(this.getClass().getSimpleName(), "onEditorAction: keycode " +
                ((event == null) ? "null" : event.toString()) + " actionId " + actionId);
        if(actionId== EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE){
            android.util.Log.d(this.getClass().getSimpleName(),"entry is: "+v.getText().toString());
        }
        return false; // without returning false, the keyboard will not disappear or move to next field
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        android.util.Log.d(this.getClass().getSimpleName(), "onClick with which= " +which);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.studentNamesSpinner) {
            this.selectedStudent = studentSpinner.getSelectedItem().toString();
            android.util.Log.d(this.getClass().getSimpleName(), "studentSpinner item selected " + selectedStudent);
            this.loadFields();
        }else{
            android.util.Log.d(this.getClass().getSimpleName(), "CourseSpinner item selected " + coursesSpinner.getSelectedItem());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        android.util.Log.d(this.getClass().getSimpleName(), "onNothingSelected");
    }

}
