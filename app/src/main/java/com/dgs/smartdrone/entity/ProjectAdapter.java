package com.dgs.smartdrone.entity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dgs.smartdrone.activity.ProjectDetailActivity;
import com.dgs.smartdrone.R;
import com.google.gson.Gson;

import java.util.List;

public class ProjectAdapter extends BaseAdapter {
    Context context;
    List<ProjectEntity> req;
    LayoutInflater inflter;
    public ProjectAdapter(Context applicationContext, List<ProjectEntity> req) {
        this.context = applicationContext;
        this.req = req;
        inflter = (LayoutInflater.from(applicationContext));
    }
    @Override
    public int getCount() {
        return req.size();
    }
    @Override
    public Object getItem(int i) {
        return null;
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        android.text.format.DateFormat df = new android.text.format.DateFormat();


        view = inflter.inflate(R.layout.activity_gridview, null); // inflate the layout
        TextView Name = (TextView) view.findViewById(R.id.txtMarkerListName);
        Name.setText(req.get(i).getNamaProject());
        @SuppressLint("WrongViewCast") ImageButton detail = (ImageButton) view.findViewById(R.id.btnProjectDetail);
        detail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(context, ProjectDetailActivity.class);
                Gson gson = new Gson();
                intent.putExtra("datap",gson.toJson(req.get(i))); // put image data in Intent
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

//        TextView goway = (TextView) view.findViewById(R.id.txtSurveyor);
//        goway.setText(req.get(i).getNamaSurveyor());
//        TextView handling = (TextView) view.findViewById(R.id.txtPlaning);
//        handling.setText(req.get(i).getTglPlanning().substring(0,10));
//        TextView speed = (TextView) view.findViewById(R.id.txtDeploy);
//        if(req.get(i).getTglDeploy() != null)
//            speed.setText(req.get(i).getTglDeploy().substring(0,10));
        return view;
    }
}
