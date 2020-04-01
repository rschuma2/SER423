package edu.asu.cidse.se.lindquis.recyclerview;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Arrays;
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
 * * Purpose: An app to demonstrate Lists in an activity with RecyclerView
 * RecyclerView has been introduced to increase reuse of cells and reduce
 * inflating xml layouts (and findViewById). Also to provide flexibility.
 * This app to demonstrate Lists in an activity with RecyclerView
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

 * @author Tim Lindquist Tim.Lindquist@asu.edu
 *         Software Engineering, CIDSE, IAFSE, Arizona State University Polytechnic
 * @version July 27, 2018
 */

public class ARecyclerAdapter extends RecyclerView.Adapter<ARecyclerAdapter.ViewHolder> implements
        View.OnClickListener {

    private HashMap<String,String> courses;
    private String lastSelectedCourse = "";
    private View lastSelectedView = null;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView aPrefixTV;
        public TextView aTitleTV;
        public RelativeLayout parentView;

        public ViewHolder(View aView){
            super(aView);
            android.util.Log.d(this.getClass().getSimpleName()," viewholder constuctor called, calling findByView");
            parentView = ((RelativeLayout)aView.findViewById(R.id.rl));
            aPrefixTV = (TextView)parentView.findViewById(R.id.course_name);
            aTitleTV = (TextView)parentView.findViewById(R.id.course_title);
        }
    }

    public ARecyclerAdapter(HashMap<String,String> courses){
        this.courses = courses;
    }

    @Override
    public ARecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        return(new ARecyclerAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.a_text_view, parent,
                false)));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        String[] theCourses = courses.keySet().toArray(new String[]{});
        Arrays.sort(theCourses);
        holder.aPrefixTV.setOnClickListener(this);
        holder.aPrefixTV.setText(theCourses[position]);
        holder.aTitleTV.setText(courses.get(theCourses[position]));
    }

    @Override
    public int getItemCount(){
        int count = courses.keySet().toArray().length;
        android.util.Log.d(this.getClass().getSimpleName()," itemcount returning: "+count);
        return count;
    }

    public void onClick(View v){
        TextView atv = (TextView)v.findViewById(R.id.course_name);
        String aCrs = atv.getText().toString();
        v.setBackgroundResource(R.color.dark_blue);
        if(lastSelectedCourse != ""){
            lastSelectedView.setBackgroundResource(R.color.light_blue);
        }
        android.util.Log.d(this.getClass().getSimpleName(),"called onClick " + aCrs +
        " open " + courses.get(aCrs));
        lastSelectedView = v;
        lastSelectedCourse = aCrs;
    }
}
