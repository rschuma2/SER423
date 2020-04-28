package edu.asu.cisde.se.schumacher.a3_1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;


public class PlaceDetailActivity extends AppCompatActivity implements
        DialogInterface.OnClickListener {


    private TextView name;
    private TextView description;
    private TextView category;
    private TextView addressTitle;
    private TextView addressStreet;
    private TextView elevation;
    private TextView latitude;
    private TextView longitude;

    private String selectedPlace, selectedCourse;
    private PlaceLibrary places;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_detail);
        Intent intent = getIntent();
        places = intent.getSerializableExtra("places")!=null ? (PlaceLibrary)intent.getSerializableExtra("places") :
                new PlaceLibrary(this);
        selectedPlace = intent.getStringExtra("selected")!=null ? intent.getStringExtra("selected") : "unknown";
        Place aPlace = places.get(selectedPlace);
        name.setText("Description "+ (aPlace.name));
        description.setText("Description "+ (aPlace.description));
        category.setText("Description "+ (aPlace.category));
        addressTitle.setText("Description "+ (aPlace.addressTitle));
        addressStreet.setText("Description "+ (aPlace.addressStreet));
        elevation.setText("Description "+ (aPlace.elevation));
        latitude.setText("Description "+ (aPlace.latitude));
        longitude.setText("Description "+ (aPlace.longitude));
        // set up a back button to return to the view main activity / view
            try {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }catch(Exception ex){
            android.util.Log.d(this.getClass().getSimpleName(),"exception action bar: "+ex.getLocalizedMessage());
        }
        setTitle(aPlace.name+"'s Courses");
    }

    // create the menu items for this activity, placed in the action bar.
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        android.util.Log.d(this.getClass().getSimpleName(), "called onCreateOptionsMenu()");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.place_display_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /*
     * Implement onOptionsItemSelected(MenuItem item){} to handle clicks of buttons that are
     * in the action bar.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        android.util.Log.d(this.getClass().getSimpleName(), "called onOptionsItemSelected() id: "+item.getItemId()
                +" title "+item.getTitle());
        switch (item.getItemId()) {
            // the user selected the up/home button (left arrow at left of action bar)
            case android.R.id.home:
                android.util.Log.d(this.getClass().getSimpleName(),"onOptionsItemSelected -> home");
                Intent i = new Intent();
                i.putExtra("places", places);
                this.setResult(RESULT_OK,i);
                finish();
                return true;
            // the user selected the action (garbage can) to remove the student
            case R.id.action_remove:
                android.util.Log.d(this.getClass().getSimpleName(),"onOptionsItemSelected -> remove");
                this.removePlaceAlert();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // show an alert view for the user to confirm removing the selected student
    private void removePlaceAlert() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Remove Place "+this.selectedPlace+"?");
        dialog.setNegativeButton("Cancel", this);
        dialog.setPositiveButton("Remove", this);
        dialog.show();
    }

    // DialogInterface.onClickListener method. Gets called when negative or positive button is clicked
    // in the Alert Dialog created by the newPlaceAlert method.
    @Override
    public void onClick(DialogInterface dialog, int whichButton) {
        android.util.Log.d(this.getClass().getSimpleName(),"onClick positive button? "+
                (whichButton==DialogInterface.BUTTON_POSITIVE));
        if(whichButton == DialogInterface.BUTTON_POSITIVE) {
            // ok, so remove the place and return the modified model to main activity
            places.remove(this.selectedPlace);
            Intent i = new Intent();
            i.putExtra("places", places);
            this.setResult(RESULT_OK,i);
            finish();
        }
    }


}