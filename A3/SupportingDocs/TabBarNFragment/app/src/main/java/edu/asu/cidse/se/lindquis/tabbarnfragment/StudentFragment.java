package edu.asu.cidse.se.lindquis.tabbarnfragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
public class StudentFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.student_fragment, container, false);
    }

}
