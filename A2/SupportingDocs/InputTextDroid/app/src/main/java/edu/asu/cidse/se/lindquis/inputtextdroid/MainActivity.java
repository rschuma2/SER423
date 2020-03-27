package edu.asu.cidse.se.lindquis.inputtextdroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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
 * Purpose: Example to show how to get a multi-line EditText to accept
 * multiple lines of text input from the user, while still allowing the
 * keyboard to be dismissed. See comments in code below to see how to change
 * and run this example. This example also shows different input types for
 * EditText. Others are defined.
 *
 * Ser423 Mobile Applications
 * see http://pooh.poly.asu.edu/Mobile
 * @author Tim Lindquist Tim.Lindquist@asu.edu
 *         Software Engineering, CIDSE, IAFSE, ASU Poly
 * @version November 24, 2018
 */
public class MainActivity extends Activity implements TextView.OnEditorActionListener {

    private EditText titleET, releasedET, genreET, artistET;
    private Spinner genreSpinner;
    private ArrayList<String> genre = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private String selectedGenre;
    private Menu menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        titleET = (EditText) findViewById(R.id.titleET);
        //titleET.setOnEditorActionListener(this);  // no call to onEditorAction when commented
        releasedET = (EditText) findViewById(R.id.releasedET);
        releasedET.setOnEditorActionListener(this);
        genreET = (EditText)findViewById(R.id.genreET);

        // To best understand this example, in the Android Virtual Device Manager, edit
        // the emulator you are using as follows: under advanced settings, at the bottom, uncheck
        // enable keyboard input. This requires the device keyboard to be used instead of your
        // computer keyboard.
        //
        // To get a multi-line edittext to both allow input of more than 1
        // line of text and move to the next control or complete and dismiss the keyboard,
        // the user must utilize both the keyboard and the back button. Notice that the back
        // button converts between down and back buttons as appropriate.
        // I.E. the imeOption of actionDone or actionNext. There doesn't appear to be a keyboard with
        // both enter/return and Done or Next.
        //
        // To see the problem and the solution, in the layout file for this control, try the
        // following property values for the Artist multiline EditText component:
        //        android:inputType="textImeMultiLine"   -- allows the keyboard to be dismissed
        //                                                  but unable to enter a new line for the
        //                                                  user to enter multiple lines of data.
        //        android:inputType="textMultiLine"      -- can only dismiss, with the device back
        //                                                  arrow, which in emulator shows up temporarily as
        //                                                  down arrow-- on new devices with soft keys.
        //                                                  User can with this option enter multiple lines.

        artistET = (EditText) findViewById(R.id.artistET);
        artistET.setHorizontallyScrolling(false);
        artistET.setOnEditorActionListener(this);

        setTitle(getString(R.string.addmusic));
    }

    // OnEditorActionListener method. Called when actionNext or actionDone occurs
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
        // note that inputType and keyboard actions imeOptions must be defined to manage the keyboard
        // these can be defined in the xml as an attribute of the EditText.
        // action
        android.util.Log.d(this.getClass().getSimpleName(), "onEditorAction: keycode " +
                ((event == null) ? "null" : event.toString()) + " actionId " + actionId);
        if(actionId== EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE){
            android.util.Log.d(this.getClass().getSimpleName()," entry is: "+
                    v.getText().toString());
        }
        // return false, otherwise the keyboard will not disappear and focus won't move to next field
        // when this is set as an OnEditorActionListener. Change the value to observe the behavior
        //return true;
        return false;
    }


}
