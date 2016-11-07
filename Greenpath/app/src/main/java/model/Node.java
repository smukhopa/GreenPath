package model;

import java.util.ArrayList;

/**
 * Created by smukhopadhyay on 4/14/16.
 */
public class Node {

    private int nodeID;

    private ArrayList<Integer> nodesTraversed;

    private float distanceFromSource;

    private boolean visited;

    ArrayList<Edge> edges;

    /*
     * Constructor for initializing the node
     */
    public Node(int nodeID) {
        super();
        this.nodeID = nodeID;

        this.nodesTraversed = new ArrayList<Integer>();

        this.distanceFromSource = Integer.MAX_VALUE;

        this.visited = false;

        this.edges = new ArrayList<Edge>();
    }

    /*
     * Set all the nodes being traversed into the ArrayList
     */
    public void setNodesTraversed(ArrayList<Integer> nodesTraversed) {
        this.nodesTraversed = nodesTraversed;
    }

    /*
     * Used for returning the size of list traversed
     */
    public int sizeOfNodesTraversedList() {
        return nodesTraversed.size();
    }

    /*
     * Return the individual nodes that have been traversed
     */
    public int returnIndividualNodesTraversedValues(int i) {
        return nodesTraversed.get(i);
    }

    /*
     * return the distance from the source
     */
    public float getDistanceFromSource() {
        return distanceFromSource;
    }

    /*
     * set the distance from the source
     */
    public void setDistanceFromSource(float distanceFromSource) {
        this.distanceFromSource = distanceFromSource;
    }

    /*
     * check whether the node has been visited or not
     */
    public boolean isVisited() {
        return visited;
    }

    /*
     * set a node as visited
     */
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    /*
     * return all the edges associated with a node
     */
    public ArrayList<Edge> getEdges() {
        return edges;
    }

}
