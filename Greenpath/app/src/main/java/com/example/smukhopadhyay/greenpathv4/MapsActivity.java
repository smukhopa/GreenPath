package com.example.smukhopadhyay.greenpathv4;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import model.Edge;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static GoogleMap mMap;


    /*
    THE TOTAL TIME TAKEN!!!!!!!
     */
    private static float totalTime;
    public static void setTotalTime(float val) {
        totalTime = val;
    }
    public static float getTotalTime() {
        return totalTime;
    }


    /*
    THE FINAL ROUTE!!!!!!!!
     */
    private static ArrayList<FinalRoute> finalRoutes = null;
    public static void setFinalRoutes(ArrayList<FinalRoute> arr) {
        finalRoutes = arr;
    }
    public static ArrayList<FinalRoute> getFinalRoutes() {
        return finalRoutes;
    }


    /*
    NodeID of the closest point to the starting point
     */
    private static double nodeIDStartingClosest;
    public static void setNodeIDStartingClosest(double val) {
        nodeIDStartingClosest = val;
    }
    public static double getNodeIDStartingClosest() {
        return nodeIDStartingClosest;
    }


    /*
    NodeID of the closest point to finishing point
     */
    private static double nodeIDFinishingClosest;
    public static void setNodeIDFinishingClosest(double val) {
        nodeIDFinishingClosest = val;
    }
    public static double getNodeIDFinishingClosest() {
        return nodeIDFinishingClosest;
    }



    /*
    String to hold the JSON returned by the BigBox
     */
    private static String bigBoxJson;
    public static void setBigBoxJson(String str) {
        bigBoxJson = str;
    }
    public static String getBigBoxJson() {
        return bigBoxJson;
    }


    /*
    Highest and lowest node ID values
     */
    private static double highest;
    public static void setHighest(double val) {
        highest = val;
    }
    public static double getHighest() {
        return highest;
    }

    private static double lowest;
    public static void setLowest(double val) {
        lowest = val;
    }
    public static double getLowest() {
        return lowest;
    }


    /*
    Table to hold the data about the Traffic Lights which we pull from the database
     */
    static ArrayList<DatabaseNode> listDatabaseNode;
    public static void setListDatabaseNode(ArrayList<DatabaseNode> arr) {
        listDatabaseNode = arr;
    }
    public static ArrayList<DatabaseNode> getListDatabaseNode() {
        return listDatabaseNode;
    }


    /*
    Joe's table which holds the node ID's and the corresponding way ID's
     */
    static ArrayList<ArrayList<Double>> joeTable;
    public static void setJoeTable(ArrayList<ArrayList<Double>> arr) {
        joeTable = arr;
    }
    public static ArrayList<ArrayList<Double>> getJoeTable() {
        return joeTable;
    }

    /*
    ArrayList of all the nodes containing their id's and their lat and long values and their mapped values
     */
    static ArrayList<NodeMap> nodeMaps;
    public static void setNodeMaps(ArrayList<NodeMap> arr) {
        nodeMaps = arr;
    }
    public static ArrayList<NodeMap> getNodeMaps() {
        return nodeMaps;
    }


    /*
    ArrayList containing the names of all the paths
     */
    static ArrayList<String> namesOfAllThePaths;
    public static void setNamesOfAllThePaths(ArrayList<String> arr) {
        namesOfAllThePaths = arr;
    }
    public static ArrayList<String> getNamesOfAllThePaths() {
        return namesOfAllThePaths;
    }


    /*
    The Big Array
     */
    static ArrayList<SingleWay> ways;
    public static void setWays(ArrayList<SingleWay> arr) {
        ways = arr;
    }
    public static ArrayList<SingleWay> getWays() {
        return ways;
    }


    /*
    Starting Point
     */
    static LatLng startingPoint;
    public static void setStartingPoint(LatLng latLng) {
        startingPoint = latLng;
    }
    public static LatLng getStartingPoint() {
        return startingPoint;
    }


    /*
    Finishing point
     */
    static LatLng finishingPoint;
    public static void setFinishingPoint(LatLng latLng) {
        finishingPoint = latLng;
    }

    public static LatLng getFinishingPoint() {
        return finishingPoint;
    }


    /*
    Closest Point to starting point
     */
    static LatLng closesPointToStart;
    static void setClosestPointToStart(LatLng latLng) {
        closesPointToStart = latLng;
    }
    static LatLng getClosesPointToStart() {
        return closesPointToStart;
    }


    /*
    ClosestPoint to finishing point
     */
    static LatLng closestPointToEnd;
    static void setClosestPointToEnd(LatLng latLng) {
        closestPointToEnd = latLng;
    }
    static LatLng getClosestPointToEnd() {
        return closestPointToEnd;
    }


    /*
    Directions API Extreme points
     */
    static LatLng maxLATmaxLNG;
    static void setMaxLATmaxLNG(LatLng latLng) {
        maxLATmaxLNG = latLng;
    }
    static LatLng getMaxLATmaxLNG() {
        return maxLATmaxLNG;
    }

    static LatLng minLATminLNG;
    static void setMinLATminLNG(LatLng latLng) {
        minLATminLNG = latLng;
    }
    static LatLng getMinLATminLNG() {
        return minLATminLNG;
    }


    /*
    Textviews
     */
    static TextView textView;
    public static void setTextView(String str) {
        textView.setText(str);
    }

    static TextView textView2;
    public static void setTextView2(String str) {
        textView2.setText(str);
    }

    static TextView textView3;
    public static void setTextView3(String str) {
        textView3.setText(str);
    }

    static TextView textView4;
    public static void setTextView4(String str) {
        textView4.setText(str);
    }

    static TextView textView5;
    public static void setTextView5(String str) {
        textView5.setText(str);
    }

    static TextView textView6;
    public static void setTextView6(String str) {
        textView6.setText(str);
    }

    static TextView textView7;
    public static void setTextView7(String str) {
        textView7.setText(str);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        textView = (TextView)findViewById(R.id.textView);
        setTextView("Testing 1");

        textView2 = (TextView)findViewById(R.id.textView2);
        setTextView2("Testing 2");

        textView3 = (TextView)findViewById(R.id.textView3);
        setTextView3("Testing 3");

        textView4 = (TextView)findViewById(R.id.textView4);
        setTextView4("Testing 4");

        textView5 = (TextView)findViewById(R.id.textView5);
        setTextView5("Testing 5");

        textView6 = (TextView)findViewById(R.id.textView6);
        setTextView6("Testing 6");

        textView7 = (TextView)findViewById(R.id.textView7);
        setTextView7("Testing 7");

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.fragment);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                setStartingPoint(place.getLatLng());
                mMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                mMap.addMarker(new MarkerOptions().position(place.getLatLng()).title("Starting point : " + place.getName().toString()));
                Toast.makeText(getApplicationContext(), "Starting point : " + place.getName(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Status status) {
                Toast.makeText(MapsActivity.this, "An error occured" + status, Toast.LENGTH_SHORT).show();
            }
        });

        PlaceAutocompleteFragment autocompleteFragment1 = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.fragment2);
        autocompleteFragment1.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                setFinishingPoint(place.getLatLng());
                mMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                mMap.addMarker(new MarkerOptions().position(place.getLatLng()).title("Finishing point : " + place.getName().toString()));
                Toast.makeText(getApplicationContext(), "Finishing point : " + place.getName(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Status status) {
                Toast.makeText(MapsActivity.this, "An error occured" + status, Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    public void findRoute(View view) {

        LatLng start = getStartingPoint();
        LatLng finish = getFinishingPoint();

        if (start == null || finish == null) {
            Toast.makeText(getApplicationContext(), "Please Enter Start and Finish points", Toast.LENGTH_SHORT).show();
        }
        else {

            Double higherLat, higherLng, lowerLat, lowerLng;

            if (start.latitude > finish.latitude) {
                higherLat = start.latitude;
                lowerLat = finish.latitude;
            } else {
                higherLat = finish.latitude;
                lowerLat = start.latitude;
            }

            if (start.longitude > finish.longitude) {
                higherLng = start.longitude;
                lowerLng = finish.longitude;
            } else {
                higherLng = finish.longitude;
                lowerLng = start.longitude;
            }

            LatLngBounds area = new LatLngBounds(
                    new LatLng(lowerLat, lowerLng), new LatLng(higherLat, higherLng));

            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(area, 250));
            Utilities.DirectionsAPICall();
            Utilities.smallBoxStart();
            Utilities.smallBoxFinish();
            Utilities.bigBox();
        }
    }
}