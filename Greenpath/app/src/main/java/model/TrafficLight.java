package model;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by smukhopadhyay on 4/14/16.
 */
public class TrafficLight implements DEBUG {

    // Initial offset in the traffic light
    int initialDelay;

    // Time duration of green light
    int timeDurationOfGreenLight;

    // Time duration of red light
    int timeDurationOfRedLight;

    // The cycle time of the light
    int cycleTimeOfLight;

    // Constructor for initializing the traffic light
    public TrafficLight(int initialDelay, int timeDurationOfGreenLight, int timeDurationOfRedLight) {
        this.initialDelay = initialDelay;

        this.timeDurationOfGreenLight = timeDurationOfGreenLight;

        this.timeDurationOfRedLight = timeDurationOfRedLight;

        cycleTimeOfLight = timeDurationOfGreenLight + timeDurationOfRedLight;
    }

    public ArrayList<Integer> getCondition(float resultant) {

        int resultantTime = (int) resultant;


        // Results array
        // Index 0: 0(Red), 1(Green)
        // Index 1: if (Index 0 == 0), then return the amount of time for which it will stay as red
        ArrayList<Integer> res = new ArrayList<Integer>();

        // Calendar instance
        Calendar cal= Calendar.getInstance();

        // Simple date time format for formatting the calendar output
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        // Get the time value and store it as a string
        String time = sdf.format(cal.getTime());

        // Split the time value into hours, munites and seconds
        String[] splitTime = time.split(":");

        int hour = Integer.parseInt(splitTime[0]);
        int min = Integer.parseInt(splitTime[1]);
        int sec = Integer.parseInt(splitTime[2]);

        // Assume that we are at Node 1 and we'll be arriving at Node 2 in "resultant time".
        // That means we have to find out what the time will be after "resultant time"
        // We call this future time.
        int futureTimeMin = min + resultantTime + initialDelay;

        // Taking care of overflows
        if (futureTimeMin >= 60) {
            futureTimeMin = futureTimeMin - 60;
            hour++;
        }

        // Take the modulus of the future time and check in which part of the cycle we will be when we reach node 2
        Log.i("CYCLE TIME OF LIGHT", "Green Light : " + Integer.toString(timeDurationOfGreenLight) + "  Red Light : " + Integer.toString(timeDurationOfRedLight));

        if (cycleTimeOfLight == 0) {
            cycleTimeOfLight = 1;
        }

        int mod = futureTimeMin % cycleTimeOfLight;

        // If modulus value is smaller than duration of red light, we will be stuck at red when we reach Node 2
        // In such a case, we will set Index 0 of res as 0
        // We will set Index 1 as (timeDurationOfRedLight - mod) because that is the amount of time for which we will be stuck at the red light if we come to Node 2
        if (mod < timeDurationOfRedLight) {
            res.add(0);
            res.add(timeDurationOfRedLight - mod);
        } else {
            res.add(1);
        }

        if (DEBUG) {
            String result;
            if (res.get(0) == 0) {
                result = "red";
            }
            else {
                result = "green";
            }
            System.out.format("Light condition at time %02d:%02d is : %s\n", hour, futureTimeMin, result);

            if (res.get(0) == 0) {
                System.out.println("Waiting time at red light : " + res.get(1) + " minute");
            }
        }
        return res;
    }

}
