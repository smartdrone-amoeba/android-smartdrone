package com.dgs.smartdrone.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dgs.smartdrone.MainMapActivity;
import com.dgs.smartdrone.R;
import com.dgs.smartdrone.entity.Auth.ResponeLogin;
import com.dgs.smartdrone.helper.RestHelperAPI;
import com.google.gson.Gson;

import java.io.IOException;

import dji.thirdparty.okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_login);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        mApiInterface = new RestHelperAPI(this);


    }

    RestHelperAPI mApiInterface;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.btnLogin: {
                String username = ((TextView)findViewById(R.id.txtUsername)).getText().toString();
                String password = ((TextView)findViewById(R.id.txtPassword)).getText().toString();
                try {
                    Response respone = mApiInterface.postAuth(username,password);
                   if(respone.isSuccessful()) {
                       String results = respone.body().string();
                       Gson gson = new Gson();
                       ResponeLogin mainResponse = gson.fromJson(results, ResponeLogin.class);
                       if(mainResponse.getStatus().compareTo("success") == 0) {
                           mApiInterface.setBEARER_TOKEN(mainResponse.getToken());
                           //mApiInterface.setBEARER_TOKEN("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImRheWF0QGdtYWlsLmNvbSIsInVzZXJJZCI6IjYwYWEwODE0NzBiMjEwMDAwYmUyYjJhZCIsImlhdCI6MTYyMTg0NjYwN30.XIk1QkmwryiKQEEFetFIhhLtKgva6WlvBXPWJzoh8BQ");
                            Intent intent = new Intent(this, MainMapActivity.class);
                           startActivity(intent);
                       }else
                       {
                           TextView alert = (TextView) findViewById(R.id.txtAlert);
                           alert.setText("Err : " + mainResponse.getMessage());
                       }
                   }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            }
            default:
                break;
        }
    }
}