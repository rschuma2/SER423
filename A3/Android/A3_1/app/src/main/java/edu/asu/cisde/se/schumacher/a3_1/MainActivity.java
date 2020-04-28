package edu.asu.cisde.se.schumacher.a3_1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ListView.OnItemClickListener, DialogInterface.OnClickListener {

    private EditText nameBox, descriptionBox; // used in the alert dialog for adding a place
    private ListView placeLV;   // the list view for displaying place name and description
    private PlaceLibrary places;  // a collection of places (serializable)
    private String[] placeNames;

    private String[] colLabels;
    private int[] colIds;
    private List<HashMap<String,String>> fillMaps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        placeLV = (ListView)findViewById(R.id.place_list_view);

        places = new PlaceLibrary(this);
        placeNames = places.getNames();

        this.prepareAdapter();
        SimpleAdapter sa = new SimpleAdapter(this, fillMaps, R.layout.place_list_item, colLabels, colIds);
        placeLV.setAdapter(sa);
        placeLV.setOnItemClickListener(this);

        setTitle("places");
    }

    // this method generates the data needed to create a new list view simple adapter.
    private void prepareAdapter(){
        /*
         * setup the data necessary for the constructor for a simple adapter:
         * SimpleAdapter sa = new SimpleAdapter(this, fillMaps, R.layout.student_list_item, colLabels, colIds);
         * signature:
         * SimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to)
         * Parameters are:
         *    context - provides the adapter the means to reference the controller for the view in which the list exists
         *    data - a list (ArrayList) of HashMaps. Each entry in the list corresponds to one row in the list
         *           The Maps contain the data to be displayed in each row. There should be one key in the map for
         *           each string in the from string array. These correspond to the columns in each row.
         *           The value associated with each column key is the value to be placed into that position of the list row.
         *    resource - The layout xml file for a row in the list. The xml should contain an id corresponding to
         *           each int specified in the to integer array
         *    from - A string array whose elements are column names. These names are used as keys in the maps. In this example,
         *           From values are also added to a map and added to the list so there is a header row in the list.
         *    to -   An integer array (same length as from) specifying the id's within the row's layout file corresponding
         *           columns in the row.
         *
         */
        colLabels = this.getResources().getStringArray(R.array.col_header);
        colIds = new int[] {R.id.placeNameTV, R.id.placeDescriptionTV};
        this.placeNames = places.getNames();
        Arrays.sort(this.placeNames);
        fillMaps = new ArrayList<HashMap<String,String>>();
        HashMap<String,String> titles = new HashMap<>();
        // first row contains column headers
        titles.put("Name","Name");
        titles.put("Description","Description");
        fillMaps.add(titles);
        // fill in the remaining rows with first last and student id.
        for (int i = 0; i < placeNames.length; i++) {
            String name = placeNames[i];
            HashMap<String,String> map = new HashMap<>();
            Log.d(this.getClass().getSimpleName(),"mapping: "+ name);
            map.put("Name", name);
            map.put("Description", (places.get(this.placeNames[i]).description).toString());
            android.util.Log.w(this.getClass().getSimpleName(),placeNames[i]+" has  description "+
                    (places.get(this.placeNames[i]).description).toString());
            fillMaps.add(map);
        }
    }

    // create the menu items for this activity, placed in the action bar.
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        android.util.Log.d(this.getClass().getSimpleName(), "called onCreateOptionsMenu()");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Handle selections of action bar menu items. These are defined a menu.xml resource that is
    // inflated by the onCreateOptionsMenu method. Note that the home action bar (back arrow) is
    // placed by default in any action bar for an activity that is defined with a parent activity
    // in manifest.xml. In this example, StudentDisplayActivity's action bar has an arrow which
    // appears in the left of the action bar. Selecting that arrow is handled by the case:
    // onOptionsItemSelected method for that activity in:    case android.R.id.home:
    // The built-in icons that you can place in an action bar (and elsewhere) are
    // defined in the android.R.drawable static final class. See:
    //   https://developer.android.com/reference/android/R.drawable.html
    // these are referenced in the menu.xml, such as:
    //         android:icon="@android:drawable/ic_menu_delete"
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        android.util.Log.d(this.getClass().getSimpleName(), "called onOptionsItemSelected()");
        switch (item.getItemId()) {
            case R.id.action_add:
                android.util.Log.d(this.getClass().getSimpleName(),"onOptionsItemSelected -> add");
                this.newPlaceAlert();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //listview.onitemclicklistener method
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        String[] placeNames = places.getNames();
        Arrays.sort(placeNames);
        if(position > 0 && position <= placeNames.length) {
            android.util.Log.d(this.getClass().getSimpleName(), "in method onItemClick. selected: " + placeNames[position-1]);
            Intent displayPlace = new Intent(this, PlaceDetailActivity.class);
            displayPlace.putExtra("places", places);
            displayPlace.putExtra("selected", placeNames[position-1]);
            this.startActivityForResult(displayPlace, 1);
        }
    }

    // called when the finish() method is called in the StudentDisplayActivity. This occurs
    // when done displaying (and possibly modifying places). In case a student has been removed,
    // must update the list view (via a new adapter).
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        places = data.getSerializableExtra("places") != null ? (PlaceLibrary) data.getSerializableExtra("places") : places;
        this.prepareAdapter();
        SimpleAdapter sa = new SimpleAdapter(this, fillMaps, R.layout.place_list_item, colLabels, colIds);
        placeLV.setAdapter(sa);
        placeLV.setOnItemClickListener(this);
    }

    private void newPlaceAlert() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Place Name and description");
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        this.nameBox = new EditText(this);
        nameBox.setHint("Name");
        layout.addView(nameBox);

        this.descriptionBox = new EditText(this);
        descriptionBox.setHint("Description");
        layout.addView(descriptionBox);
        dialog.setView(layout);
        dialog.setNegativeButton("Cancel", this);
        dialog.setPositiveButton("Add", this);
        dialog.show();
    }

    // DialogInterface.onClickListener method. Gets called when negative or positive button is clicked
    // in the Alert Dialog created by the newPlaceAlert method.
    @Override
    public void onClick(DialogInterface dialog, int whichButton) {
        android.util.Log.d(this.getClass().getSimpleName(),"onClick positive button? "+
                (whichButton==DialogInterface.BUTTON_POSITIVE));
        if(whichButton == DialogInterface.BUTTON_POSITIVE) {
            String name = nameBox.getText().toString();
            String description = descriptionBox.getText().toString();
            places.add(new Place(name, description, "", "",
                    "", 0.0, 0.0, 0.0));
            prepareAdapter();
            SimpleAdapter sa = new SimpleAdapter(this, fillMaps, R.layout.place_list_item, colLabels, colIds);
            placeLV.setAdapter(sa);
        }
    }

}
