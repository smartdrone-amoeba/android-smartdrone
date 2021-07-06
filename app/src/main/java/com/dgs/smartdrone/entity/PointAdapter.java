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

public class PointAdapter extends BaseAdapter {
    Context context;
    List<PinEntity> req;
    LayoutInflater inflter;
    public PointAdapter(Context applicationContext, List<PinEntity> req) {
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


        view = inflter.inflate(R.layout.point_grid, null); // inflate the layoutTextView Name = (TextView) view.findViewById(R.id.txtMarkerListName);

        TextView markername = (TextView) view.findViewById(R.id.txtMarkerListName);
        markername.setText("Marker " + (i+1));

        return view;
    }
}
