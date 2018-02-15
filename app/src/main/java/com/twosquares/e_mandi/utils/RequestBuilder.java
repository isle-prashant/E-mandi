package com.twosquares.e_mandi.utils;

import android.net.Uri;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Prashant Kumar on 2/11/2018.
 */
public class RequestBuilder {
    MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public Request createPostRequest(String url, HashMap<String, String> body) {
        FormBody.Builder requestBuilder = new FormBody.Builder();
        Iterator it = body.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
//            System.out.println(pair.getKey() + " = " + pair.getValue());
            requestBuilder.add(pair.getKey().toString(), pair.getValue().toString());
        }

        RequestBody requestBody = requestBuilder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        return request;
    }

    public Request createGetRequest(String url, HashMap<String, String> body){
        Uri.Builder tempUrl = Uri.parse(url).buildUpon();
        if (body != null){
            Iterator it = body.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                tempUrl.appendQueryParameter(pair.getKey().toString(),pair.getValue().toString());
            }
        }

        String requestUrl = tempUrl.build().toString();
        Request request = new Request.Builder()
                .url(requestUrl)
                .build();
        return request;
    }
}
