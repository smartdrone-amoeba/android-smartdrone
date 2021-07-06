package com.dgs.smartdrone.helper;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

public class MyLocationListener implements LocationListener {

    private Context context;
    public MyLocationListener(Context context){
        this.context = context;
    }
    @Override
    public void onLocationChanged(Location location) {
        // Initialize the location field
//        Toast.makeText(context,  ""+location.getLatitude()+location.getLongitude(),
//                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Toast.makeText(context, provider + "'s status changed to "+status +"!",
                Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(context, "Provider " + provider + " enabled!",
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(context, "Provider " + provider + " disabled!",
                Toast.LENGTH_SHORT).show();
    }
}
