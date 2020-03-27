package edu.asu.se.lindquis.activitylc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

/**
 * Copyright 2019 Tim Lindquist,
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
 * Purpose: Main and dialog activities that show interaction between foreground and
 * background activities and views in an Android app.
 * Use this example as a basis for developing an app which demonstrates all of the
 * Android activity life-cycle methods. Explain the state changes that cause each to
 * life-cycle method be executed. Note that this approach for generating a dialog is
 * not the accepted mechanism for Android, and a better approach is covered later.
 * Also, this app mixes AppCompatActivity with Activity, which is also not a good idea.
 * This is done for placement of the new (dialog) activity
 *
 * @author Tim Lindquist Tim.Lindquist@asu.edu
 *         Software Engineering, CIDSE, IAFSE, Arizona State University Polytechnic
 * @version January, 2019
 */

public class MainActivity extends AppCompatActivity {
    TextView helloTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(getClass().getSimpleName(), "onCreate()");
        helloTV = (TextView)findViewById(R.id.helloTF);
        //TextView tv = (TextView)findViewById(R.id.helloTF);
        //tv.setText("Hello Ser423");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        Log.d(getClass().getSimpleName(), "onCreateOptionsMenu()");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            // handle activity specific action buttons by checking their id
            Log.d(getClass().getSimpleName(), "in onCreateOptionsItemSelected with settings selected.");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(getClass().getSimpleName(), "onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(getClass().getSimpleName(), "onResume()");
    }

    public void startDialog(View v) {
        android.util.Log.w("ActivityMain", "called startDialog()");
        Intent intent = new Intent(MainActivity.this, DialogActivity.class);
        startActivity(intent);
    }

    public void showTime(View v) {
        android.util.Log.w("ActivityMain:", "called showTime()");
        String curr = DateFormat.getDateTimeInstance().format(new Date());
        helloTV.setText(curr);
    }

}
