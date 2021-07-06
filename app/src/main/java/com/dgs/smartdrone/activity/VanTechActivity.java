//package com.dgs.smartdrone.activity;
//
//import android.Manifest;
//import android.app.ActionBar;
//import android.app.AlertDialog;
//import android.bluetooth.BluetoothSocket;
//import android.bluetooth.BluetoothServerSocket;
//import android.bluetooth.BluetoothDevice;
//import android.bluetooth.BluetoothAdapter;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.ContextWrapper;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.SurfaceTexture;
//import android.os.Build;
//import android.os.Handler;
//import android.os.Bundle;
//import android.os.Message;
//import android.util.Log;
//import android.view.TextureView;
//import android.view.View;
//import android.graphics.drawable.Drawable;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.RadioGroup;
//import android.widget.TextView;
//import android.widget.Toast;
//import android.widget.ImageView;
//import android.widget.ArrayAdapter;
//import android.widget.AdapterView;
//import android.widget.EditText;
//import android.widget.ListView;
//import android.view.inputmethod.EditorInfo;
//import android.view.KeyEvent;
//import android.view.WindowManager;
//
//import com.dgs.smartdrone.DJIDemoApplication;
//import com.dgs.smartdrone.R;
//import com.dgs.smartdrone.entity.Constants;
//import com.dgs.smartdrone.helper.BluetoothService;
//import com.google.android.gms.maps.CameraUpdate;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.UUID;
//import java.io.IOException;
//import java.io.File;
//import java.nio.ByteBuffer;
//import java.io.ByteArrayOutputStream;
//
//import dji.common.camera.SettingsDefinitions;
//import dji.common.flightcontroller.FlightControllerState;
//import dji.common.gimbal.Rotation;
//import dji.common.gimbal.Rotation.Builder;
//import dji.common.mission.waypoint.Waypoint;
//import dji.common.mission.waypoint.WaypointAction;
//import dji.common.mission.waypoint.WaypointActionType;
//import dji.common.mission.waypoint.WaypointMission;
//import dji.common.mission.waypoint.WaypointMissionDownloadEvent;
//import dji.common.mission.waypoint.WaypointMissionExecutionEvent;
//import dji.common.mission.waypoint.WaypointMissionExecuteState;
//import dji.common.mission.waypoint.WaypointMissionFinishedAction;
//import dji.common.mission.waypoint.WaypointMissionFlightPathMode;
//import dji.common.mission.waypoint.WaypointMissionHeadingMode;
//import dji.common.mission.waypoint.WaypointMissionUploadEvent;
//import dji.common.product.Model;
//import dji.common.util.CommonCallbacks;
//import dji.sdk.base.BaseProduct;
//import dji.sdk.camera.Camera;
//import dji.sdk.camera.VideoFeeder;
//import dji.sdk.codec.DJICodecManager;
//import dji.sdk.gimbal.Gimbal;
//import dji.sdk.flightcontroller.FlightController;
//import dji.common.error.DJIError;
//import dji.sdk.media.MediaFile;
//import dji.sdk.media.MediaManager;
//import dji.sdk.mission.waypoint.WaypointMissionOperator;
//import dji.sdk.mission.waypoint.WaypointMissionOperatorListener;
//import dji.sdk.products.Aircraft;
//import dji.sdk.sdkmanager.DJISDKManager;
//
//import androidx.annotation.Nullable;
//import androidx.core.app.ActivityCompat;
//import androidx.fragment.app.FragmentActivity;
//
//public class VanTechActivity extends FragmentActivity implements View.OnClickListener, GoogleMap.OnMapClickListener, OnMapReadyCallback, TextureView.SurfaceTextureListener {
//
//    protected static final String TAG = "Main Activity";
//
//    //Return Intent extra
//    public static String EXTRA_DEVICE_ADDRESS = "device_address";
//
//    // Intent request codes
//    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
//    private static final int REQUEST_ENABLE_BT = 3;
//
//    //Google map
//    private GoogleMap gMap;
//
//    // Buttons
//    private Button locate, add, clear;
//    private Button start;
//
//    //Drone position
//    private double droneLocationLat = 25.649916, droneLocationLng = -100.290943;
//    private Marker droneMarker = null;
//
//    // Waypoint markers
//    private boolean isAdd = false;
//    private final Map<Integer, Marker> mMarkers = new ConcurrentHashMap<Integer, Marker>();
//
//    // Waypoint mission
//    private boolean isFinishedAction = false;
//    int waypointCount = 0;
//    private int sentImagesCount = 0;
//
//    // Drone flight controller
//    private FlightController mFlightController;
//
//    // Drone camera
//    private Camera mCamera;
//    private Gimbal mGimbal;
//    private MediaManager mMediaManager;
//    private String lastPhoto;
//    private List<MediaFile> mediaList = new ArrayList<>();
//    private List<Bitmap> photoBitmapList = new ArrayList<>();
//    private List<String> photoPathList = new ArrayList<>();
//    private BitmapFactory.Options mBitmapOptions = new BitmapFactory.Options();
//
//    // Flight configuration
//    private float altitude = 15.0f;
//    private float mSpeed = 3.0f;
//    private WaypointMissionFinishedAction mFinishedAction = WaypointMissionFinishedAction.GO_HOME;
//    private WaypointMissionHeadingMode mHeadingMode = WaypointMissionHeadingMode.AUTO;
//
//    //Waypoint mission variables
//    public static WaypointMission.Builder waypointMissionBuilder;
//    private List<Waypoint> waypointList = new ArrayList<>();
//    private WaypointMissionOperator instance;
//    private EditText mWaypointLatitude;
//    private EditText mWaypointLongitude;
//    private EditText mWaypointAltitude;
//    private Button addWaypoint;
//
//    // Bluetooth variables
//    private BluetoothAdapter mBluetoothAdapter = null;
//    private BluetoothService mBluetoothService = null;
//    private String mConnectedDeviceName = null;
//    private StringBuffer mOutStringBuffer;
//    private ArrayAdapter<String> pairedDevicesArrayAdapter = null;
//
//    // Live feed variables
//    protected VideoFeeder.VideoDataCallback mReceivedVideoDataCallBack = null;
//    protected DJICodecManager mCodecManager = null; // Codec for video live view
//    protected TextureView mVideoSurface = null;
//
//    // Monitor variables
//    private TextView mBluetoothStatus;
//    private EditText mOutEditText;
//    private Button mSendButton, mDevicesButton;
//
//
//    /*********************
//     Activity overrides
//     *********************/
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        // When the compile and target version is higher than 22, please request the
//        // following permissions at runtime to ensure the
//        // SDK work well.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.VIBRATE,
//                            Manifest.permission.INTERNET, Manifest.permission.ACCESS_WIFI_STATE,
//                            Manifest.permission.WAKE_LOCK, Manifest.permission.ACCESS_COARSE_LOCATION,
//                            Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_FINE_LOCATION,
//                            Manifest.permission.CHANGE_WIFI_STATE, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
//                            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SYSTEM_ALERT_WINDOW,
//                            Manifest.permission.READ_PHONE_STATE,
//                    }
//                    , 1);
//        }
//
//        setContentView(R.layout.activity_van_tech);
//
//        initUI();
//
//        // Set result CANCELED in case the user backs out
//        setResult(MainActivity.RESULT_CANCELED);
//
//        // Initialize the local bluetooth adapter
//        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        mBluetoothStatus.setText("Not connected to bluetooth");
//
//        // Send broadcast flag to send a connection change of the dji aircraft
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(DJIDemoApplication.FLAG_CONNECTION_CHANGE);
//        registerReceiver(mReceiver, filter);
//
//        // Map async
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//
//        // Listener for waypoint missions
//        addListener();
//
//        // Live feed:
//        // The callback for receiving the raw H264 video data for camera live view
//        mReceivedVideoDataCallBack = new VideoFeeder.VideoDataCallback() {
//            @Override
//            public void onReceive(byte[] videoBuffer, int size) {
//                if (mCodecManager != null) {
//                    mCodecManager.sendDataToDecoder(videoBuffer, size);
//                }
//            }
//        };
//
//        // Hide keyboard
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        // If BT is not on, request that it be enabled.
//        // setupChat() will then be called during onActivityResult
//        if (!mBluetoothAdapter.isEnabled()) {
//            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
//            // Otherwise, setup the bluetooth session
//        } else if (mBluetoothService == null) {
//            setupBluetooth();
//        }
//    }
//
//    @Override
//    protected void onResume(){
//        super.onResume();
//        initFlightController();
//        initCameraController();
//        initPreviewer();
//
//        // Performing this check in onResume() covers the case in which BT was
//        // not enabled during onStart(), so we were paused to enable it...
//        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
//        if (mBluetoothService != null) {
//            // Only if the state is STATE_NONE, do we know that we haven't started already
//            if (mBluetoothService.getState() == BluetoothService.STATE_NONE) {
//                Log.e(TAG, "Bluetooth service start");
//                // Start the Bluetooth chat services
//                mBluetoothService.start();
//            }
//        }
//    }
//
//    @Override
//    protected void onPause(){
//        //uninitPreviewer();
//        super.onPause();
//    }
//
//    @Override
//    protected void onDestroy(){
//        unregisterReceiver(mReceiver);
//        // Remove waypoint missions listener
//        removeListener();
//
//        //uninitPreviewer();
//
//        // Stop the bluetooth sockets
//        if (mBluetoothService != null) {
//            mBluetoothService.stop();
//        }
//
//        // Make sure we're not doing discovery anymore
//        if (mBluetoothAdapter != null) {
//            mBluetoothAdapter.cancelDiscovery();
//        }
//
//        super.onDestroy();
//    }
//
//    /*********************
//     Activity init functions
//     *********************/
//
//    // Init view layout variables
//    private void initUI() {
//        // Main buttons
//        locate = (Button) findViewById(R.id.locateV);
//        add = (Button) findViewById(R.id.addV);
//        clear = (Button) findViewById(R.id.clearV);
//        start = (Button) findViewById(R.id.startV);
//
//        // Listeners for main buttons
//        locate.setOnClickListener(this);
//        add.setOnClickListener(this);
//        clear.setOnClickListener(this);
//        start.setOnClickListener(this);
//
//        // Bluetooth layout
//        mBluetoothStatus = (TextView) findViewById(R.id.bluetooth_status);
//        mOutEditText = (EditText) findViewById(R.id.edit_text_out);
//        mSendButton = (Button) findViewById(R.id.button_send);
//        mDevicesButton = (Button) findViewById(R.id.button_devices);
//
//        // Initialize the compose field with a listener for the return key (bluetooth message)
//        mOutEditText.setOnEditorActionListener(mWriteListener);
//
//        // Initialize the send button with a listener that for click events (bluetooth message)
//        mSendButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                TextView textView = (TextView) findViewById(R.id.edit_text_out);
//                String message = textView.getText().toString();
//                sendMessage(message);
//            }
//        });
//
//        // Listener for the button to check bluetooth devices
//        mDevicesButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDeviceList();
//            }
//        });
//
//        // init mVideoSurface
//        mVideoSurface = (TextureView)findViewById(R.id.video_previewer_surface);
//
//        if (null != mVideoSurface) {
//            mVideoSurface.setSurfaceTextureListener(this);
//        }
//
//        // Waypoints with coordinates texts
//        mWaypointLatitude = (EditText) findViewById(R.id.edit_waypoint_latitude);
//        mWaypointLongitude = (EditText) findViewById(R.id.edit_waypoint_longitude);
//        mWaypointAltitude = (EditText) findViewById(R.id.edit_waypoint_altitude);
//        addWaypoint = (Button) findViewById(R.id.button_add_waypoint);
//
//        addWaypoint.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                TextView latText = (TextView) findViewById(R.id.edit_waypoint_latitude);
//                TextView longText = (TextView) findViewById(R.id.edit_waypoint_longitude);
//                TextView altText = (TextView) findViewById(R.id.edit_waypoint_altitude);
//                String lat = latText.getText().toString();
//                String longi = longText.getText().toString();
//                String alti = altText.getText().toString();
//
//                if(lat.length() > 0 && longi.length() > 0 && alti.length() > 0) {
//                    LatLng point = new LatLng(Double.parseDouble(lat), Double.parseDouble(longi));
//                    setWaypoint(point, Float.parseFloat(alti));
//                    mWaypointLatitude.setText("");
//                    mWaypointLongitude.setText("");
//                }
//            }
//        });
//    }
//
//    // Setup background bluetooth operations
//    private void setupBluetooth() {
//        Log.d(TAG, "setupBluetooth()");
//
//        // Initialize the BluetoothChatService to perform bluetooth connections
//        mBluetoothService = new BluetoothService(VanTechActivity.this, mHandler);
//
//        // Initialize the buffer for outgoing messages
//        mOutStringBuffer = new StringBuffer("");
//    }
//
//    private void showDeviceList(){
//        LinearLayout deviceListDialog = (LinearLayout)getLayoutInflater().inflate(R.layout.device_list, null);
//
//        pairedDevicesArrayAdapter = new ArrayAdapter<>(this, R.layout.device_name);
//
//        // Find and set up the ListView for paired devices
//        ListView pairedListView = (ListView) deviceListDialog.findViewById(R.id.paired_devices);
//        pairedListView.setAdapter(pairedDevicesArrayAdapter);
//        pairedListView.setOnItemClickListener(mDeviceClickListener);
//
//        getSyncBluetoothDevices();
//
//        new AlertDialog.Builder(this)
//                .setTitle("")
//                .setView(deviceListDialog)
//                .setPositiveButton("Ok",new DialogInterface.OnClickListener(){
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.dismiss();
//                    }
//                })
//                .create()
//                .show();
//    }
//
//    private void initPreviewer() {
//
//        BaseProduct product = DJIDemoApplication.getProductInstance();
//
//        if (product == null || !product.isConnected()) {
//            setResultToToast(getString(R.string.disconnected));
//        } else {
//            if (null != mVideoSurface) {
//                mVideoSurface.setSurfaceTextureListener(this);
//            }
//            if (!product.getModel().equals(Model.UNKNOWN_AIRCRAFT)) {
//                if (VideoFeeder.getInstance().getVideoFeeds() != null
//                        && VideoFeeder.getInstance().getVideoFeeds().size() > 0) {
//                    VideoFeeder.getInstance().getVideoFeeds().get(0).setCallback(mReceivedVideoDataCallBack);
//                }
//            }
//        }
//    }
//
////    private void uninitPreviewer() {
////        Camera camera = DJIDemoApplication.getCameraInstance();
////        if (camera != null){
////            // Reset the callback
////            VideoFeeder.getInstance().getPrimaryVideoFeed().get(0).setCallback(null);
////        }
////    }
//
//    /*****************
//     Click manager
//     *****************/
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.locate:{
//                updateDroneLocation();
//                cameraUpdate(); // Locate the drone's place
//                break;
//            }
//            case R.id.add:{
//                enableDisableAdd();
//                break;
//            }
//            case R.id.clear:{
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        gMap.clear();
//                    }
//
//                });
//                waypointList.clear();
//                waypointMissionBuilder.waypointList(waypointList);
//                updateDroneLocation();
//                break;
//            }
//            case R.id.start:{
//                startMission();
//                break;
//            }
//            default:
//                break;
//        }
//    }
//
//    /*************
//     Connection functions
//     *************/
//
//    protected BroadcastReceiver mReceiver = new BroadcastReceiver() {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            onProductConnectionChange();
//        }
//    };
//
//    private void onProductConnectionChange()
//    {
//        initFlightController();
//        initCameraController();
//    }
//
//    /********************
//     Flight functions
//     ********************/
//
//    private void initFlightController() {
//
//        BaseProduct product = DJIDemoApplication.getProductInstance();
//        if (product != null && product.isConnected()) {
//            if (product instanceof Aircraft) {
//                mFlightController = ((Aircraft) product).getFlightController();
//            }
//        }
//
//        if (mFlightController != null) {
//            mFlightController.setStateCallback(new FlightControllerState.Callback() {
//
//                @Override
//                public void onUpdate(FlightControllerState djiFlightControllerCurrentState) {
//                    droneLocationLat = djiFlightControllerCurrentState.getAircraftLocation().getLatitude();
//                    droneLocationLng = djiFlightControllerCurrentState.getAircraftLocation().getLongitude();
//                    updateDroneLocation();
//                }
//            });
//        }
//
//        mFlightController.setGoHomeHeightInMeters((int) 20f, new CommonCallbacks.CompletionCallback() {
//            @Override
//            public void onResult(DJIError djiError) {
//                if (djiError == null) {
//                    Log.e(TAG, "Go Home height set to 20m");
//                } else {
//                    setResultToToast("Error in setGoHomeHeightInMeters: " + djiError.getDescription());
//                }
//            }
//        });
//    }
//
//    public void returnToHome() {
//        stopWaypointMission();
//        mFlightController.startGoHome(new CommonCallbacks.CompletionCallback() {
//            @Override
//            public void onResult(DJIError djiError) {
//                setResultToToast("Returning to home: " + (djiError == null ? "Success!" : djiError.getDescription()));
//            }
//        });
//    }
//
//    /********************
//     Camera functions
//     ********************/
//
//    private void initCameraController()
//    {
//        //mCamera = DJIDemoApplication.getCameraInstance();
//
//        if(mCamera != null) {
//
//            /* If next competitions of roboboat you are using a dji drone other than Phantom 4 Pro,
//                you need to check if that drone support download mode for the retrieving of photos
//            if(mCamera.isMediaDownloadModeSupported()) {
//                Log.e(TAG, " DOWNLOAD IS SUPORTED!!! ************** ");
//            }
//            else {
//                Log.e(TAG, " DOWNLOAD IS NOT SUPORTED!!! ************** ");
//            }
//            */
//
//            mMediaManager = mCamera.getMediaManager();
//
//            mCamera.setMediaFileCallback(new MediaFile.Callback() {
//                @Override
//                public void onNewFile(final MediaFile newFile) {
//                    Log.e(TAG, " ********** NEW MEDIA CREATED " + newFile.getFileName() + " ************** ");
//                    setResultToToast("New media created");
//
//                    mediaList.add(newFile);
//                }
//            });
//        }
//
//        /*
//        mGimbal = DJIDemoApplication.getGimbalInstance();
//
//        if(mGimbal != null) {
//            Rotation rotate = new Rotation();
//            mGimbal.rotate();
//        }
//        */
//
//
//    }
//
//    public void retrieveAndSendPhotos() {
//
//        // To Do: if is Media Download supported, do the fetch data in the media manager
//        /*
//         *   In this case, we know our drone support media download mode
//         *
//         */
//
//        switchCameraMode(SettingsDefinitions.CameraMode.MEDIA_DOWNLOAD);
//
//        if(mediaList.size() > 0) {
//            for(MediaFile currentFile : mediaList) {
//                downloadPhoto(currentFile);
//            }
//        }
//        else {
//            restartMission();
//        }
//    }
//
//    public void downloadPhoto(MediaFile photoFile)
//    {
//        // Bitmap options for the photos taken by the drone (reduced by half)
//        mBitmapOptions.inSampleSize = 2;
//
//
////        mMediaManager.fetchTimelapsePreviewImage(photoFile, getApplicationContext().getFilesDir(), null,
////                new MediaManager.FileListStateListener() {
////                    @Override
////                    public void onStart() {
////                        Log.e(TAG, " ************ Retrieving media ************** ");
////                        setResultToToast("Retrieving photo");
////                    }
////
////                    @Override
////                    public void onRateUpdate(long l, long l1, long l2) {
////                        long percent = (l1 * 100) /l;
////                        Log.e(TAG, "Downloading media: " + percent + " ... *************");
////                    }
////
////                    @Override
////                    public void onProgress(long l, long l1) {
////
////                    }
////
////                    @Override
////                    public void onSuccess(String imagePath) {
////                        Log.e(TAG, " ******************* Success fetching data ************************* ");
////                        Log.e(TAG, imagePath);
////                        Log.e(TAG, " ******************************************************************* ");
////
////                        // Add the image path to the photo path list
////                        photoPathList.add(imagePath);
////
////                        // Convert file created to bitmap image
////                        Bitmap mBitmapPhoto = BitmapFactory.decodeFile(imagePath, mBitmapOptions);
////
////                        // Add the bitmap photo to the photo bitmap list
////                        //photoBitmapList.add(mBitmapPhoto);
////                        sendBitmap(mBitmapPhoto);
////
////                        //show the bitmap photo in the layout
////                        showPhoto(mBitmapPhoto);
////
////                        /*
////                            To do: Monitor download success
////                         */
////                    }
////
////                    @Override
////                    public void onFailure(DJIError djiError) {
////                        setResultToToast(" Error in fetching data: " + djiError.getDescription());
////                        Log.e(TAG, " Error in fetching data: " + djiError.getDescription());
////                    }
////                });
//    }
//
//    public void showPhoto(final Bitmap mBitmapPhoto)
//    {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                ImageView imageView = (ImageView) findViewById(R.id.lastPhoto);
//
//                imageView.setImageBitmap(mBitmapPhoto);
//            }
//        });
//    }
//
//    private void switchCameraMode(SettingsDefinitions.CameraMode cameraMode){
//
////        Camera camera = DJIDemoApplication.getCameraInstance();
////        if (camera != null) {
////            camera.setMode(cameraMode, new CommonCallbacks.CompletionCallback() {
////                @Override
////                public void onResult(DJIError error) {
////
////                    if (error == null) {
////                        setResultToToast("Switch Camera Mode Succeeded");
////                    } else {
////                        setResultToToast(error.getDescription());
////                    }
////                }
////            });
////        }
//    }
//
//
//    /**********************
//     Waypoint functions
//     **********************/
//
//    public WaypointMissionOperator getWaypointMissionOperator() {
//        if (instance == null) {
//            instance = DJISDKManager.getInstance().getMissionControl().getWaypointMissionOperator();
//        }
//        return instance;
//    }
//
//    //Add Listener for WaypointMissionOperator
//    private void addListener() {
//        if (getWaypointMissionOperator() != null){
//            getWaypointMissionOperator().addListener(eventNotificationListener);
//        }
//    }
//
//    private void removeListener() {
//        if (getWaypointMissionOperator() != null) {
//            getWaypointMissionOperator().removeListener(eventNotificationListener);
//        }
//    }
//
//    private WaypointMissionOperatorListener eventNotificationListener = new WaypointMissionOperatorListener() {
//        @Override
//        public void onDownloadUpdate(WaypointMissionDownloadEvent downloadEvent) {
//
//        }
//
//        @Override
//        public void onUploadUpdate(WaypointMissionUploadEvent uploadEvent) {
//
//        }
//
//        @Override
//        public void onExecutionUpdate(WaypointMissionExecutionEvent executionEvent) {
//            if(executionEvent.getProgress().executeState == WaypointMissionExecuteState.FINISHED_ACTION) {
//                // Download and send photo to central (not used anymore)
//                if(!isFinishedAction) {
//                    waypointCount++;
//                    isFinishedAction = true;
//                    Log.e(TAG, "Waypoint " + waypointCount + " action finished");
//                    //setResultToToast("Action in Waypoint " + waypointCount + " finished");
//                }
//            }
//            else if(executionEvent.getProgress().executeState == WaypointMissionExecuteState.MOVING) {
//                isFinishedAction = false;
//            }
//        }
//
//        @Override
//        public void onExecutionStart() {
//
//        }
//
//        @Override
//        public void onExecutionFinish(@Nullable final DJIError error) {
//            setResultToToast("Execution finished: " + (error == null ? "Success!" : error.getDescription()));
//
//            /*
//                If there were not photos taken, send a message to the boat and start again the mission
//                *
//                *
//                *
//                *
//                *
//             */
//
//            retrieveAndSendPhotos();
//
//            // Remove last waypoint
//            waypointList.remove(waypointList.size() - 1);
//        }
//    };
//
//    private void startMission()
//    {
//        // Clear arrays used in previous missions
//        mediaList.clear();
//        photoPathList.clear();
//        sentImagesCount = 0;
//        waypointCount = 0;
//
//        configWayPointMission();
//    }
//
//    private void restartMission()
//    {
//        while(mFlightController.getState().isFlying()) {
//            Log.e(TAG,"waiting for landing to restart mission...");
//        }
//
//        startMission();
//    }
//
//    private void configWayPointMission(){
//
//        if (waypointMissionBuilder == null){
//            waypointMissionBuilder = new WaypointMission.Builder().finishedAction(mFinishedAction)
//                    .headingMode(mHeadingMode)
//                    .autoFlightSpeed(mSpeed)
//                    .maxFlightSpeed(mSpeed)
//                    .flightPathMode(WaypointMissionFlightPathMode.NORMAL);
//
//        }else
//        {
//            waypointMissionBuilder.finishedAction(mFinishedAction)
//                    .headingMode(mHeadingMode)
//                    .autoFlightSpeed(mSpeed)
//                    .maxFlightSpeed(mSpeed)
//                    .flightPathMode(WaypointMissionFlightPathMode.NORMAL);
//
//        }
//
//        if (waypointMissionBuilder.getWaypointList().size() > 0){
//
//            for (int i=0; i< waypointMissionBuilder.getWaypointList().size(); i++){
//                waypointMissionBuilder.getWaypointList().get(i).gimbalPitch = -90;
//                waypointMissionBuilder.getWaypointList().get(i).altitude = altitude;
//                waypointMissionBuilder.getWaypointList().get(i).addAction(new WaypointAction(WaypointActionType.START_TAKE_PHOTO, 0));
//            }
//
//            // Set last waypoint in the position where the drone took off
//            Waypoint mWaypoint = new Waypoint(droneLocationLat, droneLocationLng, 7);
//            waypointList.add(mWaypoint);
//            waypointMissionBuilder.waypointList(waypointList).waypointCount(waypointList.size());
//
//
//            setResultToToast("Set Waypoint Configuration successfully");
//        }
//
//        DJIError error = getWaypointMissionOperator().loadMission(waypointMissionBuilder.build());
//        if (error == null) {
//            setResultToToast("loadWaypoint succeeded");
//            uploadWayPointMission();
//        } else {
//            setResultToToast("loadWaypoint failed " + error.getDescription());
//        }
//    }
//
//    private void uploadWayPointMission(){
//
//        getWaypointMissionOperator().uploadMission(new CommonCallbacks.CompletionCallback() {
//            @Override
//            public void onResult(DJIError error) {
//                if (error == null) {
//                    setResultToToast("Mission upload successfully!");
//                    new java.util.Timer().schedule(
//                            new java.util.TimerTask() {
//                                @Override
//                                public void run() {
//                                    startWaypointMission();
//                                }
//                            },
//                            2000
//                    );
//                } else {
//                    setResultToToast("Mission upload failed, error: " + error.getDescription() + " retrying...");
//                    restartMission();
//                }
//            }
//        });
//    }
//
//    private void startWaypointMission(){
//
//        switchCameraMode(SettingsDefinitions.CameraMode.SHOOT_PHOTO);
//
//        getWaypointMissionOperator().startMission(new CommonCallbacks.CompletionCallback() {
//            @Override
//            public void onResult(DJIError error) {
//                setResultToToast("Mission Start: " + (error == null ? "Successfully" : error.getDescription()));
//            }
//        });
//    }
//
//    private void stopWaypointMission(){
//
//        getWaypointMissionOperator().stopMission(new CommonCallbacks.CompletionCallback() {
//            @Override
//            public void onResult(DJIError error) {
//                setResultToToast("Mission Stop: " + (error == null ? "Successfully" : error.getDescription()));
//            }
//        });
//
//    }
//
//
//    /*****************
//     Map functions
//     *****************/
//
//    private void setUpMap() {
//        gMap.setOnMapClickListener(this);// add the listener for click for amap object
//
//    }
//
//    // Update the drone location based on states from MCU.
//    private void updateDroneLocation(){
//
//        LatLng pos = new LatLng(droneLocationLat, droneLocationLng);
//        //Create MarkerOptions object
//        final MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(pos);
//        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.aircraft));
//
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                if (droneMarker != null) {
//                    droneMarker.remove();
//                }
//
//                if (checkGpsCoordination(droneLocationLat, droneLocationLng)) {
//                    droneMarker = gMap.addMarker(markerOptions);
//                }
//            }
//        });
//    }
//
//    private void markWaypoint(LatLng point){
//        //Create MarkerOptions object
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(point);
//        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
//        Marker marker = gMap.addMarker(markerOptions);
//        mMarkers.put(mMarkers.size(), marker);
//    }
//
//    private void cameraUpdate(){
//        LatLng pos = new LatLng(droneLocationLat, droneLocationLng);
//        float zoomlevel = (float) 18.0;
//        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(pos, zoomlevel);
//        gMap.moveCamera(cu);
//
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        if (gMap == null) {
//            gMap = googleMap;
//            setUpMap();
//        }
//
//        LatLng tec = new LatLng(25.65, -100.290943);
//        gMap.moveCamera(CameraUpdateFactory.newLatLng(tec));
//
//        gMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//    }
//
//    @Override
//    public void onMapClick(LatLng point) {
//        if (isAdd == true){
//            setWaypoint(point);
//        }else{
//            setResultToToast("Cannot Add Waypoint");
//        }
//    }
//
//    public void setWaypoint(LatLng point) {
//        markWaypoint(point);
//        Waypoint mWaypoint = new Waypoint(point.latitude, point.longitude, altitude);
//        //Add Waypoint to Waypoint arraylist;
//        if (waypointMissionBuilder != null) {
//            waypointList.add(mWaypoint);
//            waypointMissionBuilder.waypointList(waypointList).waypointCount(waypointList.size());
//        }else
//        {
//            waypointMissionBuilder = new WaypointMission.Builder();
//            waypointList.add(mWaypoint);
//            waypointMissionBuilder.waypointList(waypointList).waypointCount(waypointList.size());
//        }
//    }
//
//    public void setWaypoint(LatLng point, float waypointAltitude) {
//        markWaypoint(point);
//        Waypoint mWaypoint = new Waypoint(point.latitude, point.longitude, waypointAltitude);
//        //Add Waypoint to Waypoint arraylist;
//        if (waypointMissionBuilder != null) {
//            waypointList.add(mWaypoint);
//            waypointMissionBuilder.waypointList(waypointList).waypointCount(waypointList.size());
//        }else
//        {
//            waypointMissionBuilder = new WaypointMission.Builder();
//            waypointList.add(mWaypoint);
//            waypointMissionBuilder.waypointList(waypointList).waypointCount(waypointList.size());
//        }
//    }
//
//
//    /**********************************
//     Bluetooth connection functions
//     **********************************/
//
//    /**
//     * Sends a message through bluetooth.
//     *
//     * @param message A string of text to send.
//     */
//    private void sendMessage(String message) {
//        // Check that we're actually connected before trying anything
//        if (mBluetoothService.getState() != BluetoothService.STATE_CONNECTED) {
//            setResultToToast("Not connected");
//            return;
//        }
//
//        // Check that there's actually something to send
//        if (message.length() > 0) {
//            // Get the message bytes and tell the BluetoothService to write
//            byte[] send = message.getBytes();
//            mBluetoothService.write(send);
//
//            // Reset out string buffer to zero and clear the edit text field
//            mOutStringBuffer.setLength(0);
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    mOutEditText.setText(mOutStringBuffer);
//                }
//
//            });
//        }
//    }
//
//    private void sendBitmap(Bitmap bitmapImage) {
//        // Check that we're actually connected before trying anything
//        if (mBluetoothService.getState() != BluetoothService.STATE_CONNECTED) {
//            setResultToToast("Not connected");
//            return;
//        }
//
//        // Check that there's actually something to send
//        Bitmap emptyBitmap = Bitmap.createBitmap(bitmapImage.getWidth(), bitmapImage.getHeight(), bitmapImage.getConfig());
//        if (!bitmapImage.sameAs(emptyBitmap)) {
//
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100,baos);
//            byte[] byteArray = baos.toByteArray();
//
//            mBluetoothService.write(byteArray);
//
//            new java.util.Timer().schedule(
//                    new java.util.TimerTask() {
//                        @Override
//                        public void run() {
//                            sendMessage("endimage");
//                        }
//                    },
//                    1000
//            );
//
//            sentImagesCount++;
//
//            if(sentImagesCount == mediaList.size()) {
//                new java.util.Timer().schedule(
//                        new java.util.TimerTask() {
//                            @Override
//                            public void run() {
//                                sendMessage("finish");
//                                waitForLand();
//                            }
//                        },
//                        1000
//                );
//            }
//        }
//
//        // Reset out string buffer to zero and clear the edit text field
//        mOutStringBuffer.setLength(0);
//        //mOutEditText.setText(mOutStringBuffer);
//    }
//
//    public void waitForLand() {
//        while(mFlightController.getState().isFlying()) {
//            Log.e(TAG,"waiting for landing to restart mission...");
//        }
//
//        new java.util.Timer().schedule(
//                new java.util.TimerTask() {
//                    @Override
//                    public void run() {
//                        sendMessage("land");
//                    }
//                },
//                3000
//        );
//    }
//
//    public void sendGPS() {
//        String droneGPS = droneLocationLat + "," + droneLocationLng;
//        sendMessage(droneGPS);
//    }
//
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case REQUEST_CONNECT_DEVICE_SECURE:
//                // When DeviceListActivity returns with a device to connect
//                if (resultCode == MainActivity.RESULT_OK) {
//                    connectDevice(data);
//                }
//                break;
//            case REQUEST_ENABLE_BT:
//                // When the request to enable Bluetooth returns
//                if (resultCode == MainActivity.RESULT_OK) {
//                    // Bluetooth is now enabled, so set up a chat session
//                    setupBluetooth();
//                } else {
//                    // User did not enable Bluetooth or an error occurred
//                    Log.d(TAG, "BT not enabled");
//                    Toast.makeText(VanTechActivity.this, R.string.bt_not_enabled_leaving,
//                            Toast.LENGTH_SHORT).show();
//                }
//        }
//    }
//
//    /**
//     * The Handler that gets information back from the BluetoothService
//     */
//    private final Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case Constants.MESSAGE_STATE_CHANGE:
//                    switch (msg.arg1) {
//                        case BluetoothService.STATE_CONNECTED:
//                            setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
//                            break;
//                        case BluetoothService.STATE_CONNECTING:
//                            setStatus(R.string.title_connecting);
//                            break;
//                        case BluetoothService.STATE_LISTEN:
//                        case BluetoothService.STATE_NONE:
//                            setStatus(R.string.title_not_connected);
//                            mBluetoothStatus.setText("Not connected to bluetooth");
//                            break;
//                    }
//                    break;
//                case Constants.MESSAGE_WRITE:
//                    byte[] writeBuf = (byte[]) msg.obj;
//                    // construct a string from the buffer
//                    String writeMessage = new String(writeBuf);
//                    //mConversationArrayAdapter.add("Me:  " + writeMessage);
//
//
//                    /*
//                        Send image took by the drone to the central computer
//                        First, convert it into bytes and then send it
//                     */
//
//
//                    break;
//                case Constants.MESSAGE_READ:
//                    byte[] readBuf = (byte[]) msg.obj;
//                    // construct a string from the valid bytes in the buffer
//                    String readMessage = new String(readBuf, 0, msg.arg1);
//                    Log.e(TAG, readMessage);
//
//                    /*
//                        Read message from the drone to take actions
//                     */
//
//                    switch (readMessage) {
//                        case "takeoff":
//                            startMission();
//                            break;
//                        case "gps":
//                            sendGPS();
//                            break;
//                        case "emergency":
//                            returnToHome();
//                            break;
//                    }
//
//
//                    break;
//                case Constants.MESSAGE_DEVICE_NAME:
//                    // save the connected device's name
//                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
//                    Toast.makeText(VanTechActivity.this, "Connected to "
//                            + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
//                    mBluetoothStatus.setText("Connected to: " + mConnectedDeviceName);
//                    break;
//                case Constants.MESSAGE_TOAST:
//                    Toast.makeText(VanTechActivity.this, msg.getData().getString(Constants.TOAST),
//                            Toast.LENGTH_SHORT).show();
//                    break;
//            }
//        }
//    };
//
//    /**
//     * Establish connection with other device
//     *
//     * @param data   An {@link Intent} with {@link VanTechActivity#EXTRA_DEVICE_ADDRESS} extra.
//     */
//    private void connectDevice(Intent data) {
//        // Get the device MAC address
//        String address = data.getExtras()
//                .getString(VanTechActivity.EXTRA_DEVICE_ADDRESS);
//        // Get the BluetoothDevice object
//        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
//        // Attempt to connect to the device
//        mBluetoothService.connect(device);
//    }
//
//    /**
//     * Get already synched devices
//     *  to put them in the device list dialog
//     */
//    public void getSyncBluetoothDevices()
//    {
//        Log.e(TAG, "Getting Synched devices");
//        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
//        // If there are paired devices
//        if (pairedDevices.size() > 0) {
//            // Loop through paired devices
//            for (BluetoothDevice device : pairedDevices) {
//                Log.e(TAG, device.getName() + "\n" + device.getAddress());
//                // Add the name and address to an array adapter to show in a ListView
//                pairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
//            }
//        }
//        else {
//            String noDevices = getResources().getText(R.string.none_paired).toString();
//            pairedDevicesArrayAdapter.add(noDevices);
//        }
//    }
//
//    /**
//     * The action listener for the EditText widget, to listen for the return key
//     */
//    private TextView.OnEditorActionListener mWriteListener
//            = new TextView.OnEditorActionListener() {
//        public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
//            // If the action is a key-up event on the return key, send the message
//            if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
//                String message = view.getText().toString();
//                sendMessage(message);
//            }
//            return true;
//        }
//    };
//
//    /**
//     * The on-click listener for all devices in the ListViews
//     */
//    private AdapterView.OnItemClickListener mDeviceClickListener
//            = new AdapterView.OnItemClickListener() {
//        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
//            // Cancel discovery because it's costly and we're about to connect
//            mBluetoothAdapter.cancelDiscovery();
//
//            // Get the device MAC address, which is the last 17 chars in the View
//            String info = ((TextView) v).getText().toString();
//            String address = info.substring(info.length() - 17);
//
//            // Get the BluetoothDevice object
//            BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
//
//            // Attempt to connect to the device
//            mBluetoothService.connect(device);
//        }
//    };
//
//    /**
//     * Updates the status on the action bar.
//     *
//     * @param resId a string resource ID
//     */
//    private void setStatus(int resId) {
//        final ActionBar actionBar = getActionBar();
//        if (null == actionBar) {
//            return;
//        }
//        actionBar.setSubtitle(resId);
//    }
//
//    /**
//     * Updates the status on the action bar.
//     *
//     * @param subTitle status
//     */
//    private void setStatus(CharSequence subTitle) {
//        final ActionBar actionBar = getActionBar();
//        if (null == actionBar) {
//            return;
//        }
//        actionBar.setSubtitle(subTitle);
//    }
//
//
//    /***********************
//     Live feed functions
//     ***********************/
//
//    @Override
//    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
//        Log.e(TAG, "onSurfaceTextureAvailable");
//        if (mCodecManager == null) {
//            mCodecManager = new DJICodecManager(this, surface, width, height);
//        }
//    }
//
//    @Override
//    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
//        Log.e(TAG, "onSurfaceTextureSizeChanged");
//    }
//
//    @Override
//    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
//        Log.e(TAG,"onSurfaceTextureDestroyed");
//        if (mCodecManager != null) {
//            mCodecManager.cleanSurface();
//            mCodecManager = null;
//        }
//
//        return false;
//    }
//
//    @Override
//    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
//    }
//
//    /********************
//     Helper functions
//     ********************/
//
//    private void setResultToToast(final String string){
//        VanTechActivity.this.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(VanTechActivity.this, string, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void enableDisableAdd(){
//        if (isAdd == false) {
//            isAdd = true;
//            add.setText("Exit");
//        }else{
//            isAdd = false;
//            add.setText("Add");
//        }
//    }
//
//    String nulltoIntegerDefalt(String value){
//        if(!isIntValue(value)) value="0";
//        return value;
//    }
//
//    boolean isIntValue(String val)
//    {
//        try {
//            val=val.replace(" ","");
//            Integer.parseInt(val);
//        } catch (Exception e) {return false;}
//        return true;
//    }
//
//    public static boolean checkGpsCoordination(double latitude, double longitude) {
//        return (latitude > -90 && latitude < 90 && longitude > -180 && longitude < 180) && (latitude != 0f && longitude != 0f);
//    }
//}
