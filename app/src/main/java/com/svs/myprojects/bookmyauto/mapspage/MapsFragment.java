package com.svs.myprojects.bookmyauto.mapspage;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.ui.IconGenerator;
import com.svs.myprojects.bookmyauto.Constants;
import com.svs.myprojects.bookmyauto.CustomDialog;
import com.svs.myprojects.bookmyauto.R;

import java.util.ArrayList;
import java.util.List;

public class MapsFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener, GoogleMap.OnMarkerDragListener, View.OnClickListener,
        DirectionsInterface, LocationListener, CustomDialog.CustomDialogListener,
        CoordinateToAddressInterface, AddressToCoorInterface, GoogleMap.OnMarkerClickListener {

    /***********************************************************************************************
     * STATIC AND MEMBER VARIABLE DECLARATIONS
     */
    private static final LatLng RJT = new LatLng(41.917742, -88.295313);//new LatLng(41.9166844, -88.2753049);
    private static final String LOG_TAG = "SVS_me";
    private GoogleMap mGoogleMap;
    static LatLng CURRENT;// = new LatLng(53.558, 9.927);

    Context mContext;
    Polyline mPolylinePath = null;
    Button mSourceButton, mDestinationButton, mEstimateButton, mRouteOptionsButton, mShowCabsButton;
    Button mAnimateCar, mShareDirections;
    Marker mSourceMarker = null, mDestinationMarker = null, mCabMarker = null;
    LatLng mDestinationLatLng;
    LocationManager locationManager;
    Location location;
    String stringProvider;
    String mAddressTypeFromDialog = Constants.SOURCE_SELECTED;
    String mDistance;
    String mDirectionsToShare;
    View mContainer;
    MapsToCabInterface mapsToCabInterface;
    static int count = 0;


    /***********************************************************************************************
     * CONSTRUCTOR
     */
    public MapsFragment() {
    }

    /***********************************************************************************************
     * ON CREATE
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /***********************************************************************************************
     * ON CREATE VIEW
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);

        mContainer = rootView;

        getHandlers(rootView);


//        AdView adView = (AdView) rootView.findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().setRequestAgent("android_studio:ad_template").build();
//        adView.loadAd(adRequest);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mContext = getActivity();
        locationManager = (LocationManager) mContext.getSystemService(mContext.LOCATION_SERVICE);
        boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // Define the criteria how to select the location provider -> use default
        Criteria criteria = new Criteria();
        stringProvider = locationManager.getBestProvider(criteria, enabled);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        location = locationManager.getLastKnownLocation(stringProvider);
        if (location != null) {
            onLocationChanged(location);
        }

    }

    public void getHandlers(View view) {
        mDestinationButton = (Button) view.findViewById(R.id.destination_button);
        mSourceButton = (Button) view.findViewById(R.id.source_button);
        mRouteOptionsButton = (Button) view.findViewById(R.id.route_options_button);
        mEstimateButton = (Button) view.findViewById(R.id.estimate_button);
        mShowCabsButton = (Button) view.findViewById(R.id.show_cabs_button);
        mShareDirections = (Button) view.findViewById(R.id.share_directions);

        mDestinationButton.setOnClickListener(this);
        mSourceButton.setOnClickListener(this);
        mEstimateButton.setOnClickListener(this);
        mRouteOptionsButton.setOnClickListener(this);
        mShowCabsButton.setOnClickListener(this);
        mShareDirections.setOnClickListener(this);


        mAnimateCar = (Button) view.findViewById(R.id.animate_car);
        mAnimateCar.setOnClickListener(this);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mapsToCabInterface = (MapsToCabInterface) getActivity();
        // Register to receive messages.
        // We are registering an observer (mMessageReceiver) to receive Intents
        // with actions named "custom-event-name".
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mCabInitialCoordinateRcvr,
                new IntentFilter(Constants.SELECTED_CAR_INITIAL_COORDINATES));
    }

    // Our handler for received Intents. This will be called whenever an Intent
// with an action named "custom-event-name" is broadcasted.
    private BroadcastReceiver mCabInitialCoordinateRcvr = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            LatLng latLng = new LatLng(41.921323, -88.271696);
            Log.d("receiver", "Got message: " + message);

            mCabMarker = mGoogleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("Cab Number")
                    .snippet("I am coming")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));
            mCabMarker.showInfoWindow();
            List<LatLng> markerList = new ArrayList<>();

            markerList.add(mSourceMarker.getPosition());


//            LatLngBounds.Builder builder = new LatLngBounds.Builder();
//            builder.include(mCabMarker.getPosition());
//            builder.include(mSourceMarker.getPosition());
            if (mDestinationMarker != null) {
//                builder.include(mDestinationMarker.getPosition());
                markerList.add(mDestinationMarker.getPosition());
            }

            markerList.add(mCabMarker.getPosition());
            centerIncidentRouteOnMap(markerList);
//            SystemClock.sleep(3000);
//            animateCamera(mCabMarker.getPosition(), bearingBetweenLatLngs(mSourceMarker.getPosition(), mCabMarker.getPosition()));
//            LatLngBounds bounds = builder.build();
//            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 0);
//            mGoogleMap.moveCamera(cu);
//            mGoogleMap.animateCamera(cu);


//            mCabMarker.
//

        }
    };

    public void centerIncidentRouteOnMap(List<LatLng> copiedPoints) {
        double minLat = Integer.MAX_VALUE;
        double maxLat = Integer.MIN_VALUE;
        double minLon = Integer.MAX_VALUE;
        double maxLon = Integer.MIN_VALUE;
        for (LatLng point : copiedPoints) {
            maxLat = Math.max(point.latitude, maxLat);
            minLat = Math.min(point.latitude, minLat);
            maxLon = Math.max(point.longitude, maxLon);
            minLon = Math.min(point.longitude, minLon);
        }
        final LatLngBounds bounds = new LatLngBounds.Builder().include(new LatLng(maxLat, maxLon)).include(new LatLng(minLat, minLon)).build();
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 250));
    }

    //    private float bearingBetweenLatLngs(LatLng beginLatLng,LatLng endLatLng) {
//        Location beginLocation = convertLatLngToLocation(beginLatLng);
//        Location endLocation = convertLatLngToLocation(endLatLng);
//        return beginLocation.bearingTo(endLocation);
//    }
//    private Location convertLatLngToLocation(LatLng latLng) {
//        Location location = new Location("someLoc");
//        location.setLatitude(latLng.latitude);
//        location.setLongitude(latLng.longitude);
//        return location;
//    }
    @Override
    public void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mCabInitialCoordinateRcvr);
        super.onDestroy();
    }

    /***********************************************************************************************
     * BUTTON CLICK LISTENERS
     */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.source_button:
                callCustomDialog(Constants.SOURCE_SELECTED, null);
                break;
            case R.id.destination_button:
                callCustomDialog(Constants.DESTINATION_SELECTED, null);
                break;
            case R.id.estimate_button:
                estimate_button_function();
                break;
            case R.id.route_options_button:
                callCustomDialog(null, getResources().getStringArray(R.array.route_options_array));
                break;
            case R.id.show_cabs_button:
                mapsToCabInterface.communicate();
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.replace(R.id.fragment_map_container,new CabsListFragment())
//                        .addToBackStack("cabs_list")
//                        .commit();
//                Log.i(LOG_TAG, "cabs_list");
                break;
            case R.id.animate_car:
                animateCar(count);
                count++;
                break;
            case R.id.share_directions:
                callShareIntent();
                break;
        }
    }

    private void callShareIntent() {
        Intent intent = createShareForecastIntent();
        startActivity(intent);
    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mDirectionsToShare);
        return shareIntent;
    }


    public void animateCar(int position) {
        if (mCabMarker == null) {
            Toast.makeText(mContext, "Please select a Cab First", Toast.LENGTH_LONG).show();
            return;
        }
        List<LatLng> cabPath = Constants.getCabPath();
        if (position > cabPath.size()) {
            count = 0;
            position = 0;
        }
//        for (int i = 0; i < cabPath.size(); i++) {
        mCabMarker.remove();
        mCabMarker = mGoogleMap.addMarker(new MarkerOptions()
                .position(cabPath.get(position))
                .title("My Place")
                .snippet("I am coming")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));
        mCabMarker.showInfoWindow();
//                mCabMarker.setPosition(cabPath.get(i));
//                mCabMarker.
//                SystemClock.sleep(1000);
//                try {
//                    this.wait(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
        Log.i(LOG_TAG, mCabMarker.getPosition() + "");

//        }
    }

    public void callCustomDialog(String addressType, String options[]) {
        CustomDialog customDialog = new CustomDialog();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ADDRESS_TYPE, addressType);
        if (options != null) {
            bundle.putStringArray(Constants.SET_MULTIPLE_OPTIONS, options);
        }
        customDialog.setArguments(bundle);
        customDialog.setTargetFragment(this, 1);
        customDialog.show(getChildFragmentManager(), "location");
    }

    public void estimate_button_function() {
        if (mSourceMarker != null && mDestinationMarker != null && mDistance != null) {
            String cost = mDistance.replace("mi", "");
            cost = cost.replace("ft", "");
            float distanceInMiles = Float.parseFloat(cost);
            float costAsPerMiles = distanceInMiles * 2.5f;
            createDialog("Fare Estimation", "Your estimated fare is : " + costAsPerMiles + "$");
        } else {
            createDialog("Fare Estimation", "Please select your source and destination first.");

        }
    }

    /***********************************************************************************************
     * ON MAP READY
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setOnMarkerDragListener(this);
        mGoogleMap.setOnMapClickListener(this);

        // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
        if (CURRENT == null) {
            CURRENT = RJT;
        }
        animateCamera(CURRENT, 90);
        MarkerOptions markerOptions = getMarkerOptions(CURRENT);
        mSourceMarker = addAddressMarker(markerOptions, IconGenerator.STYLE_RED, Constants.SOURCE_SELECTED);
    }

    public void animateCamera(LatLng latLng, float bearing) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)      // Sets the center of the map to Mountain View
                .zoom(17)                   // Sets the zoom 21 is max
                .bearing(bearing)           //90     // Sets the orientation of the camera to east
                .tilt(65)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private String convertToString(LatLng latlng) {
        return latlng.latitude + "," + latlng.longitude;
    }

    /***********************************************************************************************
     * MAP CLICK LISTENER
     * Set destination marker by clicking on the map. It is set to be draggable to reselect the
     * exact position of destination address.
     */
    @Override
    public void onMapClick(LatLng latLng) {
        MarkerOptions markerOptions = getMarkerOptions(latLng);
        // Animating to the touched position
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        mDestinationMarker = addAddressMarker(markerOptions, IconGenerator.STYLE_GREEN, Constants.DESTINATION_SELECTED);
        mDestinationLatLng = latLng;
        mGoogleMap.setOnMapClickListener(null);
        getPath(null);
    }

    public MarkerOptions getMarkerOptions(LatLng latLng) {
        // Creating a marker
        MarkerOptions markerOptions = new MarkerOptions();
        // Setting the position for the marker
        markerOptions.position(latLng);
        // Setting the title for the marker. This will be displayed on taping the marker
        markerOptions.title(latLng.latitude + " : " + latLng.longitude);
        markerOptions.draggable(true);
        return markerOptions;
    }

    public Marker addAddressMarker(MarkerOptions markerOptions, int style, String markerDisplayText) {
        IconGenerator iconFactory = new IconGenerator(mContext);
        iconFactory.setStyle(style);
        iconFactory.setContentRotation(-90);
//            iconFactory.setRotation(90);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(markerDisplayText)));
        markerOptions.alpha(0.5f);
        markerOptions.anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());
        get_address_in_async_task(markerOptions.getPosition());
        // Placing a marker on the touched position
        Marker marker = mGoogleMap.addMarker(markerOptions);

        return marker;
    }

    /***********************************************************************************************
     * Get path in asynchronous task and values will then be plotted on map by callback method plotPoints
     */
    public void getPath(String routeOptions) {
        if (mSourceMarker != null && mDestinationMarker != null) {
            GetDirectionsInAsyncTask getDirectionsInAsyncTask = new GetDirectionsInAsyncTask(this);
            getDirectionsInAsyncTask.execute(convertToString(mSourceMarker.getPosition()),
                    convertToString(mDestinationMarker.getPosition()), Constants.DRIVING,
                    routeOptions);
        }
    }

    /***********************************************************************************************
     * DirectionsInterface implemented methods
     * Plot the path from source to destination on map.
     */
    @Override
    public void plotPoints(ArrayList<LatLng> latLngArrayList, String distance, String duration, String directions) {
        PolylineOptions polylineOptions = new PolylineOptions();
        if (latLngArrayList != null) {
            // Create polyline options with existing LatLng ArrayList
            polylineOptions.addAll(latLngArrayList);
            polylineOptions
                    .width(20)
                    .color(Color.argb(150, 34, 139, 34));// new Color(50,205,50)),
            // Adding multiple points in map using polyline and arraylist
            mPolylinePath = mGoogleMap.addPolyline(polylineOptions);
        }
        mDistance = distance;
        mDirectionsToShare = directions;
    }


    /***********************************************************************************************
     * Location Listener methods.
     */
    @Override
    public void onLocationChanged(Location location) {
        double latitude, longitude;
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        CURRENT = new LatLng(latitude, longitude);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    /**********************************************************************************************/
    public void get_address_in_async_task(LatLng latLng) {
        GeoCodingAsyncTask geoCodingAsyncTask = new GeoCodingAsyncTask(Constants.COORDINATES_TO_ADDRESS, this);
        geoCodingAsyncTask.execute(latLng.latitude + "," + latLng.longitude);
    }

    public void get_latlng_in_async_task(String address) {
        GeoCodingAsyncTask geoCodingAsyncTask = new GeoCodingAsyncTask(Constants.ADDRESS_TO_COORDINATES, this);
        geoCodingAsyncTask.execute(address);
    }

    @Override
    public void setTextAddress(String string) {
        Toast.makeText(mContext, string, Toast.LENGTH_LONG).show();
    }

    /***********************************************************************************************
     * On Marker Dragged methods.
     */
    @Override
    public void onMarkerDragStart(Marker marker) {
        if (mPolylinePath != null) {
            mPolylinePath.remove();
        }
    }

    @Override
    public void onMarkerDrag(Marker marker) {
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        get_address_in_async_task(marker.getPosition());
        getPath(null);
    }

    /**********************************************************************************************/
    public void createDialog(String title, String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.DialogBoxStyle);
        // Add the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        builder.setTitle(title);
        builder.setMessage(content);
        // Set other dialog properties
        //        ...

        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onDialogPositiveClick(String address, String addressType, String selectedItems) {
        if (selectedItems == null) {
            get_latlng_in_async_task(address);
            mAddressTypeFromDialog = addressType;
        } else {
            getPath(selectedItems);
        }
    }

    @Override
    public void setCoordinateText(LatLng latLng) {
        if (mAddressTypeFromDialog.equals(Constants.SOURCE_SELECTED)) {
            if (mSourceMarker != null) {
                mSourceMarker.remove();
            }
            if (mPolylinePath != null)
                mPolylinePath.remove();
            MarkerOptions markerOptions = getMarkerOptions(latLng);
            mSourceMarker = addAddressMarker(markerOptions, IconGenerator.STYLE_RED, Constants.SOURCE_SELECTED);


        } else {
            if (mDestinationMarker != null) {
                mDestinationMarker.remove();
            }
            MarkerOptions markerOptions = getMarkerOptions(latLng);
            mDestinationMarker = addAddressMarker(markerOptions, IconGenerator.STYLE_GREEN, Constants.DESTINATION_SELECTED);
            if (mPolylinePath != null)
                mPolylinePath.remove();
            getPath(null);
        }

        Log.i(LOG_TAG, "lat_lng" + latLng.latitude + "  " + latLng.longitude);
        animateCamera(latLng, 90);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        return false;
    }
    /**********************************************************************************************/
}
