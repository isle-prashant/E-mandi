package com.twosquares.e_mandi.managers;

import android.util.Log;

import com.twosquares.e_mandi.R;
import com.twosquares.e_mandi.services.NetworkAsync;
import com.twosquares.e_mandi.services.NetworkAsyncInterface;
import com.twosquares.e_mandi.services.Response;
import com.twosquares.e_mandi.utils.RequestBuilder;
import com.twosquares.e_mandi.views.SellingActivity;

import java.util.HashMap;

import okhttp3.Request;
import okhttp3.RequestBody;

import static com.twosquares.e_mandi.views.MainActivity.ip;

/**
 * Created by Prashant Kumar on 2/15/2018.
 */
public class UploadDataManager extends Manager implements NetworkAsyncInterface {
    private static int UPLOAD_DATA_REQUEST_CODE = 102;
    SellingActivity sellingActivity;
    public UploadDataManager(SellingActivity mActivity){
        sellingActivity = mActivity;
    }
    public void uploadData(HashMap<String, String> params){
        RequestBuilder requestBuilder = new RequestBuilder();
        Request request = requestBuilder.createPostRequest("http://" + ip + "/image.php", params);
        new NetworkAsync(this, UPLOAD_DATA_REQUEST_CODE).execute(request);
    }

    @Override
    public void onResponse(int requestCode, HashMap<Response, Object> response) {
        if (requestCode == UPLOAD_DATA_REQUEST_CODE){
            String resBody = (String) response.get(Response.BODY);
            int responseCode = (int) response.get(Response.CODE);
            Log.d("response code updata", String.valueOf(responseCode));
            System.out.println(resBody);
            if (responseCode == 200){
                sellingActivity.onResponse(true, resBody);
            } else {
                sellingActivity.onResponse(false, resBody);
            }
        }
    }
}
