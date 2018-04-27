package edu.asu.bsse.jctorre8.placesser423.data;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

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
public class FakeDataUtil {

    public static void insertFakeData(SQLiteDatabase db){
        if(db == null){
            return;
        }
        //create a list of fake guests
        List<ContentValues> list = new ArrayList<ContentValues>();

        ContentValues cv = new ContentValues();
        cv.put(PlacesContract.PlacesEntry.COLUMN_PLACE_NAME, "ASU-Poly");
        cv.put(PlacesContract.PlacesEntry.COLUMN_PLACE_DESCRIPTION, "Home of ASU's Software " +
                "Engineering Programs");
        cv.put(PlacesContract.PlacesEntry.COLUMN_ADDRESS_TITLE, "ASU Software Engineering");
        cv.put(PlacesContract.PlacesEntry.COLUMN_ADDRESS_STREET, "7171 E Sonoran Arroyo Mall\n" +
                "Peralta Hall 230\nMesa AZ 85212");
        cv.put(PlacesContract.PlacesEntry.COLUMN_PLACE_CATEGORY, "School");
        cv.put(PlacesContract.PlacesEntry.COLUMN_PLACE_ELEVATION, 1384.0);
        cv.put(PlacesContract.PlacesEntry.COLUMN_PLACE_LATITUDE, 33.306388);
        cv.put(PlacesContract.PlacesEntry.COLUMN_PLACE_LONGITUDE, -111.679121);
        list.add(cv);

        cv = new ContentValues();
        cv.put(PlacesContract.PlacesEntry.COLUMN_PLACE_NAME, "Zingermans");
        cv.put(PlacesContract.PlacesEntry.COLUMN_PLACE_DESCRIPTION, "Locals line up for generous " +
                "deli sandwiches at this funky, longtime market with specialty groceries.");
        cv.put(PlacesContract.PlacesEntry.COLUMN_ADDRESS_TITLE, "Zingerman's Delicatessen");
        cv.put(PlacesContract.PlacesEntry.COLUMN_ADDRESS_STREET, "422 Detroit St, Ann Arbor, MI 48104");
        cv.put(PlacesContract.PlacesEntry.COLUMN_PLACE_CATEGORY, "Food");
        cv.put(PlacesContract.PlacesEntry.COLUMN_PLACE_ELEVATION, 1284.0);
        cv.put(PlacesContract.PlacesEntry.COLUMN_PLACE_LATITUDE, 42.284632);
        cv.put(PlacesContract.PlacesEntry.COLUMN_PLACE_LONGITUDE, -83.745057);
        list.add(cv);

        cv = new ContentValues();
        cv.put(PlacesContract.PlacesEntry.COLUMN_PLACE_NAME, "El Morro");
        cv.put(PlacesContract.PlacesEntry.COLUMN_PLACE_DESCRIPTION, "Monumental 16th-century Spanish " +
                "fortification atop cliffside promontory, cannons pointing seaward.");
        cv.put(PlacesContract.PlacesEntry.COLUMN_ADDRESS_TITLE, "Castillo San Felipe del Morro");
        cv.put(PlacesContract.PlacesEntry.COLUMN_ADDRESS_STREET, "501 Calle Norzagaray, San Juan, " +
                "00901, Puerto Rico\n");
        cv.put(PlacesContract.PlacesEntry.COLUMN_PLACE_CATEGORY, "Historical Landmark");
        cv.put(PlacesContract.PlacesEntry.COLUMN_PLACE_ELEVATION, 300);
        cv.put(PlacesContract.PlacesEntry.COLUMN_PLACE_LATITUDE, 18.470926);
        cv.put(PlacesContract.PlacesEntry.COLUMN_PLACE_LONGITUDE, -66.124157);
        list.add(cv);

        cv = new ContentValues();
        cv.put(PlacesContract.PlacesEntry.COLUMN_PLACE_NAME, "Fieldwork");
        cv.put(PlacesContract.PlacesEntry.COLUMN_PLACE_DESCRIPTION, "Brewpub with a variety of seasonal" +
                " house beer blends, plus savory pies, in an industrial setting.");
        cv.put(PlacesContract.PlacesEntry.COLUMN_ADDRESS_TITLE, "Fieldwork Brewing Company");
        cv.put(PlacesContract.PlacesEntry.COLUMN_ADDRESS_STREET, "1160 6th St, Berkeley, CA 94710");
        cv.put(PlacesContract.PlacesEntry.COLUMN_PLACE_CATEGORY, "Brewery");
        cv.put(PlacesContract.PlacesEntry.COLUMN_PLACE_ELEVATION, 500);
        cv.put(PlacesContract.PlacesEntry.COLUMN_PLACE_LATITUDE, 37.881393);
        cv.put(PlacesContract.PlacesEntry.COLUMN_PLACE_LONGITUDE, -122.302368);
        list.add(cv);

        //insert all places in one transaction
        try
        {
            db.beginTransaction();
            //clear the table first
            db.delete (PlacesContract.PlacesEntry.TABLE_NAME,null,null);
            //go through the list and add one by one
            for(ContentValues c:list){
                db.insert(PlacesContract.PlacesEntry.TABLE_NAME, null, c);
            }
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

}
