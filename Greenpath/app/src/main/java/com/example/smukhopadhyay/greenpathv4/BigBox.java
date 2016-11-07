package com.example.smukhopadhyay.greenpathv4;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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

/**
 * Created by smukhopadhyay on 4/22/16.
 */
public class BigBox extends AsyncTask {
    @Override
    protected Object doInBackground(Object[] params) {

        while (Utilities.smallBoxStart.getStatus() == Status.RUNNING || Utilities.smallBoxStart.getStatus() == Status.PENDING
                || Utilities.smallBoxFinish.getStatus() == Status.RUNNING || Utilities.smallBoxFinish.getStatus() == Status.PENDING
                || Utilities.directionsAPICall.getStatus() == Status.RUNNING || Utilities.directionsAPICall.getStatus() == Status.PENDING)
        {

            // Wait here till the 3 other threads finish execution

        }



        // Code for getting the most extreme latitude and longitudes, which is then used to for sending
        // in the query to create the big box.
        double maxLat = MapsActivity.getMaxLATmaxLNG().latitude;
        double minLat = MapsActivity.getMinLATminLNG().latitude;
        double maxLng = MapsActivity.getMaxLATmaxLNG().longitude;
        double minLng = MapsActivity.getMinLATminLNG().longitude;

        double closestToStartingPointLat = MapsActivity.getClosesPointToStart().latitude;
        double closestToStartingPointLng = MapsActivity.getClosesPointToStart().longitude;

        if (closestToStartingPointLat > maxLat) {
            maxLat = closestToStartingPointLat;
        }

        if (closestToStartingPointLng > maxLng) {
            maxLng = closestToStartingPointLng;
        }

        if (closestToStartingPointLat < minLat) {
            minLat = closestToStartingPointLat;
        }

        if (closestToStartingPointLng < minLng) {
            minLng = closestToStartingPointLng;
        }

        double closestToFinishingPointLat = MapsActivity.getClosestPointToEnd().latitude;
        double closestToFinishingPointLng = MapsActivity.getClosestPointToEnd().longitude;

        if (closestToFinishingPointLat > maxLat) {
            maxLat = closestToFinishingPointLat;
        }

        if (closestToFinishingPointLng > maxLng) {
            maxLng = closestToFinishingPointLng;
        }

        if (closestToFinishingPointLat < minLat) {
            minLat = closestToFinishingPointLat;
        }

        if (closestToFinishingPointLng < minLng) {
            minLng = closestToFinishingPointLng;
        }

        MapsActivity.setMaxLATmaxLNG(new LatLng(maxLat, maxLng));
        MapsActivity.setMinLATminLNG(new LatLng(minLat, minLng));
        // Finished finiding the most latitude and longitude


        // Start constructing the URL for sending the query to cnstruct the big box
        String OverpassURL = "http://overpass-api.de/api/interpreter?data=(node(";
        OverpassURL += minLat + "," + minLng + "," + maxLat + "," + maxLng;
        OverpassURL += ");way(bn);<;);out;";

        // Log the whole url so that we can see it in the console
        Log.i("Overpass URL Big Box", OverpassURL);


        // Code snippet for actually making the http request
        HttpURLConnection connection = null;

        BufferedReader reader = null;

        try {
            URL url = new URL(OverpassURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();
            String line = "";

            // http request is returned and gets stored in the buffer. The return format is XML.
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            JSONObject OSMobject = null;

            // Convert the returned XML file to JSON
            OSMobject = XML.toJSONObject(buffer.toString());
            String result = OSMobject.toString(4);

            // Send the JSON file to the onPostExecute() method
            return result;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        // Save the o.toString() in a String
        MapsActivity.setBigBoxJson(o.toString());

        // Set the two blue markers on the most extreme latitude and longitude points
        //MapsActivity.mMap.addMarker(new MarkerOptions().position(MapsActivity.getMaxLATmaxLNG()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        //MapsActivity.mMap.addMarker(new MarkerOptions().position(MapsActivity.getMinLATminLNG()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        // Parse through the JSON and store all the nodes and their corresponding Lat and Lng
        Utilities.saveAllNodes(o.toString());

        Log.i("BIG BOX", "Total Number of Nodes in Big Box : " + MapsActivity.getNodeMaps().size());

        // Make an AsyncTask call to the Database and read in the entire database and save the
        // result in ArrayList<DatabaseNode> listDatabaseNode
        Utilities.saveListDatabaseNode();

        /*
        // Parse through JSON file and save the names of all the ways which exist
        Utilities.saveAllPathName(o.toString());

        // Function call to create the bigArray. Send the result to the big array
        Utilities.createBigArray(o.toString());
        */
    }
}
