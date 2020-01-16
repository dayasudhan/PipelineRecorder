package com.kuruvatech.pipelinerecorder.fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;

import android.location.LocationManager;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Looper;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;

import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.kuruvatech.pipelinerecorder.R;
import com.kuruvatech.pipelinerecorder.model.location;
import com.kuruvatech.pipelinerecorder.utils.Constants;
import com.kuruvatech.pipelinerecorder.utils.GPSTracker;
import com.kuruvatech.pipelinerecorder.utils.PermissionUtils;
import com.kuruvatech.pipelinerecorder.utils.SessionManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.os.Build;
import android.widget.Toast;
import android.widget.ToggleButton;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;


public class MainFragment extends Fragment  implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnMyLocationButtonClickListener,
        SeekBar.OnSeekBarChangeListener,GoogleMap.OnMapClickListener,
        AdapterView.OnItemSelectedListener{


    View rootview;
    Button popupSubmitBtn;
    EditText popupName,popupPhone,popupType,popupSize,popopuPurpose,popupRemarks;
    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int LREQUEST_CODE_ASK_PERMISSIONS = 123;
    // City locations for mutable polyline.

    private static final LatLng KURUVA = new LatLng(14.142235317478407, 75.66676855087282);


    private long UPDATE_INTERVAL = 2 * 1000;  /* 4 secs */
    private long FASTEST_INTERVAL = 1000; /* 2 sec */


    private static final int PATTERN_DASH_LENGTH_PX = 50;
    private static final int PATTERN_GAP_LENGTH_PX = 20;
    private static final Dot DOT = new Dot();
    private static final Dash DASH = new Dash(PATTERN_DASH_LENGTH_PX);
    private static final Gap GAP = new Gap(PATTERN_GAP_LENGTH_PX);
   private static final List<PatternItem> PATTERN_MIXED = Arrays.asList(DOT, GAP, DOT, DASH, GAP);
    float mLinewidth =(float)5.0;
    private Polyline mMutablePolyline;
    private PolylineOptions mPolylineOptions ;

  //  private CheckBox mClickabilityCheckbox;
    SupportMapFragment mFragment;
    FragmentManager fragmentManager;
    private GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    Marker mCurrLocationMarker;
    private LatLng mSelectedLatlang;
    Location lastLocationloc=null;
    int zoomleval = 15;
    LocationManager locationManager =  null;
    // These are the options for polyline caps, joints and patterns. We use their
    // string resource IDs as identifiers.
    private GoogleMap mMap;
    private GPSTracker gps;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationcallback;
    private int locationRequestCode = 1000;
    JSONArray mJsonArray = new JSONArray();
    boolean mIsStartPipeLine =false;
    private ToggleButton togglePlayButton, togglePauseButton;
    SessionManager mSession;
    Gson gson ;
    private Button maptypeButton;
    ViewGroup mContainer;
    AlertDialog alertDialog;
    boolean pause =false;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContainer = container;
        rootview = inflater.inflate(R.layout.polyline_demo, container, false);
        fragmentManager=getChildFragmentManager();
        mSession = new SessionManager(getContext());
        //+String email = mSession.getEmail();
        gson = new Gson();
        ///
       // submitDetails();
       // showCustomViewAlertDialog();
     //   mClickabilityCheckbox = (CheckBox) rootview.findViewById(R.id.toggleClickability);
        maptypeButton = (Button) rootview.findViewById(R.id.maptypebutton);
        maptypeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int t = mMap.getMapType();
                t = (t + 1) % 5;
                if(t == 0)
                {
                    t++;
                }
                mMap.setMapType(t);
                //Toast.makeText(getContext(), mMap.getMapType().toString(), Toast.LENGTH_SHORT).show();

                // GoogleMap.MAP_TYPE_NONE
            }
        });
        togglePlayButton = (ToggleButton) rootview.findViewById(R.id.togglebutton);
        togglePauseButton = (ToggleButton) rootview.findViewById(R.id.togglebutton2);
        togglePlayButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                startLocationUpdates(isChecked,pause);
                if(isChecked) {
                    togglePauseButton.setVisibility(View.VISIBLE);
                }
                else
                {
                    togglePauseButton.setVisibility(View.INVISIBLE);
                }

            }
        });
        togglePauseButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                startLocationUpdates(true,isChecked);
                //startLocationUpdates(isChecked);
               // togglePauseButton.setVisibility(View.INVISIBLE);

            }
        });

        mFragment = (SupportMapFragment)fragmentManager.findFragmentById(R.id.map);
        mFragment.getMapAsync(this);
        return rootview;
    }
    private static boolean validatePhoneNumber(String phoneNo)
    {
        if (phoneNo.matches("\\d{10}"))
            return true;
        else if(phoneNo.matches("\\+\\d{12}")) return true;
        else return false;
    }

    private void submitDetails()
    {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setTitle("Customer Details");

                final View loginFormView = getLayoutInflater().inflate(R.layout.popup, null);
                builder.setView(loginFormView);

                Button registerButton = (Button)loginFormView.findViewById(R.id.btn_send);
                registerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            popupSubmitBtn = (Button) loginFormView.findViewById(R.id.btn_send);
                            popupName=(EditText)loginFormView.findViewById(R.id.input_name);
                            popupPhone=(EditText)loginFormView.findViewById(R.id.input_phone);
                            popupType=(EditText)loginFormView.findViewById(R.id.input_type);
                            popopuPurpose=(EditText)loginFormView.findViewById(R.id.input_purpose);
                            popupSize=(EditText)loginFormView.findViewById(R.id.input_size);
                            popupRemarks=(EditText)loginFormView.findViewById(R.id.input_remarks);
                            if (popupName.getText().toString().matches("")) {
                                //Toast.makeText(getContext(), "Please Enter Customer Name", Toast.LENGTH_SHORT).show();
                                alertMessage("Please Enter Customer Name");
                                return;
                            }
                            if (!validatePhoneNumber(popupPhone.getText().toString())) {
                                //Toast.makeText(getContext(), "Please Enter Valid Customer Phone Number", Toast.LENGTH_SHORT).show();
                                alertMessage("Enter Valid Phone Number");
                                return;
                            }
                            else
                            {
                                alertDialog.cancel();
                                postPipelineWithCoordinates();
                            }
                        }catch(Exception ex)
                        {
                            ex.printStackTrace();
                        }
                    }
                });

                builder.setCancelable(true);
                alertDialog = builder.create();
                alertDialog.show();

    }
//    public void submitDetails()
//    {
//
//        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
//        View customView = layoutInflater.inflate(R.layout.popup,null);
//
//        popupSubmitBtn = (Button) customView.findViewById(R.id.btn_send);
//        popupName=(EditText)customView.findViewById(R.id.input_name);
//        popupPhone=(EditText)customView.findViewById(R.id.input_phone);
//        //instantiate popup window
//        final PopupWindow popupWindow = new PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//
//        //display the popup window
//        popupWindow.showAtLocation(rootview, Gravity.CENTER, 0, 0);
//        popupName.setText("yadhuveer");
//        showKeyboard();
//
//        //close the popup window on button click
//        popupSubmitBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (popupName.getText().toString().matches("")) {
//                    //Toast.makeText(getContext(), "Please Enter Customer Name", Toast.LENGTH_SHORT).show();
//                    alertMessage("Please Enter Customer Name");
//                    return;
//                }
//                if (!validatePhoneNumber(popupPhone.getText().toString())) {
//                    //Toast.makeText(getContext(), "Please Enter Valid Customer Phone Number", Toast.LENGTH_SHORT).show();
//                    alertMessage("Enter Valid Phone Number");
//                    return;
//                }
//                else
//                {
//
//                    popupWindow.dismiss();
//                   // postPipelineWithCoordinates();
//                }
//
////                if (!validatePhoneNumber(editPhone.getText().toString())) {
////                    alertMessage(false,"Enter Valid Phone Number");
////                }
//            }
//        });
//// //
//    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onMyLocationButtonClick() {
        //   Toast.makeText(getContext(), "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        getCurrentLocation();
        return false;
    }

    //and then register for location
    @Override
    public void onMapReady(GoogleMap map) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        mIsStartPipeLine =false;
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission((AppCompatActivity) getActivity(), LREQUEST_CODE_ASK_PERMISSIONS,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, true);

        }
        //  savetofile();
        mMap =  map;
        mLocationcallback = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {

                // do work here
                onLocationChanged(locationResult.getLastLocation());
            }
        };
        map.setContentDescription(getString(R.string.polyline_demo_description));
        mPolylineOptions = new PolylineOptions()
                .color(Color.RED)
                .width(mLinewidth)
                .clickable(true);
        mMutablePolyline = map.addPolyline(mPolylineOptions);
        mMutablePolyline.setWidth(mLinewidth);
        mMutablePolyline.setPattern(PATTERN_MIXED);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(KURUVA, 18));
        map.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
            @Override
            public void onPolylineClick(Polyline polyline) {
                // Flip the values of the red, green and blue components of the polyline's color.
                polyline.setColor(polyline.getColor() ^ 0x00ffffff);
            }
        });
        enableMyLocation();
        map.setOnMyLocationButtonClickListener(this);
        map.setOnMapClickListener(this);
    }

    private void enableMyLocation() {
       if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            PermissionUtils.requestPermission((AppCompatActivity) getActivity(), LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
            buildGoogleApiClient();
            //  Toast.makeText(getContext(), "enableMyLocation startLocationUpdates ", Toast.LENGTH_SHORT).show();
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
       // Don't do anything here.
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // Don't do anything here.
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // Don't do anything here.
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (mMutablePolyline == null) {
            return;
        }
    }

//    public void toggleClickability(View view) {
//        if (mMutablePolyline != null) {
//            mMutablePolyline.setClickable(((CheckBox) view).isChecked());
//        }
//    }

    public void getCurrentLocation()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            }
        }
        if(mLastLocation!=null) {
            //Toast.makeText(getContext(), "MyLocation button clicked 2", Toast.LENGTH_SHORT).show();
            setPosition(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
        }
        else
        {
            // Toast.makeText(getContext(), "MyLocation button clicked 3", Toast.LENGTH_SHORT).show();
        }


    }
    private void setPosition(LatLng latLng)
    {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        mCurrLocationMarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(zoomleval));
        mSelectedLatlang = latLng;
    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();//kasturbainsurance@gmail.com
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }
    // Trigger new location updates at interval
    protected void startLocationUpdates(boolean IsStartStop,boolean IsPauseResume) {
//        if(IsStartStop) {
//            Toast.makeText(getContext(), "startLocationUpdates 1 true", Toast.LENGTH_SHORT).show();
//        }
//        else
//        {
//            Toast.makeText(getContext(), "startLocationUpdates 1 false", Toast.LENGTH_SHORT).show();
//        }
        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(getActivity());
        settingsClient.checkLocationSettings(locationSettingsRequest);
        Toast.makeText(getContext(), "startLocationUpdates 2", Toast.LENGTH_SHORT).show();
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //Toast.makeText(getContext(), "startLocationUpdates 3", Toast.LENGTH_SHORT).show();
            // Permission to access the location is missing.
            PermissionUtils.requestPermission((AppCompatActivity) getActivity(), LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
            // new Google API SDK v11 uses getFusedLocationProviderClient(this)
            if(IsStartStop == true) {
                Toast.makeText(getContext(), "startLocationUpdates 31", Toast.LENGTH_SHORT).show();
                getFusedLocationProviderClient(getActivity()).requestLocationUpdates(mLocationRequest, mLocationcallback,
                        Looper.myLooper());


            }
            else
            {
                Toast.makeText(getContext(), "startLocationUpdates 32", Toast.LENGTH_SHORT).show();
                getFusedLocationProviderClient(getActivity()).requestLocationUpdates(mLocationRequest, null,
                        null);
            }
        }
        else
        {
            mMap.setMyLocationEnabled(true);
            //  Toast.makeText(getContext(), "startLocationUpdates 4", Toast.LENGTH_SHORT).show();
            if(IsStartStop == true) {
                Toast.makeText(getContext(), "startLocationUpdates 41", Toast.LENGTH_SHORT).show();

                if(IsPauseResume == false) {
                    Toast.makeText(getContext(), "IsPauseResume == false", Toast.LENGTH_SHORT).show();
                    getFusedLocationProviderClient(getActivity()).requestLocationUpdates(mLocationRequest, mLocationcallback,
                            null);
                }
                else
                {
                    Toast.makeText(getContext(), "IsPauseResume == true", Toast.LENGTH_SHORT).show();
                    getFusedLocationProviderClient(getActivity()).removeLocationUpdates( mLocationcallback);
                }
                //savetodb();
            }
            else if(IsStartStop == false)
            {
                Toast.makeText(getContext(), "stopped recording pipeline", Toast.LENGTH_SHORT).show();
                getFusedLocationProviderClient(getActivity()).removeLocationUpdates(mLocationcallback);
                // savetofile();
                // savetofirebasestorage();
//                savetodb();
                //postPipelineWithCoordinates();
                submitDetails();
               // Toast.makeText(getContext(), "startLocationUpdates 43", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onLocationChanged(Location location) {
        // New location has now been determined
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        JSONObject loc = new JSONObject();
        try {
            loc.put("Latitude", Double.toString(location.getLatitude()));
            loc.put("Longitude", Double.toString(location.getLongitude()));

            mJsonArray.put(loc);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mPolylineOptions = mPolylineOptions.add(latLng);

        mMutablePolyline = mMap.addPolyline(mPolylineOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(getActivity())
                        .setTitle("Permission Required")
                        .setMessage("This permission was denied earlier by you. This permission is required to get your location. So, in order to use this feature please allow this permission by clicking ok.")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        LOCATION_PERMISSION_REQUEST_CODE);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
            return false;
        } else {
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        int off = 0;
                        try {
                            off = Settings.Secure.getInt(getActivity().getContentResolver(), Settings.Secure.LOCATION_MODE);
                        } catch (Settings.SettingNotFoundException e) {
                            e.printStackTrace();
                        }
                        if(off==0){
                            Intent onGPS = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(onGPS);
                        }
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);

                    }

                } else {

                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
    public void postPipelineWithCoordinates()
    {
        ArrayList<LatLng> list = new ArrayList<LatLng>();
//        LatLng l1 = new LatLng(15.138796,75.6604763);
//        LatLng l2 = new LatLng(15.1194829,75.6511871);
//        LatLng l3 = new LatLng(14.138796,75.5604763);
//        list.add(l1);
//        list.add(l2);
//        list.add(l3);
//        JSONObject obj21 = new JSONObject();
//        JSONObject obj31 = new JSONObject();
//        try{
//            obj21.put("Latitude", Double.toString(15.9808098));
//            obj21.put("Longitude", Double.toString(75.980980980));
//            obj31.put("Latitude", Double.toString(14.9808098));
//            obj31.put("Longitude", Double.toString(17.980980980));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        mJsonArray.put(obj21);
//        mJsonArray.put(obj31);
        for(int i = 0; i < mJsonArray.length();i++) {
            try {

                LatLng l = new LatLng(mJsonArray.getJSONObject(i).getDouble("Latitude"),
                        mJsonArray.getJSONObject(i).getDouble("Longitude"));
                list.add(l);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        location loc= new location();
        loc.setCoordinates(list);
        loc.setType("Line");
        loc.setName(popupName.getText().toString());
        loc.setPhone(popupPhone.getText().toString());
        loc.setVendorusername(mSession.getEmail());
        loc.setPipe_type(popupType.getText().toString());
        loc.setPurpose(popopuPurpose.getText().toString());
        loc.setSizeofpipeline(popupSize.getText().toString());
        loc.setRemarks(popupRemarks.getText().toString());
        Gson gson = new Gson();
        String strOrder = gson.toJson(loc);
        new PostJSONAsyncTask().execute(Constants.POST_PIPELINE_URL,strOrder);
    }

    public  class PostJSONAsyncTask extends AsyncTask<String, Void, Boolean> {
        Dialog dialog;
        public  PostJSONAsyncTask()
        {
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new Dialog(getActivity(),android.R.style.Theme_Translucent);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.custom_progress_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.show();
            dialog.setCancelable(true);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {
                ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                HttpPost request = new HttpPost(urls[0]);
                HttpClient httpclient = new DefaultHttpClient();
               // UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
                StringEntity se = new StringEntity(urls[1]);

                request.setEntity(se);
                request.setHeader("Accept", "application/json");
                request.setHeader("Content-type", "application/json");
                request.setHeader(Constants.SECUREKEY_KEY, Constants.SECUREKEY_VALUE);
                request.setHeader(Constants.VERSION_KEY, Constants.VERSION_VALUE);
                request.setHeader(Constants.CLIENT_KEY, Constants.CLIENT_VALUE);
                HttpResponse response = httpclient.execute(request);

                int status = response.getStatusLine().getStatusCode();
                if (status == 200) {
                    HttpEntity entity = response.getEntity();

                    String responseOrder = EntityUtils.toString(entity);

                    return true;
                }
            } catch (ParseException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
        protected void onPostExecute(Boolean result) {

            if ((dialog != null) && dialog.isShowing()) {
                dialog.cancel();
            }
            if(result == true){
                alertMessage("Success");
            }
            else if (result == false)
                Toast.makeText(getContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();
        }
    }
    public void alertMessage(String message) {
        DialogInterface.OnClickListener dialogClickListeneryesno = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {

                    case DialogInterface.BUTTON_NEUTRAL:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("PipeLine");
        builder.setMessage(message).setNeutralButton("Ok", dialogClickListeneryesno)
                .setIcon(R.drawable.ic_launcher_background);
        final AlertDialog dialog = builder.create();
        dialog.show(); //show() should be called before dialog.getButton().
        final Button neutralButton = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);
        LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) neutralButton.getLayoutParams();
        positiveButtonLL.gravity = Gravity.CENTER;
        neutralButton.setLayoutParams(positiveButtonLL);
    }
}
