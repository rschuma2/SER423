package edu.asu.cidse.se.lindquis.actionbar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Copyright © 2017 Tim Lindquist,
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
 * Purpose: An app to demonstrate an ActionBar
 *
 * @author Tim Lindquist Tim.Lindquist@asu.edu
 *         Software Engineering, CIDSE, IAFSE, Arizona State University Polytechnic
 * @version November 20, 2018
 */

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
                                                               TextView.OnEditorActionListener {

    private EditText messageET, dateET;
    private Spinner courseSpinner;
    private ArrayAdapter<String> adapter;

    /*
    * Action Bar is provided with application style in the manifest.
    * An Action Bar is the default for sdk 11 and newer, or it
    * may be specified in the application section of AndroidManifest with the Application
    * attribute: android:theme="@style/AppTheme" as shown in this example.
    * AppTheme is defined in res/values/styles.xml,
    * and the gimp and search actions (which should appear in the Action Bar)
    * are defined in the res/menu/main_activity_actions.xml
    * To turn off the action bar and app title change the AndroidManifest application
    * attribute for theme to: "@style/AppTheme.NoActionBar
    * Run the app again to observe the action bar disappears.
    * Prior versions of the api defined and ActionBarActivity which could be extended
    * to get the Action Bar.
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        android.util.Log.d(this.getClass().getSimpleName(), "called onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get references to the two edit text's in this view and register me as their edit action listener
        messageET = (EditText) findViewById(R.id.messageET);
        messageET.setOnEditorActionListener(this);
        dateET = (EditText) findViewById(R.id.dateET);
        dateET.setOnEditorActionListener(this);

        // set up the spinner
        courseSpinner = (Spinner)findViewById(R.id.courseSpinner);
        // its better Android usage to define string arrays array resources. Ditto for all string literals.
        String[] courses = new String[] {"Ser423","Cse494","Ser598","Ser502"};
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                                           new ArrayList<>(Arrays.asList(courses)));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        courseSpinner.setAdapter(adapter);
        courseSpinner.setOnItemSelectedListener(this);



    }

    /*
     * One way to create Aciton Bar Buttons is to use xml menu specification. Create the file:
     * res/menu/main_activity_actions.xml to include contents as in this project.
     * reference to any images for the action bar should be created by right clicking on res folder
     * in the project and creating a new image asset. Be sure to specify Action Bar & Tab Icon
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        android.util.Log.d(this.getClass().getSimpleName(), "called onCreateOptionsMenu()");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /*
     * Implement onOptionsItemSelected(MenuItem item){} to handle clicks of buttons that are
     * in the action bar.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        android.util.Log.d(this.getClass().getSimpleName(), "called onOptionsItemSelected()");
        Intent i = new Intent(this, DialogActivity.class);
        switch (item.getItemId()) {
            case R.id.action_search:
                i.putExtra("message", "This is a Search dialog");
                startActivity(i);
                return true;
            case R.id.action_gimp:
                i.putExtra("message", "This is a Gimp dialog");
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
     * This method is specified in the OnEditorActionListener interface and is called
     * when the action is performed on a registered EditText
     */
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
        // note that inputType and keyboard actions imeOptions must be defined to manage the keyboard
        // these can be defined in the xml as an attribute of the EditText.
        // returning false from this method
        android.util.Log.d(this.getClass().getSimpleName(),"onEditorAction: keycode "+
                ((event==null)?"null":event.toString())+" actionId "+actionId);
        if(actionId== EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE){
            android.util.Log.d(this.getClass().getSimpleName(),"entry is: "+v.getText().toString());
        }
        return false; // without returning false, the keyboard will not disappear or move to next field
    }

    /*
     * the following two methods are specified by the AdapterView.OnItemSelectedListener interface
     */
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        android.util.Log.d(this.getClass().getSimpleName(),"Spinner selection changed to "+
                courseSpinner.getSelectedItem().toString());
    }

    public void onNothingSelected(AdapterView<?> parent) {

    }

}
