package edu.asu.cidse.se.lindquis.exampleautocompletetextview;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

/**
 * Copyright 2018 Tim Lindquist,
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
 * Purpose: Demonstrate using an AutoCompleteTextView as an editable picker.
 * As user enters text into the EditText, substring match of typed input as
 * compared to the contents of an adapter driven drop-down shows available
 * options. This control can be used to minimize user typing for common entries,
 * but also provides the ability for the user to enter new text distinct from
 * the contents of the adapter.
 *
 * Run this example using the Android Keyboard, instead of your computer's keyboard.
 * You should be able to explain what user actions cause the method onEditorAction
 * to be called, as opposed to the user actions that cause onItemClick to be called.
 * Can you get onTouch to be called? What is its effect?
 *
 * @author Tim Lindquist Tim.Lindquist@asu.edu
 *         Software Engineering, CIDSE, IAFSE, Arizona State University Polytechnic
 * @version November 24, 2018
 */
public class MainActivity extends Activity
                          implements TextView.OnEditorActionListener,
                                     AdapterView.OnItemClickListener,
                                     View.OnTouchListener{

    private AutoCompleteTextView coursesACTV;
    private String[] courses;
    private ArrayAdapter<String> courseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        coursesACTV = (AutoCompleteTextView)findViewById(R.id.coursesACTV);
        courses = new String[] {"Ser321","Cse445","Ser432","Cse494"};
        courseAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, courses);
        coursesACTV.setAdapter(courseAdapter);
        coursesACTV.setOnItemClickListener(this);
        coursesACTV.setOnEditorActionListener(this);
        coursesACTV.setOnTouchListener(this);
        coursesACTV.setThreshold(1);

    }

    public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
        // note that inputType and keyboard actions imeOptions must be defined to manage the keyboard
        // these can be defined in the xml as an attribute of the AutoCompleteTextView.
        // returning false from this method
        android.util.Log.d(this.getClass().getSimpleName(), "onEditorAction: keycode " +
                ((event == null) ? "null" : event.toString()) + " actionId " + actionId);
        if(actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE){
            android.util.Log.d(this.getClass().getSimpleName(),"entry is: "+v.getText().toString());
        }
        return false; // without returning false, the keyboard will not disappear or move to next field
    }

    public void onItemClick(AdapterView<?> adapterView, View v, int arg2, long arg3){
        android.util.Log.d(this.getClass().getSimpleName(), "onItemClicked: " + coursesACTV.getText().toString());
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
    }

    public boolean onTouch(View v, MotionEvent event){
        android.util.Log.d(this.getClass().getSimpleName(), "onTouch with content: " + coursesACTV.getText().toString());
        coursesACTV.showDropDown();
        return false;
    }

}
