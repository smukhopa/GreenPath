package model;

import android.util.Log;

import com.example.smukhopadhyay.greenpathv4.FinalRoute;
import com.example.smukhopadhyay.greenpathv4.MapsActivity;
import com.example.smukhopadhyay.greenpathv4.NodeMap;
import com.example.smukhopadhyay.greenpathv4.Utilities;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by smukhopadhyay on 4/14/16.
 */
public class Graph implements DEBUG {

    // ArrayList of nodes
    private ArrayList<Node> nodes;

    // ArrayList of edges
    private ArrayList<Edge> edges;

    // Total number or nodes
    private int numberOfNodes;

    // Total number of edges
    private int numberOfEdges;

    /*
     * Constructor for calculating number of nodes and edges and then joining the
     * edges to the nodes
     */
    public Graph(ArrayList<Edge> edges) {

        // ArrayList of edges are passed in. Store them.
        this.edges = edges;

        // From the edges, calculate the number of nodes
        numberOfNodes = calculateNumberOfNodes(edges);

        // create an arrayList of nodes
        nodes = new ArrayList<Node>();

        // Add all the nodes to the arraylist of nodes.
        // The index of these nodes is simply set by i right now.
        for (int i = 0; i < numberOfNodes; i++) {
            nodes.add(new Node(i));
        }

        // Each edge is now added to the nodes.
        // Note that each edge has 2 nodes associated with it, a "to" edge and a
        // "from" edge.
        numberOfEdges = edges.size();
        for (int i = 0; i < numberOfEdges; i++) {
            nodes.get(edges.get(i).getFromIndex()).getEdges().add(edges.get(i));
            nodes.get(edges.get(i).getToIndex()).getEdges().add(edges.get(i));
        }
    }

    /*
     * Used for calculating the total number of nodes given the edges
     */
    private int calculateNumberOfNodes(ArrayList<Edge> edges) {
        int number = 0;

        for (Edge e : edges) {
            if (e.getToIndex() > number)
                number = e.getToIndex();
            if (e.getFromIndex() > number)
                number = e.getFromIndex();
        }
        number++;
        if (DEBUG) {
            System.out.println("The number of nodes : " + number);
        }
        return number;
    }

    /*
     * Djikstra's Algo implementation.
     */
    public int calculateShortestDistance(int destination) {

        // Set the node zero as the source
        nodes.get(0).setDistanceFromSource(0);

        int currentNode = 0;

        for (int i = 0; i < nodes.size(); i++) {

            ArrayList<Edge> currentNodeEdges = nodes.get(currentNode).getEdges();

            for (int j = 0; j < currentNodeEdges.size(); j++) {

                int neighborIndex = currentNodeEdges.get(j).getNeighborIndex(currentNode);

                if (nodes.get(neighborIndex).isVisited() == false) {

                    float tentativeDistance = nodes.get(currentNode).getDistanceFromSource() + currentNodeEdges.get(j).getTotalTime(currentNode, neighborIndex);

                    ArrayList<Integer> nodesVisited = new ArrayList<Integer>();

                    int sizeOfNodesTrversedList = nodes.get(currentNode).sizeOfNodesTraversedList();

                    for (int k = 0; k < sizeOfNodesTrversedList; k++) {
                        nodesVisited.add(nodes.get(currentNode).returnIndividualNodesTraversedValues(k));
                    }


                    nodesVisited.add(currentNode);

                    if (tentativeDistance < nodes.get(neighborIndex).getDistanceFromSource()) {
                        nodes.get(neighborIndex).setDistanceFromSource(tentativeDistance);
                        nodes.get(neighborIndex).setNodesTraversed(nodesVisited);
                    }
                }
            }

            nodes.get(currentNode).setVisited(true);

            if (currentNode == destination) {

                float totalTime = nodes.get(currentNode).getDistanceFromSource();

                MapsActivity.setTotalTime(totalTime);

                MapsActivity.setTextView7("Total time of traversal : " + totalTime);

                ArrayList<FinalRoute> finalRoutes = new ArrayList<FinalRoute>();

                for (int j = 1; j < nodes.get(currentNode).sizeOfNodesTraversedList(); j++) {
                    Log.i("FINAL ROUTE", Integer.toString(nodes.get(currentNode).returnIndividualNodesTraversedValues(j)));

                    int mappedValue = nodes.get(currentNode).returnIndividualNodesTraversedValues(j);

                    NodeMap node = Utilities.searchInNodeMapUsingMappedValue(mappedValue);

                    double nodeID = node.getNodeID();

                    double lat = node.getLat();

                    double lng = node.getLng();

                    LatLng latLng = new LatLng(lat, lng);

                    FinalRoute finalRoute = new FinalRoute(mappedValue, nodeID, latLng);

                    finalRoutes.add(finalRoute);
                }

                Utilities.drawPolyline(finalRoutes);


                /*
                System.out.println("Shortest Time to node " + destination + " : " + nodes.get(currentNode).getDistanceFromSource());
                System.out.print("Route Taken : ");
                for (int j = 0; j < nodes.get(currentNode).sizeOfNodesTraversedList(); j++) {
                    System.out.print(nodes.get(currentNode).returnIndividualNodesTraversedValues(j) + " --> ");
                }
                System.out.print(destination);
                */
                return currentNode;
            }
            currentNode = getNodeWithSmallestValue();
        }

        return -1;
    }

    private int getNodeWithSmallestValue() {

        int res = 0;

        float storedDist = Integer.MAX_VALUE;

        for (int i = 0; i < nodes.size(); i++) {
            float currentDist = nodes.get(i).getDistanceFromSource();
            if (nodes.get(i).isVisited() == false && currentDist < storedDist) {
                storedDist = currentDist;
                res = i;
            }
        }
        return res;
    }

}
