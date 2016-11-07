package com.example.smukhopadhyay.greenpathv4;

import model.Node;

/**
 * Created by smukhopadhyay on 5/4/16.
 */
public class NodeMap {

    private int mappedValue;
    public void setMappedValue(int val) {
        mappedValue = val;
    }
    public int getMappedValue() {
        return mappedValue;
    }

    private double nodeID;
    public void setNodeID(double val) {
        nodeID = val;
    }
    public double getNodeID() {
        return nodeID;
    }

    private double lat;
    public void setLat(double val) {
        lat = val;
    }
    public double getLat() {
        return lat;
    }

    private double lng;
    public void setLng(double val) {
        lng = val;
    }
    public double getLng() {
        return lng;
    }

    public NodeMap(int mappedValue, double nodeID, double lat, double lng) {
        this.mappedValue = mappedValue;
        this.nodeID = nodeID;
        this.lat = lat;
        this.lng = lng;
    }

}
