package com.dgs.smartdrone.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;

import com.dgs.smartdrone.R;
import com.dgs.smartdrone.entity.ProjectAdapter;
import com.dgs.smartdrone.entity.ProjectEntity;
import com.dgs.smartdrone.entity.ResponeAPI;
import com.dgs.smartdrone.helper.RestHelperAPI;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dji.thirdparty.okhttp3.Response;

public class PojectListActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poject_list);
        mApiInterface = new RestHelperAPI(this);
        try {
            LoadProject();
        } catch (IOException e) {
            e.printStackTrace();
        }

        btnBack = findViewById(R.id.btnBackListProject);
        btnBack.setOnClickListener(this);
    }

    private GridView simpleGrid;
    RestHelperAPI mApiInterface;
    private ImageButton btnBack;
    private void LoadProject() throws IOException {
        simpleGrid = (GridView) findViewById(R.id.gvListProject); // init GridView
        List<ProjectEntity> listData =  new ArrayList<ProjectEntity>();

        //get data
        Response respone = mApiInterface.getProject();
        if(respone.isSuccessful()) {
            String results = respone.body().string();
            Gson gson = new Gson();
            ResponeAPI respne = gson.fromJson(results, ResponeAPI.class);

            listData = respne.getData();


            ProjectAdapter customAdapter = new ProjectAdapter(getApplicationContext(), listData);
            simpleGrid.setAdapter(customAdapter);
            // implement setOnItemClickListener event on GridView
            List<ProjectEntity> finalListData = listData;
            simpleGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Gson gson = new Gson();
//                    Intent intent = new Intent(getBaseContext(), ProjectAddActivity.class);
//                    intent.putExtra("dataproject", gson.toJson(finalListData.get(position))); // put image data in Intent
//
//                    startActivity(intent);
                    // set an Intent to Another Activity
                    //                Intent intent = new Intent(MissionActivity.this, SettingActivity.class);
                    //                intent.putExtra("id", list.get(position).getId()); // put image data in Intent
                    //                startActivity(intent); // start Intent
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBackListProject:
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}