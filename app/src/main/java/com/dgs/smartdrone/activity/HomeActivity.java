package com.dgs.smartdrone.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.dgs.smartdrone.entity.Auth.ResponeLogin;
import com.dgs.smartdrone.entity.ProjectAdapter;
import com.dgs.smartdrone.entity.ProjectEntity;
import com.dgs.smartdrone.entity.ResponeAPI;
import com.dgs.smartdrone.entity.SettingsAdapter;
import com.dgs.smartdrone.helper.DBHelper;
import com.dgs.smartdrone.helper.RestHelperAPI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dgs.smartdrone.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dji.thirdparty.okhttp3.Response;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private FloatingActionButton fab3;
    private FloatingActionButton fab4;
    private FloatingActionButton fab5;
    private boolean isFABOpen=false;
    private FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //FloatingActionButton fab = findViewById(R.id.fab);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab1.setOnClickListener(this);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(this);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fab3.setOnClickListener(this);
        fab4 = (FloatingActionButton) findViewById(R.id.fab4);
        fab4.setOnClickListener(this);
        fab5 = (FloatingActionButton) findViewById(R.id.fab5);
        fab5.setOnClickListener(this);

        closeFABMenu();


    }
    private int trY = 55;
    private void showFABMenu(){
        isFABOpen=true;
        fab1.animate().translationY(dpToFloat(-55));
        fab1.setVisibility(View.VISIBLE);
        fab2.animate().translationY(dpToFloat(-105));
        fab2.setVisibility(View.VISIBLE);
        fab3.animate().translationY(dpToFloat(-155));
        fab3.setVisibility(View.VISIBLE);
        fab4.animate().translationY(dpToFloat(-205));
        fab4.setVisibility(View.VISIBLE);
        fab5.animate().translationY(dpToFloat(-255));
        fab5.setVisibility(View.VISIBLE);
        fab.setImageResource(R.drawable.fabmenu);
        fab.setRotation(45);
    }

    private float dpToFloat(int value){
        return TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, value,
                this.getResources().getDisplayMetrics() );
    }

    private void closeFABMenu(){
        isFABOpen=false;
        fab1.animate().translationY(0);
        fab1.setVisibility(View.GONE);
        fab2.animate().translationY(0);
        fab2.setVisibility(View.GONE);
        fab3.animate().translationY(0);
        fab3.setVisibility(View.GONE);
        fab4.animate().translationY(0);
        fab4.setVisibility(View.GONE);
        fab5.animate().translationY(0);
        fab5.setVisibility(View.GONE);
        fab.setImageResource(R.drawable.fabmenu);
        fab.setRotation(0);
    }

    @Override
    public void onBackPressed() {
        if(!isFABOpen){
            this.onBackPressed();
        }else{
            closeFABMenu();
        }
    }
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.fab:
                if(!isFABOpen){
                    showFABMenu();
                }else{
                    closeFABMenu();
                }
                break;
            case R.id.fab1:
                break;
            case R.id.fab2:
                break;
            case R.id.fab3:
                break;
            case R.id.fab4:
                intent = new Intent(this, PojectListActivity.class);
                startActivity(intent);
                break;
            case R.id.fab5:
                intent = new Intent(this, ProjectAddActivity.class);
                startActivity(intent);
                break;
        }
    }



    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}