package edu.asu.cidse.se.lindquis.actionbar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Copyright © 2016 Tim Lindquist,
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
 * Purpose: An app to demonstrate Action Bar
 *
 * @author Tim Lindquist Tim.Lindquist@asu.edu
 *         Software Engineering, CIDSE, IAFSE, Arizona State University Polytechnic
 * @version November 20, 2018
 */

public class DialogActivity extends AppCompatActivity {
    TextView dialogTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.util.Log.d(this.getClass().getSimpleName(), "called onCreate()");
        setContentView(R.layout.activity_dialog);
        /*
         * Display a message particular to which action bar button has been selected.
         * That message is passed as the string value of the message attribute which is
         * added to the Intent object. You can visualize the Intent as a Dictionary/Map
         * To get the action bar to not display in the dialog activity, you can define
         * a new theme in styles.xml that is based on the dialog theme, but adds to it
         * to not show the action bar or the activity's title.
         */
        dialogTV = (TextView)findViewById(R.id.dialog_textview);
        Intent i = getIntent();
        dialogTV.setText(i.getStringExtra("message"));
    }

    /**
     * Callback method for the button in the View (activity_dialog.xml)
     * @param v
     */
    public void finishDialog(View v) {
        android.util.Log.d("DialogActivity:", "called finishDialog()");
        DialogActivity.this.finish();
    }

}
