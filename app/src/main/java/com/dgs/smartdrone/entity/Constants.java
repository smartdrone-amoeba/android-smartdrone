package com.dgs.smartdrone.entity;

public interface Constants {

    // Message types sent from the BluetoothService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Key names received from the BluetoothService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    public static String BASE_URL = "https://our-chess-310913.et.r.appspot.com/api/";
    public static String  LOGIN_URL = "auth/login";
    public static String POSTS_URL = "posts";
}
