package edu.asu.bsse.jctorre8.placesser423.data;

import android.provider.BaseColumns;

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
public class PlacesContract {

    public static final class PlacesEntry implements BaseColumns {
        public static final String TABLE_NAME = "places";
        public static final String COLUMN_PLACE_NAME = "name";
        public static final String COLUMN_PLACE_DESCRIPTION = "description";
        public static final String COLUMN_PLACE_CATEGORY = "category";
        public static final String COLUMN_ADDRESS_TITLE = "addressTitle";
        public static final String COLUMN_ADDRESS_STREET = "addressStreet";
        public static final String COLUMN_PLACE_ELEVATION = "elevation";
        public static final String COLUMN_PLACE_LATITUDE = "latitude";
        public static final String COLUMN_PLACE_LONGITUDE = "longitude";
    }

}
