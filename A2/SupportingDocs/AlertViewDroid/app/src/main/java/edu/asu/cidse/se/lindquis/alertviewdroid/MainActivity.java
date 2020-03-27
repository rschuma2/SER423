package edu.asu.cidse.se.lindquis.alertviewdroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

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
 * Purpose: An app to demonstrate an AlertView with an EditText for input entry
 * Note that this is a more common use of alert dialog builder than in the
 * life-cycle example of the prior section. Not because of the EditText, but
 * because the alert in this example is in the same Activity.
 * Note also that the life-cycle methods
 * are not called when the main activity presents the alert dialog. Is the dialog
 * modal? Note what happens when the user clicks neither cancel nor OK, but
 * instead touches the main view.
 * Another difference is that in the previous example, the MainActivity extends
 * AppCompatActivity, but in this example it extends Activity. AppCompat is more
 * commonly used because of its additional downward compatibility with prior
 * API (and consequently phone) versions.
 *
 * @author Tim Lindquist Tim.Lindquist@asu.edu
 *         Software Engineering, CIDSE, IAFSE, Arizona State University Polytechnic
 * @version November 23, 2018
 */
public class MainActivity extends Activity implements DialogInterface.OnClickListener {

    private TextView helloTV;
    private EditText in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helloTV = (TextView)findViewById(R.id.helloTF);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        Log.d(getClass().getSimpleName(), "onCreate()");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            android.util.Log.d(this.getClass().getSimpleName(),"in onOptionsItemSelected. Settings");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // called when start alert view button is clicked.
    public void startDialog(View v) {
        android.util.Log.w("ActivityMain", "called startDialog()");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_text));
        in = new EditText(this);
        in.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(in);
        builder.setNegativeButton(getString(R.string.cancel), this);
        builder.setPositiveButton(getString(R.string.ok),this);
        builder.show();
    }

    // DialogInterface.OnClickListener method. Get the result of the Alert View.
    @Override
    public void onClick(DialogInterface dialog, int which){
        String result = (which==DialogInterface.BUTTON_POSITIVE)? getString(R.string.ok):
                                                                  getString(R.string.cancel);
        android.util.Log.d(this.getClass().getSimpleName(),"onClick result: "+result+
        " input is: "+in.getText());
        helloTV.setText("Result: "+result+" input is: "+in.getText());
    }

    // called with Show Time button is clicked
    public void showTime(View v) {
        android.util.Log.w("ActivityMain:", "called showTime()");
        String curr = DateFormat.getDateTimeInstance().format(new Date());
        helloTV.setText(curr);
    }

    // activity life-cycle methods. Note that unlike the Unit1 dialog which involved a new
    // activity, these methods are not called (Activity state does not change) when an
    // Alert View is shown.
    @Override
    public void onRestart() {
        super.onRestart();
        Log.d(getClass().getSimpleName(), "onRestart()");
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

    @Override
    public void onPause() {
        super.onPause();
        Log.d(getClass().getSimpleName(), "onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(getClass().getSimpleName(), "onStop()");
    }

    @Override
    public void onDestroy() {
        Log.d(getClass().getSimpleName(), "onDestroy()");
        super.onDestroy();
    }

}
