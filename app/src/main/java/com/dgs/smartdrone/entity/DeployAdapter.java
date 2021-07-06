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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class DeployAdapter extends BaseAdapter {
    Context context;
    List<DeployDateEntity> req;
    LayoutInflater inflter;
    public DeployAdapter(Context applicationContext, List<DeployDateEntity> req) {
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


        view = inflter.inflate(R.layout.dialog_deploy, null); // inflate the layout
        TextView text = (TextView) view.findViewById(R.id.txtDepoyText);
        TextView date = (TextView) view.findViewById(R.id.txtDeployDate);
        TextView status = (TextView) view.findViewById(R.id.txtDeployStatus);
        SimpleDateFormat  pf= new SimpleDateFormat("dd-MM-yyyy HH:mm");

        date.setText(pf.format(req.get(i).getTglDeploy()));

        return view;
    }
}
