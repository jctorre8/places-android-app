package edu.asu.bsse.jctorre8.placesser423;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

import edu.asu.bsse.jctorre8.placesser423.data.PlacesContract;
import edu.asu.bsse.jctorre8.placesser423.data.PlacesDbHelper;

/**
 * Copyright 2018 Jean Torres,
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
 * Class to perform various earth surface calculations. Given lat/lon points
 * this class can perform distance and bearing calculations - Great Circle,
 * and Rhumb-line.
 *
 * Ser321 Principles of Distributed Software Systems
 * see http://pooh.poly.asu.edu/Ser321
 * @author Jean Torres jctorre8@asu.edu
 * @version April, 2018
 **/

public class DistanceBearingActivity extends AppCompatActivity {

    private PlacesDbHelper mDb;
    private EditText mFromPlaceNameEdit;
    private EditText mDistanceEdit;
    private EditText mHeadingEdit;
    private String selectedName;
    private Spinner toPlaces;
    private ArrayList<String> placeList;

    private final static int STATUTE = 0;
    private final static int NAUTICAL = 1;
    private final static int KMETER = 2;
    private final static double radiusE = 6371;
    private final static String LOG_TAG = DistanceBearingActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.distance_bearing_view);
        mFromPlaceNameEdit = (EditText) this.findViewById(R.id.from_place_name);
        mDistanceEdit = (EditText) this.findViewById(R.id.from_distance_edit);
        mHeadingEdit = (EditText) this.findViewById(R.id.from_heading_edit);
        toPlaces = (Spinner) this.findViewById(R.id.to_places_spinner);

        // Create a DB helper (this will create the DB if run for the first time)
        mDb = new PlacesDbHelper(this);

        //get the intent extra from the ListDataActivity
        Intent receivedIntent = getIntent();

        //now get intent data
        selectedName = receivedIntent.getStringExtra("name");
        mFromPlaceNameEdit.setText(selectedName);
        mFromPlaceNameEdit.setEnabled(false);

        placeList = new ArrayList<String>();
        this.populateToPlacesList();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, placeList);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toPlaces.setAdapter(adapter);


    }

    public void populateToPlacesList(){
        placeList.add("To Place");
        Cursor places = mDb.getData();

        while(places.moveToNext()){
            placeList.add(places.getString(places.getColumnIndex(PlacesContract.PlacesEntry.COLUMN_PLACE_NAME)));
        }
    }

    public void calculateDistAndHead(View view){
        String place1 = mFromPlaceNameEdit.getText().toString();
        String place2 = toPlaces.getSelectedItem().toString();
        if (place2.equals("To Place")) {
            toastMessage("Please select a place to calculate distance and heading.");
            return;
        }
        DecimalFormat df2 = new DecimalFormat( "0.0000" );
        double distance = distanceGCTo(place1,place2,KMETER);
        double heading = bearingGCInitTo(place1,place2,KMETER);
        double distance2dec = new Double(df2.format(distance)).doubleValue();
        double heading2dec = new Double(df2.format(heading)).doubleValue();
        mDistanceEdit.setText(String.valueOf(distance2dec));
        mHeadingEdit.setText(String.valueOf(heading2dec));
    }

    /**
     * calculate the distance great circle route between this and the
     * argument Places. Return the result in the requested scale
     */
    public double distanceGCTo(String fromPlace, String toPlace, int scale){
        Cursor place1 = mDb.getPlaceByName(fromPlace);
        Cursor place2 = mDb.getPlaceByName(toPlace);
        place1.moveToFirst();
        place2.moveToFirst();

        double fromPlaceLat = place1.getDouble(place1.getColumnIndex(
                PlacesContract.PlacesEntry.COLUMN_PLACE_LATITUDE));
        double fromPlaceLon = place1.getDouble(place1.getColumnIndex(
                PlacesContract.PlacesEntry.COLUMN_PLACE_LONGITUDE));
        double toPlaceLat = place2.getDouble(place2.getColumnIndex(
                PlacesContract.PlacesEntry.COLUMN_PLACE_LATITUDE));
        double toPlaceLon = place2.getDouble(place2.getColumnIndex(
                PlacesContract.PlacesEntry.COLUMN_PLACE_LONGITUDE));

        double ret = 0.0;
        try {
            // ret is in kilometers if you use the availble algorithms. S
            double lat1 = Math.toRadians(fromPlaceLat);
            double lat2 = Math.toRadians(toPlaceLat);
            double deltaLat = Math.toRadians(lat2 - lat1);
            double deltaLon = Math.toRadians(fromPlaceLon - toPlaceLon);
            double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                    Math.cos(lat1) * Math.cos(lat2) *
                            Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            ret = radiusE * c;
            // Switch to either Statute or Nautical if necessary
            switch (scale) {
                case STATUTE:
                    ret = ret * 0.62137119;
                    break;
                case NAUTICAL:
                    ret = ret * 0.5399568;
                    break;
                case KMETER:
                    break;
            }
        }catch (Exception e){
            // On nooo!!
        }
        return ret;
    }

    /**
     * calculate the initial heading on the circle route between this and the
     * argument Places. Return the result.
     */
    public double bearingGCInitTo(String fromPlace, String toPlace, int scale){
        Cursor place1 = mDb.getPlaceByName(fromPlace);
        Cursor place2 = mDb.getPlaceByName(toPlace);
        place1.moveToFirst();
        place2.moveToFirst();

        double fromPlaceLat = place1.getDouble(place1.getColumnIndex(
                PlacesContract.PlacesEntry.COLUMN_PLACE_LATITUDE));
        double fromPlaceLon = place1.getDouble(place1.getColumnIndex(
                PlacesContract.PlacesEntry.COLUMN_PLACE_LONGITUDE));
        double toPlaceLat = place2.getDouble(place2.getColumnIndex(
                PlacesContract.PlacesEntry.COLUMN_PLACE_LATITUDE));
        double toPlaceLon = place2.getDouble(place2.getColumnIndex(
                PlacesContract.PlacesEntry.COLUMN_PLACE_LONGITUDE));

        double ret = 0.0;
        try {
            // ret is in kilometers if you use the availble algorithms. S
            double lat1 = Math.toRadians(fromPlaceLat);
            double lat2 = Math.toRadians(toPlaceLat);
            double deltaLat = Math.toRadians(lat2 - lat1);
            double deltaLon = Math.toRadians(fromPlaceLon - toPlaceLon);
            double y = Math.sin(deltaLon) * Math.cos(lat2);
            double x = Math.cos(lat1) * Math.sin(lat2) -
                    Math.sin(lat1) * Math.cos(lat2) * Math.cos(deltaLat);
            double brng = Math.atan2(y, x);
            ret = Math.toDegrees(brng);
        }catch (Exception e){
            // On Noooo!!
        }
        return ret;
    }


    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
