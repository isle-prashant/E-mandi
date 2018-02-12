package com.twosquares.e_mandi.services;

import android.os.AsyncTask;
import com.twosquares.e_mandi.managers.Manager;
import java.io.IOException;
import java.util.HashMap;
import okhttp3.OkHttpClient;
import okhttp3.Request;


public class NetworkAsync extends AsyncTask<Request, Void, HashMap<Response, Object>> {
    private NetworkAsyncInterface networkAsyncInterface;
    private int requestCode;
    public NetworkAsync(Manager manager, int requestCode){
        networkAsyncInterface = (NetworkAsyncInterface) manager;
        this.requestCode = requestCode;
    }
    private final OkHttpClient client = new OkHttpClient();
    @Override
    protected  HashMap<Response, Object> doInBackground(Request... requests) {
        HashMap<Response, Object> res = new HashMap<>();
        okhttp3.Response response;
        try {
            response = client.newCall(requests[0]).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            if (response.body() != null){
                res.put(Response.BODY, response.body().string());
            }
            res.put(Response.CODE, response.code());


        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    protected void onPostExecute(HashMap<Response, Object> stringObjectHashMap) {
        networkAsyncInterface.onResponse(requestCode, stringObjectHashMap);
    }
}
