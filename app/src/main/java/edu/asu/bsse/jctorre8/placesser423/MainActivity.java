package edu.asu.bsse.jctorre8.placesser423;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import edu.asu.bsse.jctorre8.placesser423.data.PlacesContract;
import edu.asu.bsse.jctorre8.placesser423.data.PlacesDbHelper;
import edu.asu.bsse.jctorre8.placesser423.data.FakeDataUtil;



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

public class MainActivity extends AppCompatActivity
        implements PlacesAdapter.ListItemClickListener{

    private PlacesAdapter mAdapter;
    private PlacesDbHelper mDb;
    private EditText mNewPlaceNameEditText;
    private Toast mToast;
    private final static String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView mPlacesList;

        Toolbar myToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(myToolbar);
        myToolbar.setTitleTextColor(android.graphics.Color.WHITE);

        mPlacesList = (RecyclerView) this.findViewById(R.id.rv_places);
        mNewPlaceNameEditText = (EditText) this.findViewById(R.id.place_name_edit_text);

        // Set layout for the RecyclerView
        mPlacesList.setLayoutManager(new LinearLayoutManager(this));

        // Create a DB helper (this will create the DB if run for the first time)
        mDb = new PlacesDbHelper(this);

        // Get all places info from the database and save in a cursor
        Cursor cursor = mDb.getData();

        if(cursor.getCount() == 0){
            //Comment out to insert Fake Data for testing
            FakeDataUtil.insertFakeData(mDb.getWritableDatabase());
            cursor = mDb.getData();
        }

        // Link the adapter to the RecyclerView
        mAdapter = new PlacesAdapter(this, cursor);
        mPlacesList.setAdapter(mAdapter);
    }
    /**
     * This method is called when user clicks on the Add to waitlist button
     *
     * @param view The calling view (button)
     */
    public void addToDb(View view) {
        if (mNewPlaceNameEditText.getText().length() == 0) {
            return;
        }
        String name = mNewPlaceNameEditText.getText().toString();

        if(!mDb.isPlaceInDb(name)){
            Log.d(LOG_TAG, "onItemClick: The ID is: " + name);
            Intent editScreenIntent = new Intent(MainActivity.this, PlaceInfoActivity.class);
            editScreenIntent.putExtra("intentId",0);
            editScreenIntent.putExtra("name",name);
            startActivity(editScreenIntent);
        }
        else{
            toastMessage("This place already in the database");
        }
    }

    @Override
    public void onListItemClick(String clickedItemName) {
        if (mToast != null) {
            mToast.cancel();
        }
        if(mDb.isPlaceInDb(clickedItemName)){
            Log.d(LOG_TAG, "onItemClick: The ID is: " + clickedItemName);
            Intent editScreenIntent = new Intent(MainActivity.this, MapsActivity.class);
            editScreenIntent.putExtra("name",clickedItemName);
            startActivity(editScreenIntent);
        }
        else{
            toastMessage("No ID associated with that name");
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Log.d(LOG_TAG, "Settings Clicked: The ID is: " + item);
                Intent settingsScreenIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settingsScreenIntent);
                //this.toastMessage("Settings Pressed");
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_sync:

                this.toastMessage("Syncing Database with Server");
                // User chose the "Settings" item, show the app settings UI...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);
        return super.onCreateOptionsMenu(menu);
    }


    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        mToast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}