package com.dgs.smartdrone.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.HeatmapTileProvider;
import com.amap.api.maps.model.TileOverlay;
import com.amap.api.maps.model.TileOverlayOptions;
import com.dgs.smartdrone.R;
import com.dgs.smartdrone.entity.Settings;
import com.dgs.smartdrone.entity.SettingsDetail;
import com.dgs.smartdrone.helper.DBHelper;
import com.dji.mapkit.core.maps.DJIMap;
import com.dji.mapkit.core.models.DJILatLng;
import com.dji.mapkit.core.models.annotations.DJIMarker;
import com.dji.mapkit.core.models.annotations.DJIMarkerOptions;
import com.dji.mapkit.core.models.annotations.DJIPolylineOptions;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapOverlay;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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

import static com.here.android.mpa.common.MapSettings.setIsolatedDiskCacheRootPath;

public class SettingActivity extends Activity implements CompoundButton.OnCheckedChangeListener,
        RadioGroup.OnCheckedChangeListener,
        View.OnClickListener,
        AdapterView.OnItemSelectedListener,
        SeekBar.OnSeekBarChangeListener {
    private static final String TAG = "MapWidgetActivity";
    private MapWidget mapWidget;

    public static final String MAP_PROVIDER = "MapProvider";
    private int lineWidthValue;
    private MapOverlay mapOverlay;
    private GroundOverlay groundOverlay;
    private TileOverlay tileOverlay;
    private int mapProvider;
    private Map hereMap;
    private GoogleMap googleMap;
    private AMap aMap;
    private ScrollView scrollView;
    private ImageButton btnPanel;
    private Button btnClear;
    private Button btnSaveAndUpload;
    private boolean isPanelOpen = true;
    private List<DJIMarker> markerList;

    private List<Waypoint> waypointList = new ArrayList<>();

    public static WaypointMission.Builder waypointMissionBuilder;

    private float mSpeed = 10.0f;
    private float altitude = 100.0f;
    private WaypointMissionHeadingMode mHeadingMode = WaypointMissionHeadingMode.AUTO;
    private WaypointMissionFinishedAction mFinishedAction = WaypointMissionFinishedAction.NO_ACTION;
    private int id=0;
    @SuppressLint("WrongViewCast")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mapWidget = findViewById(R.id.map_widget);
        markerList = new ArrayList<>();
        MapWidget.OnMapReadyListener onMapReadyListener = new MapWidget.OnMapReadyListener() {
            @Override
            public void onMapReady(@NonNull final DJIMap map) {
                map.setMapType(DJIMap.MAP_TYPE_NORMAL);

                map.setOnMarkerDragListener(new DJIMap.OnMarkerDragListener() {
                    @Override
                    public void onMarkerDragStart(DJIMarker djiMarker) {
                        if (markerList.contains(djiMarker)) {
                            Toast.makeText(SettingActivity.this,
                                    "Marker " + markerList.indexOf(djiMarker) + " drag started",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onMarkerDrag(DJIMarker djiMarker) {
                        // do nothing
                    }

                    @Override
                    public void onMarkerDragEnd(DJIMarker djiMarker) {
                        if (markerList.contains(djiMarker)) {
                            Toast.makeText(SettingActivity.this,
                                    "Marker " + markerList.indexOf(djiMarker) + " drag ended",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                mapWidget.setOnMarkerClickListener(new DJIMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(DJIMarker djiMarker) {
                        Toast.makeText(SettingActivity.this, "Marker " + markerList.indexOf(djiMarker) + " clicked",
                                Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

                map.setOnMapClickListener(new DJIMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(DJILatLng djiLatLng) {
                        DJIMarker marker = map.addMarker(new DJIMarkerOptions().position(djiLatLng).draggable(true));
                        markerList.add(marker);

                        //markWaypoint(point);
                        Waypoint mWaypoint = new Waypoint(djiLatLng.latitude, djiLatLng.longitude, altitude);
                        //Add Waypoints to Waypoint arraylist;
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
                });

                map.getUiSettings().setMyLocationButtonEnabled(true);

                //setmarker
                if(id > 0) {
                    Settings data = mydb.getData(Integer.valueOf(id));
                    ArrayList<SettingsDetail> listDetail = mydb.getAllSettingsDetail(Integer.valueOf(id));

                    //data
                    TextView altitude = findViewById(R.id.altitude_setting);
                    if(listDetail.size()>0) {
                        altitude.setText(String.valueOf(listDetail.get(0).getAltitude()));
                    }
                    RadioButton speed =null;
                    switch ((int) data.getSpeed()){
                        case 3:
                            speed = findViewById(R.id.lowSpeed_setting);
                            speed.isChecked();
                            break;
                        case 5:
                            speed = findViewById(R.id.MidSpeed_setting);
                            speed.isChecked();
                            break;
                        case 10:
                            speed = findViewById(R.id.HighSpeed_setting);
                            speed.isChecked();
                            break;
                    }
                    switch ((int) data.getGoway()){
                        case 0:
                            speed = findViewById(R.id.finishNone_setting);
                            speed.isChecked();
                            break;
                        case 1:
                            speed = findViewById(R.id.finishGoHome_setting);
                            speed.isChecked();
                            break;
                        case 2:
                            speed = findViewById(R.id.finishAutoLanding_setting);
                            speed.isChecked();
                            break;
                        case 3:
                            speed = findViewById(R.id.finishToFirst_setting);
                            speed.isChecked();
                            break;
                    }
                    switch ((int) data.getHandling()){
                        case 0:
                            speed = findViewById(R.id.headingNext_setting);
                            speed.isChecked();
                            break;
                        case 1:
                            speed = findViewById(R.id.headingInitDirec_setting);
                            speed.isChecked();
                            break;
                        case 2:
                            speed = findViewById(R.id.headingRC_setting);
                            speed.isChecked();
                            break;
                        case 3:
                            speed = findViewById(R.id.headingWP_setting);
                            speed.isChecked();
                            break;
                    }
                    //detail

                    DJIPolylineOptions option = new DJIPolylineOptions().width(5).color(Color.BLUE).geodesic(true);
                    DJIMarkerOptions optionMarker = new DJIMarkerOptions();
                    int i=1;
                    for (SettingsDetail item : listDetail
                    ) {
                        //option.add(new LatLng(item.getLan(), item.getLongs()));
                        optionMarker.position(new DJILatLng(item.getLan(), item.getLongs()));
                        map.addMarker(optionMarker).setTitle("Point " + i);
                        i+=1;
                    }
                }

            }
        };
        Intent intent = getIntent();
        mapProvider = intent.getIntExtra(MAP_PROVIDER, 0);
        mapProvider = 1;
        switch (mapProvider) {
            case 0:
                boolean success = setIsolatedDiskCacheRootPath(
                        getExternalFilesDir(null) + File.separator + ".here-maps-cache");
                if (success) {
                    mapWidget.initHereMap(onMapReadyListener);
                }
                break;
            case 1:
                mapWidget.initGoogleMap(onMapReadyListener);
                break;
            case 2:
                mapWidget.initAMap(onMapReadyListener);
                break;
            default:
            case 3:
                //TODO: Remove this key before putting on github
                mapWidget.initMapboxMap(onMapReadyListener, getResources().getString(R.string.mapbox_id));
                break;
        }
        mapWidget.onCreate(savedInstanceState);

        scrollView = findViewById(R.id.settings_scroll_view);
        btnPanel = findViewById(R.id.btn_settings);
        btnPanel.setOnClickListener(this);

        btnClear = findViewById(R.id.btn_clear);
        btnClear.setOnClickListener(this);

        mapWidget.showAllFlyZones();

        btnSaveAndUpload = findViewById(R.id.btnSaveAndUpload);
        btnSaveAndUpload.setOnClickListener(this);

        movePanel();

        mydb = new DBHelper(this);

        id = intent.getIntExtra("id",0);
        showSettingDialog();


    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int id) {

//
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//
            case R.id.btn_settings:
                movePanel();
                break;
            case R.id.btnSaveAndUpload:
                submitSaveAndUpload();
                break;
            case R.id.btn_clear:
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        googleMap = (GoogleMap) mapWidget.getMap().getMap();
//                        googleMap.clear();
//                    }
//
//                });
                 waypointList.clear();
                waypointMissionBuilder.waypointList(waypointList);
                break;

        }
    }

    private void submitSaveAndUpload(){
        //LayoutInflater inflater = getLayoutInflater();
        new AlertDialog.Builder(this)
                .setTitle("Confirm Conig")
                .setPositiveButton("Finish",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id) {

                        final TextView wpAltitude_TV = (TextView) findViewById(R.id.altitude_setting);
                        String altitudeString = wpAltitude_TV.getText().toString();
                        altitude = Integer.parseInt(nulltoIntegerDefalt(altitudeString));
                        //Log.e(TAG,"altitude "+altitude);
                        setResultToToast("altitude "+altitude);
                        //Log.e(TAG,"speed "+mSpeed);
                        setResultToToast("speed "+mSpeed);
                        //Log.e(TAG, "mFinishedAction "+mFinishedAction);
                        setResultToToast("mFinishedAction "+mFinishedAction);
                        //Log.e(TAG, "mHeadingMode "+mHeadingMode);
                        setResultToToast("mHeadingMode "+mHeadingMode);
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
    private DBHelper mydb ;

    private void  configWayPointMission(){

        mydb.deleteSettings(id);
        mydb.deleteSettingsDetail(id);
        long id = mydb.insertSetting(
                "tes 1",
                mFinishedAction.value(),
                mHeadingMode.value(),
                mSpeed);

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

            for (int i=0; i< waypointMissionBuilder.getWaypointList().size(); i++){
                Waypoint item = waypointMissionBuilder.getWaypointList().get(i);
                mydb.insertSettingDetail(
                        id
                        , altitude
                        , item.coordinate.getLatitude()
                        , item.coordinate.getLongitude()
                        , 50
                        , 0);

                waypointMissionBuilder.getWaypointList().get(i).altitude = altitude;
                waypointMissionBuilder.getWaypointList().get(i).shootPhotoDistanceInterval = 6000;
                waypointMissionBuilder.getWaypointList().get(i).gimbalPitch = -60;
                waypointMissionBuilder.getWaypointList().get(i).setHeadingInner(50);
                //waypointMissionBuilder.getWaypointList().get(i).
                waypointMissionBuilder.getWaypointList().get(i).addAction(new WaypointAction(WaypointActionType.START_TAKE_PHOTO, 0));
                //waypointMissionBuilder.getWaypointList().get(i).addAction(new WaypointAction(WaypointActionType.GIMBAL_PITCH, 0));



                //waypointMissionBuilder.getWaypointList().get(i).waypointActions = waypointActions;
                /*
                misi 1 0de
                misi 2 10de
                msi 3 20de
                msi 3 40de

                *
                 */

            }
            //waypointMissionBuilder.getWaypointList().get(1).

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
    private void setResultToToast(final String string){
        SettingActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SettingActivity.this, string, Toast.LENGTH_SHORT).show();
            }
        });
    }
    boolean isIntValue(String val)
    {
        try {
            val=val.replace(" ","");
            Integer.parseInt(val);
        } catch (Exception e) {return false;}
        return true;
    }
    String nulltoIntegerDefalt(String value){
        if(!isIntValue(value)) value="0";
        return value;
    }

    private void   showSettingDialog(){

        final TextView wpAltitude_TV = (TextView) findViewById(R.id.altitude_setting);
        RadioGroup speed_RG = (RadioGroup) findViewById(R.id.speed_setting);
        RadioGroup actionAfterFinished_RG = (RadioGroup) findViewById(R.id.actionAfterFinished_setting);
        RadioGroup heading_RG = (RadioGroup) findViewById(R.id.heading_setting);

        speed_RG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.lowSpeed_setting){
                    mSpeed = 3.0f;
                } else if (checkedId == R.id.MidSpeed_setting){
                    mSpeed = 5.0f;
                } else if (checkedId == R.id.HighSpeed_setting){
                    mSpeed = 10.0f;
                }
            }

        });

        actionAfterFinished_RG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d(TAG, "Select finish action");
                if (checkedId == R.id.finishNone_setting){
                    mFinishedAction = WaypointMissionFinishedAction.NO_ACTION;
                } else if (checkedId == R.id.finishGoHome_setting){
                    mFinishedAction = WaypointMissionFinishedAction.GO_HOME;
                } else if (checkedId == R.id.finishAutoLanding_setting){
                    mFinishedAction = WaypointMissionFinishedAction.AUTO_LAND;
                } else if (checkedId == R.id.finishToFirst_setting){
                    mFinishedAction = WaypointMissionFinishedAction.GO_FIRST_WAYPOINT;
                }
            }
        });

        heading_RG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d(TAG, "Select heading");

                if (checkedId == R.id.headingNext_setting) {
                    mHeadingMode = WaypointMissionHeadingMode.AUTO;
                } else if (checkedId == R.id.headingInitDirec_setting) {
                    mHeadingMode = WaypointMissionHeadingMode.USING_INITIAL_DIRECTION;
                } else if (checkedId == R.id.headingRC_setting) {
                    mHeadingMode = WaypointMissionHeadingMode.CONTROL_BY_REMOTE_CONTROLLER;
                } else if (checkedId == R.id.headingWP_setting) {
                    mHeadingMode = WaypointMissionHeadingMode.USING_WAYPOINT_HEADING;
                }
            }
        });


        //get data



    }

    private void movePanel() {
        int translationStart;
        int translationEnd;
        if (isPanelOpen) {
            translationStart = 0;
            translationEnd = -scrollView.getWidth();
        } else {

            scrollView.bringToFront();
            translationStart = -scrollView.getWidth();
            translationEnd = 0;
        }
        TranslateAnimation animate = new TranslateAnimation(
                translationStart, translationEnd, 0, 0);
        animate.setDuration(300);
        animate.setFillAfter(true);
        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (isPanelOpen) {
                    mapWidget.bringToFront();

                }
                btnPanel.bringToFront();
                isPanelOpen = !isPanelOpen;

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        scrollView.startAnimation(animate);

    }

    private static Bitmap getBitmap(VectorDrawable vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapWidget.onResume();
    }

    @Override
    protected void onPause() {
        mapWidget.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapWidget.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapWidget.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapWidget.onLowMemory();
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void addOverlay() {
        if (mapWidget.getMap() == null) {
            Toast.makeText(getApplicationContext(), R.string.error_map_not_initialized, Toast.LENGTH_SHORT).show();
            return;
        }

        float testLat = 37.4419f;
        float testLng = -122.1430f;
        switch (mapProvider) {
            case 0:
                if (mapOverlay == null) {
                    hereMap = (Map) mapWidget.getMap().getMap();
                    ImageView overlayView = new ImageView(SettingActivity.this);
                    overlayView.setImageDrawable(getResources().getDrawable(R.drawable.ic_drone));
                    GeoCoordinate testLocation = new GeoCoordinate(testLat, testLng);
                    mapOverlay = new MapOverlay(overlayView, testLocation);
                    hereMap.addMapOverlay(mapOverlay);
                } else {
                    hereMap.removeMapOverlay(mapOverlay);
                    mapOverlay = null;
                }
                break;
            case 1:
                if (groundOverlay == null) {
                    googleMap = (GoogleMap) mapWidget.getMap().getMap();
                    LatLng latLng1 = new LatLng(testLat, testLng);
                    LatLng latLng2 = new LatLng(testLat + 0.25, testLng + 0.25);
                    LatLng latLng3 = new LatLng(testLat - 0.25, testLng - 0.25);
                    LatLngBounds bounds = new LatLngBounds(latLng1, latLng2).including(latLng3);
                    BitmapDescriptor aircraftImage = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(
                            getResources(),
                            R.drawable.ic_compass_aircraft));
                    GroundOverlayOptions groundOverlayOptions = new GroundOverlayOptions();
                    groundOverlayOptions.image(aircraftImage)
                            .positionFromBounds(bounds)
                            .transparency(0.5f)
                            .visible(true);
                    groundOverlay = googleMap.addGroundOverlay(groundOverlayOptions);
                } else {
                    groundOverlay.remove();
                    groundOverlay = null;
                }
                break;
            case 2:
                if (tileOverlay == null) {
                    aMap = (AMap) mapWidget.getMap().getMap();
                    com.amap.api.maps.model.LatLng[] latlngs = new com.amap.api.maps.model.LatLng[500];
                    for (int i = 0; i < 500; i++) {
                        double x_;
                        double y_;
                        x_ = Math.random() * 0.5 - 0.25;
                        y_ = Math.random() * 0.5 - 0.25;
                        latlngs[i] = new com.amap.api.maps.model.LatLng(testLat + x_, testLng + y_);
                    }
                    HeatmapTileProvider.Builder builder = new HeatmapTileProvider.Builder();
                    builder.data(Arrays.asList(latlngs));
                    HeatmapTileProvider heatmapTileProvider = builder.build();
                    TileOverlayOptions tileOverlayOptions = new TileOverlayOptions();
                    tileOverlayOptions.tileProvider(heatmapTileProvider).visible(true);
                    tileOverlay = aMap.addTileOverlay(tileOverlayOptions);
                } else {
                    tileOverlay.remove();
                    tileOverlay = null;
                }
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
        this.lineWidthValue = progressValue;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private void setRandomLineColor() {
        Random rnd = new Random();
        @ColorInt int randomColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));


            mapWidget.setDirectionToHomeColor(randomColor);

            //mapWidget.setFlightPathColor(randomColor);

    }
}
