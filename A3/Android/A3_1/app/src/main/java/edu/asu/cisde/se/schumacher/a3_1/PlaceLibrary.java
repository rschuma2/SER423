package edu.asu.cisde.se.schumacher.a3_1;

import android.app.Activity;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Iterator;

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
 * Purpose: An app to demonstrate a ListView with a SimpleAdapter
 * The simple adapter is not so simple, but it does allow a list with multiple row components
 * to be constructed from a pre-defined adapter. Both activities of this example create and
 * manipulate a listView using a simple adapter
 *
 * @author Tim Lindquist Tim.Lindquist@asu.edu
 *         Software Engineering, CIDSE, IAFSE, Arizona State University Polytechnic
 * @version November 20, 2018
 */
public class PlaceLibrary extends Object implements Serializable {

    public Hashtable<String, Place> places;
    private static final boolean debugOn = false;

    public PlaceLibrary(Activity parent) {
        debug("creating a new student collection");
        places = new Hashtable<String, Place>();
        try {
            this.resetFromJsonFile(parent);
        } catch (Exception ex) {
            android.util.Log.d(this.getClass().getSimpleName(), "error resetting from students json file" + ex.getMessage());
        }
    }

    private void debug(String message) {
        if (debugOn)
            android.util.Log.d(this.getClass().getSimpleName(), "debug: " + message);
    }

    public boolean resetFromJsonFile(Activity parent) {
        boolean ret = true;
        try {
            places.clear();
            InputStream is = parent.getApplicationContext().getResources().openRawResource(R.raw.places);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            // note that the json is in a multiple lines of input so need to read line-by-line
            StringBuffer sb = new StringBuffer();
            while (br.ready()) {
                sb.append(br.readLine());
            }
            String placesJsonStr = sb.toString();
            JSONObject studentsJson = new JSONObject(new JSONTokener(placesJsonStr));
            Iterator<String> it = studentsJson.keys();
            while (it.hasNext()) {
                String sName = it.next();
                JSONObject aPlace = studentsJson.optJSONObject(sName);
                debug("importing student named " + sName + " json is: " + aPlace.toString());
                if (aPlace != null) {
                    Place stu = new Place(aPlace.toString());
                    places.put(sName, stu);
                }
            }
        } catch (Exception ex) {
            android.util.Log.d(this.getClass().getSimpleName(), "Exception reading json file: " + ex.getMessage());
            ret = false;
        }
        return ret;
    }

    public boolean add(Place aPlace) {
        boolean ret = true;
        debug("adding place named: " + ((aPlace == null) ? "unknown" : aPlace.name));
        try {
            places.put(aPlace.name, aPlace);
        } catch (Exception ex) {
            ret = false;
        }
        return ret;
    }

    public boolean remove(String aName) {
        debug("removing place named: " + aName);
        return ((places.remove(aName) == null) ? false : true);
    }

    public String[] getNames() {
        String[] ret = {};
        debug("getting " + places.size() + " place names.");
        if (places.size() > 0) {
            ret = (String[]) (places.keySet()).toArray(new String[0]);
        }
        return ret;
    }

    public Place get(String aName) {
        Place ret = new Place("","","","","",0.0,0.0,0.0);
        Place aPlace = places.get(aName);
        if (aPlace != null) {
            ret = aPlace;
        }
        return ret;
    }
}

