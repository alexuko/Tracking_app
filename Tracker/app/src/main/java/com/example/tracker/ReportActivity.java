package com.example.tracker;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
/**
 * The ReportActivity class
 * This is the activity where the user will see the results of
 * the Tracking app (graph and values, min, max and average).
 * Class extends AppCompatActivity class, Base class for activities that use action bar features.
 * thus we can use all of its methods
 * <h5>Global variables</h5>
 * minAltitudeView will hold the min altitude obtained from data on a TextView
 * maxAltitudeView will hold the max altitude obtained from data on a TextView
 * avgAltitudeView will hold the average altitude obtained from data on a TextView
 * minSpeedView will hold the min speed obtained from data on a TextView
 * maxSpeedView will hold the max speed obtained from data on a TextView
 * avgSpeedView will hold the average speed obtained from data on a TextView
 * totalDistanceView will hold the total distance from the locations on a TextView
 * myPlaces_ra will hold the locations objects
 * maxAltValue holds the max altitude value in this private variable
 * avgAltValue holds the average altitude value in this private variable
 * maxSpeedValue holds the max speed value in this private variable
 * avgSpeedValue holds the average speed value in this private variable
 * @author  Alejandro Rivera
 * @version 1.0
 * @since   2019-12-13
 */

public class ReportActivity extends AppCompatActivity {
    private static final String TAG = "ReportActivity";
    private TextView minAltitudeView;
    private TextView maxAltitudeView;
    private TextView avgAltitudeView;
    private TextView minSpeedView;
    private TextView maxSpeedView;
    private TextView avgSpeedView;
    private TextView totalDistanceView;
    private ArrayList<NewLocation> myPlaces_ra;
    private double maxAltValue;
    private double avgAltValue;
    private double maxSpeedValue;
    private double avgSpeedValue;

    /**
     * Method fires up when the system first creates the activity. On activity creation,
     * the activity enters the Created state set the user interface layout for this activity.
     * the layout file is defined in the project res/layout/activity_report.xml file
     * pull out the views from the XML file that we are going to need to display information.
     *
     * @param savedInstanceState  reference to a Bundle object that is passed into the onCreate method
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        minAltitudeView = (TextView) findViewById(R.id.min_altitude);
        maxAltitudeView = (TextView) findViewById(R.id.max_altitude);
        avgAltitudeView = (TextView) findViewById(R.id.average_altitude);
        minSpeedView = (TextView) findViewById(R.id.min_speed);
        maxSpeedView = (TextView) findViewById(R.id.max_speed);
        avgSpeedView = (TextView) findViewById(R.id.average_speed);
        totalDistanceView = (TextView) findViewById(R.id.total_distance);
        Intent intent = getIntent();
        myPlaces_ra = intent.getParcelableArrayListExtra("myPlaces");
        getAltitude_values(myPlaces_ra);
        getSpeed_values(myPlaces_ra);
        getTotalDistance(myPlaces_ra);

    }

    /**
     * Method loops through the  array to find the max time value, only if the arrayList is not empty
     * otherwise returns -1
     * max variable set to the first time element in the array.
     * compare if the current is bigger than the one hold in the max variable.
     * @param max holds the value
     * @return the max time or -1 if the arrayList is empty
     */
    public long getMaxTime(){
        long max;
        if(myPlaces_ra != null){
            // max variable set to the first time element in the array
            max = myPlaces_ra.get(0).getTime();

            for (int i = 0; i < myPlaces_ra.size(); i++) {
                //compare if the current is bigger than the one hold in the max variable
                if(myPlaces_ra.get(i).getTime() > max ){
                    max = myPlaces_ra.get(i).getTime();

                }
            }
            Log.d(TAG, "getMaxTime: " + max);

        }else{
            Log.d(TAG, "array is empty");
            max = -1;
            return max;
        }
        return max;

    }

    /**
     *  Method gets the Altitude values
     *  checks that tha array list is not empty, sets the variable needed,
     *  loops through the List array to find the max, min and average altitude values
     *  also maxAltValue that will  be needed for calculations on the GraphView class
     *  format the values to something shorter as the currently hold a number with long decimal points
     *  sets the values to their assigned textViews
     * @param arrLocations arrayList holding  a bunch of locations
     */
    private void getAltitude_values(ArrayList<NewLocation> arrLocations){

        //checks that tha array list is not empty
        if(arrLocations != null){
            // sets the variable needed
            double min,max, sumAltitudes = 0, average;
            //count variable will help to get the average
            int count = 0;
            min = arrLocations.get(0).getAltitude();
            max = arrLocations.get(0).getAltitude();

            for (int i = 0; i < arrLocations.size(); i++) {
                if(arrLocations.get(i).getAltitude() < min){
                    min = arrLocations.get(i).getAltitude();
                }

                if(arrLocations.get(i).getAltitude() > max ){
                    max = arrLocations.get(i).getAltitude();
                    maxAltValue = max;
                }
                sumAltitudes += arrLocations.get(i).getAltitude();
                count++;
            }
            // sum of all altitudes divided by the count will give us the average of altitudes
            average = sumAltitudes / count;
            avgAltValue = average;
            //set a new format for the results
            DecimalFormat df = new DecimalFormat("#.##");
            df.setRoundingMode(RoundingMode.CEILING);
            Log.d(TAG, "Count_Altitudes: " +  count);
            Log.d(TAG, "MinAltitude: " + df.format(min) );
            Log.d(TAG, "MaxAltitude: " + df.format(max));
            Log.d(TAG, "AverageAltitude: " + df.format(average));
            minAltitudeView.setText(df.format(min) + " m");
            maxAltitudeView.setText(df.format(max)+ " m");
            avgAltitudeView.setText(df.format(average)+ " m");

        }else{
            Log.d(TAG, "array is empty");
        }


    }
    /**
     *  Method gets the Speed values
     *  checks that tha array list is not empty, sets the variable needed,
     *  loops through the List array to find the max, min and average speed values
     *  also maxSpeedValue and avgSpeedValue that will  be needed for calculations on the GraphView class
     *  format the values to something shorter as the currently hold a number with long decimal points
     *  sets the values to their assigned textViews
     * @param arrLocations arrayList holding  a bunch of locations
     */
    private void getSpeed_values(ArrayList<NewLocation> arrLocations){
        if(arrLocations != null){
            double min,max, sumSpeed = 0, average;
            int count = 0;
            min = arrLocations.get(0).getSpeed();
            max = arrLocations.get(0).getSpeed();

            for (int i = 0; i < arrLocations.size(); i++) {
                if(arrLocations.get(i).getSpeed() < min){
                    min = arrLocations.get(i).getSpeed();
                }

                if(arrLocations.get(i).getSpeed() > max ){
                    max = arrLocations.get(i).getSpeed();

                }
                sumSpeed += arrLocations.get(i).getSpeed();
                count++;
            }

            average = sumSpeed/ count;
            maxSpeedValue = max;
            avgSpeedValue = average;
            //set a new format for the results
            DecimalFormat df = new DecimalFormat("#.##");
            df.setRoundingMode(RoundingMode.CEILING);
            Log.d(TAG, "Count_Speeds: " +  count);
            Log.d(TAG, "MinSpeed: " + df.format(min) );
            Log.d(TAG, "MaxSpeed: " + df.format(max));
            Log.d(TAG, "AverageSpeed: " + df.format(average));
            minSpeedView.setText(df.format(min) + " m/s");
            maxSpeedView.setText(df.format(max) + " m/s");
            avgSpeedView.setText(df.format(average) + " m/s");

        }else{
            Log.d(TAG, "array is empty");
        }


    }

    /**
     * Method get the Total of the distance
     *  loops through the List array an sum up distance from location to location
     *  until the last location and the sum of the distances is displayed in its
     *  appropriate textView cutting off a few decimal points with a new format to
     *  be displayed
     * @param arrLocations arrayList holding  a bunch of locations
     */
    public void getTotalDistance(ArrayList<NewLocation> arrLocations){
        double sumOfDistances = 0;
        //traverse through each point saved in the array of locations
        for (int i = 0; i < arrLocations.size() - 1; i++) {
            sumOfDistances += getDistanceBetween_2points(arrLocations.get(i).getLatitude(),
                    arrLocations.get(i).getLongitude(),
                    arrLocations.get(i+1).getLatitude(),
                    arrLocations.get(i+1).getLongitude());
        }
        Log.d(TAG, "getTotalDistance: " + sumOfDistances);
        //set a new format for the results
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        totalDistanceView.setText(df.format(sumOfDistances)+" m");
    }

    /**
     * Method gets the distance from one location to another
     * @param initialLat latitude of the initial point
     * @param initialLon longitude of the initial point
     * @param finalLat latitude of the final point
     * @param finalLon longitude of the final point
     * @return distance between those 2 locations
     */
    public double getDistanceBetween_2points(double initialLat, double initialLon, double finalLat, double finalLon){

        //https://stackoverflow.com/questions/8049612/calculating-distance-between-two-geographic-locations
        Location startPoint = new Location("initial");
        startPoint.setLatitude(initialLat);
        startPoint.setLongitude(initialLon);

        Location endPoint = new Location("final");
        endPoint.setLatitude(finalLat);
        endPoint.setLongitude(finalLon);

        double distance =  startPoint.distanceTo(endPoint);
        Log.d(TAG, "getDistanceBetween_2points: " + distance);

        return distance;

    }

    /**
     * method uses a MenuInflater to inflate this
     * file and attach it as the menu for this activity
     * @param menu xml file to be inflated
     * @return true after xml file successfully inflated
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate the menu and return true
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * method is an event handler for the menu that is attached to this activity. Any
     * time a user clicks on a menu item of an activity this method will be automatically
     * called with the menu item that was clicked.
     * @param item id that matches the menu item
     * @return true to indicate this to the android system
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will

        int id = item.getItemId();// automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (id == R.id.tracking_activity) {
            Intent intent = new Intent(ReportActivity.this,MainActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Method gets the array of locations hold in the variable myPlaces_ra and returns it
     * @return myPlaces_ra
     */
    public ArrayList<NewLocation> getMyPlaces_ra() {
        return myPlaces_ra;
    }

    /**
     * Method get the max speed from the total of locations
     * @return maxSpeedValue max altitude
     */
    public double getMaxSpeedValue() {
        return maxSpeedValue;
    }

    /**
     * Method get the average altitude from the total of locations
     * @return avgAltValue average altitude
     */
    public double getAvgAltValue() {
        return avgAltValue;
    }

    /**
     * Method get the max altitude from the total of locations
     * @return maxAltValue max altitude
     */
    public double getMaxAltValue() {
        return maxAltValue;
    }


    /**
     * Method get the average speed from the total of locations
     * @return avgSpeedValue average speed
     */
    public double getAvgSpeedValue() {
        return avgSpeedValue;
    }
}