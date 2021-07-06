package com.dgs.smartdrone.activity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dgs.smartdrone.R;
import com.dgs.smartdrone.entity.DeployAdapter;
import com.dgs.smartdrone.entity.PinEntity;
import com.dgs.smartdrone.entity.ProjectEntity;
import com.dgs.smartdrone.entity.Settings;
import com.dgs.smartdrone.entity.SettingsDetail;
import com.dji.mapkit.core.maps.DJIMap;
import com.dji.mapkit.core.models.DJILatLng;
import com.dji.mapkit.core.models.annotations.DJIMarker;
import com.dji.mapkit.core.models.annotations.DJIMarkerOptions;
import com.dji.mapkit.core.models.annotations.DJIPolylineOptions;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.here.android.mpa.mapping.Map;

import java.util.ArrayList;
import java.util.List;

import dji.common.error.DJIError;
import dji.common.mission.waypoint.Waypoint;
import dji.common.mission.waypoint.WaypointAction;
import dji.common.mission.waypoint.WaypointActionType;
import dji.common.mission.waypoint.WaypointMission;
import dji.common.mission.waypoint.WaypointMissionFinishedAction;
import dji.common.mission.waypoint.WaypointMissionFlightPathMode;
import dji.common.mission.waypoint.WaypointMissionHeadingMode;
import dji.common.util.CommonCallbacks;
import dji.sdk.mission.waypoint.WaypointMissionOperator;
import dji.sdk.sdkmanager.DJISDKManager;
import dji.ux.widget.MapWidget;

public class ProjectDetailActivity extends FragmentActivity
        implements
        View.OnClickListener,
        GoogleMap.OnMapClickListener,
        OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, SeekBar.OnSeekBarChangeListener {

    protected static final String TAG = "GSDemoActivity";
    private ProjectEntity dataP;


    private List<DJIMarker> markerList;

    private List<Waypoint> waypointList = new ArrayList<>();
    public static WaypointMission.Builder waypointMissionBuilder;

    private WaypointMissionHeadingMode mHeadingMode = WaypointMissionHeadingMode.AUTO;
    private WaypointMissionFinishedAction mFinishedAction = WaypointMissionFinishedAction.NO_ACTION;
    private float mSpeed = 10.0f;
    private float altitude = 100.0f;

    private GoogleMap gMap;
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
        setContentView(R.layout.activity_project_detail);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapdetail);
        mapFragment.getMapAsync(this);
        ImageButton btnDeploy = findViewById(R.id.btnDeploy);
        btnDeploy.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnDeploy){
            new AlertDialog.Builder(this)
                    .setTitle("Deploy Android")
                    .setPositiveButton("Finish",new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int id) {
                            configWayPointMission();

                        }

                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }

                    })
                    .create()
                    .show();
        }
    }

    private void  configWayPointMission(){

        mHeadingMode = WaypointMissionHeadingMode.AUTO;
//        mHeadingMode = WaypointMissionHeadingMode.USING_INITIAL_DIRECTION;
//        mHeadingMode = WaypointMissionHeadingMode.CONTROL_BY_REMOTE_CONTROLLER;
//        mHeadingMode = WaypointMissionHeadingMode.USING_WAYPOINT_HEADING;

//        mFinishedAction = WaypointMissionFinishedAction.NO_ACTION;
        mFinishedAction = WaypointMissionFinishedAction.GO_HOME;
//        mFinishedAction = WaypointMissionFinishedAction.AUTO_LAND;
//        mFinishedAction = WaypointMissionFinishedAction.GO_FIRST_WAYPOINT;

//        mSpeed = 3.0f;
//        mSpeed = 5.0f;
        mSpeed = 10.0f;

        if (waypointMissionBuilder == null){

            waypointMissionBuilder = new WaypointMission.Builder().finishedAction(mFinishedAction)
                    .headingMode(mHeadingMode)

                    .autoFlightSpeed(mSpeed)
                    .maxFlightSpeed(mSpeed)
                    .flightPathMode(WaypointMissionFlightPathMode.NORMAL)
                    .setGimbalPitchRotationEnabled(true)
            ;

        }else
        {
            waypointMissionBuilder.finishedAction(mFinishedAction)
                    .headingMode(mHeadingMode)
                    .autoFlightSpeed(mSpeed)
                    .maxFlightSpeed(mSpeed)
                    .flightPathMode(WaypointMissionFlightPathMode.NORMAL)
                    .setGimbalPitchRotationEnabled(true)
            ;
        }

        if (waypointMissionBuilder.getWaypointList().size() > 0){

            for (PinEntity data: dataP.getPin()
                 ) {
                Waypoint mWaypoint = new Waypoint(data.getKoordinat().latitude,data.getKoordinat().longitude, data.getAltitude());
                mWaypoint.shootPhotoDistanceInterval = 6000;
                mWaypoint.gimbalPitch = 6000;
                mWaypoint.setHeadingInner(data.heading);
                if (waypointMissionBuilder != null) {
                    waypointList.add(mWaypoint);
                    waypointMissionBuilder.waypointList(waypointList).waypointCount(waypointList.size());
                }else
                {
                    waypointMissionBuilder = new WaypointMission.Builder();
                    waypointList.add(mWaypoint);
                    waypointMissionBuilder.waypointList(waypointList).waypointCount(waypointList.size());
                }
            }

            setResultToToast("Set Waypoint attitude successfully");
        }

        WaypointMissionOperator mo = new WaypointMissionOperator();
        mo.getCurrentState();

        DJIError error = getWaypointMissionOperator().loadMission(waypointMissionBuilder.build());
        if (error == null) {
            setResultToToast("loadWaypoint succeeded");

            //upload
            uploadWayPointMission();
        } else {
            setResultToToast("loadWaypoint failed " + error.getDescription());
        }
    }
    private WaypointMissionOperator instance;
    public WaypointMissionOperator getWaypointMissionOperator() {
        if (instance == null) {
            if (DJISDKManager.getInstance().getMissionControl() != null){
                instance = DJISDKManager.getInstance().getMissionControl().getWaypointMissionOperator();
            }
        }
        return instance;
    }
    private void uploadWayPointMission(){

        getWaypointMissionOperator().uploadMission(new CommonCallbacks.CompletionCallback() {
            @Override
            public void onResult(DJIError error) { if (error == null) {
                setResultToToast("Mission upload successfully!");

                Intent intent = new Intent(getApplicationContext(), WayPoint2Activity.class);
                startActivity(intent);
            } else {
                setResultToToast("Mission upload failed, error: " + error.getDescription() + " retrying...");
                getWaypointMissionOperator().retryUploadMission(null);
            }
            }
        });

    }
    private void setResultToToast(final String string){
        ProjectDetailActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ProjectDetailActivity.this, string, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        return false;
    }
    private void setUpMap() {
        gMap.setOnMapClickListener(this);// add the listener for click for amap object

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        if (gMap == null) {
            gMap = googleMap;
            setUpMap();
        }
        gMap.getUiSettings().setZoomControlsEnabled(true);
        gMap.getUiSettings().setAllGesturesEnabled(false);
        gMap.getUiSettings().setMyLocationButtonEnabled(true);
        gMap.getUiSettings().setCompassEnabled(true);
        Intent intent = getIntent();
        Gson gson = new Gson();
        String data =intent.getStringExtra("datap");
        if(data != null) {
            dataP = gson.fromJson(data, ProjectEntity.class);
        }


        LatLng shenzhen = new LatLng(dataP.getLokasi().getLatitude(), dataP.getLokasi().getLongitude());
        gMap.addMarker(new MarkerOptions().position(shenzhen).title("Now"));
        gMap.moveCamera(CameraUpdateFactory.newLatLng(shenzhen));

        float zoomlevel = (float) 18.0;
        LatLng pos = new LatLng(dataP.getLokasi().getLatitude(), dataP.getLokasi().getLongitude());
        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(pos, zoomlevel);
        gMap.moveCamera(cu);
        ((TextView)findViewById(R.id.txtSuveyorD)).setText(dataP.getNamaSurveyor());
        ((TextView)findViewById(R.id.txtAddressD)).setText(dataP.getAlamatProject());
        ((TextView)findViewById(R.id.txtLatitudeD)).setText(String.valueOf(dataP.getLokasi().latitude));
        ((TextView)findViewById(R.id.txtLongitudeD)).setText(String.valueOf(dataP.getLokasi().longitude));
        ((TextView)findViewById(R.id.txtDetailD)).setText(dataP.getNamaSurveyor());
        ((TextView)findViewById(R.id.txtTargetD)).setText(dataP.getTglTarget());

        PolylineOptions option = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
        option.add(new LatLng(dataP.getLokasi().getLatitude(), dataP.getLokasi().getLongitude()));
        MarkerOptions optionMarker = new MarkerOptions();

        int i=1;
        for (PinEntity pin: dataP.getPin()
        ) {
            option.add(new LatLng(pin.koordinat.getLatitude(), pin.koordinat.getLongitude()));
            optionMarker.position(new LatLng(pin.koordinat.getLatitude(), pin.koordinat.getLongitude()));
            gMap.addMarker(optionMarker).setTitle("Point " + i);

            //option.
            i+=1;
        }
        gMap.addPolyline(option);
        if(dataP.getDeploy()!= null) {
            DeployAdapter customAdapter = new DeployAdapter(this, dataP.getDeploy());

            GridView simpleGrid = (GridView) findViewById(R.id.gvListDeploy); // init GridView
            simpleGrid.setAdapter(customAdapter);
        }
    }
}