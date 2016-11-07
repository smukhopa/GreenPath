package com.example.smukhopadhyay.greenpathv4;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by smukhopadhyay on 4/23/16.
 */
public class CreateBigArray extends AsyncTask {
    @Override
    protected Object doInBackground(Object[] params) {

        // Create the big array
        ArrayList<SingleWay> ways = new ArrayList<SingleWay>();

        ArrayList<ArrayList<Double>> joeTable = new ArrayList<ArrayList<Double>>();

        // Beggining of the try catch statement in which we parse the JSON of the big box and create
        // the big array called ways.
        try {

            // Create a string called json
            String json = params[0].toString();

            // convert that string to a json object
            JSONObject object = new JSONObject(json);

            // the first thing inside the json objest is the osm object
            JSONObject osmObject = object.getJSONObject("osm");

            // inside the osm object, there's the way array
            // the way array represents all the ways that are present inside the big box
            JSONArray wayArray = osmObject.getJSONArray("way");

            int counter = 0;

            // iterate over all the ways present in the way array
            for (int i = 0; i < wayArray.length(); i++) {

                // the way array is made up of way objects
                // create a way object
                JSONObject wayObject = wayArray.getJSONObject(i);

                // save the ID of the way object
                double wayID = Double.parseDouble(wayObject.optString("id"));

                //String name = null;
                /*
                // Code for obtaining the name of road.
                // Important note: not all roads have names.
                // Discard all the roads that not have the name tag, because they represnt roads which
                // maybe too small for cars to go on
                JSONArray tagArray = wayObject.getJSONArray("tag");

                for (int k = 0; k < tagArray.length(); k++) {

                    JSONObject tagObject = tagArray.getJSONObject(k);
                    if (tagObject.get("-k").equals("name")) {
                        name = tagObject.optString("-v");
                    }
                }
                */

                // If and only if the road has a name, go ahead with the if statement
                //if (name != null) {

                // Create an array list of all the nodes that are present in the way
                ArrayList<SingleNode> arrayOfNodes = new ArrayList<SingleNode>();

                // obtain the node array
                JSONArray ndArray = wayObject.getJSONArray("nd");

                // iterate over all the nodes in the node array
                for (int j = 0; j < ndArray.length(); j++) {

                    // create a node object
                    JSONObject ndObject = ndArray.getJSONObject(j);

                    // the only thing the node object contains is a reference number
                    // store this in a variable called ref of type double.
                    // we'll be using this to look up the lat and long of the node
                    Double nodeID = Double.parseDouble(ndObject.optString("ref"));

                    // Once we obtain the NodeID, we have to search for the node in the NodeMaps
                    NodeMap node = Utilities.searchInNodeMap(nodeID);

                    // To count the total number of nodes
                    counter++;

                    if (node != null) {
                        // To count the total number of nodes
                        //counter++;

                        // Obtain the mapped value
                        int mappedValue = node.getMappedValue();

                        // store the lat and long returned in a LatLng variable
                        LatLng latLng = new LatLng(node.getLat(), node.getLng());

                        // Check if the node is an intersection
                        //boolean isIntersection = Utilities.determineIfIntersection(name);

                        ArrayList<Integer> databaseValues = Utilities.returnDatabaseValues(nodeID);

                        int offset = 0;
                        int durationOfGreenLight = 0;
                        int durationOfRedLight = 0;
                        SingleNode singleNode = null;
                        if (databaseValues != null) {
                            offset = databaseValues.get(0);
                            durationOfGreenLight = databaseValues.get(1);
                            durationOfRedLight = databaseValues.get(2);
                            singleNode = new SingleNode(mappedValue, nodeID, latLng, true, offset, durationOfGreenLight, durationOfRedLight);
                        }
                        else {
                            singleNode = new SingleNode(mappedValue, nodeID, latLng, false, offset, durationOfGreenLight, durationOfRedLight);
                        }

                        // That node is now added to the arraylist of nodes
                        arrayOfNodes.add(singleNode);
                    }
                    /*
                    // Obtain the mapped value
                    int mappedValue = node.getMappedValue();

                    // store the lat and long returned in a LatLng variable
                    LatLng latLng = new LatLng(node.getLat(), node.getLng());

                    // Check if the node is an intersection
                    //boolean isIntersection = Utilities.determineIfIntersection(name);

                    ArrayList<Integer> databaseValues = Utilities.returnDatabaseValues(nodeID);

                    int offset = 0;
                    int durationOfGreenLight = 0;
                    int durationOfRedLight = 0;
                    SingleNode singleNode = null;
                    if (databaseValues != null) {
                        offset = databaseValues.get(0);
                        durationOfGreenLight = databaseValues.get(1);
                        durationOfRedLight = databaseValues.get(2);
                        singleNode = new SingleNode(mappedValue, nodeID, latLng, true, offset, durationOfGreenLight, durationOfRedLight);
                    }
                    else {
                        singleNode = new SingleNode(mappedValue, nodeID, latLng, false, offset, durationOfGreenLight, durationOfRedLight);
                    }

                    // That node is now added to the arraylist of nodes
                    arrayOfNodes.add(singleNode);
                    */
                }

                // A single way is now created.
                SingleWay singleWay = new SingleWay(wayID, arrayOfNodes);

                // That single way is now added to the way arraylist
                ways.add(singleWay);
                //}
            }

            Log.i("COUNTER : ", Integer.toString(counter));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        MapsActivity.setWays(ways);

        return "IT'S WORKING";
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        MapsActivity.setTextView4("FROM CREATE BIG ARRAY" + o.toString());

        int sizeOfWaysArray = MapsActivity.getWays().size();
        MapsActivity.setTextView4("Size of Ways Array " + Integer.toString(sizeOfWaysArray));

        CreateGraph createGraph = new CreateGraph();
        createGraph.execute();
    }
}
