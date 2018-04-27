package edu.asu.bsse.jctorre8.placesser423;

import java.util.Vector;
import java.util.List;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONString;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.FileInputStream;
import java.util.Vector;
import java.util.Enumeration;
import java.io.Serializable;
import java.io.PrintWriter;

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

public class PlacesLibrary  extends Object implements JSONString, Serializable {
    private Vector<Place> library;

    /**
    * No parameter constructor. Just creates an empty Vector.
    */
    public PlacesLibrary(){
        this.library = new Vector<Place>();
    }

    /**
    * Places Library constructor that takes a list as an argument.
    *
    * @param oldLibrary
    */
    public PlacesLibrary(List<Place> oldLibrary){
        this.library = new Vector<Place>(oldLibrary);
    }

    /**
    * Places Library constructor that gets populated by a json file.
    * 
    * @param jsonFileName
    * TODO Implement create from JSONFile
    public PlacesLibrary(String jsonFileName){
        try{
            FileInputStream in = new FileInputStream(jsonFileName);
            JSONObject obj = new JSONObject(new JSONTokener(in));
            String [] names = JSONObject.getNames(obj);
            System.out.print("Places are: ");
            for(int j=0; j< names.length; j++){
                System.out.print(names[j]+", ");
            }
            System.out.println("");
            this.library = new Vector<Place>();
            for (int i=0; i< names.length; i++){
                Place aPlace = new Place((JSONObject)obj.getJSONObject(names[i]));
                this.library.add(aPlace);
            }
            in.close();
        }catch (Exception ex) {
            System.out.println("Exception importing from json: "+ex.getMessage());
        }
    }
    */

    /**
    * Outputs the content of the library into a string representation of json.
    * 
    * @return The string json content of the library.
    */
    public String toJSONString(){
        String ret;
        JSONObject obj = new JSONObject();
        for (Enumeration<Place> e = this.library.elements(); e.hasMoreElements();){
            Place place = (Place)e.nextElement();
            try {
                obj.put(place.getName(), place.toJSONObject());
            } catch (Exception exc){
                // Do Nothing :((
            }
        }
        ret = obj.toString();
        return ret;
    }

    /**
    * Adds a Place to the library.
    * 
    * @param aPlace
    * @return True if successful and false if don't.
    */
    public boolean add(Place aPlace){
        if(library.contains(aPlace)){
                return false;
        }
        return library.add(aPlace);
    }

    /**
     * Adds a new Place to the library.
     *
     * @param lat
     * @param lon
     * @param ele
     * @param name
     * @param description
     * @param categoty
     * @param addressTitle
     * @param addressStreet
     * @return
     */
    public boolean addNew( String name, String description, String addressTitle,String addressStreet,
                           String categoty, String lat, String lon, String ele){
        double latitude = Double.parseDouble(lat);
        double longitude = Double.parseDouble(lon);
        double elevation = Double.parseDouble(ele);
        Place temp = new Place(name, description, addressTitle, addressStreet, categoty, latitude, longitude, elevation);
        
        return library.add(temp);
    }

    /**
     * Removes the Place with the matching name.
     * @param name
     * @return
     */
    public boolean remove(String name){
        for(int i = 0; i < library.size(); i++){
            Place temp = library.get(i);
            if(name.equals(temp.getName())){
                temp = library.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Get the place that matches the given name.
     * @param name
     * @return
     */
    public Place get(String name){
        Place toReturn =  null;
        for(int i = 0; i < library.size(); i++){
            Place temp = library.get(i);
            if(name.equals(temp.getName())){
                toReturn = temp;
                return toReturn;
            }
        }
        return toReturn;
    }

    /**
     * Imports the places from JSON file.
     * @return
     * TODO Implement restore from JSONFile
    public boolean restoreFromFile(){
        try{
            FileInputStream in = new FileInputStream("places.json");
            JSONObject obj = new JSONObject(new JSONTokener(in));
            JSONArray names = JSONObject.getNames(obj);
            for (int i=0; i< names.length; i++){
                Place aPlace = new Place((JSONObject)obj.getJSONObject(names[i]));
                System.out.println(aPlace);
                this.library.add(aPlace);
            }
            in.close();
            System.out.println("Done importing Places in from places.json");
            return true;
        }catch (Exception ex) {
            System.out.println("Exception importing from json: "+ex.getMessage());
            return false;
        }
    }
    */

    /**
     * Export the current places to a JSON file.
     * @return
     */
    public boolean saveToFile(){
        try{
            PrintWriter out = new PrintWriter("places.json");
            out.println(this.toJSONString());
            out.close();
            System.out.println("Done exporting library to places.json");
            return true;
        }catch (Exception e){
            System.out.println("Exception exporting from json: "+e.getMessage());
            return false;
        }
    }

    /**
     * This method collects all the the place names in the library and returns them.
     * @return
     */
    public String[] getNames(){
        String[] toReturn = new String[library.size()];
        for(int i = 0; i < library.size(); i++){
            Place temp = library.get(i);
            toReturn[i] = temp.getName();
        }
        return toReturn;
    }

}
