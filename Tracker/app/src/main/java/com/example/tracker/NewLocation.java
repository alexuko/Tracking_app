package com.example.tracker;

import android.os.Parcel;
import android.os.Parcelable;
/**
 *  @author  Alejandro Rivera
  * @version 1.0
 *  @since   2019-12-13
 * */


/**
 * NewLocation class used to store a location with a number of required parameters
 * <h5>Global variables</h5>
 *  locationID id of the location
 *  longitude longitude of the current location
 *  altitude altitude of the current location
 *  latitude latitude of the current location
 *  speed current speed
 *  @param  time time passed
 */

public class NewLocation implements Parcelable {
    private int locationID;
    private double longitude;
    private double altitude;
    private double latitude;
    private float speed;
    private long time;

    /**
     * Public constructor takes in 6 parameter
     *  @param  locationID id of the location
     *  @param  longitude longitude of the current location
     *  @param  altitude altitude of the current location
     *  @param  latitude latitude of the current location
     *  @param  speed current speed
     *  @param  time time passed
     */
    public NewLocation(int locationID, double longitude, double altitude, double latitude, float speed, long time) {
        this.locationID= locationID;
        this.longitude = longitude;
        this.altitude = altitude;
        this.latitude = latitude;
        this.speed = speed;
        this.time = time;
    }

    /**
     * Constructor used a a  Container for a message (data and object references)
     * that can be sent through an IBinder.
     * @param in data that will be sent through a container
     */
    protected NewLocation(Parcel in) {
        locationID = in.readInt();
        longitude = in.readDouble();
        altitude = in.readDouble();
        latitude = in.readDouble();
        speed = in.readFloat();
        time = in.readLong();
    }
    /**
     * Interface that must be implemented and provided as a public CREATOR
     * field that generates instances of your Parcelable class from a Parcel.
     * Takes in a location to be parceable
     * @link https://developer.android.com/reference/android/os/Parcelable
     */
    public static final Creator<NewLocation> CREATOR = new Creator<NewLocation>() {
        @Override
        public NewLocation createFromParcel(Parcel in) {
            return new NewLocation(in);
        }

        @Override
        public NewLocation[] newArray(int size) {
            return new NewLocation[size];
        }
    };

    /**
     * Method get the Longitude of the current object
     * @return its longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Method get the altitude of the current object
     * @return its altitude
     */
    public double getAltitude() {
        return altitude;
    }
    /**
     * Method get the latitude of the current object
     * @return its latitude
     */
    public double getLatitude() {
        return latitude;
    }
    /**
     * Method get the speed of the current object
     * @return its speed
     */
    public float getSpeed() {
        return speed;
    }
    /**
     * Method get the time of the current object
     * @return its time
     */
    public long getTime() {
        return time;
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable instance's marshaled representation.
     * @return a bitmask indicating the set of special object types marshaled by this Parcelable object instance. Value is either 0 or CONTENTS_FILE_DESCRIPTOR
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     * @param dest The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written. May be 0 or PARCELABLE_WRITE_RETURN_VALUE. Value is either 0 or a combination of PARCELABLE_WRITE_RETURN_VALUE, and android.os.Parcelable.PARCELABLE_ELIDE_DUPLICATES
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(locationID);
        dest.writeDouble(longitude);
        dest.writeDouble(altitude);
        dest.writeDouble(latitude);
        dest.writeFloat(speed);
        dest.writeLong(time);

    }
}
