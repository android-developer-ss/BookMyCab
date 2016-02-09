package com.svs.myprojects.bookmyauto;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by snehalsutar on 1/31/16.
 */
public class Constants {
    public static final String APP_UNIQUE_NAME_FOR_SP = "bookmyauto";
    public static final String SELECTED_CAR_INITIAL_COORDINATES = "com.svs.myprojects.bookmyauto.mapspage.car_coordinates";
    public static final String REGISTER_SIGN_IN_TYPE = "REGISTER_SIGN_IN_TYPE";
    public static final String REGISTER_TYPE = "REGISTER_TYPE";
    public static final String SIGN_IN_TYPE = "SIGN_IN_TYPE";
    public static final String ADDRESS_TYPE = "SOURCE SELECTED";
    public static final String SOURCE_SELECTED = "SOURCE SELECTED";
    public static final String DESTINATION_SELECTED = "DESTINATION SELECTED";
    public static final String ADDRESS_TO_COORDINATES = "address_to_coordinates";
    public static final String COORDINATES_TO_ADDRESS = "coordinates_to_address";
    public static final String SET_MULTIPLE_OPTIONS = "set_multiple_options";

    public static final String AVOID_TOLLS_STRING = "tolls";
    public static final String AVOID_FERRIES_STRING = "ferries";
    public static final String AVOID_HIGHWAYS_STRING = "highways";


    public static final String DRIVING = "driving";
    public static final String WALKING = "walking";
    public static final String TRANSIT = "transit";
    public static final String BICYCLING = "bicycling";


    public static final String YES = "yes";
    public static final String NO = "no";

    static List<LatLng> cabPath = new ArrayList<>();

    public static List<LatLng> getCabPath() {
        cabPath.add(new LatLng(41.921323, -88.271696));
        cabPath.add(new LatLng(41.921158, -88.273083));
        cabPath.add(new LatLng(41.921162,-88.2763447));
        cabPath.add(new LatLng(41.921063, -88.276387));
        cabPath.add(new LatLng(41.921286, -88.277761));
        cabPath.add(new LatLng(41.921127, -88.278876));
        cabPath.add(new LatLng(41.921286, -88.280507));
        cabPath.add(new LatLng(41.921222, -88.282224));
        cabPath.add(new LatLng(41.920999, -88.283597));
        cabPath.add(new LatLng(41.920903, -88.284970));
        cabPath.add(new LatLng(41.920711, -88.285657));
        cabPath.add(new LatLng(41.920137, -88.286987));
        cabPath.add(new LatLng(41.919817, -88.287931));
        cabPath.add(new LatLng(41.919530, -88.288790));
        cabPath.add(new LatLng(41.919179, -88.289734));
        cabPath.add(new LatLng(41.918827, -88.290764));
        cabPath.add(new LatLng(41.918604, -88.291837));
        cabPath.add(new LatLng(41.918285, -88.292910));
        cabPath.add(new LatLng(41.918029, -88.294025));
        cabPath.add(new LatLng(41.917742, -88.295313));
        return cabPath;
    }
}
