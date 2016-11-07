package com.example.smukhopadhyay.greenpathv4;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by smukhopadhyay on 4/23/16.
 */
public class SingleNode {

    private int mappedValue;
    public int getMappedValue() {
        return mappedValue;
    }
    public void setMappedValue(int val) {
        mappedValue = val;
    }

    private double nodeID;
    public double getNodeID() {
        return nodeID;
    }
    public void setNodeID(double nodeID) {
        this.nodeID = nodeID;
    }


    private LatLng latLng;
    public LatLng getLatLng() {
        return latLng;
    }
    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    /*
    private boolean isIntersection;
    public boolean isIntersection() {
        return isIntersection;
    }
    public void setIntersection(boolean val) {
        isIntersection = val;
    }
    */

    private boolean hasTrafficLight;
    public boolean hasTrafficLight() {
        return hasTrafficLight;
    }
    public void setTrafficLight(boolean val) {
        hasTrafficLight = val;
    }


    private int offset;
    public int getOffset() {
        return  offset;
    }
    public void setOffset(int val) {
        offset = val;
    }


    private int durationOfGreenLight;
    public int getDurationOfGreenLight() {
        return durationOfGreenLight;
    }
    public void setDurationOfGreenLight(int val) {
        durationOfGreenLight = val;
    }


    private int durationOfRedLight;
    public int getDurationOfRedLight() {
        return durationOfRedLight;
    }
    public void setDurationOfRedLight(int val) {
        durationOfRedLight = val;
    }


    public SingleNode(int mappedValue, double nodeID, LatLng latLng, boolean hasTrafficLight,
                      int offset, int durationOfGreenLight, int durationOfRedLight) {
        this.mappedValue = mappedValue;
        this.nodeID = nodeID;
        this.latLng = latLng;
        //this.isIntersection = isIntersection;
        this.hasTrafficLight = hasTrafficLight;
        this.offset = offset;
        this.durationOfGreenLight = durationOfGreenLight;
        this.durationOfRedLight = durationOfRedLight;
    }
}
