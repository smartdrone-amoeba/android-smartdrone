package com.dgs.smartdrone.entity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dgs.smartdrone.R;

import java.util.List;

public class SettingsAdapter extends BaseAdapter {
    Context context;
    List<ProjectEntity> req;
    LayoutInflater inflter;
    public SettingsAdapter(Context applicationContext, List<ProjectEntity> req) {
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
