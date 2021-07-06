package com.dgs.smartdrone.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.dgs.smartdrone.R;
import com.dgs.smartdrone.entity.Settings;
import com.dgs.smartdrone.entity.SettingsAdapter;
import com.dgs.smartdrone.helper.DBHelper;

import java.util.List;

public class MissionActivity extends Activity {

    GridView simpleGrid;
    private DBHelper mydb ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission2);
        Button add= (Button)findViewById(R.id.btnAdd);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(getBaseContext(), SettingActivity.class);
                Intent intent = new Intent(getBaseContext(), SettingActivity.class);
                startActivity(intent);
            }
        });
        Button back= (Button)findViewById(R.id.btnBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(getBaseContext(), SettingActivity.class);
                Intent intent = new Intent(getBaseContext(), WayPoint2Activity.class);
                startActivity(intent);
            }
        });
        simpleGrid = (GridView) findViewById(R.id.missionGrid); // init GridView
        // Create an object of CustomAdapter and set Adapter to GirdView
        mydb = new DBHelper(this);

//        List<Settings> list =mydb.getAllSettings();
//        SettingsAdapter customAdapter = new SettingsAdapter(getApplicationContext(), list);
//        simpleGrid.setAdapter(customAdapter);
//        // implement setOnItemClickListener event on GridView
//        simpleGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                // set an Intent to Another Activity
//                Intent intent = new Intent(MissionActivity.this, SettingActivity.class);
//                intent.putExtra("id", list.get(position).getId()); // put image data in Intent
//                startActivity(intent); // start Intent
//            }
//        });
    }
}