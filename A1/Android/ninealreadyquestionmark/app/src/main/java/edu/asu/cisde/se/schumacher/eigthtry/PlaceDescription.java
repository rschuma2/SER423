package edu.asu.cisde.se.schumacher.eigthtry;
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

/**
 * Created by lindquis on 1/14/16.
 */
public class PlaceDescription {
    public String name;
    public String description;
    public String category;
    public String addressTitle;
    public Vector<String> addressStreet;
    public String elevation;
    public String latitude;
    public String longitude;
    PlaceDescription(String jsonStr){
        try{
            JSONObject jo = new JSONObject(jsonStr);
            name = jo.getString("name");
            description = jo.getString("description");
            category = jo.getString("category");
            addressTitle = jo.getString("address-title");
            addressStreet = new Vector<String>();
            JSONArray ja = jo.getJSONArray("address-street");
            for (int i=0; i< ja.length(); i++){
                addressStreet.add(ja.getString(i));
            }
            elevation = jo.getString("elevation");
            latitude = jo.getString("latitude");
            longitude = jo.getString("longitude");
            //latitude = jo.getDouble("latitude");
            //longitude = jo.getDouble("longitude");
        }catch (Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),
                    "error converting to/from json");
        }
    }
    public String toJsonString(){
        String ret = "";
        try{
            JSONObject jo = new JSONObject();
            jo.put("name",name);
            jo.put("description", description);
            jo.put("category", category);
            jo.put("addressTitle", addressTitle);
            JSONArray ja = new JSONArray(addressStreet);
            jo.put("addressStreet",ja);
            jo.put("elevation",elevation);
            jo.put("latitude",latitude);
            jo.put("longitude",longitude);
            ret = jo.toString();
        }catch (Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),
                    "error converting to/from json");
        }
        return ret;
    }
}