package edu.asu.cidse.se.lindquis.simpleadapterlist;

import android.app.AlertDialog;
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
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
 * Purpose: An app to demonstrate a ListView with a SimpleAdapter
 * The simple adapter is not so simple, but it does allow a list with multiple row components
 * to be constructed from a pre-defined adapter. Both activities of this example create and
 * manipulate a listView using a simple adapter
 *
 * This example also demonstrates navigating among activities via intents, and passing
 * serializable data among activities. In this case, the underlying model is a collection
 * of students. The main activity shows a list of students. When one is selected, it
 * creates an intent for the StudentDisplayActivity starting it expecting a result --
 * which in this case is a modified collection of students. To see the mechanics of
 * moving from one activity to the next and back, passing data in both directions see the
 * following methods:
 * MainActivity --> onItemClick which creates an intent for StudentDisplay and passes data via intent
 * MainActivity --> onActivityResult which through the return intent gets the modified model,
 *                  recreates the simple adapter for the list causing it to be re-rendered.
 * StudentDisplayActivity --> onCreate gets the intent used to create it and if it has an extra
 *                  for students, uses it for creating the model. It also get the selected student
 *                  whose information is to be displayed from the intent.
 * StudentDisplayActivity --> onOptionsItemSelected in the case of Home (back to main activity)
 *                  it creates the intent for holding the result, sets the result and finishes.
 *                  this causes main activity's onActivityResult to be called.
 *                  In the case of Remove (trash can in action bar), an alert is displayed asking
 *                  for confirmation to remove the student. The remove (OK) button from the alert
 *                  causes the dialogue interface's onClick method to be called, which has the same
 *                  effect as the Home button (creates an intent, adds modified students model to the
 *                  intent, and finishes the student display activity causing the main activity's
 *                  onActivityResult to be called.
 *
 * @author Tim Lindquist Tim.Lindquist@asu.edu
 *         Software Engineering, CIDSE, IAFSE, Arizona State University Polytechnic
 * @version March, 2020
 */
public class MainActivity extends AppCompatActivity implements ListView.OnItemClickListener, DialogInterface.OnClickListener {

    private EditText nameBox, idBox; // used in the alert dialog for adding a student
    private ListView studentLV;   // the list view for displaying student name and id
    private StudentCollection students;  // a collection of students (serializable)
    private String[] studNames;

    private String[] colLabels;
    private int[] colIds;
    private List<HashMap<String,String>> fillMaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        studentLV = (ListView)findViewById(R.id.student_list_view);

        students = new StudentCollection(this);
        studNames = students.getNames();

        this.prepareAdapter();
        SimpleAdapter sa = new SimpleAdapter(this, fillMaps, R.layout.student_list_item, colLabels, colIds);
        studentLV.setAdapter(sa);
        studentLV.setOnItemClickListener(this);

        setTitle("Students");
    }

    // this method generates the data needed to create a new list view simple adapter.
    private void prepareAdapter(){
        /*
        * setup the data necessary for the constructor for a simple adapter:
        * SimpleAdapter sa = new SimpleAdapter(this, fillMaps, R.layout.student_list_item, colLabels, colIds);
        * signature:
        * SimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to)
        * Parameters are:
        *    context - provides the adapter the means to reference the controller for the view in which the list exists
        *    data - a list (ArrayList) of HashMaps. Each entry in the list corresponds to one row in the list
        *           The Maps contain the data to be displayed in each row. There should be one key in the map for
        *           each string in the from string array. These correspond to the columns in each row.
        *           The value associated with each column key is the value to be placed into that position of the list row.
        *    resource - The layout xml file for a row in the list. The xml should contain an id corresponding to
        *           each int specified in the to integer array
        *    from - A string array whose elements are column names. These names are used as keys in the maps. In this example,
        *           From values are also added to a map and added to the list so there is a header row in the list.
        *    to -   An integer array (same length as from) specifying the id's within the row's layout file corresponding
        *           columns in the row.
        *
        */
        colLabels = this.getResources().getStringArray(R.array.col_header);
        colIds = new int[] {R.id.student_firstTV, R.id.student_lastTV, R.id.student_idTV};
        this.studNames = students.getNames();
        Arrays.sort(this.studNames);
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
        inflater.inflate(R.menu.main_activity_menu, menu);
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //listview.onitemclicklistener method
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        String[] studNames = students.getNames();
        Arrays.sort(studNames);
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
            prepareAdapter();
            SimpleAdapter sa = new SimpleAdapter(this, fillMaps, R.layout.student_list_item, colLabels, colIds);
            studentLV.setAdapter(sa);
        }
    }

}
