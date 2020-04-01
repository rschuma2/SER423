package edu.asu.cidse.se.lindquis.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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
 * Purpose: web view activity in an app demonstrating expandable list view control and adapter.
 * this class displays a url thats associated with a list item selection from the main activity.
 *
 * @author Tim Lindquist Tim.Lindquist@asu.edu
 *         Software Engineering, CIDSE, IAFSE, Arizona State University Polytechnic
 * @version November 22, 2018
 */
public class WebViewActivity extends AppCompatActivity {

    public WebView wv;
    public String itemname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        wv = (WebView)findViewById(R.id.aWebView);
        wv.setWebViewClient(new MyWebViewClient());
        wv.getSettings().setJavaScriptEnabled(true);

        // if you use the two lines below, comment out inflating the menuinflater
        // in onCreateOptionsMenu
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        // WebViewActivity is started by the Expandible List Adapter when an item touch is detected.
        // the adapter creates an intent, attaches the name of the list item and its corresponding url
        // Get the two strings from the intent extras
        Intent i = getIntent();
        String url = i.getStringExtra("url");
        itemname = i.getStringExtra("item");
        // now change the title in the action bar to be the name of the item.
        setTitle(itemname);
        // load the url we received into the web view. Two points: the control's network connection
        // is off the UIThread. And, you must declare internet permission in AndroidManifest.xml as:
        // <uses-permission android:name="android.permission.INTERNET" />
        wv.loadUrl(url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_web_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_back:
                // User chose the "Back Button" as defined in menu_web_view.
                android.util.Log.d(this.getClass().getSimpleName(),"back button selected");
                // return the itemname as the result of this intent startActivityForResult
                Intent intent = new Intent();
                intent.putExtra("myresult", itemname);
                setResult(RESULT_OK, intent);
                finish();
                return true;
            case android.R.id.home:
                // User chose the android defined
                android.util.Log.d(this.getClass().getSimpleName(),"home button selected");
                // return the itemname as the result of this intent startActivityForResult
                Intent intent1 = new Intent();
                intent1.putExtra("myresult", itemname);
                setResult(RESULT_OK, intent1);
                finish();
                return true;

            default:
                // If we get here, the user's action is not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    // internal class to manage the web view
    class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            wv.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                       SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.proceed();
        }
    }
}
