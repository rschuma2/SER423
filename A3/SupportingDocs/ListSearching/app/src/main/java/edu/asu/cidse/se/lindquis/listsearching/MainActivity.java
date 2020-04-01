package edu.asu.cidse.se.lindquis.listsearching;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
public class MainActivity extends AppCompatActivity implements ListView.OnItemClickListener,
                                                               DialogInterface.OnClickListener {

    private EditText nameBox, idBox; // used in the alert dialog for adding a student
    private ListView studentLV;   // the list view for displaying student name and id
    private StudentCollection students;  // a collection of students (serializable)
    private String[] studNames;
    private RadioGroup radGroup;

    private String[] colLabels;
    private int[] colIds;
    private List<HashMap<String,String>> fillMaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        studentLV = (ListView)findViewById(R.id.student_list_view);
        radGroup = (RadioGroup)findViewById(R.id.radioGroup);
        students = new StudentCollection(this);

        /*
        this.prepareAdapter();
        SimpleAdapter sa = new SimpleAdapter(this, fillMaps, R.layout.student_list_item, colLabels, colIds);
        studentLV.setAdapter(sa);
        */
        studentLV.setOnItemClickListener(this);

        setTitle("Students");
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        String query = intent.getStringExtra(SearchManager.QUERY);
        if (Intent.ACTION_SEARCH.equals(intent.getAction()) && !query.toLowerCase().equals("all")) {
            android.util.Log.d(this.getClass().getSimpleName(),"handle intent got search string: "+query);
            int which = radGroup.getCheckedRadioButtonId();
            android.util.Log.d(this.getClass().getSimpleName(),"filtering with which: "+which+" query: "+query+" name id is: "+
            R.id.radioName+" course id is: "+R.id.radioCourse);
            studNames = filter(which, query);
        } else {
            studNames = students.getNames();
        }
        Arrays.sort(studNames);
        prepareAdapter();
        SimpleAdapter sa = new SimpleAdapter(this, fillMaps, R.layout.student_list_item, colLabels, colIds);
        studentLV.setAdapter(sa);
    }

    private String[] filter(int on, String with) {
        String[] ret = new String[]{};
        if(on==R.id.radioName){
            ret = filterOnName(with);
        }else if(on==R.id.radioCourse){
            ret = filterOnCourse(with);
        }
        return ret;
    }

    private String[] filterOnName(String with){
        ArrayList<String> ret = new ArrayList<String>();
        String[] allStudNames = students.getNames();
        for(String aStud: allStudNames){
            if(aStud.toLowerCase().contains(with.toLowerCase())){
                android.util.Log.d(this.getClass().getSimpleName(),"filterName adding: "+aStud+" to return array");
                ret.add(aStud);
            }
        }
        String [] arr = ret.toArray(new String[]{});
        return arr;
    }

    private String[] filterOnCourse(String with){
        ArrayList<String> ret = new ArrayList<String>();
        String[] allStudNames = students.getNames();
        for(String aStudName:allStudNames){
            Student aStud = students.get(aStudName);
            for (Course aCrs: aStud.takes){
                if(aCrs.prefix.toLowerCase().contains(with.toLowerCase())){
                    android.util.Log.d(this.getClass().getSimpleName(),"filterCourse adding: "+aStudName+" to return array");
                    if(!ret.contains(aStudName)) {
                        ret.add(aStudName);
                    }
                }
            }
        }
        String [] arr = ret.toArray(new String[]{});
        return arr;
    }

    // this method generates the data needed to create a new list view simple adapter.
    private void prepareAdapter(){
        colLabels = this.getResources().getStringArray(R.array.col_header);
        colIds = new int[] {R.id.student_firstTV, R.id.student_lastTV, R.id.student_idTV};
        fillMaps = new ArrayList<HashMap<String,String>>();
        HashMap<String,String> titles = new HashMap<>();
        // first row contains column headers
        titles.put("First","First");
        titles.put("Last","Last");
        titles.put("Id","Id");
        fillMaps.add(titles);
        // fill in the remaining rows with first last and student id.
        for (int i = 0; i < studNames.length; i++) {
            String[]firstNLast = studNames[i].split(" ");
            HashMap<String,String> map = new HashMap<>();
            Log.d(this.getClass().getSimpleName(),"mapping: "+firstNLast[0]+" "+firstNLast[1]);
            map.put("First", firstNLast[0]);
            map.put("Last", firstNLast[1]);
            map.put("Id", ((Integer)students.get(this.studNames[i]).studentid).toString());
            android.util.Log.w(this.getClass().getSimpleName(),studNames[i]+" has  id "+
                    ((Integer)students.get(this.studNames[i]).studentid).toString());
            fillMaps.add(map);
        }
    }

    // create the menu items for this activity, placed in the action bar.
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        android.util.Log.d(this.getClass().getSimpleName(), "called onCreateOptionsMenu()");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_menu, menu);
        return super.onCreateOptionsMenu(menu);
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
            case R.id.action_search:
                android.util.Log.d(this.getClass().getSimpleName(),"onOptionsItemSelected -> search");
                onSearchRequested();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //listview.onitemclicklistener method
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        if(position > 0 && position <= studNames.length) {
            android.util.Log.d(this.getClass().getSimpleName(), "in method onItemClick. selected: " + studNames[position-1]);
            Intent displayStud = new Intent(this, StudentDisplayActivity.class);
            displayStud.putExtra("students", students);
            displayStud.putExtra("selected", studNames[position-1]);
            this.startActivityForResult(displayStud, 1);
        }
    }

    // called when the finish() method is called in the StudentDisplayActivity. This occurs
    // when done displaying (and possibly modifying students). In case a student has been removed,
    // must update the list view (via a new adapter).
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        students = data.getSerializableExtra("students")!=null ? (StudentCollection)data.getSerializableExtra("students") : students;
        studNames = students.getNames();
        Arrays.sort(studNames);
        this.prepareAdapter();
        SimpleAdapter sa = new SimpleAdapter(this, fillMaps, R.layout.student_list_item, colLabels, colIds);
        studentLV.setAdapter(sa);
        studentLV.setOnItemClickListener(this);
    }

    private void newStudentAlert() {
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
        if(whichButton == DialogInterface.BUTTON_POSITIVE) {
            String name = nameBox.getText().toString();
            String[] firstNLast = name.split(" ");
            if(firstNLast.length==0 || firstNLast[0].equals("")){
                firstNLast = new String[]{"noFirst","noLast"};
            }else if(firstNLast.length==1){
                firstNLast = new String[]{firstNLast[0],"noLast"};
            }
            int num = idBox.getText().toString().equals("") ? 0 : Integer.parseInt(idBox.getText().toString());
            students.add(new Student(firstNLast[0]+" "+firstNLast[1], num, new Course[]{}));
            studNames = students.getNames();
            Arrays.sort(studNames);
            prepareAdapter();
            SimpleAdapter sa = new SimpleAdapter(this, fillMaps, R.layout.student_list_item, colLabels, colIds);
            studentLV.setAdapter(sa);
        }
    }

}

