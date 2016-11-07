package com.example.smukhopadhyay.greenpathv4;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import model.Edge;
import model.Graph;

/**
 * Created by smukhopadhyay on 4/23/16.
 */
public class CreateGraph extends AsyncTask {

    static ArrayList<Edge> edges;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        // Create an arraylist of edges which are going to be added to the graph
        edges = new ArrayList<Edge>();
    }

    @Override
    protected Object doInBackground(Object[] params) {

        // TODO
        // connect 0 to the closest point to the start and conenct 1 to the closest point to the finish
        // Find the nodeID of the closest point to the starting point in the mapped nodes
        /*
        Edge edge = new Edge(fromIndex, toIndex, traversalTime,
                initialDelayFROM, timeDurationOfGreenLightFROM, timeDurationOfRedLightFROM,
                initialDelayTO, timeDurationOfGreenLightTO, timeDurationOfRedLightTO);
        */

        double nodeIDStartingClosest = MapsActivity.getNodeIDStartingClosest();
        NodeMap closestToStarting = Utilities.searchInNodeMap(nodeIDStartingClosest);

        int toIndex = closestToStarting.getMappedValue();

        Edge edgeStart = new Edge(0, toIndex, 0.1f, 0, 4, 0, 0, 4, 0);

        Log.i("CREATE EDGE", "Connecting " + Integer.toString(0) + " & " + Integer.toString(toIndex));

        edges.add(edgeStart);

        double nodeIDFinishingClosest = MapsActivity.getNodeIDFinishingClosest();
        NodeMap closestToFinishing = Utilities.searchInNodeMap(nodeIDFinishingClosest);

        int fromIndex = closestToFinishing.getMappedValue();

        Edge edgeFinish = new Edge(fromIndex, 1, 0.1f, 0, 4, 0, 0, 4, 0);

        Log.i("CREATE EDGE", "Connecting " + Integer.toString(fromIndex) + " & " + Integer.toString(1));

        edges.add(edgeFinish);

        // Get the ways arraylist
        ArrayList<SingleWay> ways = MapsActivity.getWays();

        // parse through all the ways that are present in the arraylist
        for (int i = 0; i < ways.size(); i++) {
            // Store all the nodes of a single path in the nodes variable
            ArrayList<SingleNode> nodes = ways.get(i).getNodes();

            // parse through all the nodes present in the way
            for (int j = 0; j < (nodes.size() - 1); j++) {
                SingleNode startingNode = nodes.get(j);
                SingleNode finishingNode = nodes.get(j + 1);
                Edge edge = createEdge(startingNode, finishingNode);
                edges.add(edge);
            }
            /*
            // Store all the nodes of a single path in the nodes variable
            ArrayList<SingleNode> nodes = ways.get(i).getNodes();

            // parse through all the nodes present in the way
            for (int j = 0; j < (nodes.size() - 1); j++) {

                // check if the node is an intersection
                //if (nodes.get(j).isIntersection()) {

                // if an intersection is found, call that the starting node
                SingleNode startingNode = nodes.get(j);

                // now parse through the rest of the nodes left in the way and again look for an intersection
                int k;
                for (k = j + 1; k < nodes.size(); k++) {

                    Edge edge = null;

                    // if another intersection is found, then call that the finishing node
                    if (nodes.get(k).isIntersection()) {
                        SingleNode finishingNode = nodes.get(k);

                        // send the nodes to a function which will create an edge out of the two nodes
                        edge = createEdge(startingNode, finishingNode);

                    }
                    edges.add(edge);
                }

                // j should now start where k left off
                j = k;
                //}
            }*/
        }

        return null;
    }


    private Edge createEdge(SingleNode startingNode, SingleNode finishingNode) {

        // store the id's of the starting and finishing node
        //double fromIndex = startingNode.getNodeID();
        int fromIndex = startingNode.getMappedValue();
        //double toIndex = finishingNode.getNodeID();
        int toIndex = finishingNode.getMappedValue();

        // TODO
        // to get the traversal time, we must call the distance matrix api and get the travel time
        float traversalTime = getTraversalTime(startingNode.getLatLng(), finishingNode.getLatLng());

        // TODO
        // Most probably we don't need to do this as this information is present in the normal time variable
        float averageSpeedOfTraffic = getAverageSpeedOfTraffic(startingNode.getLatLng(), finishingNode.getLatLng());

        // These values will stay 0 if there's no traffic light, otherwise they will have to be updated
        int initialDelayFROM = 0;
        int timeDurationOfGreenLightFROM = 0;
        int timeDurationOfRedLightFROM = 0;

        // If the node has a traffic light, initialize the traffic light
        if (startingNode.hasTrafficLight()) {
            initialDelayFROM = startingNode.getOffset();
            timeDurationOfGreenLightFROM = startingNode.getDurationOfGreenLight();
            timeDurationOfRedLightFROM = startingNode.getDurationOfRedLight();
        }

        // Same thing is happening for the finishing node
        int initialDelayTO = 0;
        int timeDurationOfGreenLightTO = 0;
        int timeDurationOfRedLightTO = 0;

        if (finishingNode.hasTrafficLight()) {
            initialDelayTO = finishingNode.getOffset();
            timeDurationOfGreenLightTO = finishingNode.getDurationOfGreenLight();
            timeDurationOfRedLightTO = finishingNode.getDurationOfRedLight();
        }

        Log.i("Time between edges", Float.toString(traversalTime));

        // Create the edge

        Log.i("CREATE EDGE", "Connecting " + Integer.toString(fromIndex) + " & " + Integer.toString(toIndex));

        Edge edge = new Edge(fromIndex, toIndex, traversalTime,
                initialDelayFROM, timeDurationOfGreenLightFROM, timeDurationOfRedLightFROM,
                initialDelayTO, timeDurationOfGreenLightTO, timeDurationOfRedLightTO);

        return edge;
        //return null;

    }

    private float getTraversalTime(LatLng starting, LatLng finishing) {
        float result = 0;

        // TODO. Call distance matrix api to get the time of traversal. Mentioned in Joe's code on Slack
        // Done

        // Building the URL for calling Distance Matrix API
        String buildURL = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=";
        buildURL += Double.toString(starting.latitude);
        buildURL += ",";
        buildURL += Double.toString(starting.longitude);
        buildURL += "&destinations=";
        buildURL += Double.toString(finishing.latitude);
        buildURL += ",";
        buildURL += Double.toString(finishing.longitude);
        buildURL += "&key=";
        buildURL += "AIzaSyAVX5IflSRZH7r2D5_wW63ojuIJJr-q98s";

        // Now we need to make the call
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

            // Now we need to parse the JSON Result
            result = Utilities.parseDistanceAPIresult(buffer.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private float getAverageSpeedOfTraffic(LatLng starting, LatLng finishing) {
        float result = 0;

        // TODO. Call distance matrix api to get the time of traversal. Mentioned in Joe's code on Slack

        return result;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        Log.i("CREATE GRAPH","EXECUTED!");

        //TODO Call Dijkstra
        Graph graph = new Graph(edges);

        Log.i("CREATE GRAPH", "NEW GRAPH");
        int res = graph.calculateShortestDistance(1);
        MapsActivity.setTextView6("Result of Djikstra : " + Integer.toString(res));
        //ArrayList<Double> finalRoute = graph.calculateShortestDistance(1);
        // TODO
        // return the result in an arrayList
        // draw polylines
        //Utilities.drawPolyline(finalRoute);
    }
}
