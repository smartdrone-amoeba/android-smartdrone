package com.dgs.smartdrone.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dgs.smartdrone.R;
import com.dgs.smartdrone.entity.ApiInterface;
import com.dgs.smartdrone.entity.GPSEntity;
import com.dgs.smartdrone.entity.GimbalModelEntity;
import com.dgs.smartdrone.entity.PinEntity;
import com.dgs.smartdrone.entity.PointAdapter;
import com.dgs.smartdrone.entity.ProjectAdapter;
import com.dgs.smartdrone.entity.ProjectEntity;
import com.dgs.smartdrone.helper.MyLocationListener;
import com.dgs.smartdrone.helper.RestHelperAPI;
import com.dji.mapkit.core.models.DJILatLng;
import com.dji.mapkit.core.models.annotations.DJIMarkerOptions;
import com.dji.mapkit.core.models.annotations.DJIPolylineOptions;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import dji.common.mission.waypoint.Waypoint;
import dji.common.mission.waypoint.WaypointMission;
import dji.common.mission.waypoint.WaypointMissionFinishedAction;
import dji.common.mission.waypoint.WaypointMissionHeadingMode;
import dji.sdk.flightcontroller.FlightController;
import dji.sdk.mission.waypoint.WaypointMissionOperator;
import dji.thirdparty.okhttp3.Response;


public class ProjectAddActivity extends FragmentActivity
        implements
        View.OnClickListener,
        GoogleMap.OnMapClickListener,
        OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, SeekBar.OnSeekBarChangeListener {

    protected static final String TAG = "GSDemoActivity";

    private GoogleMap gMap;

    private ImageButton add;
    private LocationManager locationManager;
    private String provider;
    private MyLocationListener mylistener;
    private Criteria criteria;
    private Location location;
    private TextView txtDate;
    private Calendar myCalendar;
    private EditText txtAddress ;
    RestHelperAPI mApiInterface;

    private ProjectEntity projectData;
    private List<PinEntity> listData;


    private TextView altitude;
    private TextView speed;
    private TextView heading;
    private TextView gimbal;
    ProgressDialog progress;
    private String address;


    private int i=1;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * @Description : RETURN Button RESPONSE FUNCTION
     */
    public void onReturn(View view) {
        Log.d(TAG, "onReturn");
        this.finish();
    }

    private void setResultToToast(final String string) {
        ProjectAddActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ProjectAddActivity.this, string, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initUI() {

        add = (ImageButton) findViewById(R.id.btnAddP);
        add.setOnClickListener(this);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // When the compile and target version is higher than 22, please request the
        // following permissions at runtime to ensure the
        // SDK work well.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.VIBRATE,
                            Manifest.permission.INTERNET, Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.WAKE_LOCK, Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.CHANGE_WIFI_STATE, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SYSTEM_ALERT_WINDOW,
                            Manifest.permission.READ_PHONE_STATE,
                    }
                    , 1);
        }

        setContentView(R.layout.activity_project_add);

        progress = new ProgressDialog(this);progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();

        initUI();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the location provider
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);   //default

        // user defines the criteria

        criteria.setCostAllowed(false);
        // get the best provider depending on the criteria
        provider = locationManager.getBestProvider(criteria, false);

        // the last known location of this provider
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        location = locationManager.getLastKnownLocation(provider);

        mylistener = new MyLocationListener(this);

        if (location != null) {
            mylistener.onLocationChanged(location);
        } else {
            // leads to the settings because there is no last known location
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
        // location updates: at least 1 meter and 200millsecs change
        locationManager.requestLocationUpdates(provider, 200, 1, mylistener);
        String a=""+location.getLatitude();
        Toast.makeText(this, a, Toast.LENGTH_LONG).show();

        listData = new ArrayList<PinEntity>();
        projectData = new ProjectEntity();

        loadDataToGrid();
        mApiInterface = new RestHelperAPI(this);
        progress.dismiss();


    }
    private void loadEditData(){
        progress.show();
        PolylineOptions option = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
        MarkerOptions optionMarker = new MarkerOptions();

        for (PinEntity pin: projectData.getPin()
             ) {
            option.add(new LatLng(pin.koordinat.getLatitude(), pin.koordinat.getLongitude()));
            optionMarker.position(new LatLng(pin.koordinat.getLatitude(), pin.koordinat.getLongitude()));
            gMap.addMarker(optionMarker).setTitle("Marker " + i);

            //option.
            i+=1;
        }
        gMap.addPolyline(option);

        loadDataToGrid();
        progress.dismiss();

    }
    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        txtDate.setText(sdf.format(myCalendar.getTime()));
    }


    private void setUpMap() {
        gMap.setOnMapClickListener(this);// add the listener for click for amap object

    }

    @Override
    public void onMapClick(LatLng point) {
        if (true){
            progress.show();
            PolylineOptions option = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
            MarkerOptions optionMarker = new MarkerOptions();


            option.add(new LatLng(point.latitude, point.longitude));
            optionMarker.position(new LatLng(point.latitude, point.longitude));
            gMap.addMarker(optionMarker).setTitle("Point " + i);

            //option.
            gMap.addPolyline(option);

            showSettingDialogDetail(point, i);
            i+=1;
        }else{
            setResultToToast("Cannot Add Waypoint");
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.locate:{
                break;
            }
            case R.id.btnAddP   :{
                showSettingDialog();
                break;
            }
            default:
                break;
        }
    }


    private void showSettingDialog(){
        ScrollView wayPointSettings = (ScrollView)getLayoutInflater().inflate(R.layout.dialog_add_project, null);
        txtAddress = (EditText)wayPointSettings.findViewById(R.id.txtAddressName);
        txtAddress.setText(address);

        EditText txtName = (EditText)wayPointSettings.findViewById(R.id.txtNameProject);
        EditText suveyor = (EditText)wayPointSettings.findViewById(R.id.txtSuveyorName);
        EditText txtDetail = (EditText)wayPointSettings.findViewById(R.id.txtDetail);
        myCalendar = Calendar.getInstance();

        txtDate= (TextView) wayPointSettings.findViewById(R.id.txtDate);
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        ImageButton btnDate = wayPointSettings.findViewById(R.id.btnCalender);


        btnDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(ProjectAddActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        TextView finalTxtDate = txtDate;
        new AlertDialog.Builder(this)
                .setTitle("")
                .setView(wayPointSettings)
                .setPositiveButton("Finish",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id) {
                        progress.show();
                        projectData.setNamaProject(txtName.getText().toString());
                        projectData.setAlamatProject(txtAddress.getText().toString());
                        projectData.setNamaSurveyor(suveyor.getText().toString());
                        //projectData.setde(txtName);
                        projectData.setTglTarget(finalTxtDate.getText().toString());
                        projectData.setPin(listData);

                        Gson gson = new Gson();
                        String jsondata = gson.toJson(projectData);
                        try {
                            Response respone = mApiInterface.postProject(jsondata);
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(intent);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        progress.dismiss();

                    }

                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }

                })
                .create()
                .show();
    }

    private void showSettingDialogDetail(LatLng point, int i){
        ScrollView wayPointSettings = (ScrollView)getLayoutInflater().inflate(R.layout.dialog_project_add_detail, null);

        TextView txtLat = wayPointSettings.findViewById(R.id.txtLatDetail);
        TextView txtLong = wayPointSettings.findViewById(R.id.txtLongDetail);
        txtLat.setText(String.valueOf(location.getLatitude()));
        txtLong.setText(String.valueOf(location.getLongitude()));

        altitude = wayPointSettings.findViewById(R.id.txtAltitudeValue);
        speed = wayPointSettings.findViewById(R.id.txtSpeedValue);
        heading = wayPointSettings.findViewById(R.id.txtHeadingValue);
        gimbal = wayPointSettings.findViewById(R.id.txtGimbalValue);

        SeekBar sbAltitude = wayPointSettings.findViewById(R.id.sbAltitude);
        sbAltitude.setOnSeekBarChangeListener(this);
        SeekBar sbSpeed = wayPointSettings.findViewById(R.id.sbSpeed);
        sbSpeed.setOnSeekBarChangeListener(this);
        SeekBar sbHeading = wayPointSettings.findViewById(R.id.sbHeading);
        sbHeading.setOnSeekBarChangeListener(this);
        SeekBar sbGimbal = wayPointSettings.findViewById(R.id.sbGimbal);
        sbGimbal.setOnSeekBarChangeListener(this);

        new AlertDialog.Builder(this)
                .setTitle("")
                .setView(wayPointSettings)
                .setPositiveButton("Finish",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id) {
                        progress.show();
                        PinEntity data = new PinEntity();
                        data.setAltitude(Integer.valueOf(altitude.getText().toString()));
                        data.setSpeed(Integer.valueOf(speed.getText().toString()));
                        data.setHeading(Integer.valueOf(heading.getText().toString()));
                        GimbalModelEntity gimbalmode= new GimbalModelEntity();
                        gimbalmode.setFocuspoi(true);
                        gimbalmode.setInterpolate(Integer.valueOf(gimbal.getText().toString()));
                        data.setGimbalmode(gimbalmode);
                        //

                        //latlong
                        GPSEntity koordinat = new GPSEntity();
                        koordinat.setLatitude(point.latitude);
                        koordinat.setLongitude(point.longitude);
                        data.setKoordinat(koordinat);
                        data.setName("Marker " + i);
                        listData.add(data);
                        loadDataToGrid();
                        progress.dismiss();
                    }

                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }

                })
                .create()
                .show();
        progress.dismiss();
    }

    private void loadDataToGrid(){

        GridView simpleGrid = (GridView) findViewById(R.id.gvMarkerList); // init GridView
        PointAdapter customAdapter = new PointAdapter(getApplicationContext(), listData);
        simpleGrid.setAdapter(customAdapter);
        // implement setOnItemClickListener event on GridView
        simpleGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // set an Intent to Another Activity
                //                Intent intent = new Intent(MissionActivity.this, SettingActivity.class);
                //                intent.putExtra("id", list.get(position).getId()); // put image data in Intent
                //                startActivity(intent); // start Intent
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (gMap == null) {
            gMap = googleMap;
            setUpMap();
        }

        LatLng shenzhen = new LatLng(location.getLatitude(), location.getLongitude());
        gMap.addMarker(new MarkerOptions().position(shenzhen).title("Now"));
        gMap.moveCamera(CameraUpdateFactory.newLatLng(shenzhen));

        float zoomlevel = (float) 18.0;
        LatLng pos = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(pos, zoomlevel);
        gMap.moveCamera(cu);
        gMap.getUiSettings().setZoomControlsEnabled(true);
        gMap.getUiSettings().setAllGesturesEnabled(false);
        gMap.getUiSettings().setMyLocationButtonEnabled(true);
        gMap.getUiSettings().setCompassEnabled(true);

        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

        address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();

        GPSEntity loc = new GPSEntity();
        loc.setLatitude(location.getLatitude());
        loc.setLongitude(location.getLongitude());
        projectData.setLokasi(loc);
        //txtAddress.setText(address);

        Intent intent = getIntent();
        String sxdataproject = intent.getStringExtra("dataproject");
        if(sxdataproject != null){
            Gson gson = new Gson();
            projectData = gson.fromJson(sxdataproject, ProjectEntity.class);
            loadEditData();
        }
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        if (marker.equals(marker))
        {
            setResultToToast(marker.getTitle());
            //handle click here
        }
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(seekBar.getId() == R.id.sbAltitude)
            altitude.setText(String.valueOf(progress));
        else if(seekBar.getId() == R.id.sbSpeed)
            speed.setText(String.valueOf(progress));
        else if(seekBar.getId() == R.id.sbHeading)
            heading.setText(String.valueOf(progress));
        else if(seekBar.getId() == R.id.sbGimbal)
            gimbal.setText(String.valueOf(progress));


    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
