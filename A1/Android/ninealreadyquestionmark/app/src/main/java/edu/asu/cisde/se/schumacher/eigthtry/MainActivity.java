package edu.asu.cisde.se.schumacher.eigthtry;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView name;
    private TextView description;
    private TextView category;
    private TextView addressTitle;
    private TextView addressStreet;
    private TextView elevation;
    private TextView latitude;
    private TextView longitude;
    private PlaceDescription jo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = (TextView)findViewById(R.id.nameTV);
        description = (TextView)findViewById(R.id.descriptionTV);
        category = (TextView)findViewById(R.id.categoryTV);
        addressTitle = (TextView)findViewById(R.id.addressTitleTV);
        addressStreet = (TextView)findViewById(R.id.addressStreetTV);
        elevation = (TextView)findViewById(R.id.elevationTV);
        latitude = (TextView)findViewById(R.id.latitudeTV);
        longitude = (TextView)findViewById(R.id.longitudeTV);
        jo = new PlaceDescription("{\"name\":\"ASU-Poly\",\"description\":\"Home of ASU's Software Engineering Programs\",\"category\":\"school\",\"address-title\":\"ASU Software Engineering\",\"address-street\":[\"7171 E Sonoran Arroyo Mall\",\"Peralta Hall 230\",\"Mesa AZ 85212\"],\"elevation\":1384.0,\"latitude\":33.306388,\"longitude\":-111.679121}");

    }

    public void buttonClicked(View view) {

        name.setText(jo.name);
        description.setText(jo.description);
        category.setText(jo.category);
        addressTitle.setText(jo.addressTitle);
        addressStreet.setText(jo.addressStreet.get(0));
        elevation.setText(jo.elevation);
        latitude.setText(jo.latitude);
        longitude.setText(jo.longitude);
    }

}
