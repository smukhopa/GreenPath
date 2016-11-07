package com.example.smukhopadhyay.greenpathv4;

/**
 * Created by smukhopadhyay on 5/3/16.
 */

/*
This class exactly replicates the database structure. The following diagram shows the database structure"

| NodeID | HasTrafficLight | Offset | GreenLightDuration | RedLightDuration |
|        |                 |        |                    |                  |
|(double)|(int 0 or 1)     | int    | int                | int              |
 */
public class DatabaseNode {

    private double nodeID;
    public void setNodeID(double val) {
        nodeID = val;
    }
    public double getNodeID() {
        return nodeID;
    }


    private boolean hasTrafficLight;
    public void setHasTrafficLight(boolean val) {
        hasTrafficLight = val;
    }
    public boolean isHasTrafficLight() {
        return hasTrafficLight;
    }


    private int offset;
    public void setOffset(int val) {
        offset = val;
    }
    public int getOffset() {
        return offset;
    }


    private int greenLightDuration;
    public void setGreenLightDuration(int val) {
        greenLightDuration = val;
    }
    public int getGreenLightDuration() {
        return greenLightDuration;
    }


    private int redLightDuration;
    public void setRedLightDuration(int val) {
        redLightDuration = val;
    }
    public int getRedLightDuration() {
        return redLightDuration;
    }

    public DatabaseNode(double nodeID, boolean hasTrafficLight, int offset, int greenLightDuration, int redLightDuration) {
        this.nodeID = nodeID;
        this.hasTrafficLight = hasTrafficLight;
        this.offset = offset;
        this.greenLightDuration = greenLightDuration;
        this.redLightDuration = redLightDuration;
    }

}
