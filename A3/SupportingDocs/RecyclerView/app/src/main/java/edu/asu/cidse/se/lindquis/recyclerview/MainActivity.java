package edu.asu.cidse.se.lindquis.recyclerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
/*
 * must add:
 * compile 'com.android.support:recyclerview-v7:23.1.1'
 * to build.gradle (Module: app) to access recyclerview
 */
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.HashMap;

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
 * Purpose: An app to demonstrate Lists in an activity with RecyclerView
 * RecyclerView was introduced by Google to provide for more efficient list
 * scrolling than via ListView, and to provide additional flexibility in
 * lists. Two expensive aspects of scrolling a list are creating (inflating)
 * views for items newly introduced into view, and the method findViewById.
 * Both iOS and Android minimize the number of times you must de-serialize
 * a row's view from xml by re-using views. iOS reduces the overhead in accessing
 * components of a view (the terminal nodes in the view hierarchy) by using
 * IBOutlets (Interface Builder Outlets) and setting them appropriately for each
 * view. Android's ListView recycles views rather than inflating new row views,
 * but historically the hierarchical search to find the controls within a row's
 * view (TextView's and ImageView, for example) had to be accomplished with each
 * (re)population of values into a row's view. Newly (2011) introduced recylclerview
 * removes repeated findViewById searches through use of the ViewHolder (sub)class.
 * The problem with RecyclerView is that there is extensive code to be added by the
 * developer when using this approach. See the referenced course textbook for a deeper
 * explanation and examples.
 *
 * @author Tim Lindquist Tim.Lindquist@asu.edu
 *         Software Engineering, CIDSE, IAFSE, Arizona State University Polytechnic
 * @version July 27, 2018
 */

public class MainActivity extends AppCompatActivity {

    private RecyclerView listOfCoursesRV;
    private RecyclerView.Adapter anAdapter;
    private RecyclerView.LayoutManager aLayoutManager;
    private HashMap<String,String> courses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        courses = new HashMap<String,String>();
        courses.put("Ser100","Object-Oriented Software Development");
        courses.put("Ser200","Core Data Structures with Object Oriented Programming");
        courses.put("Ser215","Software Enterprise: Personal Process");
        courses.put("Ser216","Software Enterprise: Personal Process and Quality");
        courses.put("Ser221","Programming Languages and Their Execution Environment");
        courses.put("Ser222","Design and Analysis of Data Structures and Algorithms");
        courses.put("Ser232","Computer Systems Fundamentals I");
        courses.put("Ser250","Microcomputer Architecture and Programming");
        courses.put("Ser315","Software Enterprise: Design and Process");
        courses.put("Ser316","Software Enterprise: Construction and Transition");
        courses.put("Ser321","Principles of Distributed Software Systems");
        courses.put("Ser322","Principles of Database Management");
        courses.put("Ser334","Operating Systems and Networks");
        courses.put("Ser401","Computing Capstone Project I");
        courses.put("Ser402","Computing Capstone Project II");
        courses.put("Ser415","Software Enterprise: Inception and Elaboration");
        courses.put("Ser416","Software Enterprise: Project and Process Management");
        courses.put("Ser421","Web-Based Applications and Mobile Systems");
        courses.put("Ser422","Web Application Programming");
        courses.put("Ser423", "Mobile Systems");

        setContentView(R.layout.activity_main);
        listOfCoursesRV = (RecyclerView)findViewById(R.id.recycler_view);
        listOfCoursesRV.setHasFixedSize(true);
        aLayoutManager = new LinearLayoutManager(this);
        listOfCoursesRV.setLayoutManager(aLayoutManager);
        anAdapter = new ARecyclerAdapter(courses);
        listOfCoursesRV.setAdapter(anAdapter);
    }
}
