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
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.Gson;
import com.kuruvatech.pipelinerecorder.FullScreenViewActivity;
import com.kuruvatech.pipelinerecorder.R;
import com.kuruvatech.pipelinerecorder.SingleViewActivity;
import com.kuruvatech.pipelinerecorder.model.GeoPoint;
import com.kuruvatech.pipelinerecorder.model.location;
import com.kuruvatech.pipelinerecorder.utils.Constants;
import com.kuruvatech.pipelinerecorder.utils.GPSTracker;
import com.kuruvatech.pipelinerecorder.utils.PermissionUtils;
import com.kuruvatech.pipelinerecorder.utils.SessionManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import android.net.ParseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


/**
 * Created by dayas on 05-08-2019.
 */

public class SingleLineFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMapClickListener, GoogleMap.OnCameraIdleListener,GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener,
        AdapterView.OnItemSelectedListener{

    View rootview;

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int LREQUEST_CODE_ASK_PERMISSIONS = 123;
    // City locations for mutable polyline.

    private static final LatLng KURUVA = new LatLng(14.142235317478407, 75.66676855087282);


    // Airport locations for geodesic polyline.

    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */


    private static final String LOCATIONS = "Locations";
    private static final String LATITUDE = "Lat";
    private static final String LONGITUDE = "Logt";
    private static final int PATTERN_DASH_LENGTH_PX = 50;
    private static final int PATTERN_GAP_LENGTH_PX = 20;
    private static final Dot DOT = new Dot();
    private static final Dash DASH = new Dash(PATTERN_DASH_LENGTH_PX);
    private static final Gap GAP = new Gap(PATTERN_GAP_LENGTH_PX);
    private static final List<PatternItem> PATTERN_DOTTED = Arrays.asList(DOT, GAP);
    private static final List<PatternItem> PATTERN_DASHED = Arrays.asList(DASH, GAP);
    private static final List<PatternItem> PATTERN_MIXED = Arrays.asList(DOT, GAP, DOT, DASH, GAP);
    float mLinewidth =(float)5.0;
    private Polyline mMutablePolyline;
    private PolylineOptions mPolylineOptions ;

    //private CheckBox mClickabilityCheckbox;
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
    private double wayLatitude = 0.0, wayLongitude = 0.0;
    JSONArray mJsonArray = new JSONArray();
    boolean mIsStartPipeLine =false;
    private Button togglePlayButton;
    FirebaseFirestore mDb;
    FirebaseStorage mStorage ;
    Gson gson ;
    ArrayList<location> lineInfoList ;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // MobileAds.initialize(getActivity(), Constants.ADMOBAPPID);
        rootview = inflater.inflate(R.layout.fragment_multiline, container, false);
        fragmentManager=getChildFragmentManager();
        gson = new Gson();


        mDb = FirebaseFirestore.getInstance();
        //Get a non-default Storage bucket

//        mClickabilityCheckbox = (CheckBox) rootview.findViewById(R.id.toggleClickability);
        togglePlayButton = (Button) rootview.findViewById(R.id.maptypebutton);
        //     togglePauseButton = (ToggleButton) rootview.findViewById(R.id.togglebutton2);
        togglePlayButton.setOnClickListener(new View.OnClickListener() {
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

        mFragment = (SupportMapFragment)fragmentManager.findFragmentById(R.id.map);
        mFragment.getMapAsync(this);
        return rootview;
    }

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
        Toast.makeText(getContext(), "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        getCurrentLocation();
        return false;
    }

    //and then register for location
    @Override
    public void onMapReady(GoogleMap map) {

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
                //   onLocationChanged(locationResult.getLastLocation());
            }
        };
        // Override the default conte
        // nt description on the view, for accessibility mode.
        mMap.setContentDescription(getString(R.string.polyline_demo_description));


//        int color = Color.HSVToColor(
//                mAlphaBar.getProgress(), new float[]{mHueBar.getProgress(), 1, 1});
        mPolylineOptions = new PolylineOptions()
                .color(Color.RED)
                .width(mLinewidth)
                .clickable(true);
        // .add(KURUVA, KURUVA2, KURUVA3, KURUVA4);


        mMutablePolyline = map.addPolyline(mPolylineOptions);
        mMutablePolyline.setWidth(mLinewidth);
        mMutablePolyline.setPattern(PATTERN_MIXED);
        // mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        // Move the map so that it is centered on the mutable polyline.
        // map.moveCamera(CameraUpdateFactory.newLatLngZoom(MELBOURNE, 5));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(KURUVA, 10));
        // map.setMyLocationEnabled(true);
        // Add a listener for polyline clicks that changes the clicked polyline's color.
        mMap.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
            @Override
            public void onPolylineClick(Polyline polyline) {
                // Flip the values of the red, green and blue components of the polyline's color.
                polyline.setColor(polyline.getColor() ^ 0x00ffffff);
                polyline.getTag().toString();
                //polyline.g
                alertMessage((location) polyline.getTag());
                //Toast.makeText(getActivity(),  polyline.getTag().toString(), Toast.LENGTH_LONG).show();
                // polyline.getId()

            }
        });
        //openlinesfromfirestorage();
//        map.setLatLngBoundsForCameraTarget();

        enableMyLocation();
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMapClickListener(this);
        mMap.setOnMarkerClickListener((GoogleMap.OnMarkerClickListener) this);
        mMap.setOnInfoWindowClickListener(this);
     //   mMap.setOnCameraIdleListener(this);
//        map.onCameraChange(new GoogleMap.OnCameraChangeListener() {
//
//            @Override
//            public void onCameraChange(CameraPosition arg0) {
//                moveMapCameraToBoundsAndInitClusterkraf();
//            }
//        });
//        VisibleRegion visibleRegion = mMap.getProjection().getVisibleRegion();
//        LatLngBounds latLngBounds = visibleRegion.latLngBounds;
        getPipelineWithinCoordinates();

    }
    public void alertMessage(location obj) {
        DialogInterface.OnClickListener dialogClickListeneryesno = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {

                    case DialogInterface.BUTTON_NEUTRAL:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        //  builder.setTitle("PipeLine");
        // builder.setView()
        final View loginFormView = getLayoutInflater().inflate(R.layout.lineinfo, null);
        TextView name = loginFormView.findViewById(R.id.infoName);
        TextView phone = loginFormView.findViewById(R.id.infoPhone);
        TextView type = loginFormView.findViewById(R.id.infoType);
        TextView purpose = loginFormView.findViewById(R.id.infoPurpose);
        TextView size = loginFormView.findViewById(R.id.infoSize);
        TextView remarks = loginFormView.findViewById(R.id.infoRemarks);
        name.setText(obj.getName());
        phone.setText(obj.getPhone());
        type.setText(obj.getType());
        purpose.setText(obj.getPurpose());
        size.setText(obj.getSize());
        remarks.setText(obj.getRemarks());
        builder.setView(loginFormView);
        builder.setNeutralButton("Ok", dialogClickListeneryesno).show();
        //   .setIcon(R.drawable.ic_action_about).show();

    }
    private void enableMyLocation() {
        //  Toast.makeText(getCon"enableMyLocation ", Toast.LENGTH_SHORT).show();

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
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
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case LREQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Toast.makeText(getActivity(), "Permission Granted 10", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    // Permission Denied
                    Toast.makeText(getActivity(), "Permission Denied 11", Toast.LENGTH_SHORT)
                            .show();
                }
                break;

            case LOCATION_PERMISSION_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Toast.makeText(getActivity(), "Permission Granted 20", Toast.LENGTH_SHORT)
                            .show();
                    // mMap.setMyLocationEnabled(true);
                } else {
                    // Permission Denied
                    Toast.makeText(getActivity(), "Permission Denied 21", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    @Override
    public void onCameraIdle() {
        VisibleRegion visibleRegion = mMap.getProjection().getVisibleRegion();
        LatLngBounds latLngBounds = visibleRegion.latLngBounds;

    }

    public void getPipelineWithinCoordinates()
    {
        SessionManager mSession = new SessionManager(getContext());
      // String url = Constants.GET_PIPELINE_VENDOR + mSession.getEmail();
        String url = Constants.GET_PIPELINE_URL_FINAL;
        new PostJSONAsyncTask().execute(url);
    }

//    @Override
//    public boolean onMarkerClick(Marker marker) {
//        Toast.makeText(getContext(), "onMarkerClick", Toast.LENGTH_LONG).show();
//        Intent i = new Intent(getActivity(), FullScreenViewActivity.class);
//        i.putExtra("position", 0);
//        ArrayList<String> imageList = new ArrayList<String>();
//        imageList.add("https://chunavane.s3.ap-south-1.amazonaws.com/bsy/image/main1513709806497.jpg");
//        imageList.add("https://chunavane.s3.ap-south-1.amazonaws.com/bsy/image/main1513709891180.jpg");
//        imageList.add("https://chunavane.s3.ap-south-1.amazonaws.com/bsy/image/main1513710152785.jpg");
//        i.putExtra("imageurls",imageList);
//        startActivity(i);
//        return false;
//    }
    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent i = new Intent(getContext(), SingleViewActivity.class);
        i.putExtra("url", "https://chunavane.s3.ap-south-1.amazonaws.com/bsy/image/main1513709806497.jpg");
        startActivity(i);

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
                HttpGet request = new HttpGet(urls[0]);
                HttpClient httpclient = new DefaultHttpClient();
           //     UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
                //StringEntity se = new StringEntity(urls[1]);
             //   request.setEntity(se);
                request.addHeader(Constants.SECUREKEY_KEY, Constants.SECUREKEY_VALUE);
                request.addHeader(Constants.VERSION_KEY, Constants.VERSION_VALUE);
                request.addHeader(Constants.CLIENT_KEY, Constants.CLIENT_VALUE);
                HttpResponse response = httpclient.execute(request);

                int status = response.getStatusLine().getStatusCode();
                if (status == 200) {
                    HttpEntity entity = response.getEntity();

                    String responseOrder = EntityUtils.toString(entity);
                    try {
                        lineInfoList = new ArrayList<location>();
                        JSONArray jsonArray = new JSONArray(responseOrder);
                        for(int i = 0; i < (jsonArray.length() -1); i++)
                        {
                            location lineInfo = null;
                            try {
                                lineInfo = gson.fromJson(jsonArray.getString(i), location.class);
                                JSONObject obj = jsonArray.getJSONObject(i);
                                {
                                    if (obj.has("location")) {
                                        JSONObject obj2 = obj.getJSONObject("location");
                                        if (obj2.has("coordinates")){
                                            JSONArray obj3 = obj2.getJSONArray("coordinates");
                                            for (int j = 0; j < obj3.length(); j++) {
                                                JSONArray obj4 = obj3.getJSONArray(j);
                                                Double objlat = obj4.getDouble(0);
                                                Double objlong = obj4.getDouble(1);
                                                Double objele = obj4.getDouble(2);
                                                Double objres = obj4.getDouble(3);
//                                        1        if(obj4.length() > 2)
//                                                {
//                                                    Double objlong = obj4.getDouble(2);
//                                                }2
                                                lineInfo.getCoordinates().add(new LatLng(objlat, objlong));
                                                lineInfo.getElevation().add(new GeoPoint(new LatLng(objlat, objlong),objele,objres));
                                            }
                                        }
                                    }
                                    if(obj.has("name"))
                                    {
                                        lineInfo.setName(obj.getString("name"));
                                    }
                                    if(obj.has("phone"))
                                    {
                                        lineInfo.setPhone(obj.getString("phone"));
                                    }
                                    if(obj.has("size"))
                                    {
                                        lineInfo.setSizeofpipeline(obj.getString("size"));
                                    }
                                    if(obj.has("purpose"))
                                    {
                                        lineInfo.setPurpose(obj.getString("purpose"));
                                    }
                                    if(obj.has("pipe_type"))
                                    {
                                        lineInfo.setPipe_type(obj.getString("pipe_type"));
                                    }
                                    if(obj.has("remarks"))
                                    {
                                        lineInfo.setRemarks(obj.getString("remarks"));
                                    }
                                    if(obj.has("date"))
                                    {
                                        lineInfo.setDate(obj.getString("date"));
                                    }
                                }
                            }
                            catch(Exception e)
                            {
                                e.printStackTrace();
                            }
                            String str = lineInfo.getName();
                            lineInfoList.add(lineInfo);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

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

                try {
                    // int sx = lineInfoList.size() - 3;
                    for (int i = 0 ; i < lineInfoList.size() ; i++) {
                        location loc = lineInfoList.get(i);
                        int points  = loc.getCoordinates().size();
                        mPolylineOptions = new PolylineOptions()
                                .color(Color.MAGENTA)
                                .width(mLinewidth)
                                .clickable(true);
                        for (int j = 0; j < points; j++) {
                            double lat = loc.getElevation().get(j).getLatlng().latitude;
                            double lon = loc.getElevation().get(j).getLatlng().longitude;
                            LatLng latLng = new LatLng(lat, lon);
                            mPolylineOptions = mPolylineOptions.add(latLng);
                            if (j % 20 == 0)
                            {
                                //    mMap.addMarker()
                                //  private static final LatLng MELBOURNE = new LatLng(-37.813, 144.962);
                                double elevation = loc.getElevation().get(j).getElevation();
                              //  double lon = loc.getCoordinates().get(j).getLatlng().longitude;
                                Marker melbourne = mMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .title("Elevation")
                                        .snippet(new String(String.valueOf(elevation)))
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker)));


//                                GroundOverlayOptions newarkMap = new GroundOverlayOptions()
//                                        .image(BitmapDescriptorFactory.fromResource(R.drawable.navheader))
//                                        .position(KURUVA, 8600f, 6500f);
//                                mMap.addGroundOverlay(newarkMap);
                            }
                        }
                        mMutablePolyline = mMap.addPolyline(mPolylineOptions);
                        String tag = lineInfoList.get(i).getName() + " ( " + lineInfoList.get(i).getPhone() + " ) " + "Size:" + lineInfoList.get(i).getSizeofpipeline();
                        mMutablePolyline.setTag(tag);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            else if (result == false)
                Toast.makeText(getContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();
        }
    }
}
