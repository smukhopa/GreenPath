package com.example.smukhopadhyay.greenpathv4;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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
public class DirectionsAPICall extends AsyncTask {
    @Override
    protected Object doInBackground(Object[] params) {
        LatLng start = MapsActivity.getStartingPoint();
        LatLng finish = MapsActivity.getFinishingPoint();

        String buildURL = "https://maps.googleapis.com/maps/api/directions/json?origin=";
        buildURL += Double.toString(start.latitude);
        buildURL += ",";
        buildURL += Double.toString(start.longitude);
        buildURL += "&destination=";
        buildURL += Double.toString(finish.latitude);
        buildURL += ",";
        buildURL += Double.toString(finish.longitude);
        buildURL += "&key=";
        buildURL += "AIzaSyAVX5IflSRZH7r2D5_wW63ojuIJJr-q98s";

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {

            URL url = new URL(buildURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();

            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            ArrayList<LatLng> extremePoints = Utilities.parseDirectionsAPIResult(buffer.toString());
            MapsActivity.setMaxLATmaxLNG(extremePoints.get(0));
            MapsActivity.setMinLATminLNG(extremePoints.get(1));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        MapsActivity.setTextView3("Google Directions API : max : " + MapsActivity.getMaxLATmaxLNG() + "  min : " + MapsActivity.getMinLATminLNG());
        //MapsActivity.mMap.addMarker(new MarkerOptions().position(MapsActivity.getMaxLATmaxLNG()).title("Max Lat and Max Lng from Directions API"));
        //MapsActivity.mMap.addMarker(new MarkerOptions().position(MapsActivity.getMinLATminLNG()).title("Min Lat and Min Lng from Directions API"));
    }
}
