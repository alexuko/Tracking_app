package com.example.tracker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * The MainActivity class
 * This is the activity that the user will interact with
 * as part of the Tracking app I have built.
 * Class extends AppCompatActivity class, Base class for activities that use action bar features.
 * thus we can use all of its methods
 * <h5>Global variables</h5>
 * All of the necessary variable to initialize and use our MainActivity
 * locationManager obtaining information from the GPS
 * thisActivity reference itself
 * start_btn button to start GPS tracking
 * stop_btn button to stop GPS tracking
 * date current date
 * TAG  tag for this activity
 * myPlaces array to hold a list of locations
 * locations number of Locations counted
 * time time variable that increase on each GPS reading
 * chronometer view used to display the elapse time since user start the GPS tracking
 * chronometer checks that the timer is running or not
 * isRunning checks that the timer is running or not
 * pauseOffset
 * calendar class to extract the time and date
 * @author  Alejandro Rivera
 * @version 1.0
 * @since   2019-12-13
 */
public class MainActivity extends AppCompatActivity {
    private LocationManager locationManager;
    private MainActivity thisActivity = this;
    private Button start_btn;
    private Button stop_btn;
    private TextView date;
    private static final String TAG = "MainActivity";
    private ArrayList<NewLocation> myPlaces = new ArrayList<NewLocation>();
    private int locations = 0;
    private int time = 0;
    private Chronometer chronometer;
    private boolean isRunning;
    private long pauseOffset;
    private Calendar calendar;

    /**
     * Method fires up when the system first creates the activity. On activity creation,
     * the activity enters the Created state set the user interface layout for this activity.
     * the layout file is defined in the project res/layout/activity_main.xml file
     * pull out the views from the XML file that we are going to need to display information,
     * Initialises calendar and locationManager classes.
     * <p>start_btn.setOnClickListener</p>
     * Listener is set to the start button, clears array if not empty, makes stop button visible,
     * makes itself invisible, display a short text to user, so he know the app is running, along with
     * a chronometer, and starts the GPS service with addLocationListener()
     *<p>start_btn.setOnClickListener</p>
     * Listener set to the stop button which stops the chronometer,checks is any
     * information has been saved or if there is at least 2 locations as a minimum.
     * if not a message will be notifying the user that data is too short
     * otherwise it takes user to the ReportActivity, passing as extra the arrayList of locations with the intent
     * @param savedInstanceState  reference to a Bundle object that is passed into the onCreate method
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start_btn = (Button) findViewById(R.id.start_btn);
        stop_btn = (Button) findViewById(R.id.stop_btn);
        date= (TextView) findViewById(R.id.date);
        chronometer = (Chronometer) findViewById(R.id.chronometer);

        calendar = Calendar.getInstance();
        String today = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        date.setText(today);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        start_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Start");
                myPlaces.clear();
                // button stop becomes visible and start btn invisible
                stop_btn.setVisibility(View.VISIBLE);
                start_btn.setVisibility(View.GONE);
                Toast.makeText(thisActivity, "Tracking", Toast.LENGTH_SHORT).show();
                // reset Chronometer
                resetChronometer();
                // start Chronometer
                startChronometer();
                addLocationListener();

                //Mock Locations
//                myPlaces.add(new NewLocation(locations++,-6.231027042694415,18.3517698,53.35354326465989,0.25f,time++));
//                myPlaces.add(new NewLocation(locations++,-6.230953496420932,21.3530474,53.35359704414011,1.66f,time++));
//                myPlaces.add(new NewLocation(locations++,-6.230877063093608,33.3562136,53.35365293473008,5.8f,time++));
//                myPlaces.add(new NewLocation(locations++,-6.230800738606992,45.3584751,53.3537087457321,5.20f,time++));
//                myPlaces.add(new NewLocation(locations++,-6.2320229381217715,53.1427934,53.35376563603337,7.0f,time++));
//                myPlaces.add(new NewLocation(locations++,-6.234036583077384,57.3723318,53.3538287817127,6.3f,time++));
//                myPlaces.add(new NewLocation(locations++,-6.231514935413685,40.3589535,53.35391773452857,5.10f,time++));
//                myPlaces.add(new NewLocation(locations++,-6.237027042694415,32.3517698,53.35354326465989,9.95f,time++));
//                myPlaces.add(new NewLocation(locations++,-6.238053496420932,24.3530474,53.35359704414011,1.66f,time++));
//                myPlaces.add(new NewLocation(locations++,-6.239077063093608,48.3562136,53.35365293473008,5.8f,time++));
//                myPlaces.add(new NewLocation(locations++,-6.231330738606992,45.3584751,53.3537087457321,5.20f,time++));
//                myPlaces.add(new NewLocation(locations++,-6.2307229381217715,53.1427934,53.35376563603337,7.0f,time++));
//                myPlaces.add(new NewLocation(locations++,-6.230636583077384,57.3723318,53.3538287817127,6.3f,time++));
//                myPlaces.add(new NewLocation(locations++,-6.230514935413685,59.3589535,53.35391773452857,5.10f,time++));


            }
        });

        stop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Stop");
                Log.d(TAG, "TIME----------: " + time);
                // button stop becomes invisible and start btn visible
                stop_btn.setVisibility(View.GONE);
                start_btn.setVisibility(View.VISIBLE);
                //pause Chronometer once button has been pressed
                pauseChronometer();
                // if there is  enough data then  go to the reportActivity class
                if(!myPlaces.isEmpty() && myPlaces.size() > 1 ){
                    Toast.makeText(thisActivity, "Your Results ", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, ReportActivity.class);
                    intent.putExtra("myPlaces", myPlaces);
                    startActivity(intent);
                }else{
                    // if there is not enough data then  display a message to the user
                    Toast.makeText(thisActivity, "Tracking data is too short", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    /**
     * Method starts a chronometer, checks if there is none running to start one
     * elapsedRealtime() time since the system was booted, and include deep sleep
     * minus pauseOffset is the base time as set through since timer started,
     * zero will be its value at the beginning and variable isRunning
     * will be set to true meaning that there is a chronometer running
     */
    public void startChronometer() {
        if(!isRunning){
            //starts the chronometer with the time when pressing the button instead of when the activity starts
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            //starts the chronometer
            chronometer.start();
            isRunning = true;
        }
    }

    /**
     * Method stop the chronometer, sets pauseOffset the time that elapsed since button
     * start was pressed - is the base time as set through
     * setting isRunning variable to false so there is not a current chronometer running
     */
    public void pauseChronometer(){
        if(isRunning){
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            isRunning = false;
        }
    }

    /**
     * Method resets values to their defaults
     * pauseOffset is set to zero, so previous holding time is erased
     */
    public void resetChronometer(){
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
    }

    /**
     * Method that will add a location listener to the location manager
     * checks is there is a permission already granted by the user otherwise
     * it request permission to access the GPS service
     * <p>locationManager.requestLocationUpdates()</p>
     * Method requestLocationUpdates request the GPS_PROVIDER every 5 seconds and with a min
     * distance of 5m to get a location.final argument specifies the listener
     * that is to be registered with the  LocationManager. We define an anonymous
     * class here that will implement our
     * listener
     *
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void addLocationListener() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // request the required permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, new LocationListener() {
            /**
             * Method take in a location class and it will be fired every time that there is a
             * change in the location
             * On every change a NewLocation object will be added to the list
             * @param location class representing a geographic location
             *
             */
            @Override
            public void onLocationChanged(Location location) {
                // the location of the device has changed so update the textViews to reflect this
                myPlaces.add(new NewLocation(locations++,location.getLongitude(), location.getAltitude(), location.getLatitude(), location.getSpeed(),time++));

//                Toast.makeText(thisActivity, "Longitude " + location.getLongitude()
//                                                 +" Altitude: "+  location.getAltitude()
//                                                 +" Latitude "+ location.getLatitude()
//                                                 +" Speed: "+ location.getSpeed()
//                                                 +" Time: "+ time, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Longitude " + location.getLongitude()
                        +" Altitude: "+  location.getAltitude()
                        +" Latitude "+ location.getLatitude()
                        +" Speed: "+ location.getSpeed()
                        +" Time: "+ time);


            }

            /**
             * Method is called whenever the user has disabled either GPS location or network location
             * method will be called for you with the name of the provider that was disabled
             * a short message will be prompted to the user if that happens
             * @param provider current GPS provider
             */
            @Override
            public void onProviderDisabled(String provider) {
                if (provider == LocationManager.GPS_PROVIDER) {
                    Toast.makeText(thisActivity, "GPS Disabled", Toast.LENGTH_SHORT).show();
                }
            }

            /**
             * Method is called whenever the user has enabled a provider.
             * switch from no location to network location and to
             * GPS location if the user enables them in that way
             * checking permissions again
             * @param provider provider you are using to get your network  locations
             */
            @Override
            public void onProviderEnabled(String provider) {
                if (provider == LocationManager.GPS_PROVIDER) {
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // request the required permission
                        ActivityCompat.requestPermissions(thisActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                        return;
                    }

                    // if there is a last known location then set it on the TextViews
                    Location l = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (l != null) {
                        //we'll do nothing
                    }
                }
            }

            /**
             * Method will be called whenever the location is temporarily unavailable or becomes available again
             * @param provider provider you are using to get your network  locations
             * @param status GPS status
             * @param extras determine the number of GPS satellites that are being used to locate the device's position.
             */
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
        });
    }

    /**
     * Method request permission to access the GPS service
     * @param requestCode permission code
     * @param permissions available Type of permission
     * @param grantResults whether permission was granted or not
     */
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        if (requestCode == 1) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else { // permission denied, boo! Disable the functionality that depends on this permission.
                Log.i("onReqPermissionsResult", "permission was not granted");
            }
            return;
            // other 'case' lines to check for other permissions this app might request
        }
    }

}
