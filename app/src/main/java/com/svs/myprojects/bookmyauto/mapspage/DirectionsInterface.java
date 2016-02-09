package com.svs.myprojects.bookmyauto.mapspage;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by snehalsutar on 1/28/16.
 */
public interface DirectionsInterface {
    public void plotPoints(ArrayList<LatLng> latLngArrayList,
                           String distance, String duration, String directions);
}
