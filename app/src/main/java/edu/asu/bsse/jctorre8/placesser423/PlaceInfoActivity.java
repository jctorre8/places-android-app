package edu.asu.bsse.jctorre8.placesser423;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class PlaceInfoActivity extends AppCompatActivity {

    private PlacesDbHelper mDb;
    private EditText mNewPlaceNameEdit;
    private EditText mNewPlaceDescriptionEdit;
    private EditText mNewPlaceCategoryEdit;
    private EditText mNewPlaceAddressTitleEdit;
    private EditText mNewPlaceAddressStreetEdit;
    private EditText mNewPlaceElevationEdit;
    private EditText mNewPlaceLatitudeEdit;
    private EditText mNewPlaceLongitudeEdit;
    private String selectedName;
    private int intentID;
    private final static String LOG_TAG = PlaceInfoActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_information_view);
        mNewPlaceNameEdit = (EditText) this.findViewById(R.id.place_name_edit);
        mNewPlaceDescriptionEdit = (EditText) this.findViewById(R.id.place_description_edit);
        mNewPlaceCategoryEdit = (EditText) this.findViewById(R.id.place_category_edit);
        mNewPlaceAddressTitleEdit = (EditText) this.findViewById(R.id.place_address_title_edit);
        mNewPlaceAddressStreetEdit = (EditText) this.findViewById(R.id.place_address_street_edit);
        mNewPlaceElevationEdit = (EditText) this.findViewById(R.id.place_elevation_edit);
        mNewPlaceLatitudeEdit = (EditText) this.findViewById(R.id.place_latitude_edit);
        mNewPlaceLongitudeEdit = (EditText) this.findViewById(R.id.place_longitude_edit);

        // Set buttons
        Button mUpdateButton  = (Button) this.findViewById(R.id.btnUpdate);
        Button mDistButton = (Button) this.findViewById(R.id.btnDist);
        Button mDeleteButton = (Button) this.findViewById(R.id.btnDelete);

        // Create a DB helper (this will create the DB if run for the first time)
        mDb = new PlacesDbHelper(this);

        //get the intent extra from the ListDataActivity
        Intent receivedIntent = getIntent();

        //now get intent data
        intentID = receivedIntent.getIntExtra("intentId",0);
        selectedName = receivedIntent.getStringExtra("name");

        if(intentID == 0){
            // Set default values
            mNewPlaceNameEdit.setText(selectedName);
            mNewPlaceNameEdit.setEnabled(false);
            String save = "Save";
            mUpdateButton.setText(save);
            mDistButton.setEnabled(false);
            mDeleteButton.setEnabled(false);

        }else if(intentID == 1){

            Cursor data = mDb.getPlaceByName(selectedName); //get the id associated with that name
            int itemID = -1;
            data.moveToFirst();
            itemID = data.getInt(0);
            if(itemID > -1){

                String des = data.getString(data.getColumnIndex(PlacesContract.PlacesEntry.COLUMN_PLACE_DESCRIPTION));
                String cat = data.getString(data.getColumnIndex(PlacesContract.PlacesEntry.COLUMN_PLACE_CATEGORY));
                String addTit = data.getString(data.getColumnIndex(PlacesContract.PlacesEntry.COLUMN_ADDRESS_TITLE));
                String addSt = data.getString(data.getColumnIndex(PlacesContract.PlacesEntry.COLUMN_ADDRESS_STREET));
                String ele = String.valueOf(data.getDouble(data.getColumnIndex(PlacesContract.PlacesEntry.COLUMN_PLACE_ELEVATION)));
                String lat = String.valueOf(data.getDouble(data.getColumnIndex(PlacesContract.PlacesEntry.COLUMN_PLACE_LATITUDE)));
                String lon = String.valueOf(data.getDouble(data.getColumnIndex(PlacesContract.PlacesEntry.COLUMN_PLACE_LONGITUDE)));

                mNewPlaceNameEdit.setText(selectedName);
                mNewPlaceNameEdit.setEnabled(false);
                mNewPlaceDescriptionEdit.setText(des);
                mNewPlaceCategoryEdit.setText(cat);
                mNewPlaceAddressTitleEdit.setText(addTit);
                mNewPlaceAddressStreetEdit.setText(addSt);
                mNewPlaceElevationEdit.setText(ele);
                mNewPlaceLatitudeEdit.setText(lat);
                mNewPlaceLongitudeEdit.setText(lon);
            }

        }else{
            mNewPlaceNameEdit.setEnabled(false);
            mNewPlaceDescriptionEdit.setEnabled(false);
            mNewPlaceCategoryEdit.setEnabled(false);
            mNewPlaceAddressTitleEdit.setEnabled(false);
            mNewPlaceAddressStreetEdit.setEnabled(false);
            mNewPlaceElevationEdit.setEnabled(false);
            mNewPlaceLatitudeEdit.setEnabled(false);
            mNewPlaceLongitudeEdit.setEnabled(false);
            toastMessage("The app messed up and you should not be here!!!");

        }


    }
    /**
     * This method is called when user clicks on the Add to waitlist button
     *
     * @param view The calling view (button)
     */
    public void updatePlace(View view) {
        if (mNewPlaceNameEdit.getText().length() == 0 ||
                mNewPlaceDescriptionEdit.getText().length() == 0 ||
                mNewPlaceCategoryEdit.getText().length() == 0 ||
                mNewPlaceAddressTitleEdit.getText().length() == 0 ||
                mNewPlaceAddressStreetEdit.getText().length() == 0 ||
                mNewPlaceElevationEdit.getText().length() == 0 ||
                mNewPlaceLatitudeEdit.getText().length() == 0 ||
                mNewPlaceLongitudeEdit.getText().length() == 0 ) {
            toastMessage("All Fields must be filled!");
            return;
        }
        String des = mNewPlaceDescriptionEdit.getText().toString();
        String cat = mNewPlaceCategoryEdit.getText().toString();
        String addTit = mNewPlaceAddressTitleEdit.getText().toString();
        String addSt = mNewPlaceAddressStreetEdit.getText().toString();
        double ele = Double.parseDouble(mNewPlaceElevationEdit.getText().toString());
        double lat = Double.parseDouble(mNewPlaceLatitudeEdit.getText().toString());
        double lon = Double.parseDouble(mNewPlaceLongitudeEdit.getText().toString());

        if(intentID == 0){
            mDb.addPlace(selectedName,des,cat,addTit,addSt,ele,lat,lon);
            if(mDb.isPlaceInDb(selectedName)){
                toastMessage(selectedName + "has been added to Database.");
            } else{
                toastMessage("ERROR adding " + selectedName + " to Database.");
            }
        } else if(intentID == 1){
            mDb.updatePlace(selectedName,des,cat,addTit,addSt,ele,lat,lon);
            toastMessage(selectedName + " has been updated.");
        }else{
            toastMessage("Pressed the updatePlace Button but no action taken.");
        }

        Intent editScreenIntent = new Intent(PlaceInfoActivity.this, MainActivity.class);
        startActivity(editScreenIntent);
    }

    /**
     * This method is called when user clicks on the Delete button
     *
     * @param view The calling view (button)
     */
    public void deletePlace(View view) {
        if (mNewPlaceNameEdit.getText().length() == 0) {
            return;
        }
        if(mDb.deletePlace(mNewPlaceNameEdit.getText().toString())){
            toastMessage(selectedName + "has been deleted from Database.");
        } else{
            toastMessage("ERROR deleting " + selectedName + " from Database.");
        }
        Intent editScreenIntent = new Intent(PlaceInfoActivity.this, MainActivity.class);
        startActivity(editScreenIntent);
    }

    /**
     * This method is called when user clicks on the DistBearing button
     *
     * @param view The calling view (button)
     */
    public void goDistanceAndBearing(View view) {
        if (mNewPlaceNameEdit.getText().length() == 0) {
            return;
        }
        Intent editScreenIntent = new Intent(PlaceInfoActivity.this, DistanceBearingActivity.class);
        editScreenIntent.putExtra("name",selectedName);
        startActivity(editScreenIntent);
    }

    /**
     * This method is called when user clicks on the See Place in Map button
     *
     * @param view The calling view (button)
     */
    public void goToMap(View view) {
        if (mNewPlaceNameEdit.getText().length() == 0) {
            return;
        }
        Intent editScreenIntent = new Intent(PlaceInfoActivity.this, MapsActivity.class);
        editScreenIntent.putExtra("name",selectedName);
        startActivity(editScreenIntent);
    }

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
