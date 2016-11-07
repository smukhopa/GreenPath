package com.example.smukhopadhyay.greenpathv4;

import java.util.ArrayList;

/**
 * Created by smukhopadhyay on 4/23/16.
 */
public class SingleWay {

    private double wayID;
    public double getWayID() {
        return wayID;
    }
    public void setWayID(double ID) {
        this.wayID = ID;
    }

    /*
    private String name;
    public String getName() {
        return name;
    }
    public void setName(String str) {
        name = str;

    }
    */

    private ArrayList<SingleNode> nodes;
    public ArrayList<SingleNode> getNodes() {
        return nodes;
    }
    public void setNodes(ArrayList<SingleNode> nodes) {
        this.nodes = nodes;
    }

    public SingleWay(double ID, ArrayList<SingleNode> nodes) {
        this.wayID = ID;
        this.nodes = nodes;
    }

}
