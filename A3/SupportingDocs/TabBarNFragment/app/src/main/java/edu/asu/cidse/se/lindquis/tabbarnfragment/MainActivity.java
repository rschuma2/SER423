package edu.asu.cidse.se.lindquis.tabbarnfragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

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
 * Purpose: Demonstrate tab bar with action bar and use of fragments.
 * To get action bar tab project to work from a new project using appcompat and sdk version 23
 * the following additions were made:
 * 1. Manually add following dependencies to the build.gradle module: app file:
 *     compile 'com.android.support:appcompat-v7:23.4.0'
 *     compile 'com.android.support:design:23.4.0'
 * 2. And, manually add the following items to the AppTheme in res/values/styles.xml
 *         <item name="windowActionBar">false</item>
 *         <item name="windowNoTitle">true</item>
 *
 * @author Tim Lindquist Tim.Lindquist@asu.edu
 *         Software Engineering, CIDSE, IAFSE, Arizona State University Polytechnic
 * @version November 21, 2018
 */
public class MainActivity extends AppCompatActivity  implements TabLayout.OnTabSelectedListener  {

    private Toolbar aToolbar;
    private TabLayout aTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        aToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(aToolbar);

        aTabLayout = (TabLayout)findViewById(R.id.tab_layout);
        aTabLayout.addTab(aTabLayout.newTab().setText(getString(R.string.course)));
        aTabLayout.addTab(aTabLayout.newTab().setText(getString(R.string.student)));
        aTabLayout.addOnTabSelectedListener(this);

        // Create a new course fragment to initiate the activity layout matching the initially
        // selected tab (Course).
        CourseFragment firstFrag = new CourseFragment();
        // This activity could get started with special data from an
        // Intent. Pass the Intent extras to the fragment as arguments
        firstFrag.setArguments(getIntent().getExtras());
        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, firstFrag).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // add any menu items (from xml layout) here. Nil here
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        android.util.Log.d(this.getClass().getSimpleName(),"onOPtionsItemSelected "+item.toString());
        return super.onOptionsItemSelected(item);
    }

    // method to implement the OnTabSelectedListener interface
    @Override
    public void onTabSelected(TabLayout.Tab aTab) {
        android.util.Log.d(this.getClass().getSimpleName(),"onTabSelected "+aTab.getText());
        String selected = aTab.getText().toString();
        if(selected.equals(getString(R.string.course))){
            // CourseFragment to replace student fragment.
            CourseFragment newFragment = new CourseFragment();
            Bundle args = new Bundle();
            args.putString("Course", "Ser423");
            newFragment.setArguments(args);
            FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
            // Replace the fragment_container's view with this fragment's view,
            // and add the transaction to the "back stack" for future navigation
            trans.replace(R.id.fragment_container, newFragment);
            trans.addToBackStack(null);
            trans.commit();
        }else{
            // studentfragment to replace course fragment.
            StudentFragment newFragment = new StudentFragment();
            Bundle args = new Bundle();
            args.putString("StudentName", "Tim");
            newFragment.setArguments(args);
            FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
            // Replace the fragment_container's view with this fragment's view,
            // and add the transaction to the "back stack" for future navigation
            trans.replace(R.id.fragment_container, newFragment);
            trans.addToBackStack(null);
            trans.commit();
        }
    }

    // method to implement the OnTabSelectedListener interface
    @Override
    public void onTabUnselected(TabLayout.Tab aTab){
        android.util.Log.d(this.getClass().getSimpleName(),"onTabUnSelected "+aTab.getText());

    }

    // method to implement the OnTabSelectedListener interface
    @Override
    public void onTabReselected(TabLayout.Tab aTab) {
        android.util.Log.d(this.getClass().getSimpleName(), "onTabReSelected " + aTab.getText());
    }

}
