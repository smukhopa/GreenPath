package com.example.smukhopadhyay.greenpathv4;

import android.location.Location;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by smukhopadhyay on 4/22/16.
 */
public class Utilities {

    public static SmallBoxStart smallBoxStart;

    public static void smallBoxStart() {

        LatLng latLng = MapsActivity.getStartingPoint();
        double boxSize = 0.0015;

        LatLng highest = new LatLng(latLng.latitude + boxSize, latLng.longitude + boxSize);
        //MapsActivity.mMap.addMarker(new MarkerOptions().position(highest).title("Highest Lat and Long"));
        LatLng lowest = new LatLng(latLng.latitude - boxSize, latLng.longitude - boxSize);
        //MapsActivity.mMap.addMarker(new MarkerOptions().position(lowest).title("Lowest Lat and Long"));

        smallBoxStart = new SmallBoxStart();
        smallBoxStart.execute();

    }

    public static SmallBoxFinish smallBoxFinish;

    public static void smallBoxFinish() {

        LatLng latLng = MapsActivity.getFinishingPoint();

        double boxSize = 0.0015;
        LatLng highest = new LatLng(latLng.latitude + boxSize, latLng.longitude + boxSize);
        //MapsActivity.mMap.addMarker(new MarkerOptions().position(highest).title("Highest Lat and Long"));
        LatLng lowest = new LatLng(latLng.latitude - boxSize, latLng.longitude - boxSize);
        //MapsActivity.mMap.addMarker(new MarkerOptions().position(lowest).title("Lowest Lat and Long"));

        smallBoxFinish = new SmallBoxFinish();
        smallBoxFinish.execute();

    }

    public static DirectionsAPICall directionsAPICall;

    public static void DirectionsAPICall() {
        directionsAPICall = new DirectionsAPICall();
        directionsAPICall.execute();
    }

    public static void bigBox() {
        BigBox bigBox = new BigBox();
        bigBox.execute();
    }

    public static LatLng findClosest(String json, LatLng latLng, String startFinish) {

        ArrayList<ArrayList<Double>> arrayLists = new ArrayList<ArrayList<Double>>();

        try {
            JSONObject object = new JSONObject(json);

            JSONObject osmObject = object.getJSONObject("osm");

            JSONArray wayArray = osmObject.getJSONArray("way");

            for (int i = 0; i < wayArray.length(); i++) {

                JSONObject wayObject = wayArray.getJSONObject(i);

                JSONArray ndArray = wayObject.getJSONArray("nd");

                for (int j = 0; j < ndArray.length(); j++) {

                    ArrayList<Double> temp = new ArrayList<Double>();

                    JSONObject ndObject = ndArray.getJSONObject(j);

                    Double ref = Double.parseDouble(ndObject.optString("ref"));

                    ArrayList<Double> LatLong = findLatLong(json, ref);

                    temp.add(Double.parseDouble(ndObject.optString("ref")));
                    temp.add(LatLong.get(0));
                    temp.add(LatLong.get(1));

                    arrayLists.add(temp);
                }
            }

            Location location = new Location("Middle Point");
            location.setLatitude(latLng.latitude);
            location.setLongitude(latLng.longitude);

            float smallest = Integer.MAX_VALUE;
            Double closestRef = 0.0;

            for (int i = 0; i < arrayLists.size(); i++) {

                Location temp = new Location("Temp");
                temp.setLatitude(arrayLists.get(i).get(1));
                temp.setLongitude(arrayLists.get(i).get(2));

                float distance = location.distanceTo(temp);

                if (distance < smallest) {
                    smallest = distance;
                    closestRef = arrayLists.get(i).get(0);
                }

            }

            ArrayList<Double> closestPoint = new ArrayList<Double>();
            closestPoint = findLatLong(json, closestRef);

            LatLng closestLatlng = new LatLng(closestPoint.get(0), closestPoint.get(1));

            // Set the value of the closest ref
            if (startFinish.equals("start")) {
                MapsActivity.setNodeIDStartingClosest(closestRef);
            }
            else if (startFinish.equals("finish")) {
                MapsActivity.setNodeIDFinishingClosest(closestRef);
            }

            return closestLatlng;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    /*
    public static ArrayList<Double> findLatLongOfBigBox(Double nodeID) {
        ArrayList<Double> result = new ArrayList<Double>();

        ArrayList<ArrayList<Double>> allNodes = MapsActivity.getAllNodes();
        for (int i = 0; i < allNodes.size(); i++) {
            if (allNodes.get(i).get(0) == nodeID) {
                // This is the Lat
                result.add(allNodes.get(i).get(1));
                // This is the Long
                result.add(allNodes.get(i).get(2));
            }
        }

        return result;
    }
    */

    public static ArrayList<Double> findLatLong(String json, Double ref) {
        ArrayList<Double> result = new ArrayList<Double>();

        result.add(0.0);
        result.add(0.0);

        try {
            JSONObject object = new JSONObject(json);

            JSONObject osmObject = object.getJSONObject("osm");

            JSONArray nodeArray = osmObject.getJSONArray("node");

            for (int i = 0; i < nodeArray.length(); i++) {

                JSONObject wayObject = nodeArray.getJSONObject(i);

                if (Double.parseDouble(wayObject.optString("id")) == ref) {
                    result.set(0, Double.parseDouble(wayObject.optString("lat")));
                    result.set(1, Double.parseDouble(wayObject.optString("lon")));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static ArrayList<LatLng> parseDirectionsAPIResult(String json) {
        //ArrayList<String> res = new ArrayList<String>();
        ArrayList<LatLng> extremePoints = new ArrayList<LatLng>();

        try {
            JSONObject object = new JSONObject(json);

            JSONArray routesArray = object.optJSONArray("routes");

            JSONObject jsonObject = routesArray.getJSONObject(0);

            JSONArray legsArray = jsonObject.optJSONArray("legs");

            JSONObject jsonObject1 = legsArray.getJSONObject(0);

            JSONArray stepsArray = jsonObject1.optJSONArray("steps");

            int size = stepsArray.length();

            JSONObject start = stepsArray.getJSONObject(0);

            JSONObject startLocation = start.getJSONObject("start_location");

            double maxLat = Double.parseDouble(startLocation.optString("lat").toString());
            double minLat = Double.parseDouble(startLocation.optString("lat").toString());
            double maxLng = Double.parseDouble(startLocation.optString("lng").toString());
            double minLng = Double.parseDouble(startLocation.optString("lng").toString());

            for (int i = 0; i < size; i++) {
                JSONObject object1 = stepsArray.getJSONObject(i);
                JSONObject endLocation = object1.getJSONObject("end_location");

                double lat = Double.parseDouble(endLocation.optString("lat").toString());
                double lng = Double.parseDouble(endLocation.optString("lng").toString());

                if (lat > maxLat) maxLat = lat;
                if (lat < minLat) minLat = lat;
                if (lng > maxLng) maxLng = lng;
                if (lng < minLng) minLng = lng;

            }

            extremePoints.add(new LatLng(maxLat, maxLng));
            extremePoints.add(new LatLng(minLat, minLng));
            return extremePoints;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static float parseDistanceAPIresult(String json) {
        float result = 0;

        try {
            JSONObject object = new JSONObject(json);

            JSONArray rowsArray = object.getJSONArray("rows");

            for (int i = 0; i < rowsArray.length(); i++) {
                JSONObject rowsObject = rowsArray.getJSONObject(i);

                JSONArray elementsArray = rowsObject.getJSONArray("elements");

                for (int j = 0; j < elementsArray.length(); j++) {
                    JSONObject elementsObject = elementsArray.getJSONObject(j);

                    JSONObject durationObject = elementsObject.getJSONObject("duration");

                    // duration looks like this : 4 min. We must get rid of the min
                    String duration = durationObject.optString("text");

                    String[] durationSplit = duration.split(" ");

                    result = Float.parseFloat(durationSplit[0]);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }


    public static void createBigArray(String json) {
        CreateBigArray createBigArray = new CreateBigArray();
        createBigArray.execute(json);

    }

    public static boolean determineIfIntersection(String nameOfWay) {
        // Write OSM query which sends the name of the way and the name of all the ways that are present in the json
        boolean result = false;

        ArrayList<String> namesOfAllThePaths = MapsActivity.getNamesOfAllThePaths();

        for (int i = 0; i < namesOfAllThePaths.size(); i++) {
            String tempName = namesOfAllThePaths.get(i);

            if (!nameOfWay.equals(tempName)) {
                // Pardis original
                //http://overpass-api.de/api/interpreter?data=[bbox:40.741591417809666,-73.99362802505493,40.744330773141044,-73.99005532264708];way[%22highway%22][%22name%22=%226th%20Avenue%22];node(w)-%3E.n1;way[%22highway%22][%22name%22=%22West%2023rd%20Street%22];node(w)-%3E.n2;node.n1.n2;out%20meta;

                // Modified
                //http://overpass-api.de/api/interpreter?data=[bbox:40.741591417809666,-73.99362802505493,40.744330773141044,-73.99005532264708];way[%22highway%22][%22name%22=%226th Avenue%22];node(w)-%3E.n1;way[%22highway%22][%22name%22=%22West 23rd Street%22];node(w)-%3E.n2;node.n1.n2;out%20meta;

                // Construct the URL for checking
                String overpassURL = "http://overpass-api.de/api/interpreter?data=[bbox:";

                // Min lat
                overpassURL += Double.toString(MapsActivity.getMinLATminLNG().latitude) + ",";

                // Min long
                overpassURL += Double.toString(MapsActivity.getMinLATminLNG().longitude) + ",";

                // Max Lat
                overpassURL += Double.toString(MapsActivity.getMaxLATmaxLNG().latitude) + ",";

                // Max Long
                overpassURL += Double.toString(MapsActivity.getMaxLATmaxLNG().longitude);
                overpassURL += "];way[%22highway%22][%22name%22=%22";

                // Name of current path
                overpassURL += nameOfWay;

                overpassURL += "%22];node(w)-%3E.n1;way[%22highway%22][%22name%22=%22";

                // Name of temp path
                overpassURL += tempName;

                overpassURL += "%22];node(w)-%3E.n2;node.n1.n2;out%20meta;";

                Log.i("STREET INTERSECTION URL", overpassURL);

                HttpURLConnection connection = null;

                BufferedReader reader = null;

                try {
                    URL url = new URL(overpassURL);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();

                    InputStream stream = connection.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer();

                    String line = "";

                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }

                    // Start parsing the string which has been returned

                    // convert string to json object
                    JSONObject object = XML.toJSONObject(buffer.toString());

                    JSONObject osmObject = object.getJSONObject("osm");

                    JSONObject nodeObject = osmObject.getJSONObject("node");

                    // if a node object exists, then simply return true
                    if (nodeObject != null) {
                        return true;
                    }
                    // else return false
                    else return false;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }

        return result;
    }

    private static ArrayList<Integer> resultOfDatabaseQuery;

    public static ArrayList<Integer> returnDatabaseValues(Double nodeID) {
        ArrayList<DatabaseNode> listDatabaseNode = MapsActivity.getListDatabaseNode();
        for (int i = 0; i < listDatabaseNode.size(); i++) {
            DatabaseNode databaseNode = listDatabaseNode.get(i);
            if (databaseNode.getNodeID() == nodeID) {
                ArrayList<Integer> returnList = new ArrayList<Integer>();

                returnList.add(databaseNode.getOffset());
                returnList.add(databaseNode.getGreenLightDuration());
                returnList.add(databaseNode.getRedLightDuration());
                return returnList;
            }
        }
        return null;
    }

    public static ArrayList<Integer> getSingleTrafficLightData(Double nodeID) {
        ArrayList<Integer> singleTrafficLightData = new ArrayList<Integer>();

        // Set the offset of the traffic light
        singleTrafficLightData.add(resultOfDatabaseQuery.get(1));

        // Set the duration of the green light
        singleTrafficLightData.add(resultOfDatabaseQuery.get(2));

        // Set the duration of the red ligth
        singleTrafficLightData.add(resultOfDatabaseQuery.get(3));

        return singleTrafficLightData;
    }

    public static void saveAllPathName(String json) {
        ArrayList<String> namesOfAllThePaths = new ArrayList<String>();


        try {
            // convert that string to a json object
            JSONObject object = new JSONObject(json);

            // the first thing inside the json objest is the osm object
            JSONObject osmObject = object.getJSONObject("osm");

            // inside the osm object, there's the way array
            // the way array represents all the ways that are present inside the big box
            JSONArray wayArray = osmObject.getJSONArray("way");

            // iterate over all the ways present in the way array
            for(int i = 0; i < wayArray.length(); i++) {
                // the way array is made up of way objects
                // create a way object
                JSONObject wayObject = wayArray.getJSONObject(i);

                // save the ID of the way object
                double ID = Double.parseDouble(wayObject.optString("id"));

                String name = null;

                //TODO
                // Ask Joe to parse this and find the name
                // Code for obtaining the name of road.
                // Important note: not all roads have names.
                // Discard all the roads that not have the name tag, because they represnt roads which
                // maybe too small for cars to go on
                JSONArray tagArray = wayObject.getJSONArray("tag");

                for (int k = 0; k < tagArray.length(); k++) {

                    JSONObject tagObject = tagArray.getJSONObject(k);
                    if (tagObject.has("name")) {
                        name = tagObject.optString("name");
                    }
                }
                namesOfAllThePaths.add(name);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MapsActivity.setNamesOfAllThePaths(namesOfAllThePaths);

    }

    public static void saveAllNodes(String json) {

        ArrayList<NodeMap> nodeMaps = new ArrayList<NodeMap>();

        try {
            // convert that string to a json object
            JSONObject object = new JSONObject(json);

            // the first thing inside the json objest is the osm object
            JSONObject osmObject = object.getJSONObject("osm");

            // Find the node array inside the osm object
            JSONArray nodeArray = osmObject.getJSONArray("node");

            // parse through the node array
            int i;
            for (i = 0; i < nodeArray.length(); i++) {

                JSONObject nodeObject = nodeArray.getJSONObject(i);

                // The mapped value
                // The reason we add 2 is because we want to start from 2. 0 and 1 are reserved for
                //  the starting and the ending node respectively
                int mappedValue = i + 2;

                // save the node ID
                double nodeID = Double.parseDouble(nodeObject.optString("id"));

                // save the node Lat
                double nodeLat = Double.parseDouble(nodeObject.optString("lat"));

                // save the node Long
                double nodeLong = Double.parseDouble(nodeObject.optString("lon"));

                // Create a single instance of a node map
                NodeMap nodeMap = new NodeMap(mappedValue, nodeID, nodeLat, nodeLong);

                // Add the nodeMap to the arraylist
                nodeMaps.add(nodeMap);
            }
            MapsActivity.setNodeMaps(nodeMaps);
            MapsActivity.setTextView5("Number of nodes : " + Integer.toString(i));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void joeFunction(Double nodeID, Double wayID) {

        ArrayList<ArrayList<Double>> joeTable = MapsActivity.getJoeTable();

        int index = 0;
        boolean flag = false;

        for (int i = 0; i < joeTable.size(); i++) {
            if (nodeID == joeTable.get(i).get(0)) {
                index = i;
                flag = true;
                break;
            }
        }

        if (flag) {
            joeTable.get(index).add(wayID);
        } else {
            ArrayList<Double> row = new ArrayList<Double>();
            row.add(nodeID);
            row.add(wayID);
            joeTable.add(row);
        }

    }


    public static DatabaseCall databaseCall = null;
    public static void saveListDatabaseNode() {
        databaseCall = new DatabaseCall();
        databaseCall.execute();
    }


    public static void drawPolyline(ArrayList<FinalRoute> finalRoutes) {
        for (int i = 0; i < (finalRoutes.size() - 1); i++) {
            FinalRoute start = finalRoutes.get(i);
            FinalRoute finish = finalRoutes.get(i + 1);

            PolylineOptions polylineOptions = new PolylineOptions()
                    .add(start.getLatLng())
                    .add(finish.getLatLng());
            Polyline polyline = MapsActivity.mMap.addPolyline(polylineOptions);
        }
    }

    public static NodeMap searchInNodeMap(Double nodeID) {
        Log.i("NODE MAP", "enterring node map with nodeID " + Double.toString(nodeID));

        ArrayList<NodeMap> nodeMaps = MapsActivity.getNodeMaps();
        for (int i = 0; i < nodeMaps.size(); i++) {
            double tempID = nodeMaps.get(i).getNodeID();
            if (nodeID.compareTo(tempID) == 0) {
                return nodeMaps.get(i);
            }
        }
        return null;
    }

    public static NodeMap searchInNodeMapUsingMappedValue(int mappedValue) {
        ArrayList<NodeMap> nodeMaps = MapsActivity.getNodeMaps();
        for(int i = 0; i < nodeMaps.size(); i++) {
            int tempMappedValue = nodeMaps.get(i).getMappedValue();
            if (mappedValue == tempMappedValue) {
                return nodeMaps.get(i);
            }
        }
        return null;
    }
}