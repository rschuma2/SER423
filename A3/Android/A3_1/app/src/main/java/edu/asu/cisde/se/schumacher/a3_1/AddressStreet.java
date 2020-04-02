package edu.asu.cisde.se.schumacher.a3_1;

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

import org.json.JSONObject;
import java.io.Serializable;


public class AddressStreet implements Serializable{

    public String prefix;
    public String title;


    public AddressStreet(JSONObject jobj){
        try{
            this.prefix = jobj.optString("prefix","unknown");
            this.title = jobj.optString("title","unknown");
        }catch(Exception ex){
            android.util.Log.d(this.getClass().getSimpleName(),"exception in json contstructor");
        }
    }
}
