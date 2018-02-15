package com.twosquares.e_mandi.services;

import android.os.AsyncTask;
import android.util.Log;
import com.twosquares.e_mandi.managers.Manager;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.Request;


public class NetworkAsync extends AsyncTask<Request, Void, HashMap<Response, Object>> {
    private NetworkAsyncInterface networkAsyncInterface;
    private int requestCode;

    public NetworkAsync(Manager manager, int requestCode) {
        networkAsyncInterface = (NetworkAsyncInterface) manager;
        this.requestCode = requestCode;
    }

    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected HashMap<Response, Object> doInBackground(Request... requests) {
        HashMap<Response, Object> res = new HashMap<>();
        okhttp3.Response response;
        if (isCancelled()){
            return null;
        }
        try {
            response = client.newCall(requests[0]).execute();
            Log.d("response from async", response.toString());
          /*  if (!response.isSuccessful()){
                res.put(Response.CODE, 404);
                res.put(Response.MESSAGE, response);
                throw new IOException("Unexpected code " + response);
            }*/
            if (response.body() != null) {
                res.put(Response.BODY, response.body().string());
            }
            res.put(Response.CODE, response.code());
            res.put(Response.MESSAGE, response.message());
        } catch (IOException e) {
            e.printStackTrace();
            res.put(Response.CODE, 404);
            res.put(Response.MESSAGE, e.getMessage());
        }
        return res;


    }

    @Override
    protected void onPostExecute(HashMap<Response, Object> stringObjectHashMap) {
        networkAsyncInterface.onResponse(requestCode, stringObjectHashMap);
    }
}
