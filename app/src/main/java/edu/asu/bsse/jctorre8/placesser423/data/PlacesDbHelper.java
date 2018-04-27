package edu.asu.bsse.jctorre8.placesser423.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
public class PlacesDbHelper extends SQLiteOpenHelper {

    // The database name
    private static final String DATABASE_NAME = "places.db";

    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 1;

    private final static String LOG_TAG = PlacesDbHelper.class.getSimpleName();

    // Constructor
    public PlacesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Create a table to hold places data
        final String SQL_CREATE_PLACES_TABLE = "CREATE TABLE " + PlacesContract.PlacesEntry.TABLE_NAME + " (" +
                PlacesContract.PlacesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                PlacesContract.PlacesEntry.COLUMN_PLACE_NAME + " TEXT NOT NULL, " +
                PlacesContract.PlacesEntry.COLUMN_PLACE_DESCRIPTION + " TEXT NOT NULL, " +
                PlacesContract.PlacesEntry.COLUMN_PLACE_CATEGORY + " TEXT NOT NULL, " +
                PlacesContract.PlacesEntry.COLUMN_ADDRESS_TITLE + " TEXT NOT NULL, " +
                PlacesContract.PlacesEntry.COLUMN_ADDRESS_STREET + " TEXT NOT NULL, " +
                PlacesContract.PlacesEntry.COLUMN_PLACE_ELEVATION + " DOUBLE NOT NULL, " +
                PlacesContract.PlacesEntry.COLUMN_PLACE_LATITUDE + " DOUBLE NOT NULL, " +
                PlacesContract.PlacesEntry.COLUMN_PLACE_LONGITUDE + " DOUBLE NOT NULL" +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_PLACES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // For now simply drop the table and create a new one. This means if you change the
        // DATABASE_VERSION the table will be dropped.
        // In a production app, this method might be modified to ALTER the table
        // instead of dropping it, so that existing data is not deleted.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PlacesContract.PlacesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    /**
     * This method adds a place to the database with the given parameters
     *
     * @param name  The name of the place.
     * @param des   The description of the place.
     * @param cat   The category of the place
     * @param addTit The Address Title of the place.
     * @param addSt The Address Street of the place
     * @param ele   The elevation of the place.
     * @param lat   The latitude of the place.
     * @param lon   The longitude of the place.
     * @return
     */
    public boolean addPlace(String name, String des, String cat, String addTit, String addSt,
                            double ele, double lat, double lon){
        boolean toReturn = false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(PlacesContract.PlacesEntry.COLUMN_PLACE_NAME, name);
        cv.put(PlacesContract.PlacesEntry.COLUMN_PLACE_DESCRIPTION, des);
        cv.put(PlacesContract.PlacesEntry.COLUMN_ADDRESS_TITLE, addTit);
        cv.put(PlacesContract.PlacesEntry.COLUMN_ADDRESS_STREET, addSt);
        cv.put(PlacesContract.PlacesEntry.COLUMN_PLACE_CATEGORY, cat);
        cv.put(PlacesContract.PlacesEntry.COLUMN_PLACE_ELEVATION, ele);
        cv.put(PlacesContract.PlacesEntry.COLUMN_PLACE_LATITUDE, lat);
        cv.put(PlacesContract.PlacesEntry.COLUMN_PLACE_LONGITUDE, lon);

        //insert all places in one transaction
        try
        {
            db.beginTransaction();
            db.insert(PlacesContract.PlacesEntry.TABLE_NAME, null, cv);
            db.setTransactionSuccessful();
            toReturn = true;
        }
        catch (SQLException e) {
            //too bad :(
        }
        finally
        {
            db.endTransaction();
        }
        return toReturn;
    }

    /**
     * This method updates a place to the database with the given parameters
     *
     * @param name  The name of the place.
     * @param des   The description of the place.
     * @param cat   The category of the place
     * @param addTit The Address Title of the place.
     * @param addSt The Address Street of the place
     * @param ele   The elevation of the place.
     * @param lat   The latitude of the place.
     * @param lon   The longitude of the place.
     * @return
     */
    public void updatePlace(String name, String des, String cat, String addTit, String addSt,
                            double ele, double lat, double lon) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(PlacesContract.PlacesEntry.COLUMN_PLACE_DESCRIPTION, des);
        cv.put(PlacesContract.PlacesEntry.COLUMN_ADDRESS_TITLE, addTit);
        cv.put(PlacesContract.PlacesEntry.COLUMN_ADDRESS_STREET, addSt);
        cv.put(PlacesContract.PlacesEntry.COLUMN_PLACE_CATEGORY, cat);
        cv.put(PlacesContract.PlacesEntry.COLUMN_PLACE_ELEVATION, ele);
        cv.put(PlacesContract.PlacesEntry.COLUMN_PLACE_LATITUDE, lat);
        cv.put(PlacesContract.PlacesEntry.COLUMN_PLACE_LONGITUDE, lon);

        //insert all places in one transaction
        try
        {
            // Which row to update, based on the title
            String selection = PlacesContract.PlacesEntry.COLUMN_PLACE_NAME + " = ?";
            String[] selectionArgs = { name };
            db.beginTransaction();
            db.update(PlacesContract.PlacesEntry.TABLE_NAME, cv, selection, selectionArgs);
            db.setTransactionSuccessful();
        }
        catch (SQLException e) {
            //too bad :(
        }
        finally
        {
            db.endTransaction();
        }
    }

    /**
     * Updates the name of the place in the database.
     * @param oldName  The name of the place.
     * @param newName   The new name for the place.
     */
    public void updatePlaceName(String oldName, String newName){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + PlacesContract.PlacesEntry.TABLE_NAME +
                " SET " + PlacesContract.PlacesEntry.COLUMN_PLACE_NAME +  " = '" + newName + "'" +
                " WHERE " + PlacesContract.PlacesEntry.COLUMN_PLACE_NAME + " = '" + oldName + "'";
        db.execSQL(query);
    }

    /**
     * Updates the description of the place in the database.
     * @param name  The name of the place.
     * @param des   The new description for the place.
     */
    public void updatePlaceDescription(String name, String des){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + PlacesContract.PlacesEntry.TABLE_NAME +
                " SET " + PlacesContract.PlacesEntry.COLUMN_PLACE_DESCRIPTION +  " = '" + des + "'" +
                " WHERE " + PlacesContract.PlacesEntry.COLUMN_PLACE_NAME + " = '" + name + "'";
        db.execSQL(query);
    }

    /**
     * Updates the category of the place in the database.
     * @param name  The name of the place.
     * @param cat   The new category for the place.
     */
    public void updatePlaceCategory(String name, String cat){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + PlacesContract.PlacesEntry.TABLE_NAME +
                " SET " + PlacesContract.PlacesEntry.COLUMN_PLACE_CATEGORY +  " = '" + cat + "'" +
                " WHERE " + PlacesContract.PlacesEntry.COLUMN_PLACE_NAME + " = '" + name + "'";
        db.execSQL(query);
    }

    /**
     * Updates the Address Title of the place in the database.
     * @param name  The name of the place.
     * @param addTit   The new Address Title for the place.
     */
    public void updatePlaceAddressTitle(String name, String addTit){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + PlacesContract.PlacesEntry.TABLE_NAME +
                " SET " + PlacesContract.PlacesEntry.COLUMN_ADDRESS_TITLE +  " = '" + addTit + "'" +
                " WHERE " + PlacesContract.PlacesEntry.COLUMN_PLACE_NAME + " = '" + name + "'";
        db.execSQL(query);
    }

    /**
     * Updates the Address Street of the place in the database.
     * @param name  The name of the place.
     * @param addSt   The new Address Street for the place.
     */
    public void updatePlaceAddressStreet(String name, String addSt){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + PlacesContract.PlacesEntry.TABLE_NAME +
                " SET " + PlacesContract.PlacesEntry.COLUMN_ADDRESS_STREET +  " = '" + addSt + "'" +
                " WHERE " + PlacesContract.PlacesEntry.COLUMN_PLACE_NAME + " = '" + name + "'";
        db.execSQL(query);
    }

    /**
     * Updates the elevation of the place in the database.
     * @param name  The name of the place.
     * @param ele   The new elevation for the place.
     */
    public void updatePlaceElevation(String name, double ele){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + PlacesContract.PlacesEntry.TABLE_NAME +
                " SET " + PlacesContract.PlacesEntry.COLUMN_PLACE_ELEVATION +  " = " + ele +
                " WHERE " + PlacesContract.PlacesEntry.COLUMN_PLACE_NAME + " = '" + name + "'";
        db.execSQL(query);
    }

    /**
     * Updates the latitude of the place in the database.
     * @param name  The name of the place.
     * @param lat   The new latitude for the place.
     */
    public void updatePlaceLatitude(String name, double lat){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + PlacesContract.PlacesEntry.TABLE_NAME +
                " SET " + PlacesContract.PlacesEntry.COLUMN_PLACE_LATITUDE +  " = " + lat +
                " WHERE " + PlacesContract.PlacesEntry.COLUMN_PLACE_NAME + " = '" + name + "'";
        db.execSQL(query);
    }

    /**
     * Updates the longitude of the place in the database.
     * @param name  The name of the place.
     * @param lon   The new longitude for the place.
     */
    public void updatePlaceLongitude(String name, double lon){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + PlacesContract.PlacesEntry.TABLE_NAME +
                " SET " + PlacesContract.PlacesEntry.COLUMN_PLACE_LONGITUDE +  " = " + lon +
                " WHERE " + PlacesContract.PlacesEntry.COLUMN_PLACE_NAME + " = '" + name + "'";
        db.execSQL(query);
    }

    /**
     * This method deletes a place from the database.
     * @param name The name of the method to delete.
     * @return If the transaction was successful.
     */
    public boolean deletePlace(String name){
        boolean toReturn = false;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + PlacesContract.PlacesEntry.TABLE_NAME + " WHERE "
                + PlacesContract.PlacesEntry.COLUMN_PLACE_NAME + " = '" + name + "'";
        Log.d(LOG_TAG, "deleteName: query: " + query);
        Log.d(LOG_TAG, "deleteName: Deleting " + name + " from database.");
        db.execSQL(query);

        if(!this.isPlaceInDb(name)){
            toReturn = true;
        }

        return toReturn;
    }

    /**
     * This method checks if a place is in the database.
     * @param name  The name of the place to check.
     * @return True if the place is in the database.
     */
    public boolean isPlaceInDb(String name){
        boolean toReturn = false;
        Cursor data = this.getPlaceByName(name); //get the id associated with that name
        int itemID = -1;
        while(data.moveToNext()){
            itemID = data.getInt(0);
        }
        if(itemID > -1){
            toReturn = true;
        }
        return toReturn;
    }

    /**
     * Returns all the data in the database.
     * @return A Cursor with all the data.
     */
    public Cursor getData(){

        SQLiteDatabase db = this.getWritableDatabase();
        return db.query(
                PlacesContract.PlacesEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                PlacesContract.PlacesEntry._ID
        );

    }

    /**
     * This method searches for a particular place and returns it.
     * @param name The name of the place being looked for.
     * @return The place in a cursor.
     */
    public Cursor getPlaceByName(String name){

        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(
                PlacesContract.PlacesEntry.TABLE_NAME,
                null,
                PlacesContract.PlacesEntry.COLUMN_PLACE_NAME + " = '"+ name +"'",
                null,
                null,
                null,
                PlacesContract.PlacesEntry._ID
        );

    }
}
