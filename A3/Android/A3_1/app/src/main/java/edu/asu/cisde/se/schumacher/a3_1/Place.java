package edu.asu.cisde.se.schumacher.a3_1;

/*
 * Copyright 2016 Tim Lindquist,
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
 * Purpose: Example classes conversion to/from json
 * This example shows the use of Android's
 * org.json package in creating json string of a Java object.
 *
 * Ser423 Mobile Applications
 * see http://pooh.poly.asu.edu/Mobile
 * @author Tim Lindquist Tim.Lindquist@asu.edu
 *         Software Engineering, CIDSE, IAFSE, ASU Poly
 * @version January 2016
 */

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Vector;


import java.io.Serializable;
import java.util.Arrays;


/**
 * Created by lindquis on 1/14/16.
 */
public class Place implements Serializable{

    private static final boolean debugOn = false;


    public String name;
    public String description;
    public String category;
    public String addressTitle;
    public String addressStreet;
    public Double elevation;
    public Double latitude;
    public Double longitude;


    public Place(String name, String description, String category, String addressTitle,
                 String addressStreet, Double elevation, Double latitude, Double longitude) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.addressTitle = addressTitle;
        this.addressStreet = addressStreet;
        this.elevation = elevation;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Place(String jsonStr){
        try{
            JSONObject jo = new JSONObject(jsonStr);
            name = jo.getString("name");
            description = jo.getString("description");
            category = jo.getString("category");
            addressTitle = jo.getString("address-title");
            addressStreet = jo.getString("address-street");
            elevation = jo.getDouble("elevation");
            latitude = jo.getDouble("latitude");
            longitude = jo.getDouble("longitude");
        }catch (Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),
                    "error converting to/from json");
        }
    }


    public Place(JSONObject jsonObj){
        try{
            debug("constructor from json received: " + jsonObj.toString());
            name = jsonObj.optString("name","unknown");
            description = jsonObj.optString("description");
            category = jsonObj.optString("category");
            addressTitle = jsonObj.optString("address-title");
            addressStreet = jsonObj.optString("address-street");
            elevation = jsonObj.optDouble("studentid");
            latitude = jsonObj.optDouble("latitude");
            longitude = jsonObj.optDouble("longitude");
        }catch(Exception ex){
            android.util.Log.d(this.getClass().getSimpleName(), "error getting Student from json string");
        }
    }


    public JSONObject toJson(){
        JSONObject jo = new JSONObject();
        try{
            jo.put("name",name);
            jo.put("description",description);
            jo.put("category",category);
            jo.put("addressTitle",addressTitle);
            jo.put("addressStreet",addressStreet);
            jo.put("elevation",elevation);
            jo.put("latitude",latitude);
            jo.put("longitude",longitude);
        }catch (Exception ex){
            android.util.Log.d(this.getClass().getSimpleName(), "error getting Student from json string");
        }
        return jo;
    }


    public String toJsonString(){
        String ret = "";
        try{
            ret = this.toJson().toString();
        }catch (Exception ex){
            android.util.Log.d(this.getClass().getSimpleName(), "error getting Student from json string");
        }
        return ret;
    }

    /*
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Student ").append(name).append(" has id ");
        sb.append(studentid).append(" and takes courses ");
        for (int i=0; i<takes.size(); i++){
            sb.append(takes.get(i).toString()).append(" ");
        }
        return sb.toString();
    }
     */

    private void debug(String message) {
        if (debugOn)
            android.util.Log.d(this.getClass().getSimpleName(), message);
    }

}
