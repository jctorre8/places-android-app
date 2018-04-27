package edu.asu.bsse.jctorre8.placesser423;

import java.lang.Math;
import java.io.*;
import java.util.*;
import java.text.NumberFormat;
import org.json.JSONString;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONArray;

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

public class Place extends Object {

   public final static int STATUTE = 0;
   public final static int NAUTICAL = 1;
   public final static int KMETER = 2;
   public final static double radiusE = 6371;

   private double lat; // degrees lat in DD.D format (+ north, - south)
   private double lon; // degrees lon in DD.D format (+ east, - west)
   private double ele; // elevation in feet MSL
   private String name; // a name for the Place
    private String description;
    private String addressTitle;
    private String addressStreet;
    private String category;

   public Place(String name, String description, String addressTitle, String addressStreet,
                String category, double lat, double lon, double ele ){
      this.lat = lat;
      this.lon = lon;
      this.ele = ele;
      this.name = name;
       this.addressTitle = addressTitle;
       this.addressStreet = addressStreet;
       this.description = description;
       this.category = category;
   }

   public Place(Place copy){
      this.lat = copy.getLatitude();
      this.lon = copy.getLongitude();
      this.ele = copy.getElevation();
      this.name = copy.getName();
       this.addressTitle = copy.getAddressTitle();
       this.addressStreet = copy.getAddressStreet();
       this.description = copy.getDescription();
       this.category = copy.getCategory();
   }

   public Place(JSONObject obj){
       try {
           this.lat = obj.getDouble("lat");
           this.lon = obj.getDouble("lon");
           this.ele = obj.getDouble("ele");
           this.name = obj.getString("name");
           this.addressTitle = obj.getString("addressTitle");
           this.addressStreet = obj.getString("addressStreet");
           this.category = obj.getString("category");
           this.description = obj.getString("description");
       }catch(Exception e){
           //Do nothing :(
       }
   }
   
   /**
    * calculate the distance great circle route between this and the
    * argument Places. Return the result in the requested scale
    */
   public double distanceGCTo(Place wp, int scale){
      double ret = 0.0;
      // ret is in kilometers if you use the availble algorithms. S
      double lat1 = Math.toRadians(this.getLatitude());
      double lat2 = Math.toRadians(wp.getLatitude());
      double deltaLat = Math.toRadians(wp.getLatitude()-this.getLatitude());
      double deltaLon = Math.toRadians(wp.getLongitude()-this.getLongitude());
      double a = Math.sin(deltaLat/2)*Math.sin(deltaLat/2) +
                 Math.cos(lat1)*Math.cos(lat2)*
                 Math.sin(deltaLon/2)*Math.sin(deltaLon/2);
      double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
      ret = radiusE * c;
      // Switch to either Statute or Nautical if necessary
      switch(scale) {
      case STATUTE:
         ret = ret * 0.62137119;
         break;
      case NAUTICAL:
         ret = ret * 0.5399568;
         break;
      }
      return ret;
   }

   /**
    * calculate the initial heading on the circle route between this and the
    * argument Places. Return the result.
    */
   public double bearingGCInitTo(Place wp, int scale){
      double ret = 0.0;
      double lat1 = Math.toRadians(this.getLatitude());
      double lat2 = Math.toRadians(wp.getLatitude());
      double deltaLat = Math.toRadians(wp.getLatitude()-this.getLatitude());
      double deltaLon = Math.toRadians(wp.getLongitude()-this.getLongitude());
      double y = Math.sin(deltaLon) * Math.cos(lat2);
      double x = Math.cos(lat1) * Math.sin(lat2) - 
                 Math.sin(lat1) * Math.cos(lat2) * Math.cos(deltaLat);
      double brng = Math.atan2(y,x);
      ret = Math.toDegrees(brng);
      return ret;
   }

   /**
    * Latitude getter. 
    * @return The Latitude of the Place.
    */
    public double getLatitude(){
        return this.lat;
    }

    /**
    * Latitude setter. 
    * @param  lat
    */
    public void setLatitude(double lat){
        this.lat = lat;
    }

    /**
    * Longitude getter. 
    * @return The Longitude of the Place.
    */
    public double getLongitude(){
        return this.lon;
    }

    /**
    * Longitude setter. 
    * @param lon
    */
    public void setLongitude(double lon){
        this.lon = lon;
    }

    /**
    * Elevation getter. 
    * @return The release date of the Place.
    */
    public double getElevation(){
        return this.ele;
    }

    /**
    * Elevation setter. 
    * @param ele
    */
    public void setElevation(double ele){
        this.ele = ele;
    }

    /**
    * Name getter. 
    * @return The Name of the Place.
    */
    public String getName(){
        return this.name;
    }

    /**
    * Name setter. 
    * @param name name of the Place.
    */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Address Title getter.
     * @return The Title ofAddress of the Place.
     */
    public String getAddressTitle(){
        return this.addressTitle;
    }

    /**
     * Address Title setter.
     * @param addressTitle
     */
    public void setAddressTitle(String addressTitle){
        this.addressTitle = addressTitle;
    }

    /**
     * Address Street getter.
     * @return The Street of the Address of the Place.
     */
    public String getAddressStreet(){
        return this.addressStreet;
    }

    /**
     * Address Streetsetter.
     * @param addressStreet
     */
    public void setAddressStreet(String addressStreet){
        this.addressStreet = addressStreet;
    }

    /**
     * description getter.
     * @return The description of the Place.
     */
    public String getDescription(){
        return this.description;
    }

    /**
     * Description setter.
     * @param description
     */
    public void setDescription(String description){
        this.description = description;
    }

    /**
     * Category getter.
     * @return The Category of the Place.
     */
    public String getCategory(){
        return this.category;
    }

    /**
     * Category setter.
     * @param category
     */
    public void setCategory(String category){
        this.category = category;
    }

    /**
    * Export the Place information into a JSONObject.
    * 
    * @return A JSONObject with all the Place information.
    */
    public JSONObject toJSONObject(){

        JSONObject obj = new JSONObject();
        try {
            obj.put("lon", this.lon);
            obj.put("addressTitle", this.addressTitle);
            obj.put("addressStreet", this.addressStreet);
            obj.put("category", this.category);
            obj.put("description", this.description);
            obj.put("name", this.name);
            obj.put("lat", this.lat);
            obj.put("ele", this.ele);
        } catch(Exception e){
            // Do nothing :((
        }
        return obj;
    }

    /**
    * Returns a String representation of the Place.
    * 
    * @return A string with all the information.
    */
    public void print () {
      System.out.println("Place "+name+": lat "+lat+" lon "+lon+
                         " elevation "+ele);
   }

}
