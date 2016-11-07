package com.example.smukhopadhyay.greenpathv4;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by smukhopadhyay on 5/3/16.
 */
public class DatabaseCall extends AsyncTask {
    @Override
    protected Object doInBackground(Object[] params) {

        String response = null;

        String buildURL = "http://greenpath.x10host.com/pullfriends.php";

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

            response = buffer.toString();

            // Parse the data which comes in from the database
            JSONObject object = new JSONObject(response);

            // Obtain the JSON object
            //JSONObject JSON = object.getJSONObject("JSON");

            // Here's the response array
            JSONArray responseArray = object.getJSONArray("response");

            // ArrayList to hold the database nodes
            ArrayList<DatabaseNode> databaseNodes = new ArrayList<DatabaseNode>();


            // parse through the entire response array
            int i = 0;
            for (i = 0; i < responseArray.length(); i++) {

                Log.i("DATABASE", "===============================================================");
                Log.i("DATABASE", Integer.toString(i));

                // here are the response objects
                JSONObject responseObject = responseArray.getJSONObject(i);

                Double nodeID = Double.parseDouble(responseObject.optString("NodeID"));
                Log.i("DATABASE", Double.toString(nodeID));

                int hasLight = Integer.parseInt(responseObject.optString("HasTrafficLight"));

                boolean hasTrafficLight;
                if (hasLight == 1) {
                    hasTrafficLight = true;
                } else {
                    hasTrafficLight = false;
                }
                Log.i("DATABASE", Boolean.toString(hasTrafficLight));

                int offset = Integer.parseInt(responseObject.optString("Offset"));
                Log.i("DATABASE", Integer.toString(offset));

                int greenLightDuration = Integer.parseInt(responseObject.optString("GreenLightDuration"));
                Log.i("DATABASE", Integer.toString(greenLightDuration));

                int redLightDuration = Integer.parseInt(responseObject.optString("RedLightDuration"));
                Log.i("DATABASE", Integer.toString(redLightDuration));

                DatabaseNode databaseNode = new DatabaseNode(nodeID, hasTrafficLight, offset, greenLightDuration, redLightDuration);

                databaseNodes.add(databaseNode);
                Log.i("DATABASE", "===============================================================");

            }

            MapsActivity.setListDatabaseNode(databaseNodes);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        int sizeOfDataBase = MapsActivity.getListDatabaseNode().size();

        MapsActivity.setTextView6("Size of database" + Integer.toString(sizeOfDataBase));

        // Function call to create the bigArray. Send the result to the big array
        String bigBoxJson = MapsActivity.getBigBoxJson();
        Utilities.createBigArray(bigBoxJson);

    }
}
