package edu.asu.bsse.jctorre8.placesser423;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import edu.asu.bsse.jctorre8.placesser423.data.PlacesContract;
import edu.asu.bsse.jctorre8.placesser423.data.PlacesDbHelper;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private PlacesDbHelper mDb;
    private String selectedName;
    private Spinner toPlaces;
    private String des;
    private String cat;
    private String addTit;
    private String addSt;
    private double ele;
    private double lat;
    private double lon;
    private float zoom;
    private ArrayList<String> placeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        toPlaces = (Spinner) this.findViewById(R.id.new_map_place_spinner);

        // Create a DB helper (this will create the DB if run for the first time)
        mDb = new PlacesDbHelper(this);

        //get the intent extra from the ListDataActivity
        Intent receivedIntent = getIntent();

        //now get intent data
        selectedName = receivedIntent.getStringExtra("name");
        Cursor data = mDb.getPlaceByName(selectedName); //get the id associated with that name
        int itemID = -1;
        data.moveToFirst();
        itemID = data.getInt(0);
        if(itemID > -1) {

            this.des = data.getString(data.getColumnIndex(PlacesContract.PlacesEntry.COLUMN_PLACE_DESCRIPTION));
            this.cat = data.getString(data.getColumnIndex(PlacesContract.PlacesEntry.COLUMN_PLACE_CATEGORY));
            this.addTit = data.getString(data.getColumnIndex(PlacesContract.PlacesEntry.COLUMN_ADDRESS_TITLE));
            this.addSt = data.getString(data.getColumnIndex(PlacesContract.PlacesEntry.COLUMN_ADDRESS_STREET));
            this.ele = data.getDouble(data.getColumnIndex(PlacesContract.PlacesEntry.COLUMN_PLACE_ELEVATION));
            this.lat = data.getDouble(data.getColumnIndex(PlacesContract.PlacesEntry.COLUMN_PLACE_LATITUDE));
            this.lon = data.getDouble(data.getColumnIndex(PlacesContract.PlacesEntry.COLUMN_PLACE_LONGITUDE));
        } else{
            this.des = "A lovely place!";
            this.cat = "City";
            this.addTit = "Ann Arbor";
            this.addSt = "Ann Arbor, MI";
            this.ele = 0;
            this.lat = 42.284629;
            this.lon = -83.745103;
        }
        zoom = 16;

        placeList = new ArrayList<String>();
        this.populateToPlacesList();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, placeList);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        toPlaces.setAdapter(adapter);

            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void populateToPlacesList(){
        placeList.add("To Place");
        Cursor places = mDb.getData();

        while(places.moveToNext()){
            placeList.add(places.getString(places.getColumnIndex(PlacesContract.PlacesEntry.COLUMN_PLACE_NAME)));
        }
    }

    public void refreshPlaceInfo(){
        this.selectedName = toPlaces.getSelectedItem().toString();

        Cursor data = mDb.getPlaceByName(selectedName); //get the id associated with that name
        int itemID = -1;
        data.moveToFirst();
        itemID = data.getInt(0);
        if(itemID > -1) {

            this.des = data.getString(data.getColumnIndex(PlacesContract.PlacesEntry.COLUMN_PLACE_DESCRIPTION));
            this.cat = data.getString(data.getColumnIndex(PlacesContract.PlacesEntry.COLUMN_PLACE_CATEGORY));
            this.addTit = data.getString(data.getColumnIndex(PlacesContract.PlacesEntry.COLUMN_ADDRESS_TITLE));
            this.addSt = data.getString(data.getColumnIndex(PlacesContract.PlacesEntry.COLUMN_ADDRESS_STREET));
            this.ele = data.getDouble(data.getColumnIndex(PlacesContract.PlacesEntry.COLUMN_PLACE_ELEVATION));
            this.lat = data.getDouble(data.getColumnIndex(PlacesContract.PlacesEntry.COLUMN_PLACE_LATITUDE));
            this.lon = data.getDouble(data.getColumnIndex(PlacesContract.PlacesEntry.COLUMN_PLACE_LONGITUDE));
        } else{
            this.des = "A lovely place!";
            this.cat = "City";
            this.addTit = "Ann Arbor";
            this.addSt = "Ann Arbor, MI";
            this.ele = 0;
            this.lat = 42.284629;
            this.lon = -83.745103;
        }
        zoom = 16;
    }
    /**
     * This method is called when user clicks on the See Place in Map button
     *
     * @param view The calling view (button)
     */
    public void refreshMap(View view) {
        this.refreshPlaceInfo();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * This method is called when user clicks on the See Place in Map button
     *
     * @param view The calling view (button)
     */
    public void toPlaceInfo(View view) {
        Intent editScreenIntent = new Intent(MapsActivity.this, PlaceInfoActivity.class);
        editScreenIntent.putExtra("intentId",1);
        editScreenIntent.putExtra("name",this.selectedName);
        startActivity(editScreenIntent);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng place = new LatLng(this.lat, this.lon);

        mMap.addMarker(new MarkerOptions().position(place).title(this.addTit).snippet(this.des));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place,this.zoom),5000,null);
    }
}
