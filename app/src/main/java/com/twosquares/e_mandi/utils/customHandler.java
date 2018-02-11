package com.twosquares.e_mandi.utils;

/**
 * Created by prkumar on 5/17/2017.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.pushbots.push.Pushbots;
import com.pushbots.push.utils.PBConstants;
import com.twosquares.e_mandi.datamodels.RowItem;
import com.twosquares.e_mandi.utils.UserLocalStore;
import com.twosquares.e_mandi.views.ProductDetails;

import org.json.JSONException;
import org.json.JSONObject;


public class customHandler extends BroadcastReceiver
{
    public RowItem item;
    private String TAG = "PB3";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        UserLocalStore userLocalStore = new UserLocalStore(context);

        String action = intent.getAction();
        Log.d(TAG, "action=" + action);

        // Handle Push Message when opened
        if (action.equals(PBConstants.EVENT_MSG_OPEN)) {

            //Bundle containing all fields of the opened notification
            Bundle bundle = intent.getExtras().getBundle(PBConstants.EVENT_MSG_OPEN);

            //Record opened notification
            Pushbots.PushNotificationOpened(context, bundle);

            Log.i(TAG, "User clicked notification with Message: " + bundle.get("message"));
            String star = "false";
            String product = (String) bundle.get("product");
            String user = (String) bundle.get("user");
            JSONObject jobj;
            try {
                jobj = new JSONObject(product);
                String items[]  = new String[]{jobj.getString("title"), jobj.getString("id"), jobj.getString("price"), jobj.getString("phoneNo"), jobj.getString("location"), jobj.getString("description"), star, jobj.getString("u_id"), jobj.getString("quantity")};
                Log.e("items array", ""+items);
                item = new RowItem(items);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Intent i = new Intent(context, ProductDetails.class);
            i.putExtra("rowItem", item);
            i.putExtra("Adapter", "DashboardAdapter");
            context.startActivity(i);


        }else if(action.equals(PBConstants.EVENT_MSG_RECEIVE)){

            //Bundle containing all fields of the notification
            Bundle bundle = intent.getExtras().getBundle(PBConstants.EVENT_MSG_RECEIVE);
            Log.i(TAG, "User received notification with Message: " + bundle.get("message"));
            String product = (String) bundle.get("product");
            String user = (String) bundle.get("user");
            JSONObject jobj;
            try {
                jobj = new JSONObject(product);
                String productId = jobj.getString("id");
                jobj = new JSONObject(user);
                String userStar = new String(productId + "$" + jobj.getString("name") + "$" + jobj.getString("email") + "$" + jobj.getString("phone_no"));

                userLocalStore.setproductStars(userStar);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}