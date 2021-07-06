package com.dgs.smartdrone.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.dgs.smartdrone.entity.Auth.ResponeLogin;
import com.dgs.smartdrone.entity.ProjectEntity;

import java.io.IOException;

import dji.thirdparty.okhttp3.MediaType;
import dji.thirdparty.okhttp3.OkHttpClient;
import dji.thirdparty.okhttp3.Request;
import dji.thirdparty.okhttp3.RequestBody;
import dji.thirdparty.okhttp3.Response;
import dji.thirdparty.retrofit2.Retrofit;
import dji.thirdparty.retrofit2.converter.gson.GsonConverterFactory;

public class RestHelperAPI {
    public String getBEARER_TOKEN() {
        //return BEARER_TOKEN;
        return preferences.getString("BEARER_TOKEN", "");
    }

    public void setBEARER_TOKEN(String BEARER_TOKEN) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("BEARER_TOKEN", BEARER_TOKEN); //
        editor.commit();

    }

    private SharedPreferences preferences;
    private Context context;

    public RestHelperAPI(Context context){
        this.context = context;
        preferences = context.getSharedPreferences("com.dgs.smartdrone", Context.MODE_PRIVATE);

    }

    public Response postAuth(String email, String password) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType =  MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "email=" + email + "&password=" + password);
        Request request = new Request.Builder()
                .url(Constants.BASE_URL+ "auth/login")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }

    public Response getProject() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(Constants.BASE_URL+ "project/get-all")
                .method("GET", null)
                .addHeader("Authorization", "Bearer: " + getBEARER_TOKEN())
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }
    public Response postProject(String data) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,
//                "{\n    " +
//                        "\"namaProject\": \"Padang Sidimpuan\",\n    " +
//                        "\"namaSurveyor\": \"hasibuan\",\n    " +
//                        "\"alamatProject\": \"Medan sunggal Jl. SM Raja\",\n    " +
//                        "\"tglTarget\": \"2021-08-30T01:42:39.000Z\",\n    " +
//                        "\"pin\": [\n        {\n            " +
//                            "\"name\": \"pin 1\",\n            " +
//                            "\"koordinat\"    : {\n                " +
//                                "\"latitude\" : 1, \n                " +
//                                "\"longitude\" : 3\n            " +
//                            "},\n            " +
//                            "\"speed\":12,\n            " +
//                            "\"altitude\": 10,\n            " +
//                            "\"heading\": 30,\n            " +
//                            "\"curvesize\": 1,\n            " +
//                            "\"rotationdir\":10,\n            " +
//                            "\"poi\": {\n                " +
//                                "\"poiStatus\":true,\n                " +
//                                "\"poiMode\" :5,\n                " +
//                                "\"poiLatitude\":1,\n                " +
//                                "\"poiLongtude\":2,\n                " +
//                                "\"poiAltiutde\":6\n            " +
//                            "},\n            " +
//                            "\"gimbalmode\": {\n                " +
//                                "\"disable\": false,\n                " +
//                                "\"focuspoi\":true,\n                " +
//                                "\"interpolate\": 11\n            " +
//                            "},\n            " +
//                            "\"intervalmode\": {\n                " +
//                                "\"disable\": false,\n                " +
//                                "\"seconda\":2000,\n                " +
//                                "\"meters\":52\n            " +
//                            "},\n            " +
//                            "\"actions\": {\n                " +
//                                "\"act01\": 1,\n               " +
//                                " \"act02\": 2,\n                " +
//                                "\"act03\": 3,\n                " +
//                                "\"act04\": 4,\n                " +
//                                "\"act05\": 5,\n                " +
//                                "\"act06\": 1,\n                " +
//                                "\"act08\": 2,\n                " +
//                                "\"act09\": 3\n            " +
//                            "}  \n        " +
//                        "}]\n}");
                data);
        Request request = new Request.Builder()
                .url(Constants.BASE_URL+ "project/add")
                .method("POST", body)
                .addHeader("Authorization", "Bearer: " + getBEARER_TOKEN())
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }

}
