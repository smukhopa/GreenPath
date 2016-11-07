package com.example.smukhopadhyay.greenpathv4;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by smukhopadhyay on 5/5/16.
 */
public class FinalRoute {

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

    private LatLng latLng;
    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
    public LatLng getLatLng() {
        return latLng;
    }

    public FinalRoute(int mappedValue, double nodeID, LatLng latLng) {
        this.mappedValue = mappedValue;
        this.nodeID = nodeID;
        this.latLng = latLng;
    }

}
