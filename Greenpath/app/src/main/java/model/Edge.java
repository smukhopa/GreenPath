package model;

import java.util.ArrayList;

/**
 * Created by smukhopadhyay on 4/14/16.
 */
public class Edge implements DEBUG{

    // The two index are simply the two indexes that join the edge. They do not
    // signify that cars can move only from "fromIndex" to "toIndex". Cars can
    // move in either direction.
    private int fromIndex;
    private int toIndex;

    // Amount of time taken when driving at SPEEDLIMIT
    // Calculated simply by (Distance / SPEEDLIMIT)
    private float traversalTime;

    // The average speed of traffic on that road
    // It is indicated in mph.
    //private float avgSpeedOfTraffic;

    // The resultant time due to traffic
    //private float resultantTime;

    // Create two traffic lights, each associated with with one end of the edge
    TrafficLight tFROM = null;
    TrafficLight tTO = null;

    /*
     * Constructor to set the two indexes, the amount of time it would take to
     * go down that road without any traffic and the average flow of traffic
     * on that road
     */
    public Edge(int fromIndex, int toIndex, float traversalTime,
                  int initialDelayFROM, int timeDurationOfGreenLightFROM, int timeDurationOfRedLightFROM,
                  int initialDelayTO, int timeDurationOfGreenLightTO, int timeDurationOfRedLightTO) {
        this.fromIndex = fromIndex;
        this.toIndex = toIndex;
        this.traversalTime = traversalTime;

        //this.normalTime = normalTime;
        //this.avgSpeedOfTraffic = avgSpeedOfTraffic;

        // Calculate the amount of time it would take to traverse the edge
        // with the given traffic conditions;
        //float lengthOfEdge = this.normalTime * SPEEDLIMIT;
        //this.resultantTime = lengthOfEdge / this.avgSpeedOfTraffic;

        // Create two traffic lights
        tFROM = new TrafficLight(initialDelayFROM, timeDurationOfGreenLightFROM, timeDurationOfRedLightFROM);
        tTO = new TrafficLight(initialDelayTO, timeDurationOfGreenLightTO, timeDurationOfRedLightTO);
    }

    /*
     * getter for "From Index"
     */
    public int getFromIndex() {
        return fromIndex;
    }

    /*
     * setter for "From Index"
     */
    public void setFromIndex(int fromIndex) {
        this.fromIndex = fromIndex;
    }

    /*
     * getter for "To Index"
     */
    public int getToIndex() {
        return toIndex;
    }

    /*
     * setter for "To Index"
     */
    public void setToIndex(int toIndex) {
        this.toIndex = toIndex;
    }

    /*
     * return the amount of time it would take to traverse the entire edge
     */
    public float getTotalTime(int currentNode, int neighborIndex) {

        // Time taken due to traffic
        //resultantTime = (float)Math.ceil(resultantTime);

        // Used for storing the conditions sent in by the traffic lights
        ArrayList<Integer> condition = new ArrayList<Integer>();

        // if-else to determine which traffic light condition has to be checked
        if (currentNode == fromIndex && neighborIndex == toIndex) {
            condition = tTO.getCondition(traversalTime);
        } else {
            condition = tFROM.getCondition(traversalTime);
        }

        // if the light is red, then calculate the stoppage time
        if (condition.get(0) == 0) {
            traversalTime = traversalTime + condition.get(1);
        }

        // return total time required for traversing the edge
        return traversalTime;
    }

    /*
     * Used for returning either the from-index or the to-index of an edge
     */
    public int getNeighborIndex(int nodeIndex) {
        if (fromIndex == nodeIndex)
            return toIndex;
        else return fromIndex;
    }

}
