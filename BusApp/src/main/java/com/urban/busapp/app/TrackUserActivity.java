package com.urban.busapp.app;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.urban.busapp.app.models.StopPoint;
import com.urban.busapp.app.models.TimeMeasure;
import com.urban.busapp.app.tasks.HttpPostAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class TrackUserActivity extends Activity implements
        GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener,
        LocationListener {

    private final static int
            CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private static final int MILLISECONDS_PER_SECOND = 1000;
    private static final long NANOSECONDS_PER_SECOND = 1000000000;
    public static final int UPDATE_INTERVAL_IN_SECONDS = 2;
    private static final long UPDATE_INTERVAL =
            MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
    private static final int FASTEST_INTERVAL_IN_SECONDS = 1;
    private static final long FASTEST_INTERVAL =
            MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;
    public static final float DISTANCE_THRESHOLD = 30f;
    public static final float DISTANCE_RESET_THRESHOLD = 20f;

    private LocationClient mLocationClient;
    private LocationRequest mLocationRequest;

    private Location mCurrentLocation;
    private Marker now;
    private GoogleMap mMap;
    private List<LatLng> pointsPath;
    private Polyline polyline;
    private ArrayList<StopPoint> allStopPoints;
    private ArrayList<StopPoint> stopPointsUseful;
    private boolean started;
    private ArrayList<TimeMeasure> times;

    private TextView mLatLng;
    private Button locationButton;
    private Button startButton;
    private Button stopButton;
    private Long startTime;
    private Long timeDiff;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(
                LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationClient = new LocationClient(this, this, this);

        pointsPath = new ArrayList<LatLng>();
        times = new ArrayList<TimeMeasure>();
        stopPointsUseful = new ArrayList<StopPoint>();
        Intent intent = getIntent();
        allStopPoints = intent.getParcelableArrayListExtra("points");
        started = false;

        setContentView(R.layout.map_activity);
        locationButton = (Button) findViewById(R.id.mapButton);
        locationButton.setOnClickListener(new OnClickGetLocation(this));
        startButton = (Button) findViewById(R.id.startTrack);
        startButton.setOnClickListener(new OnClickStartTracking());
        stopButton = (Button) findViewById(R.id.stopTrack);
        stopButton.setOnClickListener(new OnClickStopTracking());
        mLatLng = (TextView) findViewById(R.id.lat_lng);
        setUpMapIfNeeded();
    }

    @Override
    public void onStart() {
        super.onStart();
        mLocationClient.connect();
    }

    @Override
    public void onStop() {
        if (servicesConnected()) {
            stopPeriodicUpdates();
        }
        mLocationClient.disconnect();
        super.onStop();
    }

    public void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.locationMap)).getMap();
            if (mMap == null) {
                Toast.makeText(this,
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            } else {
                mMap.setMyLocationEnabled(true);
                PolylineOptions lineOptions = new PolylineOptions()
                        .color(0xE60071C5);
                polyline = mMap.addPolyline(lineOptions);
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        boolean firstTime = false;
        if(now != null)
            now.remove();
        else
            firstTime = true;

        mCurrentLocation = location;

        if(mCurrentLocation != null && mMap != null){
            LatLng latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            pointsPath.add(latLng);
            polyline.setPoints(pointsPath);

            now = mMap.addMarker(new MarkerOptions().position(latLng).visible(false));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            if(firstTime){
                mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));
            }
            if(!started){
                initializeStopPoints(mCurrentLocation);
            } else {
                checkIsClose(mCurrentLocation);
            }
        }
    }

    private void checkIsClose(Location currentLocation) {
        if(stopPointsUseful.size() > 0){
            Location stop = new Location("stop");
            stop.setLatitude(stopPointsUseful.get(0).getPoint().latitude);
            stop.setLongitude(stopPointsUseful.get(0).getPoint().longitude);
            Float distance = currentLocation.distanceTo(stop);
            System.out.println("DistanceUntil: " + distance.toString());
            if(distance < DISTANCE_RESET_THRESHOLD){
                StopPoint stopPoint = stopPointsUseful.remove(0);
                Long timeNow = System.nanoTime();
                if(startTime != null){
                    timeDiff = timeNow - startTime;
                    startTime = timeNow;
                }
                Long timeSeconds = timeDiff/NANOSECONDS_PER_SECOND;
                Toast.makeText(this, "Time: " + timeSeconds.toString(), Toast.LENGTH_SHORT).show();
                TimeMeasure measure = new TimeMeasure(
                        stopPoint.getSegId().longValue(), timeSeconds.intValue());
                times.add(measure);
            }
        }
    }

    private void initializeStopPoints(Location currentLocation) {
        Integer count = 0;
        boolean found = false;
        for(StopPoint pt : allStopPoints){
            Location other = new Location("other");
            other.setLatitude(pt.getPoint().latitude);
            other.setLongitude(pt.getPoint().longitude);
            Float distance = currentLocation.distanceTo(other);
            System.out.println("Distance(" + count.toString() + "): " + distance.toString());
            if(distance < DISTANCE_THRESHOLD){
                found = true;
                break;
            }
            ++count;
        }
        if(found){
            Toast.makeText(this, "Found stop: " + count.toString(), Toast.LENGTH_SHORT).show();
            started = true;
            stopPointsUseful = new ArrayList<StopPoint>(allStopPoints.subList(count+1, allStopPoints.size()));
        }
    }

    private void startPeriodicUpdates() {
        startTime = System.nanoTime();
        mLocationClient.requestLocationUpdates(mLocationRequest, this);
    }

    private void stopPeriodicUpdates() {
        if(startTime != null)
            timeDiff = System.nanoTime() - startTime;
        mLocationClient.removeLocationUpdates(this);
    }

    private void sendMeasuresToServer(){
        String json = TimeMeasure.toJsonArray(times).toString();
        System.out.println("Data:\n" + json + "\n");
        HttpPostAsyncTask task = new HttpPostAsyncTask(this, json);
        String url = "https://bus-estimates.herokuapp.com/busapp/createmeasures/";
        task.execute(url);
    }

    private void sendPointsToServer(){
        Geocoder geocoder;
        geocoder = new Geocoder(this, Locale.getDefault());

        String fromAddress = "";
        String fromName="";
        String toAddress = "";
        String toName="";
        try {
            List<Address> addressFrom;
            addressFrom = geocoder.getFromLocation(
                    pointsPath.get(0).latitude, pointsPath.get(0).longitude, 1);
            String address = addressFrom.get(0).getAddressLine(0);
            String city = addressFrom.get(0).getAddressLine(1);
            String country = addressFrom.get(0).getAddressLine(2);
            fromAddress = address + ", " + city + " - " + country;
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            List<Address> addressTo;
            addressTo = geocoder.getFromLocation(
                    pointsPath.get(pointsPath.size()-1).latitude, pointsPath.get(0).longitude, 1);
            String address = addressTo.get(0).getAddressLine(0);
            String city = addressTo.get(0).getAddressLine(1);
            String country = addressTo.get(0).getAddressLine(2);
            toAddress = address + ", " + city + " - " + country;
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject jsonPoints = new JSONObject();
        try {
            JSONObject stop1 = new JSONObject();
            stop1.put("address", fromAddress);
            stop1.put("name", fromName);
            jsonPoints.put("first_stop", stop1);

            JSONObject stop2 = new JSONObject();
            stop2.put("address", toAddress);
            stop2.put("name", toName);
            jsonPoints.put("second_stop", stop2);

            jsonPoints.put("points", buildArrayPoints());

            Long timeSeconds = timeDiff/NANOSECONDS_PER_SECOND;
            jsonPoints.put("time_measured", timeSeconds.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String json = jsonPoints.toString();
        HttpPostAsyncTask task = new HttpPostAsyncTask(this, json);
        String url = "https://bus-estimates.herokuapp.com/busapp/createsegment/";
        task.execute(url);
    }

    private JSONArray buildArrayPoints(){
        JSONArray points = new JSONArray();
        for(LatLng pt : pointsPath){
            try {
                JSONObject point = new JSONObject();

                NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
                DecimalFormat df = (DecimalFormat)nf;
                df.setMaximumFractionDigits(6);

                point.put("lat", df.format(pt.latitude));
                point.put("lon", df.format(pt.longitude));
                points.put(point);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return points;
    }

    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDisconnected() {
        Toast.makeText(this, "Disconnected. Please re-connect.",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            showErrorDialog(connectionResult.getErrorCode());
        }

    }

    private void showErrorDialog(int errorCode) {

        // Get the error dialog from Google Play services
        Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                errorCode,
                this,
                LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST);

        // If Google Play services can provide an error dialog
        if (errorDialog != null) {

            // Create a new DialogFragment in which to show the error dialog
            ErrorDialogFragment errorFragment = new ErrorDialogFragment();

            // Set the dialog in the DialogFragment
            errorFragment.setDialog(errorDialog);

            // Show the error dialog in the DialogFragment
            errorFragment.show(getFragmentManager(), LocationUtils.APPTAG);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Choose what to do based on the request code
        switch (requestCode) {

            // If the request code matches the code sent in onConnectionFailed
            case LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST :

                switch (resultCode) {
                    // If Google Play services resolved the problem
                    case Activity.RESULT_OK:

                        // Log the result
                        Log.d(LocationUtils.APPTAG, getString(R.string.resolved));

                        break;

                    // If any other result was returned by Google Play services
                    default:
                        // Log the result
                        Log.d(LocationUtils.APPTAG, getString(R.string.no_resolution));

                        break;
                }

                // If any other request code was received
            default:
                // Report that this Activity received an unknown requestCode
                Log.d(LocationUtils.APPTAG,
                        getString(R.string.unknown_activity_request_code, requestCode));

                break;
        }
    }

    private boolean servicesConnected() {
        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.
                        isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates",
                    "Google Play services is available.");
            // Continue
            return true;
            // Google Play services was not available for some reason.
            // resultCode holds the error code.
        } else {
            // Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                    resultCode,
                    this,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);

            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                // Create a new DialogFragment for the error dialog
                ErrorDialogFragment errorFragment =
                        new ErrorDialogFragment();
                // Set the dialog in the DialogFragment
                errorFragment.setDialog(errorDialog);
                // Show the error dialog in the DialogFragment
                errorFragment.show(getFragmentManager(),
                        "Location Updates");
            }
            return false;
        }
    }

    public static class ErrorDialogFragment extends DialogFragment {
        // Global field to contain the error dialog
        private Dialog mDialog;
        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }
        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }
        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }

    public class OnClickGetLocation implements View.OnClickListener {

        private final Activity trackFragActivity;

        public OnClickGetLocation(Activity activity) {
            trackFragActivity = activity;
        }

        @Override
        public void onClick(View v) {
            // If Google Play Services is available
            if (servicesConnected()) {

                // Get the current location
                Location currentLocation = mLocationClient.getLastLocation();
                mCurrentLocation = currentLocation;

                // Display the current location in the UI
                mLatLng.setText(LocationUtils.getLatLng(trackFragActivity, currentLocation));
                Toast.makeText(trackFragActivity, LocationUtils.getLatLng(trackFragActivity, currentLocation),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class OnClickStartTracking implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            startPeriodicUpdates();
        }
    }

    public class OnClickStopTracking implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (servicesConnected()) {
                stopPeriodicUpdates();
//                sendPointsToServer();
                sendMeasuresToServer();
            }
        }
    }

}
