package com.twosquares.e_mandi.managers;

import android.util.Log;

import com.twosquares.e_mandi.datamodels.RowItem;
import com.twosquares.e_mandi.services.NetworkAsync;
import com.twosquares.e_mandi.services.NetworkAsyncInterface;
import com.twosquares.e_mandi.services.Response;
import com.twosquares.e_mandi.utils.RequestBuilder;
import com.twosquares.e_mandi.views.MainActivity;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.HashMap;
import okhttp3.Request;
import static com.twosquares.e_mandi.datamodels.User.stars;
import static com.twosquares.e_mandi.views.MainActivity.ip;
import static com.twosquares.e_mandi.views.MainActivity.rowItems;

/**
 * Created by Prashant Kumar on 2/16/2018.
 */
public class HomeManager extends Manager implements NetworkAsyncInterface {
    private static int HOME_REQUEST_CODE = 101;
    private MainActivity mainActivity;

    public HomeManager(MainActivity activity) {
        this.mainActivity = activity;
    }

    public void getHomeData() {
        if (!isNetworkAvailable(mainActivity)) {
            mainActivity.onResponse(false, "No Internet Connection");
            return;
        }
        RequestBuilder requestBuilder = new RequestBuilder();
        Request request = requestBuilder.createGetRequest("http://" + ip + "/index.json", null);
        new NetworkAsync(this, HOME_REQUEST_CODE).execute(request);
    }

    @Override
    public void onResponse(int requestCode, HashMap<Response, Object> response) {
        if (requestCode == HOME_REQUEST_CODE) {
            String resBody = (String) response.get(Response.BODY);
            int responseCode = (int) response.get(Response.CODE);
            Log.d("response code", String.valueOf(responseCode));
            System.out.println(resBody);
            if (responseCode == 200) {
                rowItems.clear();
                try {
                    jsonArray = new JSONArray(resBody);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        RowItem newItem = gson.fromJson(jsonObject.toString(), RowItem.class);
                        Boolean star = false;
                        if (stars.contains(jsonObject.getString("image_id"))) {
                            star = true;
                        }
                        newItem.setImportant(star);
                        rowItems.add(newItem);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mainActivity.onResponse(false, "Something went wrong");
                }
                mainActivity.onResponse(true, String.valueOf(response.get(Response.MESSAGE)));
            } else {
                mainActivity.onResponse(false, String.valueOf(response.get(Response.MESSAGE)));
            }
        }
    }
}
