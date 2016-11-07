package com.example.smukhopadhyay.greenpathv4;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

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
public class SmallBoxFinish extends AsyncTask {
    @Override
    protected Object doInBackground(Object[] params) {

        LatLng latLng = MapsActivity.getFinishingPoint();

        double boxSize = 0.0015;

        String highestLat = Double.toString(latLng.latitude + boxSize);
        String highestLng = Double.toString(latLng.longitude + boxSize);
        String smallestLat = Double.toString(latLng.latitude - boxSize);
        String smallestLng = Double.toString(latLng.longitude - boxSize);

        String OverpassURL = "http://overpass-api.de/api/interpreter?data=(node(";
        OverpassURL += smallestLat + "," + smallestLng + "," + highestLat + "," + highestLng;
        OverpassURL += ");way(bn);<;);out;";

        Log.i("OVERPASS URL FINISH", OverpassURL);

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

            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            JSONObject OSMobject = XML.toJSONObject(buffer.toString());

            LatLng closestPoint = Utilities.findClosest(OSMobject.toString(), MapsActivity.getFinishingPoint(), "finish");

            MapsActivity.setClosestPointToEnd(closestPoint);

            return closestPoint.toString();

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
        MapsActivity.setTextView2("Finishing point : " + o.toString());
        //MapsActivity.mMap.addMarker(new MarkerOptions().position(MapsActivity.getClosestPointToEnd()).title("Closest Point to Finish"));
        PolylineOptions polylineOptions = new PolylineOptions()
                .add(MapsActivity.getFinishingPoint())
                .add(MapsActivity.getClosestPointToEnd());
        Polyline polyline = MapsActivity.mMap.addPolyline(polylineOptions);
    }
}
