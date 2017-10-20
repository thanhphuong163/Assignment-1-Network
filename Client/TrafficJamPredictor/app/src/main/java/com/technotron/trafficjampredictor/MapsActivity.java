package com.technotron.trafficjampredictor;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Geocoder;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.location.Location;
import android.provider.Settings.Secure;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.maps.android.ui.IconGenerator;

import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;
import android.widget.Toast;
import android.widget.ZoomControls;
import android.content.DialogInterface;
import java.io.IOException;
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener,GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    Button markBt;
    Button geoLocationBt;
    Button clearBt;
    Button satView;
    Button setServerIPBt;
    FloatingActionButton sendDataBt;
    FloatingActionButton setTimerBt;


    public static boolean isSendingData = false;
    public static Double myLatitude = null;
    public static Double myLongitude = null;

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    protected static final String TAG = "MapsActivity";
    private final static int MY_PERMISSION_FINE_LOCATION = 101;

    private Thread UdpClientThread = null;    private Thread TcpClientThread = null;

    public static String predictionStr;
    public static String android_id;
    private String timer_text = null;
    public static String serverIP = "192.168.1.1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        android_id = Secure.getString(getContentResolver(), Secure.ANDROID_ID);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        locationRequest = new LocationRequest();
        locationRequest.setInterval(15 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        geoLocationBt = (Button) findViewById(R.id.btSearch);
        geoLocationBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText searchText = (EditText) findViewById(R.id.etLocationEntry);
                String search = searchText.getText().toString();
                if (search != null && !search.equals("")) {
                    List<android.location.Address> addressList = null;
                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(search, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    android.location.Address address = addressList.get(0);
                    LatLng loct = new LatLng(address.getLatitude(), address.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(loct).title("From geocoder"));
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(loct));
                }
            }
        });

        satView = (Button) findViewById(R.id.btSatellite);
        satView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    satView.setText("NORM");
                } else {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    satView.setText("SAT");
                }
            }
        });

        clearBt = (Button) findViewById(R.id.btClear);
        clearBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.clear();
            }
        });

        addMarker();
        setTimerForSendingData();
        inputIPofServer();
        sendData();
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
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng BK = new LatLng(10.7726084, 106.6577464);
        mMap.addMarker(new MarkerOptions().position(BK).title("Đai hoc Bach Khoa"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom((BK),16.0f));
        mMap.setOnMapClickListener(new OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.addMarker(new MarkerOptions().position(latLng).title("From map click"));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        });
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            mMap.setMyLocationEnabled(true);
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_FINE_LOCATION);
        }

        mMap.setOnMarkerClickListener(this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "This app requires location permission to be able to work", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
        }
    }

    public void addMarker() {
        markBt = (Button) findViewById(R.id.btMark);
        markBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng myLocation = new LatLng(myLatitude, myLongitude);
                mMap.addMarker(new MarkerOptions().position(myLocation).title("My location"));
            }
        });
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {
        requestLocationUpdates();
    }

    private void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: " + connectionResult.getErrorCode());
    }

    @Override
    public void onLocationChanged(Location location) {
        myLatitude = location.getLatitude();
        myLongitude = location.getLongitude();
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (googleApiClient.isConnected()) {
            requestLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    public void sendData(){
        sendDataBt = (FloatingActionButton) findViewById(R.id.btDataSend);
        sendDataBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isSendingData) {
                    isSendingData = true;
                    sendDataBt.setImageResource(R.drawable.stop_sending_img);
                    try {
                        UDPClientTask newTask;
                        if (timer_text != null && !timer_text.equals("")) {
                            newTask = new UDPClientTask(Integer.parseInt(timer_text));
                        }
                        else {
                            newTask = new UDPClientTask();
                        }
                        UdpClientThread = new Thread(newTask);
                        UdpClientThread.start();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                } else {
                    isSendingData = false;
                    sendDataBt.setImageResource(R.drawable.send_data);
                    try {
                        UdpClientThread.join();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    public void setTimerForSendingData(){
        setTimerBt = (FloatingActionButton) findViewById(R.id.btSetTimer);
        setTimerBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayTimerDialog(view);
            }
        });

    }

    public void displayTimerDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext(),R.style.MyDialogTheme);

        builder.setTitle("Set intervals for sending (in seconds)");
        final EditText input = new EditText(view.getContext());
        input.setTextColor(ColorStateList.valueOf(Color.WHITE));
        // Specify the type of input expected;
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                timer_text = input.getText().toString();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        final Marker newMarker = marker;
        final AlertDialog.Builder altBox = new AlertDialog.Builder(this,R.style.MyDialogTheme);
        final LatLng location = marker.getPosition();
        altBox.setMessage("Get traffic prediction for this location ?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPrediction(location.latitude,location.longitude);
                        setIconToMarker(predictionStr,newMarker);
                    }

                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }

        });

        AlertDialog alert = altBox.create();
        alert.setTitle("Attention");
        alert.show();
        return true;
    }

    public void requestPrediction(Double lat, Double lng) {
        TCPClientTask newTask = new TCPClientTask(lat,lng);
        TcpClientThread = new Thread(newTask);
        TcpClientThread.start();
        try {
            TcpClientThread.join();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Toast.makeText(getApplicationContext(), predictionStr , Toast.LENGTH_SHORT).show();
    }

    public void setIconToMarker(String text, Marker marker) {
        IconGenerator icon = new IconGenerator(this);
        if (text.equals("Jam")) {
            icon.setStyle(IconGenerator.STYLE_RED);
        } else if (text.equals("Not jam")){
            icon.setStyle(IconGenerator.STYLE_GREEN);
        } else {
            icon.setStyle(IconGenerator.STYLE_BLUE);
        }

        icon.setRotation(-90);
        icon.setContentRotation(90);
        LatLng location = marker.getPosition();
        marker.remove();
        MarkerOptions markerOptions = new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(icon.makeIcon(text)))
                .position(location)
                .anchor(icon.getAnchorU(), icon.getAnchorV());
        mMap.addMarker(markerOptions);
    }

    public void inputIPofServer(){
        setServerIPBt = (Button) findViewById(R.id.serverSettings);
        setServerIPBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayServerIPDialog(view);
            }
        });

    }

    public void displayServerIPDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext(),R.style.MyDialogTheme);

        builder.setTitle("Input IP of server for connection");
        final EditText input = new EditText(view.getContext());
        input.setTextColor(ColorStateList.valueOf(Color.WHITE));
        // Specify the type of input expected;
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                serverIP = input.getText().toString();
                Toast.makeText(MapsActivity.this, serverIP, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}


